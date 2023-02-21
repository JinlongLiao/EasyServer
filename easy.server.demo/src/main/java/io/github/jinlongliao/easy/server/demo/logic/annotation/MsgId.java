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
    TEST0(94, "Same95 测试Groovy+缓存"),
    TEST1(95, "测试Groovy+缓存"),
    TEST2(99, "测试Groovy"),
    ;
    private final int mgsId;
    private final String desc;

    MsgId(int mgsId, String desc) {
        this.mgsId = mgsId;
        this.desc = desc;
    }

    public int getMgsId() {
        return mgsId;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "MsgId{" +
                "mgsId=" + mgsId +
                ", desc='" + desc + '\'' +
                '}';
    }
}
