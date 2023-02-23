package io.github.jinlongliao.easy.server.netty.demo.logic.request;

/**
 * 服务器公共头抽取
 *
 * @author: liaojinlong
 * @date: 2022-08-03 15:32
 */
public abstract class IRequest {


    /**
     * 消息类型
     */
    private String logicId;


    /**
     * 用户Id
     */
    private int userId;

    public String getLogicId() {
        return logicId;
    }

    public void setLogicId(String logicId) {
        this.logicId = logicId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "IRequest{" +
                "logicId=" + logicId +
                ", userId=" + userId +
                '}';
    }
}
