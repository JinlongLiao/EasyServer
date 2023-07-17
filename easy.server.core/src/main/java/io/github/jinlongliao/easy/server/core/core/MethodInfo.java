package io.github.jinlongliao.easy.server.core.core;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.method.DirectMethod;
import io.github.jinlongliao.easy.server.core.model.MsgModel;

import java.util.List;

/**
 * 函数模型
 *
 * @author liaojinlong
 * @since 2022-01-28 12:16
 */
public class MethodInfo {
    /**
     * 消息ID
     */
    private final String logicId;
    private final DirectMethod directMethod;
    private final List<MsgModel> msgModels;
    /**
     * 用于 OpenApi 测试使用
     */
    private final String desc;

    public MethodInfo(String logicId, DirectMethod directMethod, List<MsgModel> msgModels, String desc) {
        this.logicId = logicId;
        this.directMethod = directMethod;
        this.msgModels = msgModels;
        this.desc = desc;
    }


    public String getDesc() {
        return desc;
    }

    public DirectMethod getDirectMethod() {
        return directMethod;
    }

    public List<MsgModel> getMsgModels() {
        return msgModels;
    }

    public String getLogicId() {
        return logicId;
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "logicId=" + logicId +
                ", directMethod=" + directMethod +
                ", msgModels=" + msgModels +
                ", desc='" + desc + '\'' +
                '}';
    }
}
