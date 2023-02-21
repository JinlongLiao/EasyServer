package io.github.jinlongliao.easy.server.core.parser;

import io.github.jinlongliao.easy.server.utils.common.IPAddressUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 针对16进制数据 ，包装类的数据解析
 *
 * @author liaojinlong
 * @since 2021/1/23 01:33
 */
public interface IMessageParserCallBack {
    /**
     * 读取16进制消息体消息，包装为对象
     *
     * @param request
     * @param meType
     * @param arg     扩展参数
     * @return 解析后的数据
     */
    Object parserParamBody(IRequestStreamFactory request, MeType meType, Object arg);

    /**
     * 读取HttpServletRequest消息，包装为对象
     *
     * @param request
     * @param meType
     * @param arg     扩展参数
     * @return 解析后的数据
     */
    Object parserParamBody(HttpServletRequest request, MeType meType, Object arg);

    /**
     * 读取16进制消息体消息，包装为公共属性
     *
     * @param request
     * @param meType
     * @param arg     扩展参数
     * @return 解析后的数据
     */
    Object parserCommonParam(IRequestStreamFactory request, MeType meType, Object arg);

    /**
     * 读取HttpServletRequest消息，公共属性
     *
     * @param request
     * @param meType
     * @param arg     扩展参数
     * @return 解析后的数据
     */
    Object parserCommonParam(HttpServletRequest request, MeType meType, Object arg);

    default Object parserInnerFiled(Object args, MeType meType, Object arg) {
        if (meType.getParamName().equals("__CLIENT_IP__")) {
            if (args instanceof HttpServletRequest) {
                return IPAddressUtil.getIp((HttpServletRequest) args);
            } else if (args instanceof IRequestStreamFactory) {
                return ((IRequestStreamFactory) args).getClientIp();
            }
        }
        return IPAddressUtil.DEFAULT;
    }
}
