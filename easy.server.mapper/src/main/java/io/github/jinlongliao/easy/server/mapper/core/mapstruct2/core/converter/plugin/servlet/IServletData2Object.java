package io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin.servlet;


import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.IData2Object2;

import javax.servlet.http.HttpServletRequest;

/**
 * 扩展JavaX Servlet
 *
 * @author: liaojinlong
 * @date: 2022/5/22 10:26
 */
public interface IServletData2Object<T> extends IData2Object2<T> {

    /**
     * {@link javax.servlet.http.HttpServletRequest}转换接口
     *
     * @param request
     * @return T
     */
    T toHttpServletRequestConverter(HttpServletRequest request);
}
