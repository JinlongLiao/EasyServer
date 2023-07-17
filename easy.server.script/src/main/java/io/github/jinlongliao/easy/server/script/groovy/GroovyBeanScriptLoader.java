package io.github.jinlongliao.easy.server.script.groovy;

import io.github.jinlongliao.easy.server.script.groovy.annotation.EnableRefresh;
import io.github.jinlongliao.easy.server.script.groovy.bean.ScriptConfigFactoryBean;
import io.github.jinlongliao.easy.server.script.groovy.config.ScriptConfig;
import io.github.jinlongliao.easy.server.script.groovy.constant.ScriptLang;
import io.github.jinlongliao.easy.server.script.groovy.bean.TargetSourceFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.integration.scripting.RefreshableResourceScriptSource;
import org.springframework.integration.scripting.ScriptingException;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.groovy.GroovyScriptFactory;
import org.springframework.scripting.support.StaticScriptSource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Groovy 脚本加载
 *
 * @author liaojinlong
 * @since 2022-02-18 15:00
 */
public class GroovyBeanScriptLoader implements ImportBeanDefinitionRegistrar, BeanFactoryAware, ResourceLoaderAware {
    private static final Logger log = LoggerFactory.getLogger(GroovyBeanScriptLoader.class);
    public final Map<ResourceScriptSource, TargetSourceFactoryBean> targetSourceCache = new ConcurrentHashMap<>(32);
    private static final String SCRIPT_FACTORY_NAME_PREFIX = "scriptFactory.";
    private ScriptConfig scriptConfig;
    private ResourceLoader resourceLoader;
    private BeanFactory beanFactory;
    private ResourceScriptSource[] resourceScriptSources;
    private BeanDefinitionRegistry registry;

    public ResourceScriptSource[] getScriptResource() {
        if (resourceScriptSources != null) {
            return resourceScriptSources;
        }
        synchronized (GroovyBeanScriptLoader.class) {
            if (resourceScriptSources != null) {
                return resourceScriptSources;
            }
            boolean needRefresh = true;
            ScriptLang scriptType = scriptConfig.getScriptType();
            Resource[] scripts = scriptConfig.getScripts();
            if (scriptType == ScriptLang.GROOVY_CLASS) {
                needRefresh = scriptConfig.isRefresh();
            }
            this.resourceScriptSources = new ResourceScriptSource[scripts.length];
            int index = 0;
            for (Resource script : scripts) {
                ScriptSource scriptSource;
                if (needRefresh) {
                    scriptSource = new RefreshableResourceScriptSource(script, 1);
                } else {
                    try {
                        byte[] bytes = FileCopyUtils.copyToByteArray(script.getInputStream());
                        scriptSource = new StaticScriptSource(new String(bytes, StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        throw new ScriptingException(e.getMessage(), e);
                    }
                }
                this.resourceScriptSources[index++] = new ResourceScriptSource(script, scriptSource);
            }
            return resourceScriptSources;
        }
    }


    public void getTargetSources(ResourceScriptSource[] scriptSources) {
        for (ResourceScriptSource scriptSource : scriptSources) {
            Resource resource = scriptSource.getResource();
            String resourceFilename = resource.getFilename();
            String filename = resourceFilename.substring(0, resourceFilename.lastIndexOf("."));
            String beanName = lowerCaseFirstLetter(filename);
            String path;
            try {
                path = (resource.getURL().getFile());
            } catch (IOException e) {
                throw new ScriptingException(e.getMessage(), e);
            }
            BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(GroovyScriptFactory.class);
            definition.addConstructorArgValue(path);
            AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
            String scriptName = SCRIPT_FACTORY_NAME_PREFIX + beanName;
            this.registry.registerBeanDefinition(scriptName, beanDefinition);
            GroovyScriptFactory groovyScriptFactory = beanFactory.getBean(scriptName, GroovyScriptFactory.class);
            BeanDefinitionBuilder targetSourceFactoryBeanBuilder = BeanDefinitionBuilder.genericBeanDefinition(TargetSourceFactoryBean.class);
            targetSourceFactoryBeanBuilder.addConstructorArgValue(scriptSource);
            targetSourceFactoryBeanBuilder.addConstructorArgValue(groovyScriptFactory);
            this.registry.registerBeanDefinition(beanName, targetSourceFactoryBeanBuilder.getBeanDefinition());
            TargetSourceFactoryBean sourceFactoryBean = this.beanFactory.getBean("&" + beanName, TargetSourceFactoryBean.class);
            targetSourceCache.put(scriptSource, sourceFactoryBean);
        }
    }

    public void initScriptConfig(Map<String, Object> annotationAttributes) throws IOException {
        this.scriptConfig = new ScriptConfig();
        scriptConfig.setScriptPaths((String[]) annotationAttributes.get("scriptPaths"));
        scriptConfig.setScriptType((ScriptLang) annotationAttributes.get("scriptType"));
        scriptConfig.setRefresh((Boolean) annotationAttributes.get("refresh"));
        scriptConfig.setRefreshDelay((Long) annotationAttributes.get("refreshDelay"));
        scriptConfig.setTargetSourceCache(targetSourceCache);
        List<Resource> resources = new ArrayList<>();
        Resource[] scripts = this.scriptConfig.getScripts();
        if (scripts != null) {
            resources.addAll(Arrays.asList(scripts));
        }

        String[] scriptPaths = this.scriptConfig.getScriptPaths();
        if (scriptPaths != null) {
            for (String scriptPath : scriptPaths) {
                Resource[] resource = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(scriptPath);
                resources.addAll(Arrays.asList(resource));
            }
        }
        this.scriptConfig.setScripts(resources.toArray(new Resource[0]));

        this.getTargetSources(getScriptResource());
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        this.registry = registry;
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(EnableRefresh.class.getName());
        if (annotationAttributes == null || annotationAttributes.isEmpty()) {
            return;
        }
        try {
            this.initScriptConfig(annotationAttributes);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        this.registry.registerBeanDefinition("scriptConfig", BeanDefinitionBuilder
                .genericBeanDefinition(ScriptConfigFactoryBean.class)
                .addConstructorArgValue(this.scriptConfig).getBeanDefinition());
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * 首字母变小写.
     *
     * @param str 英文单词.
     * @return 首字母变小写的英文单词.
     */
    public String lowerCaseFirstLetter(String str) {
        char[] chars = str.toCharArray();
        // 如果是大写字母
        if ('A' <= chars[0] && chars[0] <= 'Z') {
            // 大写+32是变小写
            chars[0] += 32;
            return String.valueOf(chars);
        } else {
            return str;
        }
    }
}
