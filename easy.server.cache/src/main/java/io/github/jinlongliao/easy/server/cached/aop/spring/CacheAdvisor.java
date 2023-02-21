/**
 * Created on  13-09-19 20:40
 */
package io.github.jinlongliao.easy.server.cached.aop.spring;

import io.github.jinlongliao.easy.server.cached.field.spring.CacheConfig;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public class CacheAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    public static final String CACHE_ADVISOR_BEAN_NAME = "internalCacheAdvisor";
    private final CacheConfig cacheConfig;
    private CachePointcut pointcut;
    private String[] basePackages;

    public CacheAdvisor(CacheConfig cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    @Override
    public Pointcut getPointcut() {
        if (pointcut == null) {
            pointcut = new CachePointcut(getBasePackages(), cacheConfig);
        }
        return pointcut;
    }

    public String[] getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }
}
