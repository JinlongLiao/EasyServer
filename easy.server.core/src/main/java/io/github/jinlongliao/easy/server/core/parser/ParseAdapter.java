package io.github.jinlongliao.easy.server.core.parser;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 组合解析
 *
 * @author: liaojinlong
 * @date: 2022-09-16 10:28
 */
public class ParseAdapter {

    private final IRequestParseRule parseRule;
    private final IMessageParserCallBack msgHexParserCallBack;

    public ParseAdapter(IRequestParseRule parseRule, IMessageParserCallBack msgHexParserCallBack) {
        this.parseRule = parseRule;
        this.msgHexParserCallBack = msgHexParserCallBack;
    }

    /**
     * Hex Parser Message
     *
     * @param requestStreamFactory Hex Factory
     * @param args                 透传参数
     * @return /
     */
    public Object[] parseHexMsg(IRequestStreamFactory requestStreamFactory, Object... args) {
        return this.parseRule.readHexMsg(requestStreamFactory, msgHexParserCallBack, args);
    }

    /**
     * Servlet Parser Message
     *
     * @param request Servlet
     * @param args    透传参数
     * @return /
     */
    public Object[] parseMsg(HttpServletRequest request, Object... args) {
        return this.parseRule.readServletMsg(request, msgHexParserCallBack, args);
    }
}
