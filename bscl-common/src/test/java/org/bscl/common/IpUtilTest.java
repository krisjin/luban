package org.bscl.common;

import org.bscl.common.network.IpUtil;
import org.junit.Test;

import java.net.InetAddress;

/**
 * User: shijingui
 * Date: 2016/5/11
 */
public class IpUtilTest {

    @Test
    public void getIp() {
        String ip = IpUtil.getIP();
        System.out.println(ip);
    }

    @Test
    public void getLocalIp() {
        InetAddress inetAddress = IpUtil.getLocalIpAddress();
        String hostAddress = inetAddress.getHostAddress();
        System.out.println(hostAddress);
    }
}
