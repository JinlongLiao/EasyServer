package io.github.jinlongliao.easy.server.extend.test.response;

import io.github.jinlongliao.easy.server.extend.annotation.GeneratorResponse;
import io.github.jinlongliao.easy.server.extend.response.ICommonResponse;
import io.github.jinlongliao.easy.server.extend.response.IResponseStreamFactory;

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
