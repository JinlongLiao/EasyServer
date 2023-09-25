package io.github.jinlongliao.easy.server.cache.test;

import io.github.jinlongliao.easy.server.cached.aop.el.ParamElParser;


public class TestParamElParser implements ParamElParser {
    @Override
    public String parseValue(StringBuilder stringBuilder, Object param) {
        A a = (A) param;
        stringBuilder.append(a.getA());
        stringBuilder.append(":");
        stringBuilder.append(a.getB().getB1());
        stringBuilder.append(":");
        stringBuilder.append(a.getB().getB2());
        stringBuilder.append(":");
        stringBuilder.append(a.isC());
        stringBuilder.append(":");
        stringBuilder.append(String.valueOf(a.getD()));
        stringBuilder.append(":");

        return stringBuilder.toString();
    }
}
