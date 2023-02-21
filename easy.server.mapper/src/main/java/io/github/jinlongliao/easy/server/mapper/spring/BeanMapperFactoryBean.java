package io.github.jinlongliao.easy.server.mapper.spring;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author: liaojinlong
 * @date: 2022-06-16 17:53
 */
public class BeanMapperFactoryBean implements FactoryBean<IBeanMapper> {
    private static final ClassLoader LOADER = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
        @Override
        public ClassLoader run() {
            return this.getClass().getClassLoader();
        }
    });

    private final IBeanMapper beanMapper;

    public BeanMapperFactoryBean() {
        this(false);
    }

    public BeanMapperFactoryBean(boolean searchParent) {
        beanMapper = (IBeanMapper) Proxy.newProxyInstance(LOADER, new Class[]{IBeanMapper.class}, new BeanMapperFactory(searchParent));
    }

    @Override
    public IBeanMapper getObject() {
        return beanMapper;
    }

    @Override
    public Class<?> getObjectType() {
        return IBeanMapper.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
