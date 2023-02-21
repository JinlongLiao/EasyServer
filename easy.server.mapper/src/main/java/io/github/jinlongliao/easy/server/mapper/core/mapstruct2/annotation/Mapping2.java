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
package io.github.jinlongliao.easy.server.mapper.core.mapstruct2.annotation;


import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.converter.InnerConverter;

import java.lang.annotation.*;

/**
 * 映射数据源
 *
 * @author liaojinlong
 * @since 2020/11/21 23:38
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Mapping2 {
    /**
     * @return 映射源名称
     */
    String sourceName() default "";

    /**
     * set Method Name
     * @return
     */
    String putMethod() default "";


    /**
     * 针对非基本类型（String,byte,short,int,float,double,long,char）<br/>
     * 除外需要指定自定义 静态转换函数
     * eg:
     * <pre>
     *     public static Person person(Object obj){
     *         return (Person)obj;
     *     }
     * </pre>
     *
     * @return 数据强转函数名称
     */
    String converterMethod() default "";

    /**
     * @return Class Name
     * @see {@link Mapping2#converterMethod()}
     */
    Class converterClass() default InnerConverter.class;
}
