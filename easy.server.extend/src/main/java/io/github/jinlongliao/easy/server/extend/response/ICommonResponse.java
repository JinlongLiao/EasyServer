package io.github.jinlongliao.easy.server.extend.response;


import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.github.jinlongliao.easy.server.extend.response.proxy.ProxyResponse;
import io.github.jinlongliao.easy.server.extend.response.proxy.ProxyResponseFactory;

/**
 * 客户端响应基础消息
 *
 * @author liaojinlong
 * @since 2021-12-27 12:13
 */
public interface ICommonResponse {
    /**
     * 生成返回值
     *
     * @return
     */
    default byte[] genResHex() {
        return this.genResHex(null);
    }

    /**
     * 生成返回值
     *
     * @return
     */
    default byte[] genResHex(IResponseStreamFactory factory) {
        if (Objects.isNull(factory)) {
            factory = this.buildResponseStreamFactory();
        }
        return this.buildProxyResponse()
                .genResHex(factory, this);
    }

    IResponseStreamFactory buildResponseStreamFactory();

    /**
     * 头部有需要添加的
     *
     * @return /
     */
    default List<Field> headerAppender() {
        return Collections.emptyList();
    }

    /**
     * 获取代理实例
     *
     * @return /
     */
    default ProxyResponse buildProxyResponse() {
        return ProxyResponseFactory.getProxyResponse(this);
    }
}
