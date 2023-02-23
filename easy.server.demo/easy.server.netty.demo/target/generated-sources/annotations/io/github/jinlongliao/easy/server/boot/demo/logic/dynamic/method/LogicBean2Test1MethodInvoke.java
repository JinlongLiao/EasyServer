package io.github.jinlongliao.easy.server.boot.demo.logic.dynamic.method;

import io.github.jinlongliao.easy.server.netty.demo.logic.LogicBean2;
import io.github.jinlongliao.easy.server.netty.demo.logic.param.UserModel;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.method.MethodInvoke;
import io.github.jinlongliao.easy.server.mapper.exception.MethodInvokeException;

@javax.annotation.Generated(value="easy.server.mapper Generated",date = "2023-02-23T18:11:08.086", comments = "common-mapper 动态生成")
public class LogicBean2Test1MethodInvoke implements MethodInvoke {
   public Object invoke(Object var1, Object... var2) throws MethodInvokeException {
      try {
         LogicBean2 var3 = (LogicBean2)var1;
         return var3.test1((String)var2[0], (Integer)var2[1], (UserModel)var2[2]);
      } catch (Exception var4) {
         throw new MethodInvokeException(var4);
      }
   }
}
