package org.luban.common.network;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * IP工具类
 */
public final class IpUtil {

    public static final String IP_STR = "ip";

    private IpUtil() {
    }

    /**
     * 取得当前用户IP
     *
     * @param request
     * @return
     */
    public static String getUserIP(HttpServletRequest request) {
        // 根据优先级规则，如果取得一个有效ip，立即返回
        String ip = request.getHeader("X-Real-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("x-forwarded-for");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIp(ip)) {
            return ip;
        }
        ip = request.getRemoteAddr();
        return ip;
    }

    public static String getIP() {
        String ip = "127.0.0.1";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ip;
    }

    public static InetAddress getLocalIpAddress() {
        try {
            List<InetAddress> addresses = new ArrayList<InetAddress>(3);
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i.isLoopbackAddress() || i.isMulticastAddress()) {
                        continue;
                    }
                    addresses.add(i);
                }
            }

            InetAddress shouldReturnAddress = null;
            if (addresses.size() == 1) {
                shouldReturnAddress = addresses.get(0);
            } else {
                byte[] firstByteArray = new byte[]{(byte) 172, (byte) 10, (byte) 192};
                for (byte firstByte : firstByteArray) {
                    for (InetAddress address : addresses) {
                        if (address.getAddress()[0] == firstByte) {
                            shouldReturnAddress = address;
                            break;
                        }
                    }
                    if (shouldReturnAddress != null) {
                        break;
                    }
                }
            }
            return shouldReturnAddress;
        } catch (SocketException socketEx) {
            throw new RuntimeException("getIP error", socketEx);
        }
    }

    /**
     * 是否是有效的ip地址
     *
     * @param ip
     * @return
     */
    private static boolean isValidIp(String ip) {
        return (!Strings.isNullOrEmpty(ip)) && (!"unknown".equalsIgnoreCase(ip));
    }

}
