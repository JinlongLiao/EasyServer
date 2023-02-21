package io.github.jinlongliao.easy.server.script.groovy.constant;

/**
 * 脚本语言名称
 *
 * @author liaojinlong
 * @since 2022-02-18 14:08
 */
public enum ScriptLang {
    GROOVY_CLASS("groovy"),
    GROOVY_DSL("groovy"),
    ;
    private final String suffix;

    ScriptLang(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
