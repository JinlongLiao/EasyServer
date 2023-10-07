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
import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.core.parser.ParseAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    private static final Map<String, LogicDispatcher> LOGIC_DISPATCHER_CACHE = new ConcurrentHashMap<>(16);
    private static final Map<LogicModel, ParseAdapter> PARSE_ADAPTER_CACHE = new ConcurrentHashMap<>(16);
    private final MethodParse methodParse;
    /**
     * 业务处理id
     */
    private static final String MSG_TYPE = "logicId";
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
    protected void todoLogic(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String logicId;
        try {
            logicId = (req.getParameter(MSG_TYPE));
        } catch (NumberFormatException ignore) {
            logicId = (req.getParameter(KEY));
        }
        LogicModel logicModel = methodParse.getLogicDefineCache().get(logicId);
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Content-Type", "application/json");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json;charset=UTF-8");

        try {
            Object[] args = getArgs(req, resp, logicModel);
            LogicDispatcher logicDispatcher = getLogicDispatcher(logicId);
            String result = logicDispatcher.dispatcher(logicId, args);
            resp.getWriter().write(result);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public String[] supportPath() {
        return new String[]{"/handlerMsg"};
    }

    private LogicDispatcher getLogicDispatcher(String logicId) {
        LogicDispatcher logicDispatcher;
        logicDispatcher = LOGIC_DISPATCHER_CACHE.computeIfAbsent(logicId, key -> new LogicDispatcher(
                methodParse.getLogicDefineCache().get(key), new ILogicResultHandler() {
            @Override
            public String logicResultHandler(String logicId, Object obj) throws Exception {
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
            public String logicExceptionHandler(String logicId, Exception exception) throws Exception {
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

    private Object[] getArgs(HttpServletRequest req, HttpServletResponse resp, LogicModel logicModel) {

        ParseAdapter parseAdapter = PARSE_ADAPTER_CACHE.computeIfAbsent(logicModel, key ->
                new ParseAdapter(new StaticRequestParseRule(logicModel), new DemoMessageParserCallBack(beanMapper)));
        return parseAdapter.parseMsg(req, req, resp);
    }

    @Override
    public String getDescription() {
        return "Logic 入口Servlet";
    }
}
