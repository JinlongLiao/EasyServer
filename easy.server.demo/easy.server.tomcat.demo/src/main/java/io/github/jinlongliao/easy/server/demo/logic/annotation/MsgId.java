package io.github.jinlongliao.easy.server.demo.logic.annotation;

/**
 * 消息ID
 *
 * @author: liaojinlong
 * @date: 2023/1/31 22:17
 */
public enum MsgId {
    /**
     * "测试Groovy+缓存"
     */
    TEST0("94", "Same95 测试Groovy+缓存"),
    TEST1("95", "测试Groovy+缓存"),
    TEST2("99", "测试Groovy"),
    ;
    private final String logicId;
    private final String desc;

    MsgId(String logicId, String desc) {
        this.logicId = logicId;
        this.desc = desc;
    }

    public String getLogicId() {
        return logicId;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "MsgId{" +
                "logicId=" + logicId +
                ", desc='" + desc + '\'' +
                '}';
    }
}
