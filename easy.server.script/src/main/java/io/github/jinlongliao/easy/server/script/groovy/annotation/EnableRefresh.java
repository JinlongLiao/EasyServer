package io.github.jinlongliao.easy.server.script.groovy.annotation;

import io.github.jinlongliao.easy.server.script.groovy.GroovyBeanScriptLoader;
import io.github.jinlongliao.easy.server.script.groovy.constant.ScriptLang;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启热更新
 *
 * @author liaojinlong
 * @since 2022-02-18 17:32
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GroovyBeanScriptLoader.class)
public @interface EnableRefresh {
    /**
     * 脚本类型
     */
    ScriptLang scriptType() default ScriptLang.GROOVY_CLASS;

    /**
     * 脚本路径
     */
    String[] scriptPaths();

    /**
     * 是否需要动态更新
     */
    boolean refresh() default true;

    /**
     * 刷新间隔
     */
    long refreshDelay() default -1;
}
