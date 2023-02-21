package io.github.jinlongliao.easy.server.swagger.config;

import io.github.jinlongliao.easy.server.swagger.model.ApiDocInfo;

import java.util.regex.Pattern;

/**
 * @author: liaojinlong
 * @date: 2022/6/19 19:36
 */
public class ApiConfig {
    private final String host;
    private final String basePath;
    private final String apiResourceUrl;
    private final String apiResourceLocation;
    private final String[] schemes;
    private final ApiDocInfo apiDocInfo;
    private final Pattern excludePath;
    private final String proxyAccessPath;

    /***
     * 是否开启basic验证,默认不开启
     */
    private boolean enableBasicAuth = false;

    private String userName;

    private String password;

    public ApiConfig(String host,
                     String basePath,
                     String apiResourceUrl,
                     String apiResourceLocation,
                     String[] schemes,
                     ApiDocInfo apiDocInfo,
                     Pattern excludePath,
                     String proxyAccessPath) {

        this.host = host;
        String end = "/";
        if (!basePath.endsWith(end)) {
            this.basePath = basePath + end;
        } else {
            this.basePath = basePath;
        }
        this.apiResourceUrl = apiResourceUrl;
        this.apiResourceLocation = apiResourceLocation;
        this.schemes = schemes;
        this.apiDocInfo = apiDocInfo;
        this.excludePath = excludePath;
        this.proxyAccessPath = proxyAccessPath;
    }

    public String getProxyAccessPath() {
        return proxyAccessPath;
    }

    public Pattern getExcludePath() {
        return excludePath;
    }

    public String getHost() {
        return host;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getApiResourceUrl() {
        return apiResourceUrl;
    }

    public String getApiResourceLocation() {
        return apiResourceLocation;
    }

    public String[] getSchemes() {
        return schemes;
    }

    public ApiDocInfo getApiDocInfo() {
        return apiDocInfo;
    }

    public boolean isEnableBasicAuth() {
        return enableBasicAuth;
    }

    public void setEnableBasicAuth(boolean enableBasicAuth) {
        this.enableBasicAuth = enableBasicAuth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
