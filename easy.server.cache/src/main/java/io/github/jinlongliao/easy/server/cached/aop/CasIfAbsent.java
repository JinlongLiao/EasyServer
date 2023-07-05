package io.github.jinlongliao.easy.server.cached.aop;

import java.time.Duration;

public interface CasIfAbsent {
 boolean   setKeyIfAbsentDuration(String key, Duration duration);
}
