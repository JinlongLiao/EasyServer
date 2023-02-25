package io.github.jinlongliao.easy.server.demo.logic.param;

import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorCopy;
import io.github.jinlongliao.easy.server.mapper.core.mapstruct2.annotation.Mapping2;
import io.github.jinlongliao.easy.server.core.annotation.LogicRequestParam;

import javax.validation.constraints.NotNull;

/**
 * @author liaojinlong
 * @since 2021/2/22 23:05
 */
@GeneratorCopy
public class UserModel {
    @NotNull
    @LogicRequestParam("userId")
    private String userId;
    @LogicRequestParam("age")
    private Integer age;
    @LogicRequestParam("key")
    @Mapping2(sourceName = "logicId")
    private Integer key;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userId='" + userId + '\'' +
                ", age=" + age +
                ", key=" + key +
                '}';
    }
}
