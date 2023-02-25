package io.github.jinlongliao.easy.server.netty.demo.logic.request;


import io.netty.buffer.ByteBuf;


/**
 * @author: liaojinlong
 * @date: 2022-08-09 15:01
 */
public class EmptyRequest {

    /**
     * 消息类型
     */
    private int logicId;


    /**
     * 用户Id
     */
    private int userId;

    protected void writeCommonResp(ByteBuf byteBuf) {
        byteBuf.writeInt(0);
        /*header details*/
        byteBuf.writeIntLE(getLogicId());
        byteBuf.writeIntLE(getUserId());
    }

    /**
     * 生成返回值
     *
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public void writeResponse(ByteBuf byteBuf) {
        this.writeCommonResp(byteBuf);

    }

    public void endWrite(ByteBuf byteBuf) {
        int size = byteBuf.writerIndex();
        //hard encode for client
        byteBuf.setShortLE(0, 3);
        byteBuf.setShort(2, size);
    }

    public int getLogicId() {
        return logicId;
    }

    public void setLogicId(int logicId) {
        this.logicId = logicId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
