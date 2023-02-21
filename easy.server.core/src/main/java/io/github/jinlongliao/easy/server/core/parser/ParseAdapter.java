package io.github.jinlongliao.easy.server.core.parser;

import javax.servlet.http.HttpServletRequest;

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
     * @return /
     */
    public Object[] parseHexMsg(IRequestStreamFactory requestStreamFactory) {
        return this.parseRule.readHexMsg(requestStreamFactory, msgHexParserCallBack);
    }

    /**
     * Servlet Parser Message
     *
     * @param request Servlet
     * @return /
     */
    public Object[] parseMsg(HttpServletRequest request) {
        return this.parseRule.readServletMsg(request, msgHexParserCallBack);
    }
}
