package io.github.jinlongliao.easy.server.script.groovy.bean;

import io.github.jinlongliao.easy.server.script.groovy.config.ScriptConfig;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author liaojinlong
  * @since 2022-02-21 16:07
 */
public class ScriptConfigFactoryBean implements FactoryBean<ScriptConfig> {
    private final ScriptConfig scriptConfig;

    public ScriptConfigFactoryBean(ScriptConfig scriptConfig) {
        this.scriptConfig = scriptConfig;
    }

    @Override
    public ScriptConfig getObject() throws Exception {
        return scriptConfig;
    }

    @Override
    public Class<?> getObjectType() {
        return ScriptConfig.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
