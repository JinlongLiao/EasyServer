package io.github.jinlongliao.easy.server.netty.demo.logic.response;

import io.netty.buffer.ByteBuf;

/**
 * @author: liaojinlong
 * @date: 2022-08-08 16:51
 */
public class VoidResponse extends RootResponse {

    /**
     * 私有的构造方法
     */
    private VoidResponse() {
        super(0);
    }

    private static final class InstanceHolder {
        /**
         * 本实例
         */
        private static final VoidResponse INSTANCE = new VoidResponse();
    }

    /**
     * 单例的实例
     *
     * @return this
     */
    public static VoidResponse getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    protected void writeCommonResp(ByteBuf byteBuf) {
    }

    @Override
    public void writeResponse(ByteBuf byteBuf) {
    }

    @Override
    protected void writePrivateResp(ByteBuf byteBuf) {
    }
}
