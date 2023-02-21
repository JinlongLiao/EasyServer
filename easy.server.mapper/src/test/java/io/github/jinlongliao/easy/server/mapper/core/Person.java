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

import io.github.jinlongliao.easy.server.mapper.core.mapstruct.annotation.Ignore;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct.annotation.Mapping;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.annotation.Ignore2;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.annotation.Mapping2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Person extends Grep implements IAnimal {
    private static final Logger log = LoggerFactory.getLogger(Person.class);
    @Ignore
    @Ignore2
    private int ignore;
    private String name;
    private int age;
    @Mapping(method = "setN")
    @Mapping2(putMethod = "setN")
    private Date birthday;
    private List<String> arr;
    private Map<String, Object> arr2;
    public int[] array;

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setN(Date birthday) {
        this.birthday = birthday;
    }

    public Map<String, Object> getArr2() {
        return arr2;
    }

    public void setArr2(Map<String, Object> arr2) {
        this.arr2 = arr2;
    }

    public List<String> getArr() {
        return arr;
    }

    public void setArr(List<String> arr) {
        this.arr = arr;
    }

    public int getIgnore() {
        return ignore;
    }

    public void setIgnore(int ignore) {
        this.ignore = ignore;
    }

    @Override
    public String toString() {
        return "Person{" +
                "ignore=" + ignore +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                ", arr=" + arr +
                ", arr2=" + arr2 +
                ", array=" + Arrays.toString(array) +
                '}';
    }
}
