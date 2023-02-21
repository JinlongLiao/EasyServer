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


import java.util.Map;

/**
 * API文档信息。
 * <p>
 * 可以通过如下的方式来构建一个API文件信息对象：
 * </p>
 * <pre>
 *  	// 创建一个构建器
 *  	ApiDocInfo.Builder builder = new ApiDocInfo.Builder();
 *  	// 设置可选参数的值
 *  	builder.description("API描述信息");
 *  	builder.version("API版本号");
 *  	// 设置其他可选参数
 *  	// ...
 *  	// 构建API文档信息
 *  	builder.build();
 * 	</pre>
 *
 * @author yonghaun
 * @since 1.0.0
 */
public class ApiDocInfo {
    private   String description;
    private   String version;
    private   String title;
    private   String termsOfService;
    private   Map<String, Object> contact;
    private   License license;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }

    public void setContact(Map<String, Object> contact) {
        this.contact = contact;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public String getTitle() {
        return title;
    }

    public String getTermsOfService() {
        return termsOfService;
    }

    public Map<String, Object> getContact() {
        return contact;
    }

    public License getLicense() {
        return license;
    }

}
