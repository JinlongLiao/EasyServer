package io.github.jinlongliao.easy.server.netty.demo.constant;

/**
 * @author: liaojinlong
 * @date: 2022-08-08 18:21
 */
public interface LogicId {
    /**
     * 心跳
     */
    int HEART_BEAT = 0x1;
    /**
     * 用户鉴权
     */
    int USER_AUTH = 0x2;

    /**
     * 用户上下线
     */
    int USER_LINE_STATUS = 0x3;

    /**
     * 创建2v2房间
     */
    int HANDLE_CREATE_2V2_ROOM = 0x4;
    /**
     * 用户行为消息
     */
    int HANDLE_2V2_MESSAGE = 0x5;
    /**
     * 触发历史消息推送
     */
    int HANDLE_HISTORY_MESSAGE = 0x6;
}
