package io.github.jinlongliao.easy.server.mapper.ext;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.annotation.Mapping;

public class B {
    @Mapping(className = "io.github.jinlongliao.easy.server.mapper.ext.B", methodName = "aC")
    private A a;
    private Boolean as;

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public Boolean getAs() {
        return as;
    }

    public void setAs(Boolean as) {
        this.as = as;
    }

    @Override
    public String toString() {
        return "B{" +
                "a=" + a +
                ", as='" + as + '\'' +
                '}';
    }

    public static A aC(Object obj) {
        return (A) obj;
    }
}
