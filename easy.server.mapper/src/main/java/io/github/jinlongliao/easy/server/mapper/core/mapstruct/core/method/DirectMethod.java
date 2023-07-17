package io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.method;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

/**
 * 直接使用java 字节码访问
 *
 * @author: liaojinlong
 * @date: 2022/8/16 22:30
 */
public class DirectMethod {
    private final Method method;
    private final MethodInvoke methodInvoke;

    public DirectMethod(Method method) {
        if (Objects.nonNull(method)) {
            this.method = method;
            this.methodInvoke = buildInvoke(method);
        } else {
            this.method = null;
            this.methodInvoke = null;
        }
    }

    private MethodInvoke buildInvoke(Method method) {
        return MethodInvokeFactory.buildMethodInvoke(method);
    }

    public Object invoke(Object obj, Object... args) throws Exception {
        return this.methodInvoke.invoke(obj, args);

    }

    public Method getMethod() {
        return method;
    }

    public static DirectMethod valueOf(Method method) {
        return new DirectMethod(method);
    }

    public String getName() {
        return method.getName();
    }

    private Type getGenericReturnType() {
        return method.getGenericReturnType();
    }

    private Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    private Class<?> getReturnType() {
        return method.getReturnType();
    }

    private Annotation[] getAnnotations() {
        return method.getAnnotations();
    }

    private Class<?> getDeclaringClass() {
        return method.getDeclaringClass();
    }

    private Annotation[][] getParameterAnnotations() {
        return method.getParameterAnnotations();
    }

    private Type[] getGenericParameterTypes() {
        return method.getGenericParameterTypes();
    }

    private int getParameterCount() {
        return method.getParameterCount();
    }

    private TypeVariable<Method>[] getTypeParameters() {
        return method.getTypeParameters();
    }

}
