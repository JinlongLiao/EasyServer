package io.github.jinlongliao.easy.server.cached.aop.el;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 包装
 *
 * @author: liaojinlong
 * @date: 2023/7/12 12:56
 */
public class ElField {
    private final Field field;
    private final Class<?> fieldClass;
    private final String fieldName;
    private final boolean generic;
    private final boolean root;

    public ElField(Field field) {
        this(field, field.getType(), field.getName(), false);
    }

    public ElField(Field field, Class<?> fieldClass, String fieldName, boolean generic) {
        this.field = field;
        this.fieldClass = fieldClass;
        this.fieldName = fieldName;
        this.generic = generic;
        this.root = Objects.isNull(field);
    }

    public boolean isRoot() {
        return root;
    }

    public Class<?> getFieldClass() {
        return fieldClass;
    }

    public Field getField() {
        return field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isGeneric() {
        return generic;
    }
}
