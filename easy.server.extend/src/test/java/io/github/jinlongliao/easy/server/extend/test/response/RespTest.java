package io.github.jinlongliao.easy.server.extend.test.response;

import io.github.jinlongliao.easy.server.extend.response.ICommonResponse;
import io.github.jinlongliao.easy.server.extend.response.IResponseStreamFactory;

public class RespTest implements ICommonResponse {
    private boolean _bool = true;
    private Boolean bool = true;

    public boolean is_bool() {
        return _bool;
    }

    public Boolean getBool() {
        return bool;
    }

    @Override
    public IResponseStreamFactory buildResponseStreamFactory() {
        return new ResponseStreamFactory();
    }
}
