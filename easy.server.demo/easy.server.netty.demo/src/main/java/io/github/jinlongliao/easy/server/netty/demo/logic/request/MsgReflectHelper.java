package io.github.jinlongliao.easy.server.netty.demo.logic.request;

import io.github.jinlongliao.easy.server.core.core.MethodParse;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.core.parser.ParseAdapter;
import io.github.jinlongliao.easy.server.extend.parser.StaticRequestParseRule;
import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * 消息转换
 *
 * @author liaojinlong
 * @since 2021-12-27 14:09
 */
public class MsgReflectHelper {
    private final TcpConnectionFactory tcpConnectionFactory;

    private final MethodParse methodParse;
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Map<String, ParseAdapter> parseRuleMap = new HashMap<>(32);

    public MsgReflectHelper(TcpConnectionFactory tcpConnectionFactory, MethodParse methodParse) {
        this.tcpConnectionFactory = tcpConnectionFactory;
        this.methodParse = methodParse;
    }

    /**
     * transfer msg info from request stream.
     *
     * @param request 请求
     * @return
     */
    public LogicRequest transferMsgInfo(RequestStreamFactory request) {
        // 获取签名密钥
        String logicId = request.readInt() + "";
        int userId = request.readInt();
        LogicModel logicModel;
        boolean isNull = Objects.isNull(logicModel = methodParse.getLogicDefineCache().get(logicId));
        if (isNull) {
            throw new RuntimeException(String.format("logicId : [%s] 此消息类型不存在", logicId));
        }
        LogicRequest requestMsgInfo = new LogicRequest(logicModel);
        this.putCommonValue(requestMsgInfo, logicId, userId);
        request.setRequest(requestMsgInfo);
        ParseAdapter parseRule = parseRuleMap.computeIfAbsent(logicId, k -> new ParseAdapter(new StaticRequestParseRule(logicModel), new TcpMsgParserCallBack()));
        if (log.isDebugEnabled()) {
            log.debug("[gold interface msg : {}]", requestMsgInfo);
        }
        requestMsgInfo.setArgs(parseRule.parseHexMsg(request));
        return requestMsgInfo;
    }

    private void putCommonValue(LogicRequest requestMsgInfo, String logicId, int userId) {
        requestMsgInfo.setLogicId(logicId);
        requestMsgInfo.setUserId(userId);
    }
}
