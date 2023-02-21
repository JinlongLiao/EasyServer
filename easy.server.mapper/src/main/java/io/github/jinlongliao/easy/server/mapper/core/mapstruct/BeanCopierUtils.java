/**
 * Copyright 2020-2021 JinlongLiao
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.jinlongliao.easy.server.mapper.core.mapstruct;


import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.IData2Object;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.Proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean 转换
 *
 * @author liaojinlong
 * @since 2020/11/21 23:32
 */

public class BeanCopierUtils {
    /**
     * 实现类缓存
     */
    private final static Map<Class, IData2Object> DATA_2_OBJECT_CACHE = new ConcurrentHashMap<>(1 << 8);
    private final static Map<Class, IData2Object> FULL_DATA_2_OBJECT_CACHE = new ConcurrentHashMap<>(1 << 8);
    private static final Proxy PROXY = new Proxy();

    /**
     * 获取数据转换类
     *
     * @param tClass
     * @return IData2Object
     */
    public static <T> IData2Object<T> getData2Object(Class<T> tClass) {
        if (DATA_2_OBJECT_CACHE.containsKey(tClass)) {
            return DATA_2_OBJECT_CACHE.get(tClass);
        }
        synchronized (tClass) {
            if (DATA_2_OBJECT_CACHE.containsKey(tClass)) {
                return DATA_2_OBJECT_CACHE.get(tClass);
            } else {
                final IData2Object proxyObject = PROXY.getProxyObject(tClass);
                DATA_2_OBJECT_CACHE.put(tClass, proxyObject);
                return proxyObject;
            }
        }
    }

    /**
     * 获取数据转换类,包涵父类属性
     *
     * @param tClass
     * @return IData2Object
     */
    public static <T> IData2Object<T> getFullData2Object(Class<T> tClass) {
        if (FULL_DATA_2_OBJECT_CACHE.containsKey(tClass)) {
            return FULL_DATA_2_OBJECT_CACHE.get(tClass);
        }
        synchronized (tClass) {
            if (FULL_DATA_2_OBJECT_CACHE.containsKey(tClass)) {
                return FULL_DATA_2_OBJECT_CACHE.get(tClass);
            } else {
                final IData2Object proxyObject = PROXY.getProxyObject(tClass, true);
                FULL_DATA_2_OBJECT_CACHE.put(tClass, proxyObject);
                return proxyObject;
            }
        }
    }
}
