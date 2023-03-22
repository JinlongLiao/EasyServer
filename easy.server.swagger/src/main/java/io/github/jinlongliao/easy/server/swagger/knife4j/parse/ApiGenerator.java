package io.github.jinlongliao.easy.server.swagger.knife4j.parse;


import io.github.jinlongliao.easy.server.swagger.model.ApiDoc;
import io.github.jinlongliao.easy.server.swagger.model.ApiResource;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author: liaojinlong
 * @date: 2022-06-17 17:10
 */
public interface ApiGenerator {
    /**
     * 生成 具体API 文档
     *
     * @param apiResource 选择菜单
     * @return /
     */
    ApiDoc generatorApiDoc(ApiResource apiResource);

    /**
     * 顶级菜单选择
     *
     * @return /
     */
    Collection<ApiResource> generatorApiDocApiResource();

    /**
     * 扩展使用
     *
     * @param apiResource  /
     * @param apiDoc       /
     * @param apiResources /
     * @param apiDocMap    /
     */
    void extraApiDoc(int tag, ApiResource apiResource, ApiDoc apiDoc, Set<ApiResource> apiResources, Map<ApiResource, ApiDoc> apiDocMap);

    /**
     * 重新刷新
     */
    void refresh();
}
