package io.github.jinlongliao.easy.server.mapper.core.mapstruct.core;

/**
 * 用于扩展的转换的接口
 *
 * @author: liaojinlong
 * @date: 2022-06-16 09:41
 */
public interface ExtraFieldConverter {
    /**
     * Converter
     *
     * @param value     值
     * @param index     字段索引
     * @param filedName 字段名称
     * @param typeName  类型名称
     * @param isParent  是否父类的属性
     * @return /
     */
    Object converter(Object value, int index, String filedName, String typeName, boolean isParent);

}
