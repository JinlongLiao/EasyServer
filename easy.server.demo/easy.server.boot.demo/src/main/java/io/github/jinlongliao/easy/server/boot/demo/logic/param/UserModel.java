package io.github.jinlongliao.easy.server.boot.demo.logic.param;

import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;
import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorCopy;
import io.github.jinlongliao.easy.server.mapper.annotation.Mapping;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liaojinlong
 * @since 2021/2/22 23:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@GeneratorCopy
public class UserModel {
    @NotNull
    @LogicRequestParam("userId")
    private String userId;
    @LogicRequestParam("age")
    private Integer age;
    @LogicRequestParam("key")
    @Mapping(sourceName = "logicId")
    private Integer key;
}
