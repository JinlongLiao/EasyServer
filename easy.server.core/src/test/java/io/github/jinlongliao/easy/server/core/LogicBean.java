package io.github.jinlongliao.easy.server.core;

import io.github.jinlongliao.easy.server.core.annotation.LogicController;
import io.github.jinlongliao.easy.server.core.annotation.LogicMapping;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;

/**
 * @author liaojinlong
 * @since 2021/1/22 19:06
 */
@LogicController
public class LogicBean {
    @LogicMapping(99)
    public void test(@LogicRequestParam("userId") String userId, @LogicRequestParam("age") int age) {

    }
}
