package io.github.jinlongliao.easy.server.extend.response;

import io.github.jinlongliao.easy.server.extend.annotation.GeneratorResponse;

@GeneratorResponse
public class TestCommonResponse implements ICommonResponse {
    private int restFreeNum;

    public int getRestFreeNum() {
        return restFreeNum;
    }

    @Override
    public IResponseStreamFactory buildResponseStreamFactory() {
        return new ResponseStreamFactory();
    }

}
