package io.github.jinlongliao.easy.server.test.spring;

import io.github.jinlongliao.easy.server.test.mapper2.Mapper;
import io.github.jinlongliao.easy.server.mapper.spring.BeanMapperFactoryBean;
import io.github.jinlongliao.easy.server.mapper.spring.IBeanMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Date;

public class SpringTest {
    private IBeanMapper beanMapper;

    @Before
    public void init() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(SpringTest.class);
        applicationContext.refresh();
        this.beanMapper = applicationContext.getBean(IBeanMapper.class);
    }

    @Test
    public void testBeanMapper() {
        byte b = 1;
        boolean bool = false;
        char c = 65;
        short s = 22;
        int i = 3423;
        long l = 3423424;
        float f = 1022f;
        double d = 1.84968;
        Object[] param = {b, b, bool, bool, c, c, s, s, i, i, l, l, f, f, d, d,new Date()};
        Mapper mapper = beanMapper.arrayBeanMapper(Mapper.class, param);
        System.out.println("mapper = " + mapper);
    }

    @Bean
    public BeanMapperFactoryBean beanMapperFactoryBean() {
        return new BeanMapperFactoryBean(true);
    }
}
