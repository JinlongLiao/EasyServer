package io.github.jinlongliao.easy.server.swagger.knife4j.parse;

import io.github.jinlongliao.easy.server.swagger.model.ApiDoc;
import io.github.jinlongliao.easy.server.swagger.model.ApiResource;

/**
 * @author: liaojinlong
 * @date: 2022-06-17 18:09
 */
public interface ExtraApiDocGenerator {
    /**
     * 扩展使用
     * @param apiResource /
     * @param apiDoc /
     */
    void generatorApiDoc(ApiResource apiResource, ApiDoc apiDoc);

}
