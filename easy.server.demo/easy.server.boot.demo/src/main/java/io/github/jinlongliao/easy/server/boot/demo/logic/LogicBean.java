package io.github.jinlongliao.easy.server.boot.demo.logic;

import io.github.jinlongliao.easy.server.boot.demo.logic.annotation.MsgId;
import io.github.jinlongliao.easy.server.boot.demo.logic.param.DiamondParam;
import io.github.jinlongliao.easy.server.boot.demo.logic.response.TestResponse;
import io.github.jinlongliao.easy.server.boot.demo.logic.annotation.Logic;
import io.github.jinlongliao.easy.server.boot.demo.logic.annotation.UserId;
import io.github.jinlongliao.easy.server.boot.demo.logic.service.TestAsyncService;
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

import java.lang.invoke.MethodHandles;

/**
 * @author liaojinlong
 * @since 2021/1/22 19:06
 */
@RefreshClass
@EnableCache
@LogicController(desc = "测试示例")
public class LogicBean extends ApplicationObjectSupport {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @RefreshValue
    private final IGroovyService groovyService;
    private final TestAsyncService testAsyncService;

    public LogicBean(IGroovyService groovyService,
                     TestAsyncService testAsyncService) {
        this.groovyService = groovyService;
        this.testAsyncService = testAsyncService;
    }


    @SimpleGetCache(milliSecond = 10000L)
    @Logic({MsgId.TEST1})
    public Object test1(@NotNull @LogicRequestParam("userId") String userId, @LogicRequestParam("age") int age, @LogicRequestBody("userModel") UserModel userModel) {
        testAsyncService.testAsync();
        return this.groovyService.getTest(userModel);
    }

    @Logic({MsgId.TEST0})
    @SimpleGetCache(keyValueEl = "diamondParam.num and diamondParam.data.userId", handler = SimpleLimitPerAccessFilterHandler.class, milliSecond = 300L)
    public void diamond(@NotNull @LogicRequestBody("diamondParam") DiamondParam<UserModel> diamondParam) {
        log.info("diamondParam {}", diamondParam);
    }

    @Logic(MsgId.TEST2)
    public Object test2(@LogicRequestBody("userModel")  UserModel userModel) {
        Object o = testAsyncService.testWeAsync(false);
        log.info("threadId:{} name:{} result:{}", Thread.currentThread().getId(), Thread.currentThread().getName(), o);
        try {
            o = testAsyncService.testWeAsync(true);
            log.info("block threadId:{} name:{} result:{}", Thread.currentThread().getId(), Thread.currentThread().getName(), o);

        } catch (Exception e) {
            log.error("weAsync error:{}", e.getMessage(), e);
        }
        return this.groovyService.getTest(userModel);
    }

    @Logic(MsgId.TEST3)
    @SimpleGetCache(keyValueEl = "userModel.userId and userModel.age", handler = SimpleLimitPerAccessFilterHandler.class, milliSecond = 3000L)
    public Object test3(@LogicRequestBody("userModel")  UserModel userModel) {
        Object o = testAsyncService.testWeAsync(false);
        log.info("threadId:{} name:{} result:{}", Thread.currentThread().getId(), Thread.currentThread().getName(), o);
        return this.groovyService.getTest(userModel);
    }

    @LogicMapping(value = "100", desc = "测试组合注解")
    public void testAnn(@UserId int userId, @LogicRequestIp String clientIp) {
        log.info("userId: {}\t clientIp: {}", userId, clientIp);
    }

    @LogicMapping(value = "101", desc = "Hex Response")
    @SimpleGetCache(keyValueEl = "newUserId")
    public TestResponse testHex(@LogicAlias("newUserId")@UserId(newV = "newUserId") int userId,
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
