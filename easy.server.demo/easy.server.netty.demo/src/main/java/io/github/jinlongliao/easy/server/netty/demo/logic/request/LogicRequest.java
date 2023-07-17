package io.github.jinlongliao.easy.server.netty.demo.logic.request;


import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.core.model.MsgModel;
import io.github.jinlongliao.easy.server.mapper.annotation.Ignore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author liaojinlong
 * @since 2021/1/25 10:25
 */
public class LogicRequest extends IRequest {

    @Ignore
    private static boolean init = true;
    @Ignore
    private static Set<String> common_field;
    @Ignore
    private LogicModel logicModel;


    /**
     * 反射函数使用的参描述
     */
    @Ignore
    private Object[] args;

    static {
        synchronized (LogicRequest.class) {
            if (init) {
                Field[] declaredFields = IRequest.class.getDeclaredFields();
                common_field = new HashSet<>(declaredFields.length);
                for (Field field : declaredFields) {
                    int modifiers = field.getModifiers();
                    if (!Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers)) {
                        common_field.add(field.getName());
                    }
                }
                init = false;
            }
        }
    }

    public LogicRequest(LogicModel logicModel) {
        this.logicModel = logicModel;
    }

    public LogicRequest() {
        this(null);
    }


    public LogicModel getLogicModel() {
        return logicModel;
    }

    public void setLogicModel(LogicModel logicModel) {
        this.logicModel = logicModel;
    }

    public boolean isCommon(MsgModel model) {
        return common_field.contains(model.getParamName());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());
        if (Objects.nonNull(args)) {
            for (Object arg : args) {
                if (arg instanceof IRequest) {
                    continue;
                }
                builder.append(arg);
            }
        }
        return builder.toString();
    }

    public static boolean isInit() {
        return init;
    }

    public static void setInit(boolean init) {
        LogicRequest.init = init;
    }

    public static Set<String> getCommon_field() {
        return common_field;
    }

    public static void setCommon_field(Set<String> common_field) {
        LogicRequest.common_field = common_field;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
