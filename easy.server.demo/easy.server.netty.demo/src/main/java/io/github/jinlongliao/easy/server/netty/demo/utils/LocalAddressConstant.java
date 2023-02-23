package io.github.jinlongliao.easy.server.netty.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.*;
import java.util.Enumeration;


/**
 * @author: liaojinlong
 * @date: 2022-08-09 10:03
 */
public final class LocalAddressConstant {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public final static boolean LOCAL_ADDRESS_LOAD_MODE;
    public final static String LOCAL_ADDRESS;

    static {
        /**
         * 是否从系统变量中读取域名信息
         */
        LOCAL_ADDRESS_LOAD_MODE = Boolean.parseBoolean(System.getProperty("notification.bootstrap.mode", "false"));
        LOCAL_ADDRESS = LOCAL_ADDRESS_LOAD_MODE ? System.getProperty("notification.domain") : getLocalIpByNetcard();
    }

    public static final String DEFAULT_IP = "127.0.0.1";


    /**
     * 直接根据第一个网卡地址作为其内网ipv4地址，避免返回 127.0.0.1
     *
     * @return
     */
    private static String getLocalIpByNetcard() {
        try {
            for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements(); ) {
                NetworkInterface item = e.nextElement();
                for (InterfaceAddress address : item.getInterfaceAddresses()) {
                    if (item.isLoopback() || !item.isUp()) {
                        continue;
                    }
                    InetAddress inet4Address = address.getAddress();
                    if (inet4Address instanceof Inet4Address) {
                        return ((Inet4Address) inet4Address).getHostAddress();
                    }
                }
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            log.error(e.getMessage(), e);
            return DEFAULT_IP;
        }
    }
}
