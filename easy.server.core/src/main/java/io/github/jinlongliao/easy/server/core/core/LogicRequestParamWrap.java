package io.github.jinlongliao.easy.server.core.core;

import java.util.Objects;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;

import java.lang.annotation.Annotation;

/**
 * @date 2023-02-15 19:56
 * @author: liaojinlong
 * @description: 用于展示
 **/

public class LogicRequestParamWrap implements LogicRequestParam {
    /**
     * 参数名称
     *
     * @return 参数名称
     */
    private String value;

    /**
     * @return 参数 长度
     */
    private Integer length;

    /**
     * 动态长度
     * 只支持String
     */
    private Boolean dynamicLength;


    /**
     * 参数默认值
     *
     * @return /
     */
    private String defaultValue;

    /**
     * 公共属性
     *
     * @return /
     */
    private Boolean isCommon;

    /**
     * 表明此属性位 内部解析
     *
     * @return /
     */
    private Boolean innerParse;
    private final LogicRequestParam logicRequestParam;

    public LogicRequestParamWrap(LogicRequestParam logicRequestParam) {
        this.logicRequestParam = logicRequestParam;
    }

    @Override
    public String value() {
        return Objects.isNull(value) ? logicRequestParam.value() : value;
    }

    @Override
    public int length() {
        return Objects.isNull(length) ? logicRequestParam.length() : length;
    }

    @Override
    public boolean dynamicLength() {
        return logicRequestParam.dynamicLength();
    }

    @Override
    public String defaultValue() {
        return Objects.isNull(defaultValue) ? logicRequestParam.defaultValue() : defaultValue;
    }

    @Override
    public boolean isCommon() {
        return Objects.isNull(isCommon) ? logicRequestParam.isCommon() : isCommon;
    }

    @Override
    public boolean innerParse() {
        return Objects.isNull(innerParse) ? logicRequestParam.innerParse() : innerParse;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return logicRequestParam.annotationType();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public void setDynamicLength(Boolean dynamicLength) {
        this.dynamicLength = dynamicLength;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setCommon(Boolean common) {
        isCommon = common;
    }

    public void setInnerParse(Boolean innerParse) {
        this.innerParse = innerParse;
    }
}
