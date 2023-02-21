package io.github.jinlongliao.easy.server.core.parser.inner;


import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.core.model.MsgModel;

import io.github.jinlongliao.easy.server.core.parser.DynamicString;
import io.github.jinlongliao.easy.server.core.parser.IDefaultValueConverter;
import io.github.jinlongliao.easy.server.core.parser.IRequestParseRule;
import io.github.jinlongliao.easy.server.core.parser.MeType;

import java.util.*;

/**
 * 消息解析规则
 *
 * @author liaojinlong
 * @since 2021/1/22 20:12
 */
public abstract class AbstractRequestParseRule implements IRequestParseRule {
    public static final String REQUEST_ARG = "__REQUEST_ARG__";
    protected final IDefaultValueConverter defaultValueConverter;
    /**
     * 构建规则
     */
    protected List<MeType> rules;

    protected final LogicModel logicModel;


    public AbstractRequestParseRule(LogicModel logicModel) {
        this(IDefaultValueConverter.DEFAULT, logicModel);
    }


    public AbstractRequestParseRule(IDefaultValueConverter defaultValueConverter, LogicModel logicModel) {
        this.defaultValueConverter = defaultValueConverter;
        this.logicModel = logicModel;
        this.initParseRule(logicModel);
    }

    protected void addType(MeType meType) {
        rules.add(meType);
    }

    /**
     * 获取构建规则
     *
     * @return /
     */
    protected void initParseRule(LogicModel logicModel) {
        List<MsgModel> msgModels = logicModel.getMsgModel();
        // 初始化Len
        rules = new ArrayList<>(msgModels.size());
        for (MsgModel msgModel : msgModels) {
            final String paramName = msgModel.getParamName();
            Object def = defaultValueConverter.toConverterDefaultValue(msgModel);
            // 包装参数
            if (msgModel.isRequestBody()) {
                this.addType(new MeType(msgModel.getType(), 0, paramName, def, false, true));
                continue;
            }
            // 内部参数
            if (msgModel.isInnerField()) {
                MeType meType = new MeType(msgModel.getType(), 0, paramName, def, true);
                meType.setInnerField(true);
                this.addType(meType);
                continue;
            }
            // 公共参数
            if (msgModel.isCommon()) {
                this.addType(new MeType(msgModel.getType(), 0, paramName, def, true));
                continue;
            }
            Class<?> fieldClazz = msgModel.getType();
            if (fieldClazz == String.class) {
                boolean dynamicLength = msgModel.isDynamicLength();
                if (dynamicLength) {
                    this.addType(new MeType(DynamicString.class, msgModel.getLength(), paramName, def));
                } else {
                    // 当变量为字符串类型时，获取该字符长度
                    this.addType(new MeType(fieldClazz, msgModel.getLength(), paramName, def));
                }
            } else if (CLassUtils.isBool(fieldClazz)) {
                this.addType(new MeType(fieldClazz, 0, paramName, def));
            } else if (CLassUtils.isLong(fieldClazz)) {
                this.addType(new MeType(fieldClazz, 0, paramName, def));
            } else if (CLassUtils.isInteger(fieldClazz)) {
                this.addType(new MeType(fieldClazz, 0, paramName, def));
            } else if (CLassUtils.isByte(fieldClazz)) {
                this.addType(new MeType(fieldClazz, 0, paramName, def));
            } else if (CLassUtils.isShort(fieldClazz)) {
                this.addType(new MeType(fieldClazz, 0, paramName, def));
            } else if (fieldClazz == List.class) {
                // 目前list里只能支持int类型
                this.addType(new MeType(fieldClazz, 0, paramName, def));
            } else if (fieldClazz.isArray()) {
                // 当变量为数组类型时，获取该数组长度
                this.addType(new MeType(fieldClazz, msgModel.getLength(), paramName, def));
            } else {
                throw new RuntimeException("参数类型错误：" + fieldClazz.getName());
            }
        }

    }

    public IDefaultValueConverter getDefaultValueConverter() {
        return defaultValueConverter;
    }

    public List<MeType> getRules() {
        return rules;
    }


    public LogicModel getLogicModel() {
        return logicModel;
    }
}
