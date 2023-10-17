package io.github.jinlongliao.easy.server.kotlin.demo.logic.annotation

/**
 * 消息ID
 *
 * @author: liaojinlong
 * @date: 2023/1/31 22:17
 */
enum class MsgId(val logicId: String, val desc: String) {
    /**
     * "测试Groovy+缓存"
     */
    TEST0("94", "Same95 测试Groovy+缓存"),
    TEST1("95", "测试Groovy+缓存"),
    TEST2("99", "测试Groovy"),
    TEST3("100", "测试Cache");

    override fun toString(): String {
        return "MsgId{" +
                "logicId=" + logicId +
                ", desc='" + desc + '\'' +
                '}'
    }
}
