package io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.ExtraFieldConverter;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.ClassMethodCoreGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.generator.FieldParserBody;
 import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.ClassWriter;

import java.util.List;

/**
 * 用于生成扩展函数的
 *
 * @author: liaojinlong
 * @date: 2022/5/21 22:01
 */
public abstract class AbstractExtraClassMethodGenerator extends ClassMethodCoreGenerator {
    @Override
    public <T> List<FieldParserBody> initMethod(ClassWriter cw,
                                                String owner,
                                                Class<T> tClass,
                                                boolean searchParentField,
                                                int javaVersion,
                                                Class<? extends ExtraFieldConverter> filedValueConverter) {
        String className = tClass.getName().replace('.', '/');
        List<FieldParserBody> fieldParserBodyList = getObjectFiledParserBody(searchParentField, tClass);
        this.initExtraMethod(fieldParserBodyList, owner, className, cw, filedValueConverter);
        return fieldParserBodyList;
    }

    public abstract <T> void initExtraMethod(List<FieldParserBody> filedParserBodies,
                                             String owner,
                                             String className,
                                             ClassWriter cw,
                                             Class<? extends ExtraFieldConverter> filedValueConverter);


    @Override
    public abstract Class getTargetConverter();

}
