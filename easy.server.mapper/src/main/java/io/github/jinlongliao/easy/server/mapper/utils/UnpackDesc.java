package io.github.jinlongliao.easy.server.mapper.utils;

/**
 * <p>基础类型装开箱转换描述</p>
 *
 * @author: liaojinlong
 * @date: 2022-08-18 09:29
 */
public final class UnpackDesc {
    private final String owner;
    private final String baseMethodName;
    private final String baseDescriptor;
    private final String packMethodName;
    private final String packDescriptor;

    private UnpackDesc(String owner,
                       String baseMethodName,
                       String baseDescriptor,
                       String packMethodName,
                       String packDescriptor) {
        this.owner = owner;
        this.baseMethodName = baseMethodName;
        this.baseDescriptor = baseDescriptor;
        this.packMethodName = packMethodName;
        this.packDescriptor = packDescriptor;
    }

    public String getOwner() {
        return owner;
    }

    public String getBaseMethodName() {
        return baseMethodName;
    }

    public String getBaseDescriptor() {
        return baseDescriptor;
    }

    public String getPackMethodName() {
        return packMethodName;
    }

    public String getPackDescriptor() {
        return packDescriptor;
    }

    @Override
    public String toString() {
        return "PackageClass{" +
                "owner='" + owner + '\'' +
                ", baseMethodName='" + baseMethodName + '\'' +
                ", baseDescriptor='" + baseDescriptor + '\'' +
                ", packMethodName='" + packMethodName + '\'' +
                ", packDescriptor='" + packDescriptor + '\'' +
                '}';
    }

    public static final UnpackDesc BOOLEAN = new UnpackDesc("java/lang/Boolean", "booleanValue", "()Z", "valueOf", "(Z)Ljava/lang/Boolean;");
    public static final UnpackDesc BYTE = new UnpackDesc("java/lang/Byte", "byteValue", "()B", "valueOf", "(B)Ljava/lang/Byte;");
    public static final UnpackDesc CHARACTER = new UnpackDesc("java/lang/Character", "charValue", "()C", "valueOf", "(C)Ljava/lang/Character;");

    public static final UnpackDesc SHORT = new UnpackDesc("java/lang/Short", "shortValue", "()S", "valueOf", "(S)Ljava/lang/Short;");
    public static final UnpackDesc INTEGER = new UnpackDesc("java/lang/Integer", "intValue", "()I", "valueOf", "(I)Ljava/lang/Integer;");
    public static final UnpackDesc LONG = new UnpackDesc("java/lang/Long", "longValue", "()J", "valueOf", "(J)Ljava/lang/Long;");

    public static final UnpackDesc FLOAT = new UnpackDesc("java/lang/Float", "floatValue", "()F", "valueOf", "(F)Ljava/lang/Float;");
    public static final UnpackDesc DOUBLE = new UnpackDesc("java/lang/Double", "doubleValue", "()D", "valueOf", "(D)Ljava/lang/Double;");
}
