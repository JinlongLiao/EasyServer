package io.github.jinlongliao.easy.server.demo.logic.response;

import io.github.jinlongliao.easy.server.extend.annotation.GeneratorResponse;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;

@GeneratorResponse
public class TestResponse extends RootResponse {
    @LogicRequestParam(length = 20)
    private final String name;
    @LogicRequestParam(dynamicLength = true)
    private final String age;

    public TestResponse() {
        this(0, "", "");
    }

    public TestResponse(int status, String name, String age) {
        super(status);
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }
}
