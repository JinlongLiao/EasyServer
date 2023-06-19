package io.github.jinlongliao.easy.server.mapper.utils;

import io.github.jinlongliao.easy.server.mapper.core.field.DynamicField;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.ClassWriter;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.MethodVisitor;

import java.util.*;
import java.util.Objects;

import static io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Opcodes.*;

/**
 * @author: liaojinlong
 * @date: 2022/5/19 14:05
 */
public class CLassUtils {
    public static final String OBJECT_SUPER_NAME = CLassUtils.getJvmClass(Object.class);

    public static boolean isBoolType(Class type) {
        return Boolean.TYPE.equals(type);
    }

    public static boolean isBoolClass(Class type) {
        return Boolean.class.equals(type);
    }

    public static boolean isBool(Class type) {
        return isBoolType(type) || isBoolClass(type);
    }

    public static boolean isByteType(Class type) {
        return Byte.TYPE.equals(type);
    }

    public static boolean isByteClass(Class type) {
        return Byte.class.equals(type);
    }

    public static boolean isByte(Class type) {
        return isByteClass(type) || isByteType(type);
    }

    public static boolean isCharacterType(Class type) {
        return Character.TYPE.equals(type);
    }

    public static boolean isCharacterClass(Class type) {
        return Character.class.equals(type);
    }

    public static boolean isCharacter(Class type) {
        return isCharacterClass(type) || isCharacterType(type);
    }

    public static boolean isShortType(Class type) {
        return Short.TYPE.equals(type);
    }

    public static boolean isShortClass(Class type) {
        return Short.class.equals(type);
    }

    public static boolean isShort(Class type) {
        return isShortClass(type) || isShortType(type);
    }

    public static boolean isIntegerType(Class type) {
        return Integer.TYPE.equals(type);
    }

    public static boolean isIntegerClass(Class type) {
        return Integer.class.equals(type);
    }

    public static boolean isInteger(Class type) {
        return isIntegerClass(type) || isIntegerType(type);
    }

    public static boolean isLongType(Class type) {
        return Long.TYPE.equals(type);
    }

    public static boolean isLongClass(Class type) {
        return Long.class.equals(type);
    }

    public static boolean isLong(Class type) {
        return isLongClass(type) || isLongType(type);
    }

    public static boolean isFloatType(Class type) {
        return Float.TYPE.equals(type);
    }

    public static boolean isFloatClass(Class type) {
        return Float.class.equals(type);
    }

    public static boolean isFloat(Class type) {
        return isFloatClass(type) || isFloatType(type);
    }

    public static boolean isDoubleType(Class type) {
        return Double.TYPE.equals(type);
    }

    public static boolean isDoubleClass(Class type) {
        return Double.class.equals(type);
    }

    public static boolean isDouble(Class type) {
        return isDoubleClass(type) || isDoubleType(type);
    }

    public static boolean isStringClass(Class type) {
        return String.class.equals(type);
    }

    public static boolean isDynamicFieldClass(Class type) {
        return DynamicField.class.equals(type);
    }

    public static String getClassType(Class type) {
        return getCommonJvmClass(type, true, true);
    }

    public static String getJvmClass(Class type) {
        return getCommonJvmClass(type, false, false);
    }

    public static String getCommonJvmClass(Class type, boolean addFirst, boolean addEnd) {
        if (type == null || Void.TYPE.equals(type) || Void.class.equals(type)) {
            return "V";
        }
        if (isBoolType(type)) {
            return "Z";
        }
        if (isCharacterType(type)) {
            return "C";
        }
        if (isByteType(type)) {
            return "B";
        }
        if (isShortType(type)) {
            return "S";
        }
        if (isIntegerType(type)) {
            return "I";
        }
        if (isLongType(type)) {
            return "J";
        }
        if (isFloatType(type)) {
            return "F";
        }
        if (isDoubleType(type)) {
            return "D";
        }
        if (type.isArray()) {
            return type.getName().replace('.', '/').intern();
        }

        String className = type.getName().replace('.', '/');
        if (addFirst) {
            className = "L" + className;
        }
        if (addEnd) {
            className = className + ";";
        }
        return className;
    }


    public static boolean isBaseType(Class type) {
        return isBoolType(type) ||
                isCharacterType(type) ||
                isByteType(type) ||
                isShortType(type) ||
                isIntegerType(type) ||
                isLongType(type) ||
                isFloatType(type) ||
                isDoubleType(type);
    }

    public static Class getBaseType(Class type) {
        return isBoolClass(type) ? Boolean.TYPE
                : isCharacterClass(type) ? Character.TYPE
                : isByteClass(type) ? Byte.TYPE
                : isShortClass(type) ? Short.TYPE
                : isIntegerClass(type) ? Integer.TYPE
                : isLongClass(type) ? Long.TYPE
                : isFloatClass(type) ? Float.TYPE
                : isDoubleClass(type) ? Double.TYPE
                : type;
    }

    public static Class getPackageType(Class type) {
        return isBool(type) ? Boolean.class
                : isCharacterType(type) ? Character.class
                : isByteType(type) ? Byte.class
                : isShortType(type) ? Short.class
                : isIntegerType(type) ? Integer.class
                : isLongType(type) ? Long.class
                : isFloatType(type) ? Float.class
                : isDoubleType(type) ? Double.class
                : type;
    }

