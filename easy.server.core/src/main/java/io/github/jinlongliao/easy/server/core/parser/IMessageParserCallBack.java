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
     * @param args    扩展参数
     * @return 解析后的数据
     */
    Object parserParamBody(IRequestStreamFactory request, MeType meType, Object... args);

    /**
     * 读取HttpServletRequest消息，包装为对象
     *
     * @param request
     * @param meType
     * @param args    扩展参数
     * @return 解析后的数据
     */
    Object parserParamBody(HttpServletRequest request, MeType meType, Object... args);

    /**
     * 读取16进制消息体消息，包装为公共属性
     *
     * @param request
     * @param meType
     * @param args    扩展参数
     * @return 解析后的数据
     */
    Object parserCommonParam(IRequestStreamFactory request, MeType meType, Object... args);

    /**
     * 读取HttpServletRequest消息，公共属性
     *
     * @param request
     * @param meType
     * @param args    扩展参数
     * @return 解析后的数据
     */
    Object parserCommonParam(HttpServletRequest request, MeType meType, Object... args);

    default Object parserInnerFiled(Object source, MeType meType, Object... args) {
        HttpServletRequest request = (HttpServletRequest) args[0];
        String paramName = meType.getParamName();
        if ("__CLIENT_IP__".equals(paramName)) {
            return IPAddressUtil.getIp(request);
        } else if ("http_request".equals(paramName)) {
            return args[0];
        } else if ("http_response".equals(paramName)) {
            return args[1];
        }
        return null;
    }
}
