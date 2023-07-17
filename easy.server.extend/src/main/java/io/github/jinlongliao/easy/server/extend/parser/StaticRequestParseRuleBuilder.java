package io.github.jinlongliao.easy.server.extend.parser;

import io.github.jinlongliao.easy.server.extend.exception.UnSupportFieldException;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.converter.InnerConverter;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.generator.AsmProxyCodeGenerator;
import io.github.jinlongliao.easy.server.mapper.exception.ConverterException;
import io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.*;
import io.github.jinlongliao.easy.server.mapper.utils.CLassUtils;
import io.github.jinlongliao.easy.server.mapper.utils.MapperStructConfig;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.core.parser.*;
import io.github.jinlongliao.easy.server.core.parser.inner.AbstractRequestParseRule;
import io.github.jinlongliao.easy.server.mapper.utils.UnpackDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.jinlongliao.easy.server.mapper.internal.org.objectweb.asm.Opcodes.*;


/**
 * 静态生成代码 Req 解析
 *
 * @date 2023-01-06 16:36
 * @author: liaojinlong
 * @description: /
 **/

public final class StaticRequestParseRuleBuilder {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String REQUEST_STREAM_FACTORY = CLassUtils.getJvmClass(IRequestStreamFactory.class);
    private static final String MESSAGE_PARSER_CALLBACK = CLassUtils.getJvmClass(IMessageParserCallBack.class);
    private static final String HTTP_SERVLET_REQUEST = CLassUtils.getJvmClass(HttpServletRequest.class);

    ;


