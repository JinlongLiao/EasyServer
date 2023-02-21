package io.github.jinlongliao.easy.server.swagger.knife4j.parse;


import io.github.jinlongliao.easy.server.swagger.model.ApiDoc;
import io.github.jinlongliao.easy.server.swagger.model.ApiResource;

import java.util.Collection;

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
     * 用于扩展
     * @param apiResource
     * @param apiDoc
     */
    void extraApiDoc(ApiResource apiResource, ApiDoc apiDoc);

    /**
     * 重新刷新
     */
    void refresh();
}
