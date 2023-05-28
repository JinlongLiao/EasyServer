package io.github.jinlongliao.easy.server.swagger.knife4j.parse;

import io.github.jinlongliao.easy.server.swagger.model.ApiDoc;
import io.github.jinlongliao.easy.server.swagger.model.ApiResource;

import java.util.Map;
import java.util.Set;

/**
 * @author: liaojinlong
 * @date: 2022-06-17 18:09
 */
public interface ExtraApiDocGenerator {
    /**
     * 扩展使用
     *
     * @param apiResource  /
     * @param apiDoc       /
     * @param apiResources /
     * @param apiDocMap    /
     */
    void generatorApiDoc(int tag, ApiResource apiResource, ApiDoc apiDoc, Set<ApiResource> apiResources, Map<ApiResource, ApiDoc> apiDocMap);

}
