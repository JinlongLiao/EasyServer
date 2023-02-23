package io.github.jinlongliao.easy.server.core.core.spring.register;

import java.util.Collection;

/**
 * 描述扩展接口
 *
 * @date 2023-01-29 12:23
 * @author: liaojinlong
 * @description: /
 **/

public class ExtraMethodDesc {
    private final Collection<MethodDesc> methodDes;

    public ExtraMethodDesc(Collection<MethodDesc> methodDes) {
        this.methodDes = methodDes;
    }

    public Collection<MethodDesc> getMethodDes() {
        return methodDes;
    }

    @Override
    public String toString() {
        return "ExtraMethodDesc{" +
                "methodDes=" + methodDes +
                '}';
    }

    /**
     * 描述扩展接口
     *
     * @date 2023-01-29 12:23
     * @author: liaojinlong
     * @description: /
     **/

    public static class MethodDesc {
        private final String logicId;
        private final String desc;

        public MethodDesc(String logicId, String desc) {
            this.logicId = logicId;
            this.desc = desc;
        }

        public String getLogicId() {
            return logicId;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public String toString() {
            return "ExtraMethodDesc{" +
                    "logicId=" + logicId +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }
}
