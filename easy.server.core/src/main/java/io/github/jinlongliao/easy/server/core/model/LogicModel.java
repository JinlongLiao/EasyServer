package io.github.jinlongliao.easy.server.core.model;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.method.DirectMethod;

import java.util.List;
import java.util.Objects;

/**
 * 接口定义缓存
 *
 * @author liaojinlong
 * @since 2021/1/22 17:00
 */
public class LogicModel {
    /**
     * 对应实体对象
     */
    private final Object object;
    private Class<?> sourceClass;
    /**
     * 执行的方法
     */
    private final DirectMethod directMethod;
    /**
     * 参数模型
     */
    private final List<MsgModel> msgModel;
    /**
     * 描述
     */
    private final String description;
    private final String beanName;

    /**
     * @param object
     * @param directMethod
     * @param msgModel
     * @param description
     * @param beanName
     */
    public LogicModel(Object object, DirectMethod directMethod, List<MsgModel> msgModel, String description, String beanName) {
        this.object = object;
        if (Objects.nonNull(object)) {
            this.sourceClass = object.getClass();
        }
        this.directMethod = directMethod;
        this.msgModel = msgModel;
        this.description = description;
        this.beanName = beanName;
    }

    /**
     * @param object
     * @param sourceClass
     * @param directMethod
     * @param msgModel
     * @param description
     * @param beanName
     */
    public LogicModel(Object object, Class<?> sourceClass, DirectMethod directMethod, List<MsgModel> msgModel, String description, String beanName) {
        this.object = object;
        this.sourceClass = sourceClass;
        this.directMethod = directMethod;
        this.msgModel = msgModel;
        this.description = description;
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getDescription() {
        return description;
    }


    public Object getObject() {
        return object;
    }

    public List<MsgModel> getMsgModel() {
        return msgModel;
    }

    public DirectMethod getDirectMethod() {
        return directMethod;
    }

    public Class<?> getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(Class<?> sourceClass) {
        this.sourceClass = sourceClass;
    }
}
