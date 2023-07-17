package io.github.jinlongliao.easy.server.script.groovy.bean;

import io.github.jinlongliao.easy.server.mapper.annotation.Ignore;
import io.github.jinlongliao.easy.server.script.groovy.ResourceScriptSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.integration.scripting.ScriptingException;
import org.springframework.scripting.groovy.GroovyScriptFactory;

import jakarta.annotation.Resource;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author liaojinlong
 * @since 2022-02-21 14:58
 */
public class TargetSourceFactoryBean extends ApplicationObjectSupport implements FactoryBean<Object> {
    private final ResourceScriptSource scriptSource;
    private final GroovyScriptFactory groovyScriptFactory;
    private Object nowBean;
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private boolean init = true;

    public TargetSourceFactoryBean(
            ResourceScriptSource scriptSource,
            GroovyScriptFactory groovyScriptFactory) {
        this.scriptSource = scriptSource;
        this.groovyScriptFactory = groovyScriptFactory;
    }

    @Override
    public Object getObject() throws Exception {
        if (init || needUpdate()) {
            nowBean = this.groovyScriptFactory.getScriptedObject(scriptSource);
            if (nowBean instanceof ApplicationContextAware) {
                ((ApplicationContextAware) nowBean).setApplicationContext(getApplicationContext());
            }
            try {
                this.autoWire();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            this.init = false;
        }
        return nowBean;
    }

    /**
     * 自动注入
     *
     * @throws Exception
     */
    private void autoWire() throws Exception {
        Class<?> objectType = getObjectType();
        Field[] declaredFields = objectType.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            int modifiers = declaredField.getModifiers();
            if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)) {
                continue;
            }
            if (declaredField.getAnnotation(Ignore.class) != null) {
                continue;
            }
            if (declaredField.getAnnotation(Ignore.class) != null) {
                continue;
            }
            boolean autowire = false;
            String beanName = null;
            Annotation annotation = declaredField.getAnnotation(Resource.class);
            if (annotation != null) {
                beanName = ((Resource) annotation).name();
                autowire = true;
            } else {
                annotation = declaredField.getAnnotation(Qualifier.class);
                if (annotation != null) {
                    beanName = ((Qualifier) annotation).value();
                    autowire = true;
                } else {
                    annotation = declaredField.getAnnotation(Autowired.class);
                    if (annotation != null) {
                        autowire = true;
                    }
                }
            }
            if (autowire) {
                Class<?> targetClass = declaredField.getType();
                Object value;
                if (beanName == null) {
                    value = getApplicationContext().getBean(targetClass);
                } else {
                    value = getApplicationContext().getBean(beanName, targetClass);
                }
                declaredField.setAccessible(true);
                declaredField.set(nowBean, value);
            }
        }
    }

    public boolean needUpdate() {
        return this.scriptSource.isModified();
    }

    public Object getNowBean() {
        return nowBean;
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return this.groovyScriptFactory.getScriptedObjectType(scriptSource);
        } catch (IOException e) {
            throw new ScriptingException(e.getMessage());
        }
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
