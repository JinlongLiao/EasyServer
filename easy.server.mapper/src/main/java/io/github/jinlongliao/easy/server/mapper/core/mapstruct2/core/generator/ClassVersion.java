package io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.generator;

import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Opcodes;
import io.github.jinlongliao.easy.server.mapper.utils.Objects;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @date 2023-01-29 10:43
 * @author: liaojinlong
 * @description: 使用的class 版本
 **/

public interface ClassVersion {
    ClassVersion CLASS_VERSION = AccessController.doPrivileged((PrivilegedAction<ClassVersion>) () -> {
        Iterator<ClassVersion> classVersionIterator = ServiceLoader.load(ClassVersion.class).iterator();
        ClassVersion classVersion = null;
        while (classVersionIterator.hasNext()) {
            classVersion = classVersionIterator.next();
            break;
        }
        if (Objects.isNull(classVersion)) {
            classVersion = DefaultClassVersion.getSingle();
        }
        return classVersion;
    });

    default ClassVersion getClassVersion() {
        return CLASS_VERSION;
    }

    /**
     * @return 生成class 版本
     */
    int version();
}

class DefaultClassVersion implements ClassVersion {
    private Integer defaultVersion;

    @Override
    public int version() {
        return Opcodes.V1_8;
    }

    private static DefaultClassVersion single;

    private DefaultClassVersion() {
    }

    public static DefaultClassVersion getSingle() {
        if (single == null) {
            synchronized (DefaultClassVersion.class) {
                if (single == null) {
                    single = new DefaultClassVersion();
                }
            }
        }
        return single;
    }
}
