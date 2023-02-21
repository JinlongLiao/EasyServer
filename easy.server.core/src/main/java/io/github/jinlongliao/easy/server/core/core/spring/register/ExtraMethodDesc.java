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
        private final int msgId;
        private final String desc;

        public MethodDesc(int msgId, String desc) {
            this.msgId = msgId;
            this.desc = desc;
        }

        public int getMsgId() {
            return msgId;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public String toString() {
            return "ExtraMethodDesc{" +
                    "msgId=" + msgId +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }
}
