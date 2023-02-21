package io.github.jinlongliao.easy.server.core.parser;


import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.core.parser.inner.RequestParseRule;

/**
 * 消息解析规则
 *
 * @author liaojinlong
 * @since 2021/1/22 20:12
 */
@Deprecated
public class ParseRule extends RequestParseRule {
    public ParseRule(LogicModel logicModel) {
        super(logicModel);
    }

    public ParseRule(IDefaultValueConverter defaultValueConverter, LogicModel logicModel) {
        super(defaultValueConverter, logicModel);
    }
}
