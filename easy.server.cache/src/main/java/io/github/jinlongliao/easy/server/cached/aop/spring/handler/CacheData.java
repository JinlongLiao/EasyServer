package io.github.jinlongliao.easy.server.cached.aop.spring.handler;

/**
 * @author liaojinlong
 * @since 2022-02-15 11:17
 */

public class CacheData {
    private Object value;
    private long expire;

    public CacheData() {
    }

    public CacheData(Object value, long expire) {
        this.value = value;
        this.expire = expire;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
