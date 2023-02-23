package io.github.jinlongliao.easy.server.netty.demo.logic.request;

import io.github.jinlongliao.easy.server.core.core.MethodParse;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.core.parser.ParseAdapter;
import io.github.jinlongliao.easy.server.extend.parser.StaticRequestParseRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;


/**
 * 消息转换
 *
 * @author liaojinlong
 * @since 2021-12-27 14:09
 */
public class MsgReflectHelper {
    private final MethodParse methodParse;
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Map<Integer, ParseAdapter> parseRuleMap = new HashMap<>(32);
    private static final HashSet<String> COMMON_PARAM_NAME = new HashSet<>(16);

    static {
        COMMON_PARAM_NAME.add("version");
        COMMON_PARAM_NAME.add("msgType");
        COMMON_PARAM_NAME.add("extraFlag");
        COMMON_PARAM_NAME.add("userId");
    }

    public MsgReflectHelper(MethodParse methodParse) {
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
        byte version = request.readByte();
        int msgType = request.readInt();
        int extraFlag = request.readInt();
        int userId = request.readInt();
        LogicModel logicModel;
        boolean isNull = Objects.isNull(logicModel = methodParse.getLogicDefineCache().get(msgType));
        if (isNull) {
            throw new RuntimeException(String.format("msgType : [%d] 此消息类型不存在", msgType));
        }
        LogicRequest requestMsgInfo = new LogicRequest(logicModel);
        this.putCommonValue(requestMsgInfo, version, msgType, extraFlag, userId);
        request.setRequest(requestMsgInfo);
        ParseAdapter parseRule = parseRuleMap.computeIfAbsent(msgType, k -> new ParseAdapter(new StaticRequestParseRule(  logicModel), new TcpMsgParserCallBack()));
        if (log.isDebugEnabled()) {
            log.debug("[gold interface msg : {}]", requestMsgInfo);
        }
        requestMsgInfo.setArgs(parseRule.parseHexMsg(request));
        return requestMsgInfo;
    }

    private void putCommonValue(LogicRequest requestMsgInfo, byte version, int msgType, int extraFlag, int userId) {

        requestMsgInfo.setUserId(userId);
    }
}
