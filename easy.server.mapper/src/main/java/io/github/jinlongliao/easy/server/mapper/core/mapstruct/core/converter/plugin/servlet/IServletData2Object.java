package io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.servlet;


import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.IData2Object2;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 扩展JavaX Servlet
 *
 * @author: liaojinlong
 * @date: 2022/5/22 10:26
 */
public interface IServletData2Object<T> extends IData2Object2<T> {

    /**
     * {@link jakarta.servlet.http.HttpServletRequest}转换接口
     *
     * @param request
     * @return T
     */
    T toHttpServletRequestConverter(HttpServletRequest request);
}
