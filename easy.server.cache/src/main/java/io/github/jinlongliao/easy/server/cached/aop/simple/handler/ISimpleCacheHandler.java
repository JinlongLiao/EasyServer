package io.github.jinlongliao.easy.server.cached.aop.simple.handler;

import io.github.jinlongliao.easy.server.cached.field.simple.SimpleCacheNode;

/**
 * 简单实现
 *
 * @author: liaojinlong
 * @date: 2022-11-24 14:30
 */
public interface ISimpleCacheHandler {
    /**
     * 缓存处理业务
     *
     * @param cacheNode 配置信息
     * @param target    代理类
     * @param params    参数
     * @return /
     * @throws Throwable
     */
    Object cacheHandler(SimpleCacheNode cacheNode, Object target, Object... params) throws Exception;

}
