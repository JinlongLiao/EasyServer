package io.github.jinlongliao.easy.server.netty.demo.logic.response;

import io.github.jinlongliao.easy.server.netty.demo.logic.response.stream.ResponseStreamFactory;
import io.github.jinlongliao.easy.server.extend.response.ICommonResponse;
import io.github.jinlongliao.easy.server.extend.response.IResponseStreamFactory;
import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class RootResponse implements ICommonResponse {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static List<Field> header;

    static {
        List<Field> header1;
        try {
            header1 = Collections.singletonList(RootResponse.class.getDeclaredField("status"));
        } catch (NoSuchFieldException e) {
            log.error(e.getMessage(), e);
            header1 = Collections.emptyList();
        }
        header = header1;
    }

    private final int status;

    public RootResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public IResponseStreamFactory buildResponseStreamFactory() {
        return new ResponseStreamFactory();
    }

    @Override
    public List<Field> headerAppender() {
        return header;
    }

    protected void writeCommonResp(ByteBuf byteBuf) {
        byteBuf.writeInt(0);
        /*header details*/
    }

    /**
     * 生成返回值
     *
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public void writeResponse(ByteBuf byteBuf) {
        this.writeCommonResp(byteBuf);
        this.writePrivateResp(byteBuf);
        int size = byteBuf.writerIndex();
        //hard encode for client
        byteBuf.setShortLE(0, 3);
        byteBuf.setShort(2, size);
    }

    protected void writePrivateResp(ByteBuf byteBuf) {
    }

    ;


}
