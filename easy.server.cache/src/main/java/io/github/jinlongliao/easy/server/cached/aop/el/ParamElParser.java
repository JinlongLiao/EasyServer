package io.github.jinlongliao.easy.server.cached.aop.el;

/**
 * 解析
 *
 * @author: liaojinlong
 * @date: 2023/7/5 17:01
 */
public interface ParamElParser {
    String parseValue(StringBuilder stringBuilder,Object[] param);
}
