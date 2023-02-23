package io.github.jinlongliao.easy.server.boot.demo.logic.dynamic.method;

import io.github.jinlongliao.easy.server.netty.demo.logic.LogicBean;
import io.github.jinlongliao.easy.server.netty.demo.logic.param.UserModel;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.method.MethodInvoke;
import io.github.jinlongliao.easy.server.mapper.exception.MethodInvokeException;

@javax.annotation.Generated(value="easy.server.mapper Generated",date = "2023-02-23T18:11:08.026", comments = "common-mapper 动态生成")
public class LogicBeanTest2MethodInvoke implements MethodInvoke {
   public Object invoke(Object var1, Object... var2) throws MethodInvokeException {
      try {
         LogicBean var3 = (LogicBean)var1;
         return var3.test2((UserModel)var2[0]);
      } catch (Exception var4) {
         throw new MethodInvokeException(var4);
      }
   }
}
