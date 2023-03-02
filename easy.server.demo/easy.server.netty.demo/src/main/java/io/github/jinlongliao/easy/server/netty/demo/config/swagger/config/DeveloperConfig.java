package io.github.jinlongliao.easy.server.netty.demo.config.swagger.config;

import io.github.jinlongliao.easy.server.core.core.spring.LogicRegisterContext;
import io.github.jinlongliao.easy.server.netty.demo.config.swagger.config.api.NettyTcpApiGenerator;
import io.github.jinlongliao.easy.server.swagger.config.ApiConfig;
import io.github.jinlongliao.easy.server.swagger.config.ApiSpringAutoConfig;
import io.github.jinlongliao.easy.server.swagger.model.ApiDocInfo;
import io.github.jinlongliao.easy.server.swagger.model.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 开发测试环境使用
 *
 * @author liaojinlong
 * @since 2021-12-29 09:54
 */

@Import({ApiSpringAutoConfig.class})
public class DeveloperConfig {
    private final Pattern compile = Pattern.compile("^/api/proxy");

    @Bean
    public ApiConfig apiConfig() {
        ApiDocInfo apiDocInfo = new ApiDocInfo();
        apiDocInfo.setTitle("Netty Tcp Api demo");
        apiDocInfo.setVersion("v1");
        apiDocInfo.setDescription("Netty Tcp Api demo");
        Map<String, Object> contact = new HashMap<>(4, 1.5f);
        contact.put("name", "jinlongliao");
        contact.put("email", "jinlongliao@foxmail.com");
        apiDocInfo.setContact(contact);
        apiDocInfo.setLicense(new License("Apache License", "https://www.apache.org/licenses/LICENSE-2.0.txt"));
        ApiConfig apiConfig = new ApiConfig("127.0.0.1", "/api/", "/api", "/api", new String[]{"http", "https"}, apiDocInfo,
                compile,
                "proxy");
        apiConfig.setEnableBasicAuth(true);
        apiConfig.setUserName("user");
        apiConfig.setPassword("123");
        return apiConfig;
    }


    @Bean
    public NettyTcpApiGenerator notificationUiApiGenerator(LogicRegisterContext logicRegisterContext, ApiConfig apiConfig) {
        return new NettyTcpApiGenerator(logicRegisterContext, apiConfig, Collections.emptyList());
    }

}
