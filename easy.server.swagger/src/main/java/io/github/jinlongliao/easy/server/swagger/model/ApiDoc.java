/**
 * Copyright 2020-2021 JinlongLiao
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.jinlongliao.easy.server.swagger.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * Api文档。
 *
 * @author yonghaun
 * @since 1.0.0
 */
public class ApiDoc {
    /**
     * 接口统一前缀
     */
    private String basePath;

    private Map<String, Object> definitions;
    /**
     * 接口IP
     */
    private String host;
    /**
     * 头部介绍
     */
    private ApiDocInfo info;
    /**
     * 测试接口列表
     */
    private Map<String, Map<String, Operation>> paths;
    private final String swagger = "2.0";

    /**
     * 标签名称
     */
    private Collection<Tag> tags = new ArrayList<>();

    /**
     * 协议名称
     */
    private String[] schemes = new String[]{"http", "https"};


    /**
     * @return schemes
     */
    public String[] getSchemes() {
        return schemes;
    }

    /**
     * @return swagger协议版本
     */
    public String getSwagger() {
        return swagger;
    }

    /**
     * @param schemes schemes
     */
    public void setSchemes(String[] schemes) {
        this.schemes = schemes;
    }


    /**
     * @return 全部path
     */
    public Map<String, Map<String, Operation>> getPaths() {
        return paths;
    }

    /**
     * @param paths 全部path
     */
    public void setPaths(Map<String, Map<String, Operation>> paths) {
        this.paths = paths;
    }


    /**
     * @return 全部definition
     */
    public Map<String, Object> getDefinitions() {
        return definitions;
    }

    /**
     * @param definitions 全部definition
     */
    public void setDefinitions(Map<String, Object> definitions) {
        this.definitions = definitions;
    }


    /**
     * @return 文档信息。
     */
    public ApiDocInfo getInfo() {
        return info;
    }

    /**
     * @param info 文档信息。
     */
    public void setInfo(ApiDocInfo info) {
        this.info = info;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }


    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "APIDoc{" +
                "basePath='" + basePath + '\'' +
                ", definitions=" + definitions +
                ", host='" + host + '\'' +
                ", info=" + info +
                ", paths=" + paths +
                ", swagger='" + swagger + '\'' +
                ", tags=" + tags +
                ", schemes=" + Arrays.toString(schemes) +
                '}';
    }
}
