package io.github.jinlongliao.easy.server.netty.demo.core.tcp.handler;


import io.github.jinlongliao.easy.server.netty.demo.constant.ErrorCode;
import io.github.jinlongliao.easy.server.netty.demo.logic.response.ErrorResponse;
import io.github.jinlongliao.easy.server.netty.demo.logic.response.RootResponse;
import io.github.jinlongliao.easy.server.netty.demo.logic.response.StringResponse;
import io.github.jinlongliao.easy.server.netty.demo.logic.response.VoidResponse;
import io.github.jinlongliao.easy.server.core.exception.ValidateException;
import io.github.jinlongliao.easy.server.mapper.exception.MethodInvokeException;
import io.github.jinlongliao.easy.server.utils.json.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

/**
 * 业务操作结果解析类
 *
 * @author liaojinlong
 * @since 2021/1/24 19:18
 */
public class TcpLogicResultHandler {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final JsonHelper jsonHelper;

    public TcpLogicResultHandler(JsonHelper jsonHelper) {
        this.jsonHelper = jsonHelper;
    }

    /**
     * 正常操作解析
     *
     * @param logicId
     * @param obj
     * @return 解析后的结果
     */
    public RootResponse logicResultHandler(String logicId, Object obj) throws Exception {
        if (Objects.isNull(obj)) {
            return VoidResponse.getInstance();
        }
        if (obj instanceof RootResponse) {
            return (RootResponse) obj;
        }
        return new StringResponse(this.jsonHelper.objectToJson(obj));
    }

    /**
     * 异常信息解析
     *
     * @param logicId
     * @param exception
     * @return 解析后的结果
     */
    public RootResponse logicExceptionHandler(String logicId, Exception exception) {
        Throwable throwable = exception;
        if (exception instanceof MethodInvokeException) {
            throwable = ((MethodInvokeException) exception).getTargetException();
        }
        log.warn(throwable.getMessage(), throwable);
        ErrorResponse response;
        if (throwable instanceof ValidateException) {
            response = new ErrorResponse(ErrorCode.PARAM_ERROR.getErrorCode(), ((ValidateException) exception).getLocalizedMessage());
        } else {
            response = new ErrorResponse(ErrorCode.SYS_ERROR.getErrorCode(), ErrorCode.SYS_ERROR.getErrMsg());
        }
        return response;
    }

}
