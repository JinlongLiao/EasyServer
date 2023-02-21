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
package io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.plugin.core.map;

import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.core.converter.IData2Object2;

import java.util.Map;

/**
 * 将元数据转换为相应的对象
 *
 * @author liaojinlong
 * @since 2020/11/23 21:23
 */

public interface IMapData2Object2<T> extends IData2Object2<T> {
    /**
     * 转换接口
     *
     * @param data
     * @return T
     */
    T toMapConverter(Map<String, Object> data);

}
