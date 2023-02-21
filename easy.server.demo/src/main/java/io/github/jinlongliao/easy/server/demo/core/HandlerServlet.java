package io.github.jinlongliao.easy.server.demo.core;

import io.github.jinlongliao.easy.server.mapper.exception.MethodInvokeException;
import io.github.jinlongliao.easy.server.mapper.spring.IBeanMapper;
import io.github.jinlongliao.easy.server.extend.parser.StaticRequestParseRule;
import io.github.jinlongliao.easy.server.extend.response.ICommonResponse;
import io.github.jinlongliao.easy.server.demo.core.parser.DemoMessageParserCallBack;
import io.github.jinlongliao.easy.server.demo.logic.param.UserModel;
import io.github.jinlongliao.easy.server.servlet.BaseHttpServlet;
import io.github.jinlongliao.easy.server.utils.common.HexUtil;
import io.github.jinlongliao.easy.server.utils.json.JsonHelper;
import io.github.jinlongliao.easy.server.core.ILogicResultHandler;
import io.github.jinlongliao.easy.server.core.LogicDispatcher;
import io.github.jinlongliao.easy.server.core.core.MethodParse;
import io.github.jinlongliao.easy.server.core.core.spring.register.LogicRegisterContext;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.core.parser.ParseAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author LiaoJL
 * @description 业务转发核心Servlet
 * @email jinlongliao@foxmail.com
 * @date 2021/2/18 22:36
 */
@Component
public class HandlerServlet extends BaseHttpServlet<UserModel> {
    private static final Logger log = LoggerFactory.getLogger(HandlerServlet.class);
    private static final Map<Integer, LogicDispatcher> LOGIC_DISPATCHER_CACHE = new ConcurrentHashMap<>(16);
    private static final Map<LogicModel, ParseAdapter> PARSE_ADAPTER_CACHE = new ConcurrentHashMap<>(16);
    private final MethodParse methodParse;
    /**
     * 业务处理id
     */
    private static final String MSG_TYPE = "msgType";
    private static final String KEY = "key";
    private final JsonHelper jsonHelper;
    private final IBeanMapper beanMapper;

    public HandlerServlet(LogicRegisterContext logicRegisterContext, JsonHelper jsonHelper, IBeanMapper beanMapper) {
        super(beanMapper);
        this.methodParse = logicRegisterContext.getParse();
        this.jsonHelper = jsonHelper;
        this.beanMapper = beanMapper;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int msgType = 0;
        try {
            msgType = Integer.parseInt(req.getParameter(MSG_TYPE));
        } catch (NumberFormatException ignore) {
            msgType = Integer.parseInt(req.getParameter(KEY));
        }
        LogicModel logicModel = methodParse.getLogicDefineCache().get(msgType);
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json;charset=UTF-8");

        try {
            Object[] args = getArgs(req, logicModel);
            LogicDispatcher logicDispatcher = getLogicDispatcher(msgType);
            String result = logicDispatcher.dispatcher(msgType, args);
            resp.getWriter().write(result);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public String[] supportPath() {
        return new String[]{"/handlerMsg"};
    }

    private LogicDispatcher getLogicDispatcher(int msgType) {
        LogicDispatcher logicDispatcher;
        logicDispatcher = LOGIC_DISPATCHER_CACHE.computeIfAbsent(msgType, key -> new LogicDispatcher(
                methodParse.getLogicDefineCache().get(key), new ILogicResultHandler() {
            @Override
            public String logicResultHandler(Integer msgType, Object obj) throws Exception {
                final Map<String, Object> data = new HashMap<>(2, 1.5f);
                data.put("status", 0);
                if (Objects.nonNull(obj)) {
                    if (obj instanceof ICommonResponse) {
                        data.put("hex", HexUtil.byte2Hex(((ICommonResponse) obj).genResHex()));
                    } else {
                        data.put("msg", obj);
                    }
                }
                return jsonHelper.objectToJson(data);
            }

            @Override
            public String logicExceptionHandler(Integer msgType, Exception exception) throws Exception {
                if (exception instanceof MethodInvokeException) {
                    exception = (Exception) ((MethodInvokeException) exception).getTargetException();
                }
                log.error(exception.getLocalizedMessage(), exception);
                final Map<String, Object> data = new HashMap<>(2, 1.5f);
                data.put("status", 1);
                data.put("error", exception.getMessage());
                return data.toString();
            }
        }));

        return logicDispatcher;
    }

    private Object[] getArgs(HttpServletRequest req, LogicModel logicModel) {

        ParseAdapter parseAdapter = PARSE_ADAPTER_CACHE.computeIfAbsent(logicModel, key ->
                new ParseAdapter(new StaticRequestParseRule(logicModel), new DemoMessageParserCallBack(beanMapper)));
        return parseAdapter.parseMsg(req);
    }

    @Override
    public String getDescription() {
        return "Logic 入口Servlet";
    }
}
