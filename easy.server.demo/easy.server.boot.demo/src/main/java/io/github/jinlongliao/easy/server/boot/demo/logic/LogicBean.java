package io.github.jinlongliao.easy.server.boot.demo.logic;

import io.github.jinlongliao.easy.server.boot.demo.logic.annotation.MsgId;
import io.github.jinlongliao.easy.server.boot.demo.logic.response.TestResponse;
import io.github.jinlongliao.easy.server.boot.demo.logic.annotation.Logic;
import io.github.jinlongliao.easy.server.boot.demo.logic.annotation.UserId;
import io.github.jinlongliao.easy.server.cached.annotation.EnableCache;
import io.github.jinlongliao.easy.server.cached.annotation.GetCache;
import io.github.jinlongliao.easy.server.cached.annotation.simple.SimpleGetCache;
import io.github.jinlongliao.easy.server.cached.aop.simple.handler.SimpleLimitPerAccessFilterHandler;
import io.github.jinlongliao.easy.server.core.annotation.*;
import io.github.jinlongliao.easy.server.boot.demo.logic.param.UserModel;
import io.github.jinlongliao.easy.server.boot.demo.logic.service.IGroovyService;
import io.github.jinlongliao.easy.server.script.groovy.annotation.RefreshClass;
import io.github.jinlongliao.easy.server.script.groovy.annotation.RefreshValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.util.Assert;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;

/**
 * @author liaojinlong
 * @since 2021/1/22 19:06
 */
@RefreshClass
@EnableCache
@LogicController(desc = "测试示例")
public class LogicBean extends ApplicationObjectSupport {
    private static final Logger log = LoggerFactory.getLogger(LogicBean.class);
    @RefreshValue
    private final IGroovyService groovyService;

    public LogicBean(IGroovyService groovyService) {
        this.groovyService = groovyService;
    }


    @SimpleGetCache(milliSecond = 10000L)
    @Logic({MsgId.TEST1, MsgId.TEST0})
    public Object test1(@NotNull @LogicRequestParam("userId") String userId, @LogicRequestParam("age") int age, @LogicRequestBody UserModel userModel) {
        return this.groovyService.getTest(userModel);
    }

    @Logic(MsgId.TEST2)
    @SimpleGetCache(keyValueEl = "userId and age", handler = SimpleLimitPerAccessFilterHandler.class)
    public Object test2(@LogicRequestBody UserModel userModel) {
        return this.groovyService.getTest(userModel);
    }

    @LogicMapping(value = "100", desc = "测试组合注解")
    public void testAnn(@UserId int userId, @LogicRequestIp String clientIp) {
        log.info("userId: {}\t clientIp: {}", userId, clientIp);
    }

    @LogicMapping(value = "101", desc = "Hex Response")
    @GetCache(keyValueEl = "userId")
    public TestResponse testHex(@UserId(newV = "newUserId") int userId,
                                @HttpRequest
                                HttpServletRequest request,
                                @HttpResponse
                                HttpServletResponse response,
                                @LogicRequestIp String clientIp) {
        log.info("userId: {}\t clientIp: {}", userId, clientIp);
        Assert.notNull(request, "NOT NULL");
        Assert.notNull(response, "NOT NULL");
        return new TestResponse(userId, "ABCD", "ABCD");
    }

    @LogicMapping(value = "500", desc = "用于报错的接口")
    public Object error() {
        throw new RuntimeException("error");
    }
}
