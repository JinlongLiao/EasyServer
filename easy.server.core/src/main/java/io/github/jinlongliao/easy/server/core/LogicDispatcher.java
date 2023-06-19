package io.github.jinlongliao.easy.server.core;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.method.DirectMethod;
import io.github.jinlongliao.easy.server.core.exception.ParamValidateException;
import io.github.jinlongliao.easy.server.core.exception.ReturnValidateException;
import io.github.jinlongliao.easy.server.core.exception.ValidateException;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.*;
import jakarta.validation.executable.ExecutableValidator;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;

/**
 * 逻辑转发类
 *
 * @author liaojinlong
 * @since 2021/1/24 18:45
 */
public class LogicDispatcher {
    private static final Logger log = LoggerFactory.getLogger(LogicDispatcher.class);
    private final ILogicResultHandler logicResultHandler;
    private final DirectMethod logic;
    private final Object logicBean;
    /**
     * 校验
     */
    private final Validator validator;
    private static final ValidatorFactory VALIDATOR_FACTORY;

    static {
        HibernateValidatorConfiguration configure = Validation.byProvider(HibernateValidator.class).configure();
        try {
            boolean version6 = true;
            Class<?> base = null;
            try {
                base = AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.loadClass("org.hibernate.validator.internal.engine.AbstractConfigurationImpl");
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
        VALIDATOR_FACTORY = configure.buildValidatorFactory();
    }

    /**
     * @param logicModel
     * @param logicResultHandler
     */
    public LogicDispatcher(LogicModel logicModel, ILogicResultHandler logicResultHandler) {
        this.logic = logicModel.getDirectMethod();
        this.logicBean = logicModel.getObject();
        this.logicResultHandler = logicResultHandler;
        this.validator = VALIDATOR_FACTORY.getValidator();
    }

    /**
     * 业务转发，返回统一结果
     *
     * @param logicId
     * @param args
     * @return 结果
     */
    public String dispatcher(String logicId, Object[] args) throws Exception {
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
        return handleResult(logicId, args, result, exception);
    }

    /**
     * 操作结果解析
     *
     * @param obj
     * @param exception
     * @return 操作结果
     */
    protected String handleResult(String logicId, Object[] args, Object obj, Exception exception) throws Exception {
        String result;
        if (exception == null) {
            result = logicResultHandler.logicResultHandler(logicId, args, obj);
        } else {
            result = logicResultHandler.logicExceptionHandler(logicId, args, obj, exception);
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
                    sb.append("; ");
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
