package io.github.jinlongliao.easy.server.demo.logic.annotation;

import io.github.jinlongliao.easy.server.core.core.MethodInfo;
import io.github.jinlongliao.easy.server.core.core.spring.register.ExtraMethodAnnotationProcess;
import io.github.jinlongliao.easy.server.core.core.spring.register.ExtraMethodDesc;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 额外解析
 *
 * @author: liaojinlong
 * @date: 2023/1/31 22:23
 */
public class LogicExtraMethodAnnotationProcess implements ExtraMethodAnnotationProcess {
    @Override
    public ExtraMethodDesc extraProcessMethod(Map<Integer, MethodInfo> data, Method method) {
        Logic logic = AnnotationUtils.getAnnotation(method, Logic.class);
        if (Objects.isNull(logic)) {
            return null;
        }
        List<ExtraMethodDesc.MethodDesc> desc = Arrays.stream(logic.value())
                .map(node -> new ExtraMethodDesc.MethodDesc(node.getMgsId(), node.getDesc()))
                .collect(Collectors.toList());
        return new ExtraMethodDesc(desc);
    }
}
