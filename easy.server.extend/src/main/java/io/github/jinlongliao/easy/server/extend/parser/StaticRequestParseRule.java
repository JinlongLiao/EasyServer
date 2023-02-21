package io.github.jinlongliao.easy.server.extend.parser;

import io.github.jinlongliao.easy.server.mapper.utils.StringUtil;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.core.parser.IDefaultValueConverter;
import io.github.jinlongliao.easy.server.core.parser.IMessageParserCallBack;
import io.github.jinlongliao.easy.server.core.parser.IRequestParseRule;
import io.github.jinlongliao.easy.server.core.parser.IRequestStreamFactory;
import io.github.jinlongliao.easy.server.core.parser.inner.AbstractRequestParseRule;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 静态生成代码 Req 解析
 *
 * @date 2023-01-06 16:36
 * @author: liaojinlong
 * @description: /
 **/

public class StaticRequestParseRule extends AbstractRequestParseRule {
    private IRequestParseRule inner_0;

    public StaticRequestParseRule(LogicModel logicModel) {
        this(IDefaultValueConverter.DEFAULT, logicModel);
    }

    public StaticRequestParseRule(IDefaultValueConverter defaultValueConverter, LogicModel logicModel) {
        super(defaultValueConverter, logicModel);
        this.init0();
    }

    private static final Map<String, IRequestParseRule> REQUEST_PARSE_RULE_CACHE = new ConcurrentHashMap<>(32);


    private void init0() {
        String proxyObjectName = getProxyObjectName(logicModel);
        inner_0 = REQUEST_PARSE_RULE_CACHE.get(proxyObjectName);
        if (Objects.isNull(inner_0)) {
            synchronized (logicModel) {
                if (Objects.isNull(inner_0)) {
                    inner_0 = StaticRequestParseRuleBuilder.buildRequestParseRule(proxyObjectName,
                            this.defaultValueConverter,
                            this.logicModel,
                            this.rules);
                    REQUEST_PARSE_RULE_CACHE.put(proxyObjectName, inner_0);
                }
            }
        }
    }

    @Override
    public Object[] readHexMsg(IRequestStreamFactory request, IMessageParserCallBack msgHexParserCallBack) {
        return this.inner_0.readHexMsg(request, msgHexParserCallBack);
    }


    @Override
    public Object[] readServletMsg(HttpServletRequest request, IMessageParserCallBack msgHexParserCallBack) {
        return this.inner_0.readServletMsg(request, msgHexParserCallBack);
    }

    private static String getProxyObjectName(LogicModel logicModel) {
        Class<?> sourceClass = logicModel.getSourceClass();
        return (sourceClass.getPackage().getName())
                .replace('.', '/')
                + "/parse/"
                + sourceClass.getSimpleName()
                + StringUtil.upperFirst(logicModel.getDirectMethod().getMethod().getName()) +
                "StaticRequestParseRule";
    }
}
