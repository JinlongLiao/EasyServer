package io.github.jinlongliao.easy.server.demo.logic;

import io.github.jinlongliao.easy.server.swagger.servlet.help.ApiCleanHelper;
import io.github.jinlongliao.easy.server.cached.annotation.EnableCache;
import io.github.jinlongliao.easy.server.demo.logic.param.UserModel;
import io.github.jinlongliao.easy.server.demo.logic.service.IGroovyService;
import io.github.jinlongliao.easy.server.script.groovy.annotation.RefreshClass;
import io.github.jinlongliao.easy.server.script.groovy.annotation.RefreshValue;
import io.github.jinlongliao.easy.server.core.annotation.LogicController;
import io.github.jinlongliao.easy.server.core.annotation.LogicMapping;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestBody;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ApplicationObjectSupport;

import javax.validation.constraints.NotNull;

/**
 * @author liaojinlong
 * @since 2021/1/22 19:06
 */
@RefreshClass
@EnableCache
@LogicController(desc = "测试示例2", value = "Logic-Test-2")
public class LogicBean2 extends ApplicationObjectSupport {
    private static final Logger log = LoggerFactory.getLogger(LogicBean2.class);
    @RefreshValue
    private final IGroovyService groovyService;
    private final ApiCleanHelper apiCleanHelper;


    public LogicBean2(IGroovyService groovyService, ApiCleanHelper apiCleanHelper) {
        this.groovyService = groovyService;
        this.apiCleanHelper = apiCleanHelper;
    }


    @LogicMapping(value = "111", desc = "测试Groovy+缓存")
    public Object test1(@NotNull @LogicRequestParam("userId") String userId, @LogicRequestParam("age") int age, @LogicRequestBody("userModel") UserModel userModel) {
        return this.groovyService.getTest(userModel);
    }

    @LogicMapping(value = "112", desc = "刷新ApiUi缓存")
    public void refreshApi() {
        this.apiCleanHelper.reset();
    }
}
