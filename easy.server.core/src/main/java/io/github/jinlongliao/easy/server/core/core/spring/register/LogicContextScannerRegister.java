/*
 * Copyright 2010-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.jinlongliao.easy.server.core.core.spring.register;

import io.github.jinlongliao.easy.server.core.annotation.LogicContextScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: liaojinlong
 * @date: 2022/10/7 19:44
 */
public class LogicContextScannerRegister implements ImportBeanDefinitionRegistrar {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(LogicContextScan.class.getName()));
        if (mapperScanAttrs != null) {
            registerBeanDefinitions(mapperScanAttrs, registry);
        }
    }

    void registerBeanDefinitions(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
        log.info("LogicContextScannerRegister start");
        Set<String> basePackages = (Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
                .collect(Collectors.toSet()));
        BeanDefinitionBuilder logicRegisterContextBuilder = BeanDefinitionBuilder.genericBeanDefinition(LogicRegisterContext.class);
        logicRegisterContextBuilder.addConstructorArgValue(registry);
        logicRegisterContextBuilder.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
        registry.registerBeanDefinition("logicRegisterContext", logicRegisterContextBuilder.getBeanDefinition());
        if (registry instanceof ConfigurableListableBeanFactory) {
            ConfigurableListableBeanFactory configurableListableBeanFactory = (ConfigurableListableBeanFactory) registry;
            LogicRegisterContext logicRegisterContext = configurableListableBeanFactory.getBean("logicRegisterContext", LogicRegisterContext.class);
            configurableListableBeanFactory.addBeanPostProcessor(new LogicContextBeanPostProcessor(basePackages, logicRegisterContext));
        } else {
            BeanDefinitionBuilder logicContextBeanPostProcessorBuilder = BeanDefinitionBuilder.genericBeanDefinition(LogicContextBeanPostProcessor.class);
            logicContextBeanPostProcessorBuilder.addConstructorArgValue(basePackages);
            logicContextBeanPostProcessorBuilder.addConstructorArgReference("logicRegisterContext");
            registry.registerBeanDefinition("logicContextBeanPostProcessor", logicContextBeanPostProcessorBuilder.getBeanDefinition());
        }
        log.info("LogicContextScannerRegister Success");

    }


}
