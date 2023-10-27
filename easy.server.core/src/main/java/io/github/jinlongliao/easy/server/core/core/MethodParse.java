package io.github.jinlongliao.easy.server.core.core;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.method.DirectMethod;
import io.github.jinlongliao.easy.server.core.annotation.LogicAlias;
import io.github.jinlongliao.easy.server.core.annotation.LogicMapping;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestBody;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;
import io.github.jinlongliao.easy.server.core.core.spring.register.ExtraMethodAnnotationProcess;
import io.github.jinlongliao.easy.server.core.core.spring.register.ExtraMethodDesc;
import io.github.jinlongliao.easy.server.core.core.spring.register.MethodPostProcess;
import io.github.jinlongliao.easy.server.core.model.LogicModel;
import io.github.jinlongliao.easy.server.core.model.MsgModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author liaojinlong
 * @since 2021/1/23 00:16
 */
public class MethodParse implements AutoCloseable {
    public static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Map<String, LogicModel> logicDefineCache = new HashMap<>(32);
    private final List<MethodPostProcess> methodPostProcesses;
    private static final Collection<ExtraMethodAnnotationProcess> EXTRA_METHOD_ANNOTATION_PROCESSES = ((Supplier<Collection<ExtraMethodAnnotationProcess>>) () -> {

        List<ExtraMethodAnnotationProcess> extraMethodAnnotationProcesses = null;
        for (ExtraMethodAnnotationProcess extraMethodAnnotationProcess : ServiceLoader.load(ExtraMethodAnnotationProcess.class)) {
            if (Objects.isNull(extraMethodAnnotationProcesses)) {
                extraMethodAnnotationProcesses = new ArrayList<>();
            }
            extraMethodAnnotationProcesses.add(extraMethodAnnotationProcess);
        }
        if (Objects.isNull(extraMethodAnnotationProcesses)) {
            extraMethodAnnotationProcesses = Collections.emptyList();
        } else {
            extraMethodAnnotationProcesses = Collections.unmodifiableList(extraMethodAnnotationProcesses);
        }
        return extraMethodAnnotationProcesses;
    }).get();

    /**
     * Spring 代理实现 获取方法参数名称
     */
    private final ParameterNameDiscoverer parameterNameDiscoverer;

    public MethodParse() {
        this(MethodParse.PARAMETER_NAME_DISCOVERER, Collections.emptyList());
    }

    public MethodParse(List<MethodPostProcess> methodPostProcesses) {
        this(MethodParse.PARAMETER_NAME_DISCOVERER, methodPostProcesses);
    }


