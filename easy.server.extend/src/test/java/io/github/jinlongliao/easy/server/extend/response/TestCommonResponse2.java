package io.github.jinlongliao.easy.server.extend.response;


import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class TestCommonResponse2 extends TestCommonResponse {
    private long _long;
    private char _char;
    @LogicRequestParam(length = 20)
    private String str = "ABCD";
    private Date date;
    @LogicRequestParam(dynamicLength = true)
    private String dynamicStr = "EFG";
    private List<RespTest> respTests = Collections.singletonList(new RespTest());
    private Set<RespTest> respTest2 =Collections.singleton(new RespTest());
    private Set<RespTest> respTest3 = Collections.singleton(new RespTest());
    private Set<RespTest> respTest4 ;
    private Set<Integer> respTest5 = Collections.singleton(444);
    public long get_long() {
        return _long;
    }

    public char get_char() {
        return _char;
    }

    public Date getDate() {
        return date;
    }

    public List<RespTest> getRespTests() {
        return respTests;
    }

    public Set<RespTest> getRespTest2() {
        return respTest2;
    }

    public Set<RespTest> getRespTest3() {
        return respTest3;
    }

    public Set<RespTest> getRespTest4() {
        return respTest4;
    }

    public Set<Integer> getRespTest5() {
        return respTest5;
    }

    @Override
    public List<Field> headerAppender() {
        return Arrays.stream(TestCommonResponse.class.getDeclaredFields()).collect(Collectors.toList());
    }

    public String getStr() {
        return str;
    }

    public String getDynamicStr() {
        return dynamicStr;
    }
}
