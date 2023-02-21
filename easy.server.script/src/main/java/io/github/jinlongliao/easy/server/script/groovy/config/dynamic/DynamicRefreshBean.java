package io.github.jinlongliao.easy.server.script.groovy.config.dynamic;

import io.github.jinlongliao.easy.server.script.groovy.annotation.RefreshClass;
import io.github.jinlongliao.easy.server.script.groovy.annotation.RefreshValue;
import io.github.jinlongliao.easy.server.script.groovy.config.ScriptConfig;
import io.github.jinlongliao.easy.server.script.groovy.bean.TargetSourceFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author liaojinlong
 * @since 2022-02-21 16:11
 */
public class DynamicRefreshBean extends ApplicationObjectSupport implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(DynamicRefreshBean.class);
    private final ScriptConfig scriptConfig;
    private final ScheduledThreadPoolExecutor threadPoolTaskExecutor;
    private final Map<Object, TargetSourceFactoryBean> targetSourceDynamicFileCache;
    private final Map<Object, DynamicField> dynamicFileCache;
    private static boolean init = false;

    public DynamicRefreshBean(ScriptConfig scriptConfig, ScheduledThreadPoolExecutor threadPoolTaskExecutor) {
        this.scriptConfig = scriptConfig;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        Collection<TargetSourceFactoryBean> values = scriptConfig.getTargetSourceCache().values();
        this.targetSourceDynamicFileCache = new HashMap<>(values.size());
        this.dynamicFileCache = new HashMap<>(values.size());
        for (TargetSourceFactoryBean targetSourceFactoryBean : values) {
            targetSourceDynamicFileCache.put(targetSourceFactoryBean.getNowBean(), targetSourceFactoryBean);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> beansWithAnnotation = getApplicationContext().getBeansWithAnnotation(RefreshClass.class);
        for (Object value : beansWithAnnotation.values()) {
            value = getRealValue(value);
            Class<?> userClass = ClassUtils.getUserClass(value.getClass());
            Field[] declaredFields = userClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                RefreshValue annotation = declaredField.getAnnotation(RefreshValue.class);
                if (annotation != null) {
                    Class<?> type = declaredField.getType();
                    for (Map.Entry<Object, TargetSourceFactoryBean> beanEntry : targetSourceDynamicFileCache.entrySet()) {
                        Object key = beanEntry.getKey();
                        declaredField.setAccessible(true);
                        if (type.isInstance(key)) {
                            this.dynamicFileCache.put(key, new DynamicField(value, declaredField));
                            break;
                        }
                    }
                }
            }
        }
        if (!init) {
            synchronized (DynamicRefreshBean.class) {
                if (!init) {
                    threadPoolTaskExecutor.scheduleWithFixedDelay(new DynamicRunner(), 0, scriptConfig.getRefreshDelay(), TimeUnit.SECONDS);
                    init = true;
                }
            }
        }
    }

    private Object getRealValue(Object value) {
        boolean cglibProxy = AopUtils.isCglibProxy(value);
        if (!cglibProxy) {
            return value;
        }
        return AopProxyUtils.getSingletonTarget(value);
    }

    class DynamicRunner implements Runnable {

        @Override
        public void run() {
            Set<Object> delKey = new HashSet<>();
            for (Map.Entry<Object, DynamicField> fieldEntry : dynamicFileCache.entrySet()) {
                Object key = fieldEntry.getKey();
                TargetSourceFactoryBean targetSourceFactoryBean = targetSourceDynamicFileCache.get(key);
                if (targetSourceFactoryBean.needUpdate()) {
                    try {
                        Object object = targetSourceFactoryBean.getObject();

                        if (targetSourceDynamicFileCache.containsKey(object)) {
                            continue;
                        }
                        DynamicField dynamicField = fieldEntry.getValue();
                        targetSourceDynamicFileCache.put(object, targetSourceFactoryBean);
                        dynamicFileCache.put(object, dynamicField);
                        dynamicField.updateValue(object);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error(e.getMessage(), e);
                        continue;
                    }
                    delKey.add(key);
                }
            }
            for (Object key : delKey) {
                targetSourceDynamicFileCache.remove(key);
                dynamicFileCache.remove(key);
            }
        }
    }
}
