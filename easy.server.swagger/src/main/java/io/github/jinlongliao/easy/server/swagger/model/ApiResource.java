package io.github.jinlongliao.easy.server.swagger.model;

import java.util.Objects;

/**
 * <pre>
 *     [{"name":"默认接口","url":"/v2/api-docs?group=默认接口","swaggerVersion":"2.0","location":"/v2/api-docs?group=默认接口"}]
 * </pre>
 *
 * @author: liaojinlong
 * @date: 2022-06-17 16:55
 */
public class ApiResource {

    /**
     * 接口名称
     */
    private String name;
    /**
     * 访问地址
     */
    private String url;
    /**
     * 版本信息
     */
    private String swaggerVersion;
    /**
     * 访问地址
     */
    private String location;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setSwaggerVersion(String swaggerVersion) {
        this.swaggerVersion = swaggerVersion;
    }

    public String getSwaggerVersion() {
        return swaggerVersion;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "ApiResource{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", swaggerVersion='" + swaggerVersion + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ApiResource))
            return false;
        ApiResource that = (ApiResource) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getUrl(), that.getUrl()) && Objects.equals(getSwaggerVersion(), that.getSwaggerVersion()) && Objects.equals(getLocation(), that.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUrl(), getSwaggerVersion(), getLocation());
    }
}
