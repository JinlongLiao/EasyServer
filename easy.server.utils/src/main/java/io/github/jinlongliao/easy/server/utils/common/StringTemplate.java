package io.github.jinlongliao.easy.server.utils.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * String 模板
 *
 * @author: liaojinlong
 * @date: 2023-03-17 14:52
 */
public final class StringTemplate {
    public static final String SUFFIX = "\\{\\}";

    /**
     * 替换符号
     */
    private final String temp;
    private final boolean useful;
    private StringTemplateIni[] templates;

    private StringTemplate(String temp) {
        this.temp = temp;
        if (Objects.isNull(temp) || temp.isEmpty()) {
            useful = false;
        } else {
            List<Integer> tags = new ArrayList<>(4);
            char[] chars = temp.toCharArray();
            int length = chars.length;
            for (int i = 0; i < length; ) {
                boolean add;
                char head = chars[i++];
                add = head == '{';
                if (add && length > i) {
                    if (chars[i++] == '}') {
                        tags.add(i);
                    }
                }
            }
            if (tags.isEmpty()) {
                useful = false;
            } else {
                useful = true;
                this.initTemplate(tags, chars);
            }
        }
    }

    private void initTemplate(List<Integer> tags, char[] chars) {
        List<StringTemplateIni> temps = new ArrayList<>(tags.size() + 1);
        int lastIndex = 0;
        for (Integer tag : tags) {
            int index = tag - 2;
            if (lastIndex != index) {
                temps.add(new StringTemplateIni(false, new String(chars, lastIndex, index - lastIndex)));
            }
            temps.add(new StringTemplateIni(true, ""));
            lastIndex = tag;
        }
        if (chars.length > lastIndex) {
            temps.add(new StringTemplateIni(false, new String(chars, lastIndex, chars.length - lastIndex)));
        }
        this.templates = temps.toArray(new StringTemplateIni[0]);
    }


    public String format(Object... args) {
        if (!useful) {
            return temp;
        }
        StringBuilder builder = new StringBuilder(16);
        int maxIndex = args.length;
        int index = 0;
        for (StringTemplateIni templateIni : this.templates) {
            if (templateIni.match) {
                if (maxIndex > index) {
                    builder.append(args[index++]);
                }
            } else {
                builder.append(templateIni.tem);
            }
        }
        return builder.toString();
    }

    static class StringTemplateIni {
        private final boolean match;
        private final String tem;

        StringTemplateIni(boolean match, String tem) {
            this.match = match;
            this.tem = tem;
        }
    }

    public static StringTemplate of(String tem) {
        return new StringTemplate(tem);
    }

    @Override
    public String toString() {
        return temp;
    }
}
