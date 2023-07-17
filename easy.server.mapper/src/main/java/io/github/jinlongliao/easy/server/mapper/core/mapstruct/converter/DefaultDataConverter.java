package io.github.jinlongliao.easy.server.mapper.core.mapstruct.converter;

import io.github.jinlongliao.easy.server.mapper.exception.ConverterException;
import java.util.Objects;

public class DefaultDataConverter implements IDataConverter {
    /**
     * 数据转换为byte
     *
     * @param data
     * @return byte
     */

    @Override
    public byte getByte(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            return Byte.parseByte(String.valueOf(data));
        }
        return (byte) data;
    }


    /**
     * 数据转换为Boolean
     *
     * @param data
     * @return Boolean
     */
    @Override
    public boolean getBoolean(Object data) {
        if (data == null) {
            return false;
        }
        if (data instanceof String) {
            return Boolean.parseBoolean(String.valueOf(data));
        }
        return (boolean) data;
    }

    /**
     * 数据转换为Short
     *
     * @param data
     * @return Short
     */
    @Override
    public short getShort(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            return Short.parseShort(String.valueOf(data));
        }
        return (short) data;
    }

    /**
     * 数据转换为float
     *
     * @param data
     * @return float
     */
    @Override
    public float getFloat(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            return Float.parseFloat(String.valueOf(data));
        }
        return (float) data;
    }


    /**
     * 数据转换为double
     *
     * @param data
     * @return double
     */
    @Override
    public double getDouble(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            return Double.parseDouble(String.valueOf(data));
        }
        return (double) data;
    }


    /**
     * 数据转换为long
     *
     * @param data
     * @return long
     */
    @Override
    public long getLong(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            return Long.parseLong(String.valueOf(data));
        }
        return (long) data;
    }

    /**
     * 数据转换为int
     *
     * @param data
     * @return int
     */
    @Override
    public int getInt(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            if (((String) data).length() == 0) {
                return 0;
            }
            return Integer.parseInt(String.valueOf(data));
        }
        if (data instanceof Integer) {
            return (Integer) data;
        }
        return (int) data;
    }

    /**
     * 数据转换为String
     *
     * @param data
     * @return String
     */
    @Override
    public String getStr(Object data) {
        if (data == null) {
            return null;
        }
        return String.valueOf(data);
    }

    /**
     * 数据转换为 char
     *
     * @param data
     * @return char
     */
    @Override
    public char getChar(Object data) {
        if (data instanceof Character) {
            return (Character) data;
        }
        return (char) getInt(data);
    }


    /**
     * 泛型转换
     *
     * @param data
     * @param tClass
     * @param <T>
     * @return /
     */
    @Override
    public <T> T getT(Class<T> tClass, Object extra, Object data) {
        if (Objects.isNull(data)) {
            return null;
        }
        if (tClass.isInstance(data)) {
            return (T) data;
        }
        throw new ConverterException("not implements " + tClass.getName() + " extra: " + extra);
    }
}
