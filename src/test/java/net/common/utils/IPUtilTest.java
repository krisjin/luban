package net.common.utils;

import net.common.utils.network.IPUtil;
import org.junit.Test;

/**
 * User: shijingui
 * Date: 2016/5/11
 */
public class IPUtilTest {

    @Test
    public void getIp() {
        String ip = IPUtil.getIP();
        System.out.println(ip);
    }
}
