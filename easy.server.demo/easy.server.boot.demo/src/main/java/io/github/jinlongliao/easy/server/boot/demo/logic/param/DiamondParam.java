package io.github.jinlongliao.easy.server.boot.demo.logic.param;

import io.github.jinlongliao.easy.server.mapper.annotation.GeneratorCopy;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: liaojinlong
 * @date: 2023/10/18 13:29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@GeneratorCopy
public class DiamondParam<T> {
    private String string;
    private Integer num;
    private T data;
}
