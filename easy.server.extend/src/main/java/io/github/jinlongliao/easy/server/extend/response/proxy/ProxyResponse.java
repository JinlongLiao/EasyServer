package io.github.jinlongliao.easy.server.extend.response.proxy;

import io.github.jinlongliao.easy.server.extend.response.ICommonResponse;
import io.github.jinlongliao.easy.server.extend.response.IResponseStreamFactory;

/**
 * @date 2022-12-21 16:06
 * @author: liaojinlong
 * @description: 用于代理生成的
 **/

public interface ProxyResponse {
    byte[] genResHex(IResponseStreamFactory responseStreamFactory, ICommonResponse response)  ;
}
