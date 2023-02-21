package io.github.jinlongliao.easy.server.utils.common;


import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author: liaojinlong
 * @date: 2022-09-16 11:20
 */
public class IPAddressUtil {
    public static String DEFAULT = "127.0.0.1";

    /**
     * 解析客户端IP
     *
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        if (Objects.isNull(request)) {
            return DEFAULT;
        }
        try {
            //squid传递过来的玩家IP地址
            String ip = request.getHeader("%>a");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("x-forwarded-for");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = DEFAULT;
            }
            // 如果通过代理访问,可能获取2个IP,这时候去第二个(代理服务端IP)
            if (ip.split(",").length >= 2) {
                ip = ip.split(",")[0].trim();
            }
            return ip;
        } catch (Exception e) {
            return DEFAULT;
        }
    }

}
