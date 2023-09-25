package io.github.jinlongliao.easy.server.script.test;

import io.github.jinlongliao.easy.server.script.groovy.annotation.RefreshClass;
import io.github.jinlongliao.easy.server.script.groovy.annotation.RefreshValue;
import org.springframework.stereotype.Component;

@Component
@RefreshClass
public class TestGroovyClass {
    @RefreshValue
    private final ITestGroovy testGroovy;

    public TestGroovyClass(ITestGroovy testGroovy) {
        this.testGroovy = testGroovy;
    }

    @Override
    public String toString() {
        return "TestGroovyClass{" +
                "testGroovy=" + testGroovy +
                '}';
    }
}
