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
     * @param request
     * @param msgHexParserCallBack
     * @return /
     */
    Object[] readHexMsg(IRequestStreamFactory request, IMessageParserCallBack msgHexParserCallBack);

    /**
     * 读取 Form-Data
     *
     * @param request
     * @param msgHexParserCallBack
     * @return /
     */
    Object[] readServletMsg(HttpServletRequest request, IMessageParserCallBack msgHexParserCallBack);

}
