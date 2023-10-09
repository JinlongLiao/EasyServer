package io.github.jinlongliao.easy.server.servlet.match;

/**
 * @author: liaojinlong
 * @date: 2023/10/7 12:58
 */
public class UrlMatcher {
    private final String tempUrl;
    private final String value;
    private final String value2;
    /**
     * 全文本匹配
     */
    private final boolean fullMatch;
    /**
     * 包含后缀
     */
    private final boolean hasSuffix;
    private final String suffix;

    public UrlMatcher(String tempUrl) {
        this.tempUrl = tempUrl;
        int indexOf = tempUrl.lastIndexOf("*");
        String value;
        if (indexOf > 0) {
            fullMatch = false;
            value = tempUrl.substring(0, indexOf).replaceAll("\\*", "").replaceAll("\\.", "");
            suffix = tempUrl.substring(indexOf).replaceAll("\\*", "").replaceAll("\\.", "");
            hasSuffix = !suffix.isEmpty();
        } else {
            fullMatch = true;
            value = tempUrl;
            suffix = "";
            hasSuffix = false;
        }
        if (!value.endsWith("/")) {
            this.value2 = value;
            this.value = value + "/";
        } else {
            this.value2 = value.substring(0, value.length() - 1);
            this.value = value;
        }
    }

    /**
     * 是否匹配
     *
     * @param uri /
     * @return /
     */
    public boolean matcher(String uri) {
        boolean match = false;
        if (fullMatch) {
            return this.value.equals(uri) || this.value2.equals(uri);
        } else {
            if (uri.startsWith(this.value) || uri.startsWith(this.value2)) {
                if (hasSuffix) {
                    match = uri.endsWith(this.suffix);
                } else {
                    match = true;
                }
            }
        }
        return match;
    }

    @Override
    public String toString() {
        return "UrlMatcher{" +
                "tempUrl='" + tempUrl + '\'' +
                ", value='" + value + '\'' +
                ", fullMatch=" + fullMatch +
                ", hasSuffix=" + hasSuffix +
                ", suffix='" + suffix + '\'' +
                '}';
    }
}
