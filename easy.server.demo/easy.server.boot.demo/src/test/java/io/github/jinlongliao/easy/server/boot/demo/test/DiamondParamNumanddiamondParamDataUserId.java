package io.github.jinlongliao.easy.server.boot.demo.test;

import io.github.jinlongliao.easy.server.boot.demo.logic.param.DiamondParam;
import io.github.jinlongliao.easy.server.boot.demo.logic.param.UserModel;
import io.github.jinlongliao.easy.server.cached.aop.el.ParamElParser;

@jakarta.annotation.Generated(value="easy.server.mapper Generated",date = "2023-10-24T01:20:34.684051", comments = "common-mapper 动态生成")
public class DiamondParamNumanddiamondParamDataUserId implements ParamElParser {
   public String parseValue(StringBuilder var1, Object[] var2) {
      DiamondParam var3 = (DiamondParam)var2[0];
      var1.append(String.valueOf(var3.getNum()));
      var1.append(":");
      var1.append(String.valueOf(((UserModel)var3.getData()).getUserId()));
      var1.append(":");
      return var1.toString();
   }
}
