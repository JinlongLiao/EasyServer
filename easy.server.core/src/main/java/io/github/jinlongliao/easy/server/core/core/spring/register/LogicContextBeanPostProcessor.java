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

import io.github.jinlongliao.easy.server.core.annotation.LogicController;
import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.util.Objects;
import java.util.Set;

/**
 * @author: liaojinlong
 * @date: 2022/10/7 19:33
 */

public class LogicContextBeanPostProcessor implements DisposableBean, PriorityOrdered, BeanPostProcessor {
    private final LogicRegisterContext logicRegisterContext;
    private final Set<String> packages;
    private final boolean all;

    public LogicContextBeanPostProcessor(Set<String> packages, LogicRegisterContext logicRegisterContext) {
        this.logicRegisterContext = logicRegisterContext;
        this.packages = packages;
        if (Objects.isNull(packages) || packages.isEmpty()) {
            this.all = true;
        } else {
            this.all = false;
        }
    }


    @Override
    public void destroy() throws Exception {
        if (Objects.nonNull(this.logicRegisterContext)) {
            this.logicRegisterContext.close();
        }
        if (Objects.nonNull(packages)) {
            this.packages.clear();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 3;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (all) {
            this.iniLogic(bean, beanName);
        } else {
            String userClass = ClassUtils.getUserClass(bean.getClass()).getName();
            for (String pk : packages) {
                if (userClass.startsWith(pk)) {
                    this.iniLogic(bean, beanName);
                    break;
                }
            }
        }
        return bean;
    }

    private void iniLogic(Object bean, String beanName) {
        LogicController logicController = AnnotationUtils.findAnnotation(bean.getClass(), LogicController.class);
        if (Objects.nonNull(logicController)) {
            this.logicRegisterContext.getMetaData().put(bean, logicController);
            this.logicRegisterContext.getParse().parserLogic(beanName, bean);
        }
    }
}
