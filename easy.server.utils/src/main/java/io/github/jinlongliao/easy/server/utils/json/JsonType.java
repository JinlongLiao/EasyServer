package io.github.jinlongliao.easy.server.utils.json;

/**
 * 使用的Json 序列化类型
 *
 * @author: liaojinlong
 * @date: 2022-06-09 17:34
 */
public enum JsonType {
    /**
     * 扩展使用
     */
    EXTRA_JSON(),
    /**
     * fastjson2
     */
    FAST_JSON2(),
    /**
     * fastjson
     */
    @Deprecated
    FAST_JSON(),
    /**
     * JackJson
     */
    JACK_JSON(),
    ;
}
