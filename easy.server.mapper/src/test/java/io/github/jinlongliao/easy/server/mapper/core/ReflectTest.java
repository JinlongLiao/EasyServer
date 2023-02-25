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
package io.github.jinlongliao.easy.server.mapper.core;


import io.github.jinlongliao.easy.server.mapper.core.mapstruct.BeanCopierUtils;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.annotation.Ignore;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.core.IData2Object;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.BeanCopier2Utils;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin.core.wrap.ICoreData2Object2;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author liaojinlong
 * @since 2020/11/24 23:03
 */

public class ReflectTest {
    public static boolean warmup = false;
    private Map<String, Object> data = new HashMap<>();
    private Map<String, Object> dataMap = new TreeMap<String, Object>() {{
        put("grep", 1234);
        put("name", "liaojl");
        put("age", 26);
        put("birthday", new Date());
        put("arr", Arrays.asList("2312", "12423"));
        put("arr2", data);
        put("array", new int[]{1, 2, 3});
    }};
    private Object[] dataArray = new Object[]{13, "liaojl", 26, new Date(), Arrays.asList("2312", "12423"), data, new int[]{1, 2, 3}};
    private final ICoreData2Object2<Person> data2Object2 = BeanCopier2Utils.getFullData2Object(Person.class);

    @org.junit.Ignore
    @Test
    public void test() throws Exception {
        testMapCustomize2();
        testArrayCustomize2();
        testReflect();
        warmup = true;
        testReflect();
        testMapCustomize2();
        testArrayCustomize2();
    }

    public static final int SIZE = 1000000;

    private void testMapCustomize2() {
        final long start = System.currentTimeMillis();
        Person person;
        for (int i = 0; i < SIZE; i++) {
            final Class<Person> personClass = Person.class;
            person = data2Object2.toMapConverter(dataMap);
        }
        final long end = System.currentTimeMillis();
        if (warmup)
            System.out.println("testMapCustomize2:" + (end - start));
    }

    private void testArrayCustomize2() {
        final long start = System.currentTimeMillis();
        Person person = null;
        for (int i = 0; i < SIZE; i++) {
            person = data2Object2.toArrayConverter(dataArray);
        }
        final long end = System.currentTimeMillis();
        if (warmup)
            System.out.println("testArrayCustomize2:" + (end - start));
    }

    private void testReflect() throws Exception {
        final long start = System.currentTimeMillis();
        for (int i = 0; i < SIZE; i++) {
            final Class<Person> personClass = Person.class;
            Object person = personClass.newInstance();
            final Field[] fields = personClass.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                Ignore annotation = field.getAnnotation(Ignore.class);
                if (annotation != null) {
                    continue;
                }
                field.setAccessible(Boolean.TRUE);
                field.set(person, dataMap.get(field.getName()));
            }
        }
        final long end = System.currentTimeMillis();
        if (warmup)
            System.out.println("testReflect:" + (end - start));
    }
}

