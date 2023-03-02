package io.github.jinlongliao.easy.server.core.parser;


import javax.servlet.http.HttpServletRequest;

/**
 * 读取
 *
 * @date 2023-01-06 15:16
 * @author: liaojinlong
 * @description: /
 **/

public interface IRequestParseRule {
    /**
     * 读取 HEX
     *
     * @param request               请求
     * @param messageParserCallBack 回调 处理函数
     * @param extraParams           扩展 参数
     * @return /
     */
    Object[] readHexMsg(IRequestStreamFactory request, IMessageParserCallBack messageParserCallBack, Object... extraParams);

    /**
     * 读取 Form-Data
     *
     * @param request               请求
     * @param messageParserCallBack 回调 处理函数
     * @param extraParams           扩展 参数
     * @return /
     */
    Object[] readServletMsg(HttpServletRequest request, IMessageParserCallBack messageParserCallBack, Object... extraParams);

}
