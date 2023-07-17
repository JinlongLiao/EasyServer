package io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.wrap;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.ExtraFieldConverter;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.AbstractExtraClassMethodGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.array.ArrayClassMethodCoreGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.converter.plugin.core.map.MapClassMethodCoreGenerator;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.FieldParserBody;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.ClassWriter;

import java.util.List;

/**
 * 用于生成函数的
 *
 * @author: liaojinlong
 * @date: 2022/5/21 22:01
 */
public class WrapClassMethodCoreGenerator extends AbstractExtraClassMethodGenerator<ICoreData2Object2> {
    private final MapClassMethodCoreGenerator mapClassMethodCoreGenerator = new MapClassMethodCoreGenerator();
    private final ArrayClassMethodCoreGenerator classMethodCoreGenerator = new ArrayClassMethodCoreGenerator();

    @Override
    public <T> void initExtraMethod(List<FieldParserBody> filedParserBodies,
                                    String owner,
                                    String className,
                                    ClassWriter cw,
                                    Class<? extends ExtraFieldConverter> filedValueConverter) {
        this.mapClassMethodCoreGenerator.initExtraMethod(filedParserBodies,
                owner,
                className,
                cw,
                filedValueConverter);
        this.classMethodCoreGenerator.initExtraMethod(filedParserBodies,
                owner,
                className,
                cw,
                filedValueConverter);
    }

    @Override
    public Class<ICoreData2Object2> getTargetConverter() {
        return ICoreData2Object2.class;
    }

    @Override
    public String getSuperName() {
        return CLassUtils.OBJECT_SUPER_NAME;
    }
}
