package io.github.jinlongliao.easy.server.netty.demo.constant;

/**
 * @author: liaojinlong
 * @date: 2022-08-08 16:56
 */
public enum ErrorCode {
    /**
     * 消息不存在
     */
    MSG_NOT_FOUNT(-1, "消息不存在"),
    /**
     * 系统错误
     */
    SYS_ERROR(9999, "系统错误，请重新操作"),
    /**
     * 连接异常中断
     */
    CONN_ERROR(1000, "连接异常中断"),
    /**
     * 参数有误，请检查
     */
    PARAM_ERROR(1000, "参数有误，请检查"),
    /**
     * 用户鉴权失败
     */
    AUTHED_ERROR(101, "用户鉴权失败"),
    /**
     * 需要鉴权
     */
    NEED_AUTHED(100, "需要鉴权"),
    /**
     * 帐号正在游戏中
     */
    USER_IN_GAME(102, "帐号正在游戏中"),

    /**
     * 账号多设备登录
     */
    USER_MULTI_LOGIN(103, "账号多设备登录"),

    /**
     * 用户不在线
     */
    USER_NOT_ONLINE(104, "用户不在线"),

    /**
     * 正常响应
     */
    OK_200(200, "200 Ok 正常响应"),

    ;
    private final int errorCode;
    private final String errMsg;

    ErrorCode(int errorCode, String errMsg) {
        this.errorCode = errorCode;
        this.errMsg = errMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    @Override
    public String toString() {
        return " {" +
                "errorCode=" + errorCode +
                ", errMsg='" + errMsg + '\'' +
                '}';
    }
}
