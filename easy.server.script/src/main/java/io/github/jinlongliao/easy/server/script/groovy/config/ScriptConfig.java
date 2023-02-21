package io.github.jinlongliao.easy.server.script.groovy.config;

import io.github.jinlongliao.easy.server.script.groovy.constant.ScriptLang;
import io.github.jinlongliao.easy.server.script.groovy.ResourceScriptSource;
import io.github.jinlongliao.easy.server.script.groovy.bean.TargetSourceFactoryBean;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.Map;

/**
 * 配置动态脚本信息
 *
 * @author liaojinlong
 * @since 2022-02-18 14:08
 */
public class ScriptConfig {
    /**
     * 脚本类型
     */
    private ScriptLang scriptType;
    private Resource[] scripts;
    /**
     * 脚本路径
     */
    private String[] scriptPaths;
    /**
     * 是否需要动态更新
     */
    private boolean refresh = true;
    /**
     * 刷新间隔 s
     */
    private long refreshDelay;
    private Map<ResourceScriptSource, TargetSourceFactoryBean> targetSourceCache;
    public String[] getScriptPaths() {
        return scriptPaths;
    }

    public void setScriptPaths(String[] scriptPaths) {
        this.scriptPaths = scriptPaths;
    }

    public long getRefreshDelay() {
        return refreshDelay;
    }

    public void setRefreshDelay(long refreshDelay) {
        this.refreshDelay = refreshDelay;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public ScriptLang getScriptType() {
        return scriptType;
    }

    public void setScriptType(ScriptLang scriptType) {
        this.scriptType = scriptType;
    }

    public Resource[] getScripts() {
        return scripts;
    }

    public void setScripts(Resource[] scripts) {
        this.scripts = scripts;
    }

    public Map<ResourceScriptSource, TargetSourceFactoryBean> getTargetSourceCache() {
        return targetSourceCache;
    }

    public void setTargetSourceCache(Map<ResourceScriptSource, TargetSourceFactoryBean> targetSourceCache) {
        this.targetSourceCache = targetSourceCache;
    }

    @Override
    public String toString() {
        return "ScriptConfig{" +
                "scriptType=" + scriptType +
                ", scripts=" + Arrays.toString(scripts) +
                ", scriptPaths=" + Arrays.toString(scriptPaths) +
                ", refresh=" + refresh +
                ", refreshDelay=" + refreshDelay +
                ", targetSourceCache=" + targetSourceCache +
                '}';
    }
}
