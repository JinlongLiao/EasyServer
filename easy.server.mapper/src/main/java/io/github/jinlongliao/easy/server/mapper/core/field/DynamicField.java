package io.github.jinlongliao.easy.server.mapper.core.field;

import java.lang.reflect.Field;

/**
 * @author liaojinlong
 * @since 2021/6/30 19:49
 */
public class DynamicField {
    private final Field field;
    private final int len;

    public DynamicField(Field field, int len) {
        this.field = field;
        this.len = len;
    }

    public Field getField() {
        return field;
    }

    public int getLen() {
        return len;
    }
}
