package io.github.jinlongliao.easy.server.netty.demo.config.swagger.config.api;

import io.github.jinlongliao.easy.server.core.core.MethodParse;
import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.core.model.MsgModel;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.BeanCopier2Utils;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin.servlet.IServletData2Object;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.netty.demo.config.swagger.tcp.TcpClient;
import io.github.jinlongliao.easy.server.netty.demo.constant.LogicId;
import io.github.jinlongliao.easy.server.netty.demo.logic.request.EmptyRequest;
import io.github.jinlongliao.easy.server.netty.demo.logic.response.RootResponse;
import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;
import io.github.jinlongliao.easy.server.swagger.servlet.help.ApiCleanHelper;
import io.github.jinlongliao.easy.server.utils.json.JsonHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: liaojinlong
 * @date: 2022-06-20 14:52
 */
@Controller
public class NotificationUiProxyController {
    private static final HashSet<String> COMMON_PARAM_NAME = new HashSet<>(8, 1L);

    static {

        COMMON_PARAM_NAME.add("version");
        COMMON_PARAM_NAME.add("msgType");
        COMMON_PARAM_NAME.add("extraFlag");
        COMMON_PARAM_NAME.add("userId");

    }

    private final ApiConfig apiConfig;
    private final ApiCleanHelper apiCleanHelper;
    private final JsonHelper jsonHelper;
    private final IServletData2Object<EmptyRequest> servletConverter;
    private final TcpClient tcpClient;
    private final MethodParse parse;


    public NotificationUiProxyController(ApiConfig apiConfig,
                                         ApiCleanHelper apiCleanHelper,
                                         LogicRegisterContext logicRegisterContext,
                                         JsonHelper jsonHelper,
                                         TcpClient tcpClient) {
        this.apiConfig = apiConfig;
        this.apiCleanHelper = apiCleanHelper;
        this.jsonHelper = jsonHelper;
        this.tcpClient = tcpClient;
        this.servletConverter = BeanCopier2Utils.getData2WebObject(EmptyRequest.class);
        this.parse = logicRegisterContext.getParse();
    }


    @RequestMapping("/api/proxy")
    public void proxy(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EmptyRequest emptyRequest = this.servletConverter.toHttpServletRequestConverter(req);
        Channel channel = this.tcpClient.checkChannelActive(emptyRequest.getUserId());
        LogicModel logicModel = this.parse.getLogicDefineCache().get(emptyRequest.getLogicId() + "");
        List<MsgModel> msgModel = logicModel.getMsgModel().stream().filter(n -> !n.isRequestBody() && !COMMON_PARAM_NAME.contains(n.getParamName())).collect(Collectors.toList());
        ByteBuf buffer = Unpooled.buffer(1024, 10240);
        emptyRequest.writeResponse(buffer);
        this.privateWrite(msgModel, req, buffer);
        emptyRequest.endWrite(buffer);
        channel.writeAndFlush(buffer);
        this.writeResponse(emptyRequest, resp);
    }


    @RequestMapping("/api/proxy/api/clean")
    public void cleanApi(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        apiCleanHelper.reset();
    }

    private void writeResponse(EmptyRequest emptyRequest, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        List<RootResponse> responses = this.tcpClient.popAllResponse(emptyRequest.getUserId());
        String jsonArray = jsonHelper.objectToJsonArray(responses);
        resp.getWriter().print(jsonArray);
    }

    private void privateWrite(List<MsgModel> msgModel, HttpServletRequest req, ByteBuf buffer) {
        for (MsgModel model : msgModel) {
            String paramName = model.getParamName();
            String parameter = req.getParameter(paramName);
            Class type = model.getType();
            if (CLassUtils.isStringClass(type)) {
                this.writeString(parameter, model.getLength(), buffer);
                continue;
            }
            if (CLassUtils.isInteger(type)) {
                int value = 0;
                if (StringUtils.isNotBlank(parameter)) {
                    value = Integer.parseInt(parameter);
                }
                buffer.writeIntLE(value);
                continue;
            }
            if (CLassUtils.isByteType(type)) {
                byte value = 0;
                if (StringUtils.isNotBlank(parameter)) {
                    value = Byte.parseByte(parameter);
                }
                buffer.writeIntLE(value);
                continue;
            }
            if (CLassUtils.isLongType(type)) {
                long value = 0;
                if (StringUtils.isNotBlank(parameter)) {
                    value = Long.parseLong(parameter);
                }
                buffer.writeLongLE(value);
                continue;
            }
            if (CLassUtils.isShortType(type)) {
                short value = 0;
                if (StringUtils.isNotBlank(parameter)) {
                    value = Short.parseShort(parameter);
                }
                buffer.writeShortLE(value);
            }
        }
    }


    /**
     * 写字符串
     *
     * @param msg
     * @param arrayLen
     */
    public void writeString(String msg, int arrayLen, ByteBuf byteBuf) {
        byte[] bytes = new byte[arrayLen];
        if (StringUtils.isNotEmpty(msg)) {
            byte[] infoBytes = msg.getBytes(StandardCharsets.UTF_8);
            int len = Math.min(infoBytes.length, arrayLen);
            System.arraycopy(infoBytes, 0, bytes, 0, len);
        }
        byteBuf.writeBytes(bytes);
    }

    @Scheduled(fixedDelay = 2000)
    public void scheduleHeart() {
        Map<Integer, Channel> channelMap = this.tcpClient.getChannelMap();
        for (Map.Entry<Integer, Channel> entry : channelMap.entrySet()) {
            EmptyRequest emptyRequest = new EmptyRequest();
            emptyRequest.setLogicId(LogicId.HEART_BEAT);
            emptyRequest.setUserId(entry.getKey());
            ByteBuf buffer = Unpooled.buffer(1024, 1024);
            emptyRequest.writeResponse(buffer);
            emptyRequest.endWrite(buffer);
            entry.getValue().writeAndFlush(buffer);
        }
    }
}
