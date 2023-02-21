package io.github.jinlongliao.easy.server.script.groovy;

import org.springframework.core.io.Resource;
import org.springframework.scripting.ScriptSource;

import java.io.IOException;

/**
 * 扩展 保留文件原始信息
 *
 * @author liaojinlong
 * @since 2022-02-18 16:02
 */
public class ResourceScriptSource implements ScriptSource {
    private final Resource resource;
    private final ScriptSource scriptSource;

    @Override
    public String getScriptAsString() throws IOException {
        return scriptSource.getScriptAsString();
    }

    @Override
    public boolean isModified() {
        return scriptSource.isModified();
    }

    @Override
    public String suggestedClassName() {
        return scriptSource.suggestedClassName();
    }

    public ResourceScriptSource(Resource resource, ScriptSource scriptSource) {
        this.resource = resource;
        this.scriptSource = scriptSource;
    }

    public Resource getResource() {
        return resource;
    }

    public ScriptSource getScriptSource() {
        return scriptSource;
    }
}
