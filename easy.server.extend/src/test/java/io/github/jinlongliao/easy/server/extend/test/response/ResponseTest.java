package io.github.jinlongliao.easy.server.extend.test.response;


import io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig;
import io.github.jinlongliao.easy.server.utils.common.HexUtil;
import org.junit.Test;

public class ResponseTest {
    @Test
    public void genTest() {
        TestCommonResponse response = new TestCommonResponse();
        byte[] bytes = response.genResHex();
        System.out.println("TestResponse bytes = " + HexUtil.byte2Hex(bytes));
    }

    @Test
    public void genTest2() {
        MapperStructConfig.setDev(true, "./target/", "./target/");
        TestCommonResponse response = new TestCommonResponse2();
        byte[] bytes = response.genResHex();
        System.out.println("TestResponse2 bytes = " + HexUtil.byte2Hex(bytes));
    }
}