    public static void putInt(MethodVisitor methodVisitor, int value) {
        if (value == 0) {
            methodVisitor.visitInsn(ICONST_0);
        } else if (value == 1) {
            methodVisitor.visitInsn(ICONST_1);
        } else if (value == 2) {
            methodVisitor.visitInsn(ICONST_2);
        } else if (value == 3) {
            methodVisitor.visitInsn(ICONST_3);
        } else if (value == 4) {
            methodVisitor.visitInsn(ICONST_4);
        } else if (value == 5) {
            methodVisitor.visitInsn(ICONST_5);
        } else {
            boolean isByte = (value == ((byte) value));
            if (isByte) {
                methodVisitor.visitIntInsn(BIPUSH, value);
                return;
            }
            boolean isShort = (value == ((short) value));
            if (isShort) {
                methodVisitor.visitIntInsn(SIPUSH, value);
                return;
            }
            methodVisitor.visitIntInsn(LDC, value);
        }
    }


    public static void putFloat(MethodVisitor methodVisitor, float value) {
        if (value == 0) {
            methodVisitor.visitInsn(FCONST_0);
        } else if (value == 1) {
            methodVisitor.visitInsn(FCONST_1);
        } else if (value == 2) {
            methodVisitor.visitInsn(FCONST_2);
        } else {
            methodVisitor.visitLdcInsn(value);
        }
    }

    public static void putDouble(MethodVisitor methodVisitor, double value) {
        if (value == 0) {
            methodVisitor.visitInsn(DCONST_0);
        } else if (value == 1) {
            methodVisitor.visitInsn(DCONST_1);
        } else {
            methodVisitor.visitLdcInsn(value);
        }
    }

    public static void putNull(MethodVisitor methodVisitor) {
        methodVisitor.visitLdcInsn(ACONST_NULL);
    }

    public static UnpackDesc getUnpackDesc(Class type) {
        return isBool(type) ? UnpackDesc.BOOLEAN
                : isCharacter(type) ? UnpackDesc.CHARACTER
                : isByte(type) ? UnpackDesc.BYTE
                : isShort(type) ? UnpackDesc.SHORT
                : isInteger(type) ? UnpackDesc.INTEGER
                : isLong(type) ? UnpackDesc.LONG
                : isFloat(type) ? UnpackDesc.FLOAT
                : isDouble(type) ? UnpackDesc.DOUBLE
                : null;
    }

    public static boolean isListType(Class<?> type) {
        if (Objects.isNull(type)) {
            return false;
        }
        return type.isAssignableFrom(List.class);
    }

    public static Class<?> parseClassType(String classType) throws ClassNotFoundException {
        Class<?> type;
        switch (classType) {
            case "byte":
                type = Byte.TYPE;
                break;
            case "char":
                type = Character.TYPE;
                break;
            case "short":
                type = Short.TYPE;
                break;
            case "int":
                type = Integer.TYPE;
                break;
            case "long":
                type = Long.TYPE;
                break;
            case "float":
                type = Float.TYPE;
                break;
            case "double":
                type = Double.TYPE;
                break;
            default:
                type = AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.loadClass(classType);
        }
        return type;
    }

    public static Object getDefaultValue(Class<?> aclass) {
        Object value;
        if (aclass.equals(Byte.class) || aclass.equals(Byte.TYPE)) {
            value = (byte) 0;
        } else if (aclass.equals(Character.class) || aclass.equals(Character.TYPE)) {
            value = (char) 36;
        } else if (aclass.equals(Boolean.class) || aclass.equals(Boolean.TYPE)) {
            value = false;
        } else if (aclass.equals(Short.class) || aclass.equals(Short.TYPE)) {
            value = (short) 0;
        } else if (aclass.equals(Integer.class) || aclass.equals(Integer.TYPE)) {
            value = 0;
        } else if (aclass.equals(Long.class) || aclass.equals(Long.TYPE)) {
            value = 0L;
        } else if (aclass.equals(Float.class) || aclass.equals(Float.TYPE)) {
            value = (float) 0;
        } else if (aclass.equals(Double.class) || aclass.equals(Double.TYPE)) {
            value = (double) 0;
        } else if (aclass.equals(String.class)) {
            value = "";
        } else if (List.class.isAssignableFrom(aclass)) {
            value = Collections.emptyList();
        } else if (Set.class.isAssignableFrom(aclass)) {
            value = Collections.emptySet();
        } else if (Map.class.isAssignableFrom(aclass)) {
            value = Collections.emptyMap();
        } else {
            value = null;
        }

        return value;
    }

    public static void putDefVal(MethodVisitor methodVisitor, Class<?> type, int index) {
        if (type == Byte.TYPE || type == Short.TYPE || type == Integer.TYPE || type == Boolean.TYPE) {
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitVarInsn(ISTORE, index);
        } else if (type == Long.TYPE) {
            methodVisitor.visitInsn(LCONST_0);
            methodVisitor.visitVarInsn(LSTORE, index);
        } else if (type == Float.TYPE) {
            methodVisitor.visitInsn(FCONST_0);
            methodVisitor.visitVarInsn(FSTORE, index);
        } else if (type == Double.TYPE) {
            methodVisitor.visitInsn(DCONST_0);
            methodVisitor.visitVarInsn(DSTORE, index);
        } else {
            methodVisitor.visitInsn(ACONST_NULL);
            methodVisitor.visitVarInsn(ASTORE, 3);
        }
    }

    public static ClassWriter buildObjConstruct(String proxyObjectName, Class<?> tc) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(AsmProxyCodeGenerator.JAVA_DEF_VERSION, ACC_PUBLIC | ACC_SUPER,
                proxyObjectName,
                null,
                CLassUtils.OBJECT_SUPER_NAME,
                new String[]{CLassUtils.getJvmClass(tc)});
        MethodVisitor construct = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        construct.visitCode();
        construct.visitVarInsn(ALOAD, 0);
        construct.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        construct.visitInsn(RETURN);
        construct.visitMaxs(3, 1);
        construct.visitEnd();
        return classWriter;
    }
}

