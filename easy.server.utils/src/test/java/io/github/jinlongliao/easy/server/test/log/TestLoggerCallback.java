package io.github.jinlongliao.easy.server.test.log;

import io.github.jinlongliao.easy.server.utils.logger.core.callback.LoggerCallback;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestLoggerCallback implements LoggerCallback {
    @Override
    public <T extends CharSequence> T loggerCall(T logger) {
        List<String> collect = Arrays.stream(logger.toString().split("\n")).collect(Collectors.toList());
        collect = collect.subList(0, Math.min(5, collect.size()));
        StringBuilder builder = new StringBuilder("\n_____________________\n");
        for (String str : collect) {
            builder.append(str);
            builder.append("\n_____________________\n");
        }
        if (logger instanceof String) {
            return (T) builder.toString();
        }
        return (T) builder;
    }
}