    public static AbstractRequestParseRule buildRequestParseRule(String proxyObjectName,
                                                                 IDefaultValueConverter defaultValueConverter,
                                                                 LogicModel logicModel,
                                                                 List<MeType> meTypes) {
        try {
            String dynamicClassName = proxyObjectName.replace('/', '.');
            Class<? extends AbstractRequestParseRule> loadclass = (Class<? extends AbstractRequestParseRule>) AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.loadClass(dynamicClassName);
            return loadclass.getDeclaredConstructor(IDefaultValueConverter.class, LogicModel.class)
                    .newInstance(defaultValueConverter, logicModel);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage(), e);
            }
        }
        ClassWriter classWriter = buildConstruct(proxyObjectName);
        buildHexMethod(classWriter, proxyObjectName, meTypes);
        buildServletMethod(classWriter, proxyObjectName, meTypes);
        classWriter.visitEnd();
        byte[] classes = classWriter.toByteArray();
        MapperStructConfig.saveClassFile(classes, proxyObjectName);
        return AsmProxyCodeGenerator.MAPPER_CLASS_LOADER.reLoadInstance(proxyObjectName, classes, defaultValueConverter, logicModel);
    }

    private static ClassWriter buildConstruct(String proxyObjectName) {

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        MethodVisitor methodVisitor;
        classWriter.visit(AsmProxyCodeGenerator.JAVA_DEF_VERSION,
                ACC_PUBLIC | ACC_SUPER,
                proxyObjectName,
                null,
                "io/github/jinlongliao/easy/server/core/parser/inner/AbstractRequestParseRule", null);

        {
            String logicModelDescriptor = "(" + CLassUtils.getClassType(LogicModel.class) + ")V";
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", logicModelDescriptor, null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, CLassUtils.getJvmClass(AbstractRequestParseRule.class), "<init>", logicModelDescriptor, false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 2);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "(" + CLassUtils.getClassType(IDefaultValueConverter.class) + CLassUtils.getClassType(LogicModel.class) + ")V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, CLassUtils.getJvmClass(AbstractRequestParseRule.class), "<init>", "(" + CLassUtils.getClassType(IDefaultValueConverter.class) + CLassUtils.getClassType(LogicModel.class) + ")V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(3, 3);
            methodVisitor.visitEnd();
        }
        return classWriter;
    }

    private static void buildServletMethod(ClassWriter classWriter, String proxyObjectName, List<MeType> meTypes) {
        AtomicInteger localNum = new AtomicInteger(4);
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC,
                "readServletMsg",
                "(L" + HTTP_SERVLET_REQUEST + ";L" + MESSAGE_PARSER_CALLBACK + ";[Ljava/lang/Object;)[Ljava/lang/Object;",
                null, null);

        methodVisitor.visitCode();
        CLassUtils.putInt(methodVisitor, meTypes.size());
        methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Object");
        methodVisitor.visitVarInsn(ASTORE, localNum.getAndIncrement());

        AtomicInteger index = new AtomicInteger(0);
        for (MeType meType : meTypes) {
            if (meType.isBody()) {
                common(proxyObjectName, methodVisitor, index);
                methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                        MESSAGE_PARSER_CALLBACK,
                        "parserParamBody",
                        "(L" + HTTP_SERVLET_REQUEST + ";" + CLassUtils.getClassType(MeType.class) + "[Ljava/lang/Object;)Ljava/lang/Object;",
                        true);
            } else if (meType.isInnerField()) {
                common(proxyObjectName, methodVisitor, index);
                methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                        MESSAGE_PARSER_CALLBACK,
                        "parserInnerFiled",
                        "(Ljava/lang/Object;" + CLassUtils.getClassType(MeType.class) + "[Ljava/lang/Object;)Ljava/lang/Object;",
                        true);
            } else if (meType.isCommon()) {
                common(proxyObjectName, methodVisitor, index);
                methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                        MESSAGE_PARSER_CALLBACK,
                        "parserCommonParam",
                        "(L" + HTTP_SERVLET_REQUEST + ";" + CLassUtils.getClassType(MeType.class) + "[Ljava/lang/Object;)Ljava/lang/Object;",
                        true);
            } else {
                Class<?> meTypeType = meType.getType();
                if (!InnerConverter.containDateConverter(meTypeType) && meTypeType != DynamicString.class) {
                    int varIndex = index.get();

                    methodVisitor.visitVarInsn(ALOAD, 4);
                    methodVisitor.visitIntInsn(BIPUSH, varIndex);
                    methodVisitor.visitLdcInsn(Type.getType(CLassUtils.getClassType(meTypeType)));

                    methodVisitor.visitVarInsn(ALOAD, 0);
                    methodVisitor.visitFieldInsn(GETFIELD, proxyObjectName, "rules", "Ljava/util/List;");
                    CLassUtils.putInt(methodVisitor, varIndex);
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;", true);

                    methodVisitor.visitVarInsn(ALOAD, 1);
                    methodVisitor.visitLdcInsn(meType.getParamName());
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE, HTTP_SERVLET_REQUEST,
                            "getParameter", "(Ljava/lang/String;)Ljava/lang/String;", true);

                    methodVisitor.visitMethodInsn(INVOKESTATIC, "io/github/jinlongliao/easy/server/mapper/core/mapstruct/converter/InnerConverter",
                            "getT",
                            "(Ljava/lang/Class;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
                            false);

                } else {
                    Class<?> type = meTypeType;
                    methodVisitor.visitVarInsn(ALOAD, 1);
                    if (type == List.class) {
                        methodVisitor.visitLdcInsn(meType.getParamName());
                        methodVisitor.visitMethodInsn(INVOKEINTERFACE, HTTP_SERVLET_REQUEST,
                                "getParameterValues",
                                "(Ljava/lang/String;)[Ljava/lang/String;", true);

                        buildServletListInt(index, localNum, methodVisitor);
                    } else if (type.isArray()) {
                        boolean isIntArr;
                        String typeName = type.getName();
                        if ("[I".equals(typeName)) {
                            isIntArr = true;
                        } else if ("[Ljava.lang.Integer;".equals(typeName)) {
                            isIntArr = false;
                        } else {
                            throw new UnSupportFieldException("not support un int Type :" + typeName);
                        }
                        methodVisitor.visitLdcInsn(meType.getParamName());
                        methodVisitor.visitMethodInsn(INVOKEINTERFACE, HTTP_SERVLET_REQUEST,
                                "getParameterValues",
                                "(Ljava/lang/String;)[Ljava/lang/String;", true);
                        buildServletIntArr(localNum, methodVisitor, index, isIntArr);
                    } else {
                        methodVisitor.visitLdcInsn(meType.getParamName());
                        methodVisitor.visitMethodInsn(INVOKEINTERFACE, HTTP_SERVLET_REQUEST, "getParameter", "(Ljava/lang/String;)Ljava/lang/String;", true);
                        int varIndex = index.get();
                        int paramIndex = localNum.getAndIncrement();
                        methodVisitor.visitVarInsn(ASTORE, paramIndex);
                        methodVisitor.visitVarInsn(ALOAD, paramIndex);

                        Label label0 = new Label();
                        methodVisitor.visitJumpInsn(IFNULL, label0);
                        methodVisitor.visitVarInsn(ALOAD, paramIndex);
                        methodVisitor.visitTypeInsn(CHECKCAST, "java/lang/String");
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "isEmpty", "()Z", false);
                        Label label1 = new Label();
                        methodVisitor.visitJumpInsn(IFEQ, label1);
                        methodVisitor.visitLabel(label0);
                        loadMeType(proxyObjectName, methodVisitor, index);
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "io/github/jinlongliao/easy/server/core/parser/MeType",
                                "hasDef",
                                "()Z",
                                false);
                        methodVisitor.visitJumpInsn(IFEQ, label1);
                        loadMeType(proxyObjectName, methodVisitor, index);
                        methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
                                "io/github/jinlongliao/easy/server/core/parser/MeType",
                                "getDefaultValue",
                                "()Ljava/lang/Object;",
                                false);
                        methodVisitor.visitVarInsn(ASTORE, paramIndex);
                        methodVisitor.visitLabel(label1);
                        methodVisitor.visitVarInsn(ALOAD, 4);
                        CLassUtils.putInt(methodVisitor, varIndex);
                        methodVisitor.visitVarInsn(ALOAD, paramIndex);
                        if (type == DynamicString.class) {
                            type = String.class;
                        }

                        Method converter = InnerConverter.getGlobalConverter(type);
                        Class<?> returnType = converter.getReturnType();
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "io/github/jinlongliao/easy/server/mapper/core/mapstruct/converter/InnerConverter",
                                converter.getName(),
                                "(Ljava/lang/Object;)" + CLassUtils.getClassType(returnType),
                                false);

                        if (CLassUtils.isBaseType(returnType)) {
                            UnpackDesc unpackDesc = CLassUtils.getUnpackDesc(returnType);
                            methodVisitor.visitMethodInsn(INVOKESTATIC, unpackDesc.getOwner(),
                                    unpackDesc.getPackMethodName(),
                                    unpackDesc.getPackDescriptor(),
                                    false);
                        }
                    }
                }
            }

            methodVisitor.visitInsn(AASTORE);
            index.incrementAndGet();
        }
        methodVisitor.visitVarInsn(ALOAD, 4);
        methodVisitor.visitInsn(ARETURN);
        methodVisitor.visitMaxs(0, localNum.get());
        methodVisitor.visitEnd();
    }

    private static void buildServletIntArr(AtomicInteger localNum, MethodVisitor methodVisitor, AtomicInteger index, boolean isIntArr) {
        int arrIndex = localNum.getAndIncrement();
        methodVisitor.visitVarInsn(ASTORE, arrIndex);
        methodVisitor.visitVarInsn(ALOAD, arrIndex);
        methodVisitor.visitInsn(ARRAYLENGTH);
        if (isIntArr) {
            methodVisitor.visitIntInsn(NEWARRAY, T_INT);
        } else {
            methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Integer");
        }
        int arrayIndex = localNum.getAndIncrement();
        methodVisitor.visitVarInsn(ASTORE, arrayIndex);
        methodVisitor.visitInsn(ICONST_0);
        int indexIndex = localNum.getAndIncrement();
        methodVisitor.visitVarInsn(ISTORE, indexIndex);
        Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitVarInsn(ALOAD, arrIndex);
        methodVisitor.visitInsn(ARRAYLENGTH);
        methodVisitor.visitVarInsn(ILOAD, indexIndex);
        Label label1 = new Label();
        methodVisitor.visitJumpInsn(IF_ICMPLE, label1);
        methodVisitor.visitVarInsn(ALOAD, arrayIndex);
        methodVisitor.visitVarInsn(ILOAD, indexIndex);
        methodVisitor.visitVarInsn(ALOAD, arrIndex);
        methodVisitor.visitVarInsn(ILOAD, indexIndex);
        methodVisitor.visitIincInsn(indexIndex, 1);
        methodVisitor.visitInsn(AALOAD);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "io/github/jinlongliao/easy/server/mapper/core/mapstruct/converter/InnerConverter", "getInt2", "(Ljava/lang/Object;)Ljava/lang/Integer;", false);
        if (isIntArr) {
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
            methodVisitor.visitInsn(IASTORE);
        } else {
            methodVisitor.visitInsn(AASTORE);
        }
        methodVisitor.visitJumpInsn(GOTO, label0);
        methodVisitor.visitLabel(label1);
        methodVisitor.visitVarInsn(ALOAD, 3);
        CLassUtils.putInt(methodVisitor, index.get());
        methodVisitor.visitVarInsn(ALOAD, arrayIndex);

    }

    private static void buildServletListInt(AtomicInteger index, AtomicInteger localNum, MethodVisitor methodVisitor) {
        int arrIndex = localNum.getAndIncrement();
        methodVisitor.visitVarInsn(ASTORE, arrIndex);
        methodVisitor.visitTypeInsn(NEW, "java/util/ArrayList");
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitVarInsn(ALOAD, arrIndex);
        methodVisitor.visitInsn(ARRAYLENGTH);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "(I)V", false);
        int listIndex = localNum.getAndIncrement();
        methodVisitor.visitVarInsn(ASTORE, listIndex);
        methodVisitor.visitInsn(ICONST_0);
        int indexIndex = localNum.getAndIncrement();
        methodVisitor.visitVarInsn(ISTORE, indexIndex);
        Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitVarInsn(ALOAD, arrIndex);
        methodVisitor.visitInsn(ARRAYLENGTH);
        methodVisitor.visitVarInsn(ILOAD, indexIndex);
        Label label1 = new Label();
        methodVisitor.visitJumpInsn(IF_ICMPLE, label1);
        methodVisitor.visitVarInsn(ALOAD, listIndex);
        methodVisitor.visitVarInsn(ALOAD, arrIndex);
        methodVisitor.visitVarInsn(ILOAD, indexIndex);
        methodVisitor.visitIincInsn(indexIndex, 1);
        methodVisitor.visitInsn(AALOAD);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "io/github/jinlongliao/easy/server/mapper/core/mapstruct/converter/InnerConverter", "getInt2", "(Ljava/lang/Object;)Ljava/lang/Integer;", false);
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
        methodVisitor.visitInsn(POP);
        methodVisitor.visitJumpInsn(GOTO, label0);
        methodVisitor.visitLabel(label1);
        methodVisitor.visitVarInsn(ALOAD, 3);
        CLassUtils.putInt(methodVisitor, index.get());
        methodVisitor.visitVarInsn(ALOAD, listIndex);
    }

    private static void buildHexMethod(ClassWriter classWriter, String proxyObjectName, List<MeType> meTypes) {
        AtomicInteger localNum = new AtomicInteger(4);
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC,
                "readHexMsg",
                "(L" + REQUEST_STREAM_FACTORY + ";L" + MESSAGE_PARSER_CALLBACK + ";[Ljava/lang/Object;)[Ljava/lang/Object;",
                null, null);
        methodVisitor.visitCode();
        CLassUtils.putInt(methodVisitor, meTypes.size());
        methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Object");
        methodVisitor.visitVarInsn(ASTORE, localNum.getAndIncrement());

        AtomicInteger index = new AtomicInteger(0);
        for (MeType meType : meTypes) {
            if (meType.isBody()) {
                common(proxyObjectName, methodVisitor, index);
                methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                        MESSAGE_PARSER_CALLBACK,
                        "parserParamBody",
                        "(L" + REQUEST_STREAM_FACTORY + ";" + CLassUtils.getClassType(MeType.class) + "[Ljava/lang/Object;)Ljava/lang/Object;",
                        true);
            } else if (meType.isInnerField()) {
                common(proxyObjectName, methodVisitor, index);
                methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                        MESSAGE_PARSER_CALLBACK,
                        "parserInnerFiled",
                        "(Ljava/lang/Object;" + CLassUtils.getClassType(MeType.class) + "[Ljava/lang/Object;)Ljava/lang/Object;",
                        true);

            } else if (meType.isCommon()) {
                common(proxyObjectName, methodVisitor, index);
                methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                        MESSAGE_PARSER_CALLBACK,
                        "parserCommonParam",
                        "(L" + REQUEST_STREAM_FACTORY + ";" + CLassUtils.getClassType(MeType.class) + "[Ljava/lang/Object;)Ljava/lang/Object;",
                        true);
            } else {
                Class<?> type = meType.getType();
                if (CLassUtils.isStringClass(type)) {
                    methodVisitor.visitVarInsn(ALOAD, 1);
                    CLassUtils.putInt(methodVisitor, meType.getLen());
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                            REQUEST_STREAM_FACTORY,
                            "readString",
                            "(I)Ljava/lang/String;", true);
                    int varIndex = localNum.get();
                    methodVisitor.visitVarInsn(ASTORE, varIndex);
                    handleStringDef(methodVisitor, proxyObjectName, index, localNum);
                } else if (type == DynamicString.class) {
                    methodVisitor.visitVarInsn(ALOAD, 1);
                    methodVisitor.visitVarInsn(ALOAD, 1);
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                            REQUEST_STREAM_FACTORY,
                            "readInt",
                            "()I",
                            true);
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                            REQUEST_STREAM_FACTORY,
                            "readString",
                            "(I)Ljava/lang/String;", true);
                    int varIndex = localNum.get();
                    methodVisitor.visitVarInsn(ASTORE, varIndex);
                    handleStringDef(methodVisitor, proxyObjectName, index, localNum);
                } else if (CLassUtils.isByte(type)) {
                    methodVisitor.visitVarInsn(ALOAD, 1);
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                            REQUEST_STREAM_FACTORY,
                            "readByte",
                            "()B",
                            true);
                    methodVisitor.visitVarInsn(ISTORE, localNum.get());
                    handleNumDef(UnpackDesc.BYTE,
                            Opcodes.ILOAD,
                            () -> methodVisitor.visitVarInsn(ILOAD, localNum.get()),
                            () -> methodVisitor.visitVarInsn(ISTORE, localNum.get()),
                            meType, methodVisitor,
                            proxyObjectName,
                            index,
                            localNum);
                } else if (CLassUtils.isBool(type)) {
                    methodVisitor.visitVarInsn(ALOAD, 4);
                    CLassUtils.putInt(methodVisitor, index.get());
                    methodVisitor.visitVarInsn(ALOAD, 1);
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE, REQUEST_STREAM_FACTORY, "readBool", "()Z", true);
                    methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
                } else if (CLassUtils.isShort(type)) {
                    methodVisitor.visitVarInsn(ALOAD, 1);
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                            REQUEST_STREAM_FACTORY,
                            "readShort",
                            "()S",
                            true);
                    methodVisitor.visitVarInsn(ISTORE, localNum.get());
                    handleNumDef(UnpackDesc.SHORT,
                            Opcodes.ILOAD,
                            () -> methodVisitor.visitVarInsn(ILOAD, localNum.get()),
                            () -> methodVisitor.visitVarInsn(ISTORE, localNum.get()),
                            meType, methodVisitor,
                            proxyObjectName,
                            index,
                            localNum);
                } else if (CLassUtils.isInteger(type)) {
                    methodVisitor.visitVarInsn(ALOAD, 1);
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                            REQUEST_STREAM_FACTORY,
                            "readInt",
                            "()I",
                            true);
                    methodVisitor.visitVarInsn(ISTORE, localNum.get());
                    handleNumDef(UnpackDesc.INTEGER,
                            Opcodes.ILOAD,
                            () -> {
                                methodVisitor.visitVarInsn(ILOAD, localNum.get());
                            },
                            () -> methodVisitor.visitVarInsn(ISTORE, localNum.get()),
                            meType, methodVisitor,
                            proxyObjectName,
                            index,
                            localNum);
                } else if (CLassUtils.isLong(type)) {
                    methodVisitor.visitVarInsn(ALOAD, 1);
                    methodVisitor.visitMethodInsn(INVOKEINTERFACE,
                            REQUEST_STREAM_FACTORY,
                            "readLong",
                            "()J",
                            true);
                    methodVisitor.visitVarInsn(LSTORE, localNum.get());
                    handleNumDef(UnpackDesc.LONG, Opcodes.LLOAD,
                            () -> {
                                methodVisitor.visitVarInsn(Opcodes.LLOAD, localNum.get());
                                methodVisitor.visitInsn(LCONST_0);
                                methodVisitor.visitInsn(LCMP);
                            },
                            () -> methodVisitor.visitVarInsn(LSTORE, localNum.get()),
                            meType, methodVisitor,
                            proxyObjectName,
                            index,
                            localNum);
                } else if (type == List.class) {
                    buildListInt(index, localNum, methodVisitor);
                } else if (type.isArray()) {
                    boolean isIntArr;
                    String typeName = type.getName();
                    if ("[I".equals(typeName)) {
                        isIntArr = true;
                    } else if ("[Ljava.lang.Integer;".equals(typeName)) {
                        isIntArr = false;
                    } else {
                        throw new UnSupportFieldException("not support un int Type :" + typeName);
                    }
                    buildIntArr(localNum, methodVisitor, index, isIntArr);
                } else {
                    throw new ConverterException("not support class : " + meType);
                }
            }

            methodVisitor.visitInsn(AASTORE);
            index.incrementAndGet();
        }
        methodVisitor.visitVarInsn(ALOAD, 4);
        methodVisitor.visitInsn(ARETURN);
        methodVisitor.visitMaxs(0, localNum.get());
        methodVisitor.visitEnd();
    }

    private static void common(String proxyObjectName, MethodVisitor methodVisitor, AtomicInteger index) {
        methodVisitor.visitVarInsn(ALOAD, 4);
        int paramIndex = index.get();
        CLassUtils.putInt(methodVisitor, paramIndex);
        methodVisitor.visitVarInsn(ALOAD, 2);
        methodVisitor.visitVarInsn(ALOAD, 1);
        loadMeType(proxyObjectName, methodVisitor, index);
        methodVisitor.visitVarInsn(ALOAD, 3);
    }

    private static void buildIntArr(AtomicInteger localNum, MethodVisitor methodVisitor, AtomicInteger index, boolean isIntArr) {
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, REQUEST_STREAM_FACTORY, "readInt", "()I", true);
        int readLenIndex = localNum.getAndIncrement();
        methodVisitor.visitVarInsn(ISTORE, readLenIndex);
        methodVisitor.visitVarInsn(ILOAD, readLenIndex);
        if (isIntArr) {
            methodVisitor.visitIntInsn(NEWARRAY, T_INT);
        } else {
            methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/Integer");
        }
        int arrayIndex = localNum.getAndIncrement();
        methodVisitor.visitVarInsn(ASTORE, arrayIndex);
        methodVisitor.visitInsn(ICONST_0);
        int flagIndex = localNum.getAndIncrement();
        methodVisitor.visitVarInsn(ISTORE, flagIndex);
        Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitVarInsn(ILOAD, readLenIndex);
        methodVisitor.visitVarInsn(ILOAD, flagIndex);
        Label label1 = new Label();
        methodVisitor.visitJumpInsn(IF_ICMPLE, label1);
        methodVisitor.visitVarInsn(ALOAD, arrayIndex);
        methodVisitor.visitVarInsn(ILOAD, flagIndex);
        methodVisitor.visitIincInsn(flagIndex, 1);
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, REQUEST_STREAM_FACTORY, "readInt", "()I", true);
        if (isIntArr) {
            methodVisitor.visitInsn(IASTORE);
        } else {
            methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
            methodVisitor.visitInsn(AASTORE);
        }
        methodVisitor.visitJumpInsn(GOTO, label0);
        methodVisitor.visitLabel(label1);
        methodVisitor.visitVarInsn(ALOAD, 4);
        CLassUtils.putInt(methodVisitor, index.get());
        methodVisitor.visitVarInsn(ALOAD, arrayIndex);
    }

    private static void buildListInt(AtomicInteger index, AtomicInteger localNum, MethodVisitor methodVisitor) {
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, REQUEST_STREAM_FACTORY, "readInt", "()I", true);
        int lenIndex = localNum.getAndIncrement();
        methodVisitor.visitVarInsn(ISTORE, lenIndex);
        methodVisitor.visitTypeInsn(NEW, "java/util/ArrayList");
        methodVisitor.visitInsn(DUP);
        methodVisitor.visitVarInsn(ILOAD, lenIndex);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "(I)V", false);
        int listIndex = localNum.getAndIncrement();
        methodVisitor.visitVarInsn(ASTORE, listIndex);
        Label label0 = new Label();
        methodVisitor.visitLabel(label0);
        methodVisitor.visitVarInsn(ILOAD, lenIndex);
        methodVisitor.visitIincInsn(lenIndex, -1);
        Label label1 = new Label();
        methodVisitor.visitJumpInsn(IFLE, label1);
        methodVisitor.visitVarInsn(ALOAD, listIndex);
        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, REQUEST_STREAM_FACTORY, "readInt", "()I", true);
        methodVisitor.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
        methodVisitor.visitInsn(POP);
        methodVisitor.visitJumpInsn(GOTO, label0);
        methodVisitor.visitLabel(label1);
        methodVisitor.visitVarInsn(ALOAD, 4);
        CLassUtils.putInt(methodVisitor, index.get());
        methodVisitor.visitVarInsn(ALOAD, listIndex);
    }


    private static void handleStringDef(MethodVisitor methodVisitor, String proxyObjectName, AtomicInteger index, AtomicInteger localNum) {
        loadMeType(proxyObjectName, methodVisitor, index);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "io/github/jinlongliao/easy/server/core/parser/MeType", "hasDef", "()Z", false);
        Label label0 = new Label();
        methodVisitor.visitJumpInsn(IFEQ, label0);
        int varIndex = localNum.get();
        methodVisitor.visitVarInsn(ALOAD, varIndex);
        Label label1 = new Label();
        methodVisitor.visitJumpInsn(IFNULL, label1);
        methodVisitor.visitVarInsn(ALOAD, varIndex);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "isEmpty", "()Z", false);
        methodVisitor.visitJumpInsn(IFEQ, label0);
        methodVisitor.visitLabel(label1);

        loadMeType(proxyObjectName, methodVisitor, index);

        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "io/github/jinlongliao/easy/server/core/parser/MeType", "getDefaultValue", "()Ljava/lang/Object;", false);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
        methodVisitor.visitVarInsn(ASTORE, varIndex);
        methodVisitor.visitLabel(label0);
        methodVisitor.visitVarInsn(ALOAD, 4);
        CLassUtils.putInt(methodVisitor, index.get());
        methodVisitor.visitVarInsn(ALOAD, localNum.getAndIncrement());
    }

    private static void handleNumDef(UnpackDesc unpackDesc,
                                     int loadOpcodes,
                                     Runnable compare,
                                     Runnable putNewValue,
                                     MeType meType,
                                     MethodVisitor methodVisitor,
                                     String proxyObjectName,
                                     AtomicInteger index,
                                     AtomicInteger localNum) {
        loadMeType(proxyObjectName, methodVisitor, index);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "io/github/jinlongliao/easy/server/core/parser/MeType", "hasDef", "()Z", false);
        Label label0 = new Label();
        methodVisitor.visitJumpInsn(IFEQ, label0);
        compare.run();
        methodVisitor.visitJumpInsn(IFNE, label0);
        loadMeType(proxyObjectName, methodVisitor, index);
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "io/github/jinlongliao/easy/server/core/parser/MeType", "getDefaultValue", "()Ljava/lang/Object;", false);
        methodVisitor.visitTypeInsn(CHECKCAST, unpackDesc.getOwner());
        methodVisitor.visitMethodInsn(INVOKEVIRTUAL, unpackDesc.getOwner(), unpackDesc.getBaseMethodName(), unpackDesc.getBaseDescriptor(), false);
        putNewValue.run();
        methodVisitor.visitLabel(label0);


        methodVisitor.visitVarInsn(ALOAD, 4);
        CLassUtils.putInt(methodVisitor, index.get());
        methodVisitor.visitVarInsn(loadOpcodes, localNum.getAndIncrement());
        methodVisitor.visitMethodInsn(INVOKESTATIC, unpackDesc.getOwner(), unpackDesc.getPackMethodName(), unpackDesc.getPackDescriptor(), false);

    }

    private static void loadMeType(String proxyObjectName, MethodVisitor methodVisitor, AtomicInteger index) {
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitFieldInsn(GETFIELD, proxyObjectName, "rules", "Ljava/util/List;");
        CLassUtils.putInt(methodVisitor, index.get());
        methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;", true);
        methodVisitor.visitTypeInsn(CHECKCAST, "io/github/jinlongliao/easy/server/core/parser/MeType");
    }
}
