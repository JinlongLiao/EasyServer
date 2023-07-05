package io.github.jinlongliao.easy.server.mapper.spring;

import org.springframework.beans.factory.FactoryBean;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Proxy;


/**
 * @author: liaojinlong
 * @date: 2022-06-16 17:53
 */
public class BeanMapperFactoryBean implements FactoryBean<IBeanMapper> {
    private static final ClassLoader LOADER = MethodHandles.lookup().lookupClass().getClassLoader();

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
