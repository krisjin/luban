package org.luban.common.net;

import org.apache.commons.lang.StringUtils;
import org.luban.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * 网络相关操作工具类
 * User: shijingui
 * Date: 2016/11/8
 */
public class NetUtil {
    private final static Logger logger = LoggerFactory.getLogger(NetUtil.class);

    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;

    private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");

    public static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    public static final String ANY_HOST = "0.0.0.0";

    /**
     * 是否本地地址(127.x.x.x 或 localhost)
     *
     * @param host 地址
     */
    public static boolean isLocalHost(String host) {
        return StringUtils.isNotBlank(host) && (LOCAL_IP_PATTERN.matcher(host).matches() || "localhost".equalsIgnoreCase(host));
    }


    public static boolean isInvalidPort(int port) {
        return port > MAX_PORT || port < MIN_PORT;
    }

    /**
     * 是否默认地址 0.0.0.0
     *
     * @param host 地址
     */
    public static boolean isAnyHost(String host) {
        return ANY_HOST.equals(host);
    }


    /**
     * 是否IPv4地址 0.0.0.0
     *
     * @param host
     */
    public static boolean isIPv4Host(String host) {
        return StringUtils.isNotBlank(host) && IPV4_PATTERN.matcher(host).matches();
    }

    /**
     * 是否合法地址（非本地，非默认的IPv4地址）
     *
     * @param address InetAddress
     * @return 是否合法
     */
    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress())
            return false;
        String name = address.getHostAddress();
        return (name != null && !isAnyHost(name) && !isLocalHost(name) && isIPv4Host(name));
    }

    /**
     * 是否网卡上的地址
     *
     * @param host 地址
     */
    public static boolean isHostInNetworkCard(String host) {
        try {
            InetAddress address = InetAddress.getByName(host);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(address);
            return networkInterface != null;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 遍历本地网卡，返回第一个合理的IP，保存到缓存中
     *
     * @return 本地网卡IP
     */
    public static InetAddress getLocalAddress() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {
            logger.warn("Error when retriving ip address: " + e.getMessage(), e);
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        return address;
                                    }
                                } catch (Throwable e) {
                                    logger.warn("Error when retriving ip address: " + e.getMessage(), e);
                                }
                            }
                        }
                    } catch (Throwable e) {
                        logger.warn("Error when retriving ip address: " + e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            logger.warn("Error when retriving ip address: " + e.getMessage(), e);
        }
        logger.error("Can't get valid host, will use 127.0.0.1 instead.");
        return localAddress;
    }


    /**
     * 得到本机IPv4地址
     *
     * @return ip地址
     */
    public static String getLocalHost() {
        InetAddress address = getLocalAddress();
        return address == null ? null : address.getHostAddress();
    }

    /**
     * 检查当前指定端口是否可用，不可用则自动+1再试（随机端口从默认端口开始检查）
     *
     * @param host 当前ip地址
     * @param port 当前指定端口
     * @return 从指定端口开始后第一个可用的端口
     */
    public static int getAvailablePort(String host, int port) {
        if (isAnyHost(host) || isLocalHost(host) || isHostInNetworkCard(host)) {
        } else {
            throw new RuntimeException("The host " + host + " is not found in network cards, please check config");
        }
        if (port < MIN_PORT) {
            port = Constants.DEFAULT_SERVER_PORT;
        }
        for (int i = port; i <= MAX_PORT; i++) {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket();
                serverSocket.bind(new InetSocketAddress(host, i));
                logger.debug("ip:{} port:{} is available", host, i);
                return i;
            } catch (IOException e) {
                // continue
                logger.warn("Can't bind to address [{}:{}], " +
                        "Maybe 1) The port has been bound. " +
                        "2) The network card of this host is not exists or disable. " +
                        "3) The host is wrong.", host, i);
                logger.info("Begin try next port(auto +1):{}", i + 1);
            } finally {
                if (serverSocket != null) {
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        logger.error("ServerSocket close error...");
                    }
                }
            }
        }
        throw new RuntimeException("Can't bind to ANY port of " + host + ", please check config");
    }

    public static void main(String[] args) {
        System.out.println(isHostInNetworkCard("LOCALHOST"));
    }
}
