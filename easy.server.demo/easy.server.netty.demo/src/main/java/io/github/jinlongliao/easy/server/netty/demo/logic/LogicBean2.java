package io.github.jinlongliao.easy.server.netty.demo.logic;

import io.github.jinlongliao.easy.server.cached.annotation.EnableCache;
import io.github.jinlongliao.easy.server.core.annotation.LogicController;
import io.github.jinlongliao.easy.server.core.annotation.LogicMapping;
import io.github.jinlongliao.easy.server.netty.demo.constant.LogicId;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnection;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnectionFactory;
import io.github.jinlongliao.easy.server.netty.demo.logic.annotation.TcpCon;
import io.github.jinlongliao.easy.server.netty.demo.logic.annotation.UserId;
import io.github.jinlongliao.easy.server.script.groovy.annotation.RefreshClass;
import io.github.jinlongliao.easy.server.swagger.servlet.help.ApiCleanHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ApplicationObjectSupport;

import java.util.Collections;

/**
 * @author liaojinlong
 * @since 2021/1/22 19:06
 */
@RefreshClass
@EnableCache
@LogicController(desc = "测试示例2", value = "Logic-Test-2")
public class LogicBean2 extends ApplicationObjectSupport {
    private static final Logger log = LoggerFactory.getLogger(LogicBean2.class);
    private final TcpConnectionFactory tcpConnectionFactory;
    private final ApiCleanHelper apiCleanHelper;


    public LogicBean2(TcpConnectionFactory tcpConnectionFactory, ApiCleanHelper apiCleanHelper) {
        this.tcpConnectionFactory = tcpConnectionFactory;
        this.apiCleanHelper = apiCleanHelper;
    }


    @LogicMapping(value = LogicId.HEART_BEAT, desc = "心跳")
    public Object heart(@UserId int userId) {
        log.info("heart： userId ={} ", userId);
        return Collections.singletonMap("time_stamp", System.currentTimeMillis());
    }

    @LogicMapping(value = LogicId.USER_AUTH, desc = "认证")
    public void auth(@UserId int userId, @TcpCon TcpConnection tcpConnection) {
        tcpConnectionFactory.bindConnect(userId, tcpConnection);
        log.info("auth : userId ={} ", userId);
    }

    @LogicMapping(value = LogicId.FRESH, desc = "刷新ApiUi缓存")
    public Object refreshApi() {
        this.apiCleanHelper.reset();
        return Collections.singletonMap("success", System.currentTimeMillis());

    }
}
