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
package io.github.jinlongliao.easy.server.mapper.map;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;


public class MapTest {
    public static final int SIZE = 10000000;
    public static final String KEY = "key";
    public static final Map<String, Object> treeMap = new TreeMap<String, Object>() {{
        put(KEY, "");
    }};
    public static final Map<String, Object> hashMap = new HashMap<String, Object>() {{
        put(KEY, "");
    }};
    public static final Map<String, Object> cronHashMap = new ConcurrentHashMap<String, Object>() {{
        put(KEY, "");
    }};

    @Test
    public void test() throws Exception {
        testTreeMap();
        testHashMap();
        testCronMap();
    }

    private void testCronMap() {
        final long start = System.currentTimeMillis();
        for (int i = 0; i < SIZE; i++) {
            cronHashMap.get(KEY);
            cronHashMap.put(KEY, "");
        }
        final long end = System.currentTimeMillis();
        System.out.println("testCronMap:" + (end - start));
    }

    private void testHashMap() {
        final long start = System.currentTimeMillis();
        for (int i = 0; i < SIZE; i++) {
            hashMap.get(KEY);
            hashMap.put(KEY, "");

        }
        final long end = System.currentTimeMillis();
        System.out.println("testHashMap:" + (end - start));
    }

    private void testTreeMap() {
        final long start = System.currentTimeMillis();
        for (int i = 0; i < SIZE; i++) {
            treeMap.get(KEY);
            treeMap.put(KEY, "");
        }
        final long end = System.currentTimeMillis();
        System.out.println("testTreeMap:" + (end - start));

    }
}
