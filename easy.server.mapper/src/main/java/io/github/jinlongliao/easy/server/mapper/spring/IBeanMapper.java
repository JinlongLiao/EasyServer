package io.github.jinlongliao.easy.server.mapper.spring;

import java.util.Map;

/**
 * @author: liaojinlong
 * @date: 2022-06-16 17:49
 */
public interface IBeanMapper {
    /**
     * 基于Map 的转换
     *
     * @param tClass
     * @param data
     * @param <T>
     * @return T
     */
    <T> T mapBeanMapper(Class<T> tClass, Map<String, Object> data);

    /**
     * 基于数组 的转换
     *
     * @param tClass
     * @param data
     * @param <T>
     * @return T
     */
    <T> T arrayBeanMapper(Class<T> tClass, Object[] data);

    /**
     * 基于javax.servlet 的转换
     *
     * @param tClass
     * @param req
     * @param <T>
     * @return T
     */
    <T> T servletBeanMapper(Class<T> tClass, javax.servlet.http.HttpServletRequest req);

}
