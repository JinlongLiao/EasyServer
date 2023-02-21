package io.github.jinlongliao.easy.server.script.groovy.config.dynamic;

import java.lang.reflect.Field;

/**
 * @author liaojinlong
 * @since 2022-02-21 19:53
 */
public class DynamicField {
    private Object value;
    private Field field;

    public DynamicField() {
    }

    public DynamicField(Object value, Field field) {
        this.value = value;
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void updateValue(Object object) throws IllegalAccessException {
        this.field.set(value, object);
    }
}
