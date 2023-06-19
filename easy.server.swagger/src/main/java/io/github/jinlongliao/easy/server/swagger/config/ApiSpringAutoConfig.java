package io.github.jinlongliao.easy.server.swagger.config;

import io.github.jinlongliao.easy.server.swagger.knife4j.auth.SecurityBasicAuthFilter;
import io.github.jinlongliao.easy.server.swagger.knife4j.parse.AbstractDefaultApiGenerator;
import io.github.jinlongliao.easy.server.swagger.servlet.ApiHttpFilter;
import io.github.jinlongliao.easy.server.swagger.servlet.help.ApiCleanHelper;
import io.github.jinlongliao.easy.server.swagger.servlet.help.ApiMapping;
import io.github.jinlongliao.easy.server.swagger.servlet.help.ApiResourceHelper;
import io.github.jinlongliao.easy.server.swagger.servlet.help.CleanupApiCache;
import io.github.jinlongliao.easy.server.utils.json.JsonHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * @author: liaojinlong
 * @date: 2022/6/19 20:22
 */
public class ApiSpringAutoConfig {
    @Bean
    public ApiResourceHelper apiResourceHelper(JsonHelper jsonHelper, AbstractDefaultApiGenerator apiGenerator) {
        return new ApiResourceHelper(jsonHelper, apiGenerator);
    }

    @Bean
    public ApiCleanHelper apiCleanHelper() {
        return new ApiCleanHelper();
    }

    @Bean
    public ApiMapping apiMapping(ApiConfig apiConfig, ApiResourceHelper apiResourceHelper) {
        return new ApiMapping(apiConfig, apiResourceHelper);
    }

    @Bean
    public SecurityBasicAuthFilter securityBasicAuthFilter(ApiConfig apiConfig) {
        return new SecurityBasicAuthFilter(apiConfig);
    }

    @Bean
    public ApiHttpFilter apiHttpFilter(ApiMapping apiMapping, SecurityBasicAuthFilter securityBasicAuthFilter) {
        return new ApiHttpFilter(apiMapping, securityBasicAuthFilter);
    }

    @Bean
    public CleanupApiCache cleanupApiCache(ApiConfig apiConfig, ApiCleanHelper apiCleanHelper) {
        return new CleanupApiCache(apiCleanHelper, apiConfig);
    }

}