    public MethodParse(ParameterNameDiscoverer parameterNameDiscoverer, List<MethodPostProcess> methodPostProcesses) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
        methodPostProcesses.sort(Comparator.comparingInt(MethodPostProcess::order));
        this.methodPostProcesses = methodPostProcesses;
    }

    /**
     * 构建参数
     *
     * @param beanName /
     * @param value    /
     * @return /
     */
    public void parserLogic(String beanName, Object value) {
        Class<?> targetClass = AopUtils.getTargetClass(value);
        Map<String, MethodInfo> logics = getLogic(targetClass);
        if (CollectionUtils.isEmpty(logics)) {
            return;
        }
        for (Map.Entry<String, MethodInfo> logic : logics.entrySet()) {
            MethodInfo methodInfo = logic.getValue();
            String msgTypeId = logic.getKey();
            DirectMethod directMethod = methodInfo.getDirectMethod();
            for (MethodPostProcess methodPostProcess : methodPostProcesses) {
                directMethod = methodPostProcess.process(targetClass, directMethod);
            }
            LogicModel logicModel = new LogicModel(value, targetClass, directMethod, methodInfo.getMsgModels(), methodInfo.getDesc(), beanName);

            logicDefineCache.put(msgTypeId, logicModel);
        }
    }


    public Map<String, MethodInfo> getLogic(Class<?> targetClass) {
        Method[] declaredMethods = targetClass.getDeclaredMethods();
        if (declaredMethods.length == 0) {
            return null;
        }
        Map<String, MethodInfo> data = new HashMap<>(declaredMethods.length);
        Arrays.stream(declaredMethods).forEach(method -> this.parseMethod(data, method));
        return data;
    }

    protected void parseMethod(Map<String, MethodInfo> data, Method method) {
        LogicMapping logicMapping = AnnotationUtils.getAnnotation(method, LogicMapping.class);
        List<String> logicIds = null;
        List<ExtraMethodDesc> extraLogicIds = null;
        if (Objects.nonNull(logicMapping)) {
            logicIds = (Arrays.stream(logicMapping.value()).collect(Collectors.toList()));
        }
        for (ExtraMethodAnnotationProcess extraMethodAnnotationProcess : EXTRA_METHOD_ANNOTATION_PROCESSES) {
            ExtraMethodDesc extraMethodDesc = extraMethodAnnotationProcess.extraProcessMethod(data, method);
            if (Objects.nonNull(extraMethodDesc)) {
                if (Objects.isNull(extraLogicIds)) {
                    extraLogicIds = new ArrayList<>();
                }
                extraLogicIds.add(extraMethodDesc);
            }
        }
        if (Objects.isNull(logicIds) && Objects.isNull(extraLogicIds)) {
            return;
        }
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<MsgModel> msgModels;

        if (parameterTypes.length == 0) {
            msgModels = Collections.emptyList();
        } else {
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            msgModels = new ArrayList<>(parameterTypes.length);
            for (int i = 0; i < parameterTypes.length; i++) {
                Annotation[] annotations = parameterAnnotations[i];
                Class<?> type = parameterTypes[i];
                // @MsgRequestParam解析
                parseRequestParam(i, type, parameterNames, annotations, msgModels);
            }
        }
        if (Objects.nonNull(logicIds)) {
            for (String logicId : logicIds) {
                data.put(logicId, new MethodInfo(logicId, DirectMethod.valueOf(method), msgModels, logicMapping.desc()));
            }
        }
        if (Objects.nonNull(extraLogicIds)) {
            for (ExtraMethodDesc extraLogicId : extraLogicIds) {
                for (ExtraMethodDesc.MethodDesc methodDesc : extraLogicId.getMethodDes()) {
                    data.put(methodDesc.getLogicId(), new MethodInfo(methodDesc.getLogicId(), DirectMethod.valueOf(method), msgModels, methodDesc.getDesc()));
                }
            }
        }
    }


    /**
     * 解析 @MsgRequestParam
     *
     * @param i
     * @param type
     * @param parameterNames
     * @param annotations
     * @param msgModels
     */
    protected void parseRequestParam(int i, Class<?> type, String[] parameterNames, Annotation[] annotations, List<MsgModel> msgModels) {
        String logicId = "";
        final LogicRequestParam logicRequestParam = getAnnotation(annotations, LogicRequestParam.class);
        boolean isBody = false;
        String paramName;
        String defaultValue = "";
        int length = 0;
        boolean dynamicLen = false;
        boolean innerField = false;
        boolean isCommon = false;
        if (logicRequestParam != null) {
            defaultValue = logicRequestParam.defaultValue();
            paramName = logicRequestParam.value();
            if (paramName == null || paramName.trim().length() == 0) {
                paramName = parameterNames[i];
            }
            length = logicRequestParam.length();
            dynamicLen = logicRequestParam.dynamicLength();
            innerField = logicRequestParam.innerParse();
            isCommon = logicRequestParam.isCommon();
        } else {
            final LogicRequestBody logicRequestBody = getAnnotation(annotations, LogicRequestBody.class);
            if (logicRequestBody == null) {
                paramName = parameterNames[i];
            } else {
                defaultValue = logicRequestBody.defaultValue();
                paramName = logicRequestBody.value();
                logicId = logicRequestBody.logicId();
                isBody = true;
            }
        }
        msgModels.add(new MsgModel(paramName, type, length, dynamicLen, annotations, isBody, logicId, defaultValue, isCommon, innerField));
    }

    private <T extends Annotation> T getAnnotation(Annotation[] annotations, Class<T> ctClass) {
        T msgAnnotation = null;
        boolean first = false;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == ctClass) {
                first = true;
                msgAnnotation = (T) annotation;
            } else {
                msgAnnotation = AnnotationUtils.getAnnotation(annotation, ctClass);
            }
            if (msgAnnotation != null) {
                if (!first) {
                    if (ctClass == LogicRequestParam.class) {
                        LogicRequestParamWrap logicRequestParamWrap = new LogicRequestParamWrap((LogicRequestParam) (msgAnnotation));
                        try {
                            this.putLogicRequestParamAlias(logicRequestParamWrap, annotation);
                        } catch (Exception e) {
                            log.warn(e.getMessage(), e);
                        }
                        msgAnnotation = (T) logicRequestParamWrap;
                    }

                }
                break;
            }
        }
        return msgAnnotation;
    }

    private void putLogicRequestParamAlias(LogicRequestParamWrap logicRequestParamWrap, Annotation annotation) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = annotation.annotationType().getDeclaredMethods();
        for (Method method : methods) {
            LogicAlias logicAlias = method.getAnnotation(LogicAlias.class);
            if (Objects.nonNull(logicAlias)) {
                String value = logicAlias.value();
                if (value.equals("value")) {
                    logicRequestParamWrap.setValue((String) method.invoke(annotation));
                } else if (value.equals("length")) {
                    logicRequestParamWrap.setLength((int) method.invoke(annotation));
                } else if (value.equals("dynamicLength")) {
                    logicRequestParamWrap.setDynamicLength((boolean) method.invoke(annotation));
                } else if (value.equals("defaultValue")) {
                    logicRequestParamWrap.setDefaultValue((String) method.invoke(annotation));
                } else if (value.equals("isCommon")) {
                    logicRequestParamWrap.setCommon((boolean) method.invoke(annotation));
                } else if (value.equals("innerParse")) {
                    logicRequestParamWrap.setInnerParse((boolean) method.invoke(annotation));
                }
            }
        }

    }

    public Map<String, LogicModel> getLogicDefineCache() {
        return logicDefineCache;
    }


    public ParameterNameDiscoverer getParameterNameDiscoverer() {
        return parameterNameDiscoverer;
    }

    /**
     * 手动添加
     *
     * @param logicId
     * @param logicModel
     * @return /
     */
    public LogicModel putLogicDefine(String logicId, LogicModel logicModel) {
        return this.logicDefineCache.put(logicId, logicModel);
    }

    @Override
    public void close() throws Exception {
        this.logicDefineCache.clear();
    }
}
