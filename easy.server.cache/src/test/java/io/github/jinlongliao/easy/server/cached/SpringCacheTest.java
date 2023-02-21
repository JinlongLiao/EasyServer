package io.github.jinlongliao.easy.server.cached;

import io.github.jinlongliao.easy.server.cached.config.CacheProxyConfiguration;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringCacheTest {
    @Test
    public void test1() throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(CacheProxyConfiguration.class, TestConfig.class);
        context.scan("io.github.jinlongliao.easy.server.cached");
        context.refresh();
        TestA bean = context.getBean(TestA.class);
        int index = 12;
        while (index-- > 0) {
            String say = bean.say();
            System.out.println(say);
            Thread.sleep(1000);
        }
    }

    @Test
    public void test2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(CacheProxyConfiguration.class, TestConfig.class);
        context.scan(this.getClass().getPackage().getName());
        context.refresh();
        TestA bean = context.getBean(TestA.class);
        int index = 12;
        while (index-- > 0) {
            String say = bean.say();
            System.out.println(say);
        }
        index = 10000;
        long start = System.currentTimeMillis();
        while (index-- > 0) {
            bean.say();
        }
        long end = System.currentTimeMillis();
        System.out.println("Proxy   " + (end - start));
        index = 10000;
        TestA testA = new TestA();
        start = System.currentTimeMillis();
        while (index-- > 0) {
            testA.say();
        }
        end = System.currentTimeMillis();
        System.out.println("Normal  " + (end - start));
    }


}
