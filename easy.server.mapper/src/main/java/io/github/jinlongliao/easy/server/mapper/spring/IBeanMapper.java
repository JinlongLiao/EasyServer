package io.github.jinlongliao.easy.server.mapper.spring;

import java.util.Map;

/**
 * @author: liaojinlong
 * @date: 2022-06-16 17:49
 */
public interface IBeanMapper {
    /**
     * <p>基于Map 的转换</p>
     *
     * @param tClass
     * @param data
     * @param <T>
     * @return T
     */
    <T> T mapBeanMapper(Class<T> tClass, Map<String, Object> data);

    /**
     * <p>基于数组 的转换</p>
     *
     * @param tClass
     * @param data
     * @param <T>
     * @return T
     */
    <T> T arrayBeanMapper(Class<T> tClass, Object[] data);

    /**
     * <p>基于javax.servlet 的转换</p>
     *
     * @param tClass
     * @param req
     * @param <T>
     * @return T
     */
    <T> T servletBeanMapper(Class<T> tClass, jakarta.servlet.http.HttpServletRequest req);

}
