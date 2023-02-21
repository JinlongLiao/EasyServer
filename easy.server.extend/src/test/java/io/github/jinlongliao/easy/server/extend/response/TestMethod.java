package io.github.jinlongliao.easy.server.extend.response;

import io.github.jinlongliao.easy.server.extend.response.ICommonResponse;
import io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig;
import io.github.jinlongliao.easy.server.extend.parser.StaticRequestParseRule;
import io.github.jinlongliao.easy.server.core.annotation.LogicMapping;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestBody;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestIp;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;
import io.github.jinlongliao.easy.server.core.core.MethodInfo;
import io.github.jinlongliao.easy.server.core.core.MethodParse;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TestMethod {
    private static final int key = 0x67;

    @LogicMapping(value = key + 1)
    public void testMethod1(@LogicRequestParam(value = "str", length = 120)
                            String str

    ) {

    }

    @LogicMapping(value = key + 2)
    public void testMethod2(@LogicRequestParam(value = "str", length = 120, dynamicLength = true)
                            String str

    ) {

    }

    @LogicMapping(value = key + 3)
    public void testMethod3(@LogicRequestParam(value = "str", length = 20)
                            String str,
                            @LogicRequestParam(value = "int0")
                            int int0
    ) {

    }

    @LogicMapping(value = key + 4)
    public void testMethod4(
            @LogicRequestParam(value = "long0")
            long long0
    ) {

    }


    @LogicMapping(value = key + 5)
    public void testMethod5(@LogicRequestParam(value = "str", length = 20)
                            String str,
                            @LogicRequestParam(value = "int0")
                            int int0,
                            @LogicRequestParam(value = "long0")
                            long long0
    ) {

    }

    @LogicMapping(value = key + 6)
    public void testMethod6(@LogicRequestParam(value = "byte0")
                            byte byte0
    ) {

    }


    @LogicMapping(value = key + 7)
    public void testMethod7(@LogicRequestParam(value = "short0")
                            short short0
    ) {

    }

    @LogicMapping(value = key + 8)
    public void testMethod8(@LogicRequestParam(value = "ints")
                            List<Integer> ints
    ) {

    }

    @LogicMapping(value = key + 9)
    public void testMethod9(@LogicRequestParam(value = "ints")
                            int[] ints
    ) {

    }

    @LogicMapping(value = key + 10)
    public void testMethod10(@LogicRequestParam(value = "ints")
                             Integer[] ints
    ) {

    }

    @LogicMapping(value = key + 11)
    public void testMethodBody(@LogicRequestBody(value = "ints")
                               Integer[] ints
    ) {

    }

    @LogicMapping(value = key + 12)
    public void testMethodCommon(@LogicRequestParam(value = "ints", isCommon = true)
                                 Integer[] ints
    ) {

    }

    @LogicMapping(value = key + 13)
    public void testMethodBoolean(@LogicRequestParam(value = "bool")
                                  boolean bool
    ) {

    }

    @LogicMapping(value = key + 14)
    public void testMethodInner(@LogicRequestIp
                                Integer[] ints
    ) {

    }

    @LogicMapping(value = key + 15)
    public void testMethodAll(@LogicRequestParam(value = "str", length = 20)
                              String str,
                              @LogicRequestParam(value = "dynamic", dynamicLength = true)
                              String dynamic,
                              @LogicRequestParam(value = "int0")
                              int int0,
                              @LogicRequestParam(value = "long0")
                              long long0,
                              @LogicRequestParam(value = "byte0")
                              byte byte0,
                              @LogicRequestParam(value = "short0")
                              short short0,
                              @LogicRequestParam(value = "ints")
                              List<Integer> ints,
                              @LogicRequestParam(value = "ints")
                              Integer[] ints0,
                              @LogicRequestParam(value = "ints1")
                              Integer[] ints1,
                              @LogicRequestIp
                              String ip,
                              @LogicRequestBody
                                  ICommonResponse re
    ) {

    }


    @Test
    public void testMethod() {
        MethodParse methodParse = new MethodParse();
        MapperStructConfig.setDev(true, "./target/", "./target/");
        Map<Integer, MethodInfo> logic = methodParse.getLogic(this.getClass());
        for (MethodInfo methodInfo : logic.values()) {
            LogicModel logicModel = new LogicModel(null, methodInfo.getDirectMethod(), methodInfo.getMsgModels(), methodInfo.getDesc(), "beanName");
            logicModel.setSourceClass(this.getClass());
            StaticRequestParseRule staticRequestParseRule = new StaticRequestParseRule(logicModel);
        }
    }
}
