package io.github.jinlongliao.easy.server.cached.config;

import io.github.jinlongliao.easy.server.cached.annotation.EnableMethodCaches;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 整合注解缓存配置
 *
 * @author: liaojinlong
 * @date: 2023-03-29 17:23
 */
public class RepeatingCacheProxyConfiguration extends CacheProxyConfiguration {
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(EnableMethodCaches.class.getName()));
        if (annotationAttributes != null) {
            AnnotationAttributes[] annotations = annotationAttributes.getAnnotationArray("value");
            for (AnnotationAttributes annotation : annotations) {
                this.toBuildCacheConfig(importingClassMetadata, registry, AnnotationAttributes.fromMap(annotation));
            }
        }
    }
}
