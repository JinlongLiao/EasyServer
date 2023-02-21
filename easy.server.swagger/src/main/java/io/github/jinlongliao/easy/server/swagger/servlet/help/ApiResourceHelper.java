package io.github.jinlongliao.easy.server.swagger.servlet.help;

import io.github.jinlongliao.easy.server.swagger.knife4j.parse.AbstractDefaultApiGenerator;
import io.github.jinlongliao.easy.server.swagger.model.ApiDoc;
import io.github.jinlongliao.easy.server.swagger.model.ApiResource;
import io.github.jinlongliao.easy.server.utils.json.JsonHelper;

import java.util.Optional;

/**
 * @author: liaojinlong
 * @date: 2022-06-20 11:05
 */
public class ApiResourceHelper {
    private final JsonHelper jsonHelper;
    private final AbstractDefaultApiGenerator apiGenerator;


    public ApiResourceHelper(JsonHelper jsonHelper, AbstractDefaultApiGenerator apiGenerator) {
        this.jsonHelper = jsonHelper;
        this.apiGenerator = apiGenerator;
    }

    String apiResource() {
        return jsonHelper.objectToJsonArray(apiGenerator.generatorApiDocApiResource());
    }

    String aipDoc(String key) {
        Optional<ApiResource> resource = apiGenerator.generatorApiDocApiResource().stream().filter(i -> i.getName().equals(key)).findFirst();
        if (resource.isPresent()) {
            ApiDoc apiDoc = this.apiGenerator.generatorApiDoc(resource.get());
            return jsonHelper.objectToJson(apiDoc);
        }
        return "{}";
    }


    void cleanCache() {
        this.apiGenerator.refresh();
    }

}
