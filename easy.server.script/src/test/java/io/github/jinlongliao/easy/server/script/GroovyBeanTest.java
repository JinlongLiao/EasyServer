package io.github.jinlongliao.easy.server.script;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author liaojinlong
 * @since 2022-02-18 16:36
 */
public class GroovyBeanTest {
    private AnnotationConfigApplicationContext applicationContext;

    @Before
    public void init() {
        applicationContext = new AnnotationConfigApplicationContext();
    }

    @Test
    public void testLoad() throws InterruptedException {
        applicationContext.register(GroovyConfig.class);
        applicationContext.scan("io.github.jinlongliao.easy.server.script");
        applicationContext.refresh();
        Object bean = applicationContext.getBean(TestGroovyClass.class);
        System.out.println("bean = " + bean);
        Thread.sleep(1000);
        int times = 10;
        while (times-- > 0) {
            System.out.println("bean = " + applicationContext.getBean(TestGroovyClass.class));
            Thread.sleep(1500);

        }
    }
}
