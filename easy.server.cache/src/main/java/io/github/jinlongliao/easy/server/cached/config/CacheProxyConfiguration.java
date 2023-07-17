package io.github.jinlongliao.easy.server.cached.config;

import io.github.jinlongliao.easy.server.cached.aop.simple.SimplePointcutAndHandler;
import io.github.jinlongliao.easy.server.cached.aop.spring.handler.DefaultCacheHandler;
import io.github.jinlongliao.easy.server.cached.annotation.EnableMethodCache;
import io.github.jinlongliao.easy.server.cached.aop.spring.CacheAdvisor;
import io.github.jinlongliao.easy.server.cached.aop.spring.CacheInterceptor;
import io.github.jinlongliao.easy.server.cached.field.spring.CacheConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;


/**
 * 基于Spring 的 缓存配置
 *
 * @author liaojinlong
 * @since 2022-02-14 16:43
 */
public class CacheProxyConfiguration implements ImportBeanDefinitionRegistrar {
    private final CacheConfig cacheConfig = new CacheConfig(64);

    public CacheConfig getCacheConfig() {
        return cacheConfig;
    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        cacheConfig.addCacheHandler(new DefaultCacheHandler());

        AnnotationAttributes enableMethodCache = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(EnableMethodCache.class.getName()));
        if (enableMethodCache != null) {
            this.toBuildCacheConfig(importingClassMetadata, registry, enableMethodCache);
        }

    }

    protected void toBuildCacheConfig(AnnotationMetadata importingClassMetadata,
                                      BeanDefinitionRegistry registry,
                                      AnnotationAttributes enableMethodCache) {
        this.toBuildSimpleCacheConfig(importingClassMetadata, registry, enableMethodCache);
        this.toBuildTraditionCacheConfig(importingClassMetadata, registry, enableMethodCache);
    }

    /**
     * 基于委托模式构建
     */
    private void toBuildSimpleCacheConfig(AnnotationMetadata importingClassMetadata,
                                          BeanDefinitionRegistry registry,
                                          AnnotationAttributes enableMethodCache) {

        String[] basePackages = enableMethodCache.getStringArray("basePackages");
        BeanDefinitionBuilder cacheInterceptor = BeanDefinitionBuilder.genericBeanDefinition(SimplePointcutAndHandler.class);
        cacheInterceptor.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        cacheInterceptor.addConstructorArgValue(registry);
        cacheInterceptor.addConstructorArgValue(basePackages);
        registry.registerBeanDefinition("simplePointcutAndHandler", cacheInterceptor.getBeanDefinition());
    }

    /**
     * 传统模式构建
     */
    private void toBuildTraditionCacheConfig(AnnotationMetadata importingClassMetadata,
                                             BeanDefinitionRegistry registry,
                                             AnnotationAttributes enableMethodCache) {

        String[] basePackages = enableMethodCache.getStringArray("basePackages");
        Integer order = enableMethodCache.<Integer>getNumber("order");
        BeanDefinitionBuilder cacheInterceptor = BeanDefinitionBuilder.genericBeanDefinition(CacheInterceptor.class);
        cacheInterceptor.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        cacheInterceptor.addConstructorArgValue(cacheConfig);
        registry.registerBeanDefinition("cacheInterceptor", cacheInterceptor.getBeanDefinition());

        BeanDefinitionBuilder cacheAdvisorBuilder = BeanDefinitionBuilder.genericBeanDefinition(CacheAdvisor.class);
        cacheAdvisorBuilder.addConstructorArgValue(cacheConfig);
        cacheAdvisorBuilder.addPropertyValue("adviceBeanName", CacheAdvisor.CACHE_ADVISOR_BEAN_NAME);
        cacheAdvisorBuilder.addPropertyReference("advice", "cacheInterceptor");
        cacheAdvisorBuilder.addPropertyValue("basePackages", basePackages);
        cacheAdvisorBuilder.addPropertyValue("order", order);
        cacheAdvisorBuilder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        registry.registerBeanDefinition(CacheAdvisor.CACHE_ADVISOR_BEAN_NAME, cacheAdvisorBuilder.getBeanDefinition());
    }

}
