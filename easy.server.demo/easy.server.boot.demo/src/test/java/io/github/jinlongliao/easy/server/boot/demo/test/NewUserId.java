package io.github.jinlongliao.easy.server.boot.demo.test;

import io.github.jinlongliao.easy.server.cached.aop.el.ParamElParser;

@jakarta.annotation.Generated(value = "easy.server.mapper Generated", date = "2023-10-24T02:17:08.421993", comments = "common-mapper 动态生成")
public class NewUserId implements ParamElParser {
    @Override
    public String parseValue(StringBuilder var1, Object[] var2) {
        short var3 = (short) var2[0];
        var1.append(var3);
        var1.append(":");
        return var1.toString();
    }
}
