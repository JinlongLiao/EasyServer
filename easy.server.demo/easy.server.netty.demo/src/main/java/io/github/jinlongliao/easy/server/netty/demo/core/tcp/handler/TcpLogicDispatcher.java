package io.github.jinlongliao.easy.server.netty.demo.core.tcp.handler;

import io.github.jinlongliao.easy.server.netty.demo.core.tcp.conn.TcpConnection;
import io.github.jinlongliao.easy.server.netty.demo.logic.response.RootResponse;
import io.github.jinlongliao.easy.server.core.exception.ParamValidateException;
import io.github.jinlongliao.easy.server.core.exception.ReturnValidateException;
import io.github.jinlongliao.easy.server.core.exception.ValidateException;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.method.DirectMethod;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;

/**
 * 逻辑转发类
 *
 * @author liaojinlong
 * @since 2021/1/24 18:45
 */
public class TcpLogicDispatcher {
    private static final Logger log = LoggerFactory.getLogger(TcpLogicDispatcher.class);
    private final TcpLogicResultHandler logicResultHandler;
    private final String logicId;
    private final LogicModel logicModel;
    private final DirectMethod logic;
    private final Object logicBean;
    /**
     * 校验
     */
    private final Validator validator;
    private static final ValidatorFactory validatorFactory;

    static {
        HibernateValidatorConfiguration configure = Validation.byProvider(HibernateValidator.class).configure();
        try {
            boolean version6 = true;
            Class<?> base = null;
            try {
                base = Class.forName("org.hibernate.validator.internal.engine.AbstractConfigurationImpl");
            } catch (ClassNotFoundException e) {
                configure = configure.failFast(true);
                version6 = false;
            }
            if (version6) {
                //  HibernateValidator 6
                Field failFast = base.getDeclaredField("failFast");
                failFast.setAccessible(true);
                failFast.set(configure, true);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error("hibernate failFast error");
        }
        validatorFactory = configure.buildValidatorFactory();
    }

    /**
     * @param logicId
     * @param logicModel
     * @param logicResultHandler
     */
    public TcpLogicDispatcher(String logicId, LogicModel logicModel, TcpLogicResultHandler logicResultHandler) {
        this.logicId = logicId;
        this.logicModel = logicModel;
        this.logic = logicModel.getDirectMethod();
        this.logicBean = logicModel.getObject();
        this.logicResultHandler = logicResultHandler;
        this.validator = validatorFactory.getValidator();
    }

    /**
     * 业务转发，返回统一结果
     *
     * @param logicId
     * @param args
     * @return 结果
     */
    public RootResponse dispatcher(String logicId, Object[] args, TcpConnection localTcpConnection) throws Exception {
        Exception exception = null;
        Object result = null;
        try {
            ExecutableValidator executableValidator = validator.forExecutables();
            Set<ConstraintViolation<Object>> paramConstraintViolations = executableValidator.validateParameters(logicBean, logic.getMethod(), args);
            validate(paramConstraintViolations, true);
            result = logic.invoke(logicBean, args);
            Set<ConstraintViolation<Object>> returnConstraintViolations = executableValidator.validateReturnValue(logicBean, logic.getMethod(), result);
            validate(returnConstraintViolations, false);
        } catch (Exception e) {
            exception = e;
        }
        RootResponse iResponse = handleResult(result, exception);
        localTcpConnection.writeResponse(iResponse);
        return iResponse;
    }

    /**
     * 操作结果解析
     *
     * @param obj
     * @param exception
     * @return 操作结果
     */
    protected RootResponse handleResult(Object obj, Exception exception) throws Exception {
        RootResponse result;
        if (exception == null) {
            result = logicResultHandler.logicResultHandler(logicId, obj);
        } else {
            result = logicResultHandler.logicExceptionHandler(logicId, exception);
        }
        return result;
    }

    /**
     * 将Message 公共校验方法，转移至Message 本身，避免Logic 中的代码冗余
     * 集成 JSR 349  Bean Validation 1.1
     *
     * @param violations
     * @param type
     */
    private void validate(Set<ConstraintViolation<Object>> violations, boolean type) throws ValidateException {
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Iterator<ConstraintViolation<Object>> it = violations.iterator(); it.hasNext(); ) {
                ConstraintViolation<Object> violation = it.next();
                sb.append(violation.getMessage());
                if (it.hasNext()) {
                    sb.append(";");
                }
            }
            if (type) {
                throw new ParamValidateException(sb.toString());
            } else {
                throw new ReturnValidateException(sb.toString());
            }
        }
    }
}
