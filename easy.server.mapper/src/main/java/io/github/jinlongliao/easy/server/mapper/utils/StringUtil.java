/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package io.github.jinlongliao.easy.server.mapper.utils;


import java.util.Objects;

public class StringUtil {
    public static boolean isEmpty(String str) {
        return Objects.isNull(str) || (str.isEmpty());
    }

    public static String escapeString(String s) {
        int length = s.length();

        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);

            if ((c == '\\') || (c == '"') || (c < ' ')) {
                StringBuilder sb = new StringBuilder(length * 2);

                sb.append(s.substring(0, i));

                for (; i < length; i++) {
                    c = s.charAt(i);

                    switch (c) {
                        case '\\' -> sb.append("\\\\");
                        case '\b' -> sb.append("\\b");
                        case '\f' -> sb.append("\\f");
                        case '\n' -> sb.append("\\n");
                        case '\r' -> sb.append("\\r");
                        case '\t' -> sb.append("\\t");
                        case '"' -> sb.append("\\\"");
                        default -> {
                            if (c < ' ') {
                                sb.append("\\0");
                                sb.append((char) ('0' + ((int) c >> 3)));
                                sb.append((char) ('0' + ((int) c & 7)));
                            } else {
                                sb.append(c);
                            }
                        }
                    }
                }

                return sb.toString();
            }
        }

        return s;
    }

    public static String upperFirst(String name) {
        if (isEmpty(name)) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
}
