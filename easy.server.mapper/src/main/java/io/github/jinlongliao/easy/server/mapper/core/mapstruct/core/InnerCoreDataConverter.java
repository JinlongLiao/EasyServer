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
package io.github.jinlongliao.easy.server.mapper.core.mapstruct.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 内部基本类型数据转换
 *
 * @author liaojinlong
 * @since 2020/11/23 21:14
 */
public class InnerCoreDataConverter {
    /**
     * @param data
     * @return Object
     */
    public static Object getObject(Object data) {
        return data;
    }

    /**
     * 数据转换为byte
     *
     * @param data
     * @return byte
     */
    public static byte getByte(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            return Byte.parseByte(String.valueOf(data));
        }
        return (byte) data;
    }

    /**
     * 数据转换为byte
     *
     * @param data /
     * @return byte /
     */
    public static Byte getByte2(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            return Byte.valueOf(String.valueOf(data));
        }
        return (byte) data;
    }

    /**
     * 数据转换为 Boolean
     *
     * @param data /
     * @return Boolean
     */
    public static boolean getBoolean(Object data) {
        return getBoolean2(data);
    }

    /**
     * 数据转换为Boolean
     *
     * @param data /
     * @return Boolean
     */
    public static Boolean getBoolean2(Object data) {
        if (data == null) {
            return false;
        }
        if (data instanceof String) {
            return Boolean.parseBoolean(String.valueOf(data));
        }
        return (Boolean) data;
    }

    /**
     * 数据转换为Short
     *
     * @param data /
     * @return Short
     */
    public static short getShort(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            return Short.parseShort(String.valueOf(data));
        }
        return (short) data;
    }

    /**
     * 数据转换为Short
     *
     * @param data /
     * @return Short
     */
    public static Short getShort2(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            return Short.valueOf(String.valueOf(data));
        }
        return (Short) data;
    }

    /**
     * 数据转换为float
     *
     * @param data /
     * @return float
     */
    public static float getFloat(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            return Float.parseFloat(String.valueOf(data));
        }
        return (float) data;
    }

    /**
     * 数据转换为float
     *
     * @param data /
     * @return float
     */
    public static Float getFloat2(Object data) {
        if (data == null) {
            return 0F;
        }
        if (data instanceof String) {
            return Float.valueOf(String.valueOf(data));
        }
        return (Float) data;
    }

    /**
     * 数据转换为double
     *
     * @param data /
     * @return double
     */
    public static double getDouble(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            return Double.parseDouble(String.valueOf(data));
        }
        return (double) data;
    }

    /**
     * 数据转换为double
     *
     * @param data /
     * @return double
     */
    public static Double getDouble2(Object data) {
        if (data == null) {
            return 0D;
        }
        if (data instanceof String) {
            return Double.valueOf(String.valueOf(data));
        }
        return (Double) data;
    }

    /**
     * 数据转换为long
     *
     * @param data /
     * @return long
     */
    public static long getLong(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            return Long.parseLong(String.valueOf(data));
        }
        return (long) data;
    }

    /**
     * 数据转换为long
     *
     * @param data /
     * @return long
     */
    public static Long getLong2(Object data) {
        if (data == null) {
            return 0L;
        }
        if (data instanceof String) {
            return Long.valueOf(String.valueOf(data));
        }
        return (Long) data;
    }

    /**
     * 数据转换为int
     *
     * @param data /
     * @return int
     */
    public static int getInt(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            if (((String) data).length() == 0) {
                return 0;
            }
            return Integer.parseInt(String.valueOf(data));
        }
        return (int) data;
    }

    /**
     * 数据转换为int
     *
     * @param data /
     * @return int
     */
    public static Integer getInt2(Object data) {
        if (data == null) {
            return 0;
        }
        if (data instanceof String) {
            if (((String) data).length() == 0) {
                return 0;
            }
            return Integer.valueOf(String.valueOf(data));
        }
        return (Integer) data;
    }

    /**
     * 数据转换为String
     *
     * @param data /
     * @return String
     */
    public static String getStr(Object data) {
        if (data == null) {
            return null;
        }
        return String.valueOf(data);
    }

    /**
     * 数据转换为 char
     *
     * @param data /
     * @return char
     */
    public static char getChar(Object data) {
        return (char) getInt(data);
    }

    /**
     * 数据转换为 char
     *
     * @param data /
     * @return char
     */
    public static Character getChar2(Object data) {
        return Character.valueOf((char) getInt(data));
    }

    /**
     * 数据转换为 Date
     *
     * @param data /
     * @return Date
     */
    public static Date geDate(Object data) {
        return (Date) (data);
    }

    /**
     * 数据转换为 List
     *
     * @param data /
     * @return List
     */
    public static List<Object> getList(Object data) {

        return (List<Object>) data;
    }

    /**
     * 数据转换为 Map
     *
     * @param data /
     * @return Map
     */
    public static Map getMap(Object data) {
        return (Map) data;
    }

    /**
     * 数据转换为 Integer[]
     *
     * @param data /
     * @return Integer[]
     */
    public static Integer[] getIntArr(Object data) {
        if (!data.getClass().getName().equals("[Ljava.lang.Integer;")) {
            if (data.getClass().isArray()) {
                Object[] objects = (Object[]) data;
                Integer[] result = new Integer[objects.length];
                int index = 0;
                for (Object object : objects) {
                    if (object instanceof String) {
                        result[index++] = Integer.valueOf(String.valueOf(object));
                    } else {
                        result[index++] = ((Integer) object);
                    }
                }
                data = result;
            } else {
                data = new Integer(Integer.valueOf(String.valueOf(data)));
            }
        }
        return (Integer[]) data;
    }

    /**
     * 数据转换为 int[]
     *
     * @param data /
     * @return int[]
     */
    public static int[] getInt2Arr(Object data) {
        if (!data.getClass().getName().equals("[I")) {
            if (data.getClass().isArray()) {
                Object[] objects = (Object[]) data;
                int[] result = new int[objects.length];
                int index = 0;
                for (Object object : objects) {
                    if (object instanceof String) {
                        result[index++] = Integer.parseInt(String.valueOf(object));
                    } else {
                        result[index++] = ((Integer) object);
                    }
                }
                data = result;
            } else {
                data = new Integer(Integer.parseInt(String.valueOf(data)));
            }
        }


        return (int[]) data;
    }

    /**
     * 数据转换为 String[]
     *
     * @param data /
     * @return String[]
     */
    public static String[] getStringArr(Object data) {
        return (String[]) data;
    }

    /**
     * 泛型转换
     *
     * @param data   /
     * @param tClass /
     * @param <T>    /
     * @return /
     */
    public static <T> T getT(Class<T> tClass, Object data) {
        return tClass.cast(data);
    }
}
