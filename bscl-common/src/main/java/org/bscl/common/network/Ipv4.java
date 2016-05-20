/**
 *
 */
package org.bscl.common.network;

import java.net.*;
import java.util.*;

/**
 * Ipv4工具
 */
public class Ipv4 {

    /**
     * 管理IP
     */
    public static String MANAGE_IP = "10.";
    /**
     * 网卡
     */
    public static String NET_INTERFACE;

    /**
     * 掩码位数(1-32)对应的子网掩码
     */
    public static final String[] MASKES =
            new String[]{"128.0.0.0", "192.0.0.0", "224.0.0.0", "240.0.0.0", "248.0.0.0", "252.0.0.0", "254.0.0.0",
                    "255.0.0.0", "255.128.0.0", "255.192.0.0", "255.224.0.0", "255.240.0.0", "255.248.0.0",
                    "255.252.0.0", "255.254.0.0", "255.255.0.0", "255.255.128.0", "255.255.192.0", "255.255.224.0",
                    "255.255.240.0", "255.255.248.0", "255.255.252.0", "255.255.254.0", "255.255.255.0",
                    "255.255.255.128", "255.255.255.192", "255.255.255.224", "255.255.255.240", "255.255.255.248",
                    "255.255.255.252", "255.255.255.254", "255.255.255.255"};
    /**
     * 内网地址
     */
    public static final Lan INTRANET = new Lan("172.16.0.0/12;192.168.0.0/16;10.0.0.0/8");

    static {
        // 从环境变量里面获取默认的网卡和管理网络
        NET_INTERFACE = System.getProperty("nic");
        MANAGE_IP = System.getProperty("manage_ip", MANAGE_IP);
    }

    /**
     * 得到本机所有的地址
     *
     * @return 本机所有的地址
     * @throws Exception
     */
    public static List<String> getLocalIps() throws Exception {
        return getLocalIps(null, null);
    }

    /**
     * 得到指定网卡上的地址
     *
     * @param nic     网卡
     * @param exclude 排除的地址
     * @return 地址列表
     * @throws SocketException
     */
    public static List<String> getLocalIps(final String nic, final String exclude) throws SocketException {
        List<String> result = new ArrayList<String>();
        NetworkInterface ni;
        Enumeration<InetAddress> ias;
        InetAddress address;
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces.hasMoreElements()) {
            ni = netInterfaces.nextElement();
            if (nic != null && !nic.isEmpty() && !ni.getName().equals(nic)) {
                continue;
            }
            ias = ni.getInetAddresses();
            while (ias.hasMoreElements()) {
                address = ias.nextElement();
                if (!address.isLoopbackAddress() && (address instanceof Inet4Address)) {
                    result.add(address.getHostAddress());
                }
            }
        }
        // 只有一个IP
        int count = result.size();
        if (count <= 1) {
            return result;
        }
        if (exclude != null && !exclude.isEmpty()) {
            String ip;
            // 多个IP，排除IP
            for (int i = count - 1; i >= 0; i--) {
                ip = result.get(i);
                if (ip.startsWith(exclude)) {
                    // 删除排除的IP
                    result.remove(i);
                    count--;
                    if (count == 1) {
                        // 确保有一个IP
                        break;
                    }
                }
            }
        }

        return result;
    }

    /**
     * 得到本机内网地址
     *
     * @param nic      网卡
     * @param manageIp 管理段IP地址
     * @return 本机地址
     * @throws SocketException
     */
    public static String getLocalIp(final String nic, final String manageIp) throws SocketException {
        List<String> ips = getLocalIps(nic, manageIp);
        if (ips != null && !ips.isEmpty()) {
            if (ips.size() == 1) {
                return ips.get(0);
            }
            for (String ip : ips) {
                if (INTRANET.contains(ip)) {
                    return ip;
                }
            }
            return ips.get(0);
        }
        return null;
    }

    /**
     * 得到本机内网地址
     *
     * @param manageIp 管理段IP地址
     * @return 本机地址
     * @throws SocketException
     */
    public static String getLocalIp(final String manageIp) throws SocketException {
        return getLocalIp(NET_INTERFACE, manageIp);
    }

    /**
     * 得到本机内网地址
     *
     * @return 本机地址
     * @throws SocketException
     */
    public static String getLocalIp() throws SocketException {
        return getLocalIp(NET_INTERFACE, MANAGE_IP);
    }

    /**
     * 把地址对象转换成字符串
     *
     * @param address 地址
     * @return 地址字符串
     */
    public static String toAddress(final SocketAddress address) {
        if (address == null) {
            return null;
        }
        if (address instanceof InetSocketAddress) {
            InetSocketAddress isa = (InetSocketAddress) address;
            StringBuilder builder = new StringBuilder(50);
            builder.append(isa.getAddress().getHostAddress()).append(':').append(isa.getPort());
            return builder.toString();
        } else {
            return address.toString();
        }
    }

    /**
     * 从地址数组中获取IP，跳过必要的端口
     *
     * @param address 地址数组
     * @return IP
     */
    public static String toIp(final byte[] address) {
        if (address == null || address.length < 4) {
            return null;
        }
        int pos = 0;
        if (address.length >= 6) {
            // 跳过端口
            pos += 2;
        }
        StringBuilder builder = new StringBuilder(20);
        builder.append(address[pos++] & 0xFF).append('.');
        builder.append(address[pos++] & 0xFF).append('.');
        builder.append(address[pos++] & 0xFF).append('.');
        builder.append(address[pos++] & 0xFF);
        return builder.toString();
    }


    /**
     * 把地址转化成字节数组，如果有端口，则第一和第二字节为端口，其余为IP段
     *
     * @param address 地址
     * @return 字节数组
     */
    public static byte[] toByte(String address) {
        return toByte(address, false);
    }

    /**
     * 把地址转化成字节数组，如果有端口，则第一和第二字节为端口，其余为IP段
     *
     * @param address 地址
     * @param hex     字符串是否是16进制模式
     * @return 字节数组
     */
    public static byte[] toByte(String address, boolean hex) {
        if (address == null) {
            return null;
        }
        int pos = 0;
        byte[] buf = null;
        if (hex) {
            // 16进制地址
            int len = address.length();
            switch (len) {
                case 12:
                    buf = new byte[6];
                    buf[pos++] = (byte) Integer.parseInt(address.substring(10, 12), 16);
                    buf[pos++] = (byte) Integer.parseInt(address.substring(8, 10), 16);
                case 8:
                    buf = buf == null ? new byte[4] : buf;
                    buf[pos++] = (byte) Integer.parseInt(address.substring(0, 2), 16);
                    buf[pos++] = (byte) Integer.parseInt(address.substring(2, 4), 16);
                    buf[pos++] = (byte) Integer.parseInt(address.substring(4, 6), 16);
                    buf[pos++] = (byte) Integer.parseInt(address.substring(6, 8), 16);
                    break;
                default:
                    return null;
            }
        } else {
            // IP地址
            int[] parts = parseAddress(address);
            if (parts == null) {
                return null;
            }
            buf = parts[4] <= 0 ? new byte[4] : new byte[6];
            if (buf.length > 4) {
                buf[pos++] = (byte) (parts[4] & 0xFF);
                buf[pos++] = (byte) (parts[4] >> 8 & 0xFF);
            }
            buf[pos++] = (byte) parts[0];
            buf[pos++] = (byte) parts[1];
            buf[pos++] = (byte) parts[2];
            buf[pos++] = (byte) parts[3];
        }
        return buf;
    }


    /**
     * 地址转化成字节数组
     *
     * @param socketAddress 地址对象
     * @return 字节数组
     */
    public static byte[] toByte(InetSocketAddress socketAddress) {
        if (socketAddress == null) {
            throw new IllegalArgumentException("socketAddress is null");
        }
        InetAddress inetAddress = socketAddress.getAddress();
        if (inetAddress == null) {
            throw new IllegalArgumentException("socketAddress is invalid");
        }
        byte[] address = inetAddress.getAddress();
        byte[] result = new byte[address.length + 2];
        System.arraycopy(address, 0, result, 2, address.length);
        int port = socketAddress.getPort();
        result[1] = (byte) (port >> 8 & 0xFF);
        result[0] = (byte) (port & 0xFF);
        return result;
    }

    /**
     * 把字节数组转换成地址对象
     *
     * @param address 地址字节数组
     * @return 地址对象
     */
    public static InetSocketAddress toAddress(final byte[] address) {
        if (address == null || (address.length != 6 && address.length != 18)) {
            // 端口2个字节，IPV4 4字节，IPV6 16字节
            throw new IllegalArgumentException("address is invalid");
        }
        // 低位2个字节是端口数据
        int port = address[0] & 0xFF;
        port |= (address[1] << 8 & 0xFF00);

        try {
            InetAddress addr = InetAddress.getByAddress(null, Arrays.copyOfRange(address, 2, address.length));
            return new InetSocketAddress(addr, port);
        } catch (UnknownHostException ignored) {
            return null;
        }
    }


    /**
     * 把地址数组转换成字符串
     *
     * @param address 字节数组
     * @param builder 字符串构造器
     */
    public static void toAddress(byte[] address, StringBuilder builder) {
        if (builder == null) {
            return;
        }
        if (address == null) {
            throw new IllegalArgumentException("address is invalid");
        }
        if (address.length < 4) {
            throw new IllegalArgumentException("address is invalid");
        }
        int pos = 0;
        int port = 0;
        if (address.length >= 6) {
            port = address[pos++] & 0xFF;
            port |= (address[pos++] << 8 & 0xFF00);
        }
        builder.append(address[pos++] & 0xFF).append('.');
        builder.append(address[pos++] & 0xFF).append('.');
        builder.append(address[pos++] & 0xFF).append('.');
        builder.append(address[pos++] & 0xFF);
        if (address.length >= 6) {
            builder.append(':').append(port);
        }
    }

    /**
     * 把字节数组转化成16进制字符串
     *
     * @param address 字节数组
     * @return 16进制
     */
    public static String toHex(byte[] address) {
        StringBuilder builder = new StringBuilder();
        toHex(address, builder);
        return builder.toString();
    }

    /**
     * 把地址数组转化成16进制字符串
     *
     * @param address 字节数组
     * @param builder 字符串构造器
     */
    public static void toHex(byte[] address, StringBuilder builder) {
        if (address == null || address.length == 0 || builder == null) {
            return;
        }
        String hex;
        int pos = 0;
        int port = 0;
        // 有端口
        if (address.length >= 6) {
            port = address[pos++] & 0xFF;
            port |= (address[pos++] << 8 & 0xFF00);
        }
        // IP段
        for (int i = 0; i < 4; i++) {
            hex = Integer.toHexString(address[pos++] & 0xFF).toUpperCase();
            if (hex.length() == 1) {
                builder.append('0').append(hex);
            } else {
                builder.append(hex);
            }
        }
        // 追加端口字符串
        if (address.length >= 6) {
            hex = Integer.toHexString(port).toUpperCase();
            int len = hex.length();
            if (len == 1) {
                builder.append("000").append(hex);
            } else if (len == 2) {
                builder.append("00").append(hex);
            } else if (len == 3) {
                builder.append("0").append(hex);
            } else {
                builder.append(hex);
            }
        }
    }

    /**
     * 是否是有效的IPV4
     *
     * @param ip ip地址
     * @return 有效标示
     */
    public static boolean isIp(final String ip) {
        return parseIp(ip) != null;
    }

    /**
     * 是否是有效的地址，IP加端口
     *
     * @param address ip地址
     * @return 有效标示
     */
    public static boolean isAddress(final String address) {
        return parseAddress(address) != null;
    }

    /**
     * 解析地址<br>，分隔符支持".",":","_"
     * 第1-4个元素为IP段，第5个元素为端口，如果第5个元素为-1，则表示端口不存在
     *
     * @param address ip地址
     * @return 分段
     */
    public static int[] parseAddress(final String address) {
        if (address == null || address.isEmpty()) {
            return null;
        }
        int[] parts = new int[5];
        parts[4] = 0;
        int index = 0;
        int start = -1;
        int end = -1;
        int part;
        char[] chars = address.toCharArray();
        char ch = 0;
        for (int i = 0; i < chars.length; i++) {
            ch = chars[i];
            switch (ch) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (start == -1) {
                        start = i;
                    }
                    end = i;
                    if ((index < 3 && end - start > 2) || (index == 3 && end - start > 5)) {
                        // IP每段最大255，端口最大65535
                        return null;
                    }
                    break;
                case '_':
                case ':':
                case '.':
                    // 分隔符
                    if (start == -1) {
                        // 前面必须有字符
                        return null;
                    }
                    if (index >= 4) {
                        // 已经有4个IP段和端口了
                        return null;
                    }
                    part = Integer.parseInt(new String(chars, start, end - start + 1));
                    if (part > 255) {
                        return null;
                    }
                    parts[index++] = part;
                    start = -1;
                    end = -1;
                    break;
                default:
                    return null;
            }
        }
        if (start > -1) {
            part = Integer.parseInt(new String(chars, start, end - start + 1));
            if (index <= 3 && part > 255 || index == 4 && part > 65535) {
                return null;
            }
            parts[index] = part;
            return index >= 3 ? parts : null;
        } else {
            // 以.结尾
            return null;
        }
    }

    /**
     * 分解IP,只支持IPV4
     *
     * @param ip ip地址
     * @return 分段
     */
    public static int[] parseIp(final String ip) {
        if (ip == null || ip.isEmpty()) {
            return null;
        }
        int[] parts = new int[4];
        int index = 0;
        int start = -1;
        int end = -1;
        int part;
        char[] chars = ip.toCharArray();
        char ch = 0;
        for (int i = 0; i < chars.length; i++) {
            if (index > 3) {
                // 超过了4个数字
                return null;
            }
            ch = chars[i];
            switch (ch) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    if (start == -1) {
                        start = i;
                    }
                    end = i;
                    if (end - start > 2) {
                        // 超长了，最多3个数字
                        return null;
                    }
                    break;
                case '.':
                    // 分隔符
                    if (start == -1) {
                        // 前面必须有字符
                        return null;
                    }
                    part = Integer.parseInt(new String(chars, start, end - start + 1));
                    if (part > 255) {
                        return null;
                    }
                    parts[index++] = part;
                    start = -1;
                    end = -1;
                    break;
                default:
                    return null;
            }
        }
        if (start > -1) {
            part = Integer.parseInt(new String(chars, start, end - start + 1));
            if (part > 255) {
                return null;
            }
            parts[index] = part;
            return index == 3 ? parts : null;
        } else {
            // 以.结尾
            return null;
        }
    }

    /**
     * 把IP地址转换成长整形
     *
     * @param ip IP地址
     * @return 长整形
     */
    public static long toLong(final String ip) {
        int[] data = parseIp(ip);
        if (data == null) {
            throw new IllegalArgumentException(String.format("invalid ip %s", ip));
        }
        long result = 0;
        result += ((long) data[0]) << 24;
        result += ((long) (data[1]) << 16);
        result += ((long) (data[2]) << 8);
        result += ((long) (data[3]));
        return result;
    }

    /**
     * 把长整形转换成IP地址
     *
     * @param ip 长整形
     * @return IP字符串
     */
    public static String toIp(long ip) {
        StringBuffer sb = new StringBuffer(20);
        long part1 = (ip & 0xFFFFFFFF) >>> 24;
        long part2 = (ip & 0x00FFFFFF) >>> 16;
        long part3 = (ip & 0x0000FFFF) >>> 8;
        long part4 = ip & 0x000000FF;
        //直接右移24位
        sb.append(part1).append('.');
        //将高8位置0，然后右移16位
        sb.append(part2).append('.');
        //将高16位置0，然后右移8位
        sb.append(part3).append('.');
        //将高24位置0
        sb.append(part4);
        return sb.toString();
    }

    /**
     * 网段
     */
    public static class Segment {
        // 起始IP
        private long begin;
        // 最后IP
        private long end;

        public Segment(final String ips) {
            if (ips == null || ips.isEmpty()) {
                throw new IllegalArgumentException("ips is empty.");
            }
            int pos = ips.indexOf('-');
            if (pos == 0 || pos == ips.length() - 1) {
                throw new IllegalArgumentException(String.format("ips is invalid. %s", ips));
            } else if (pos > 0) {
                // IP-IP格式
                begin = toLong(ips.substring(0, pos));
                end = toLong(ips.substring(pos + 1));
            } else {
                pos = ips.indexOf('/');
                if (pos == 0 || pos == ips.length() - 1) {
                    throw new IllegalArgumentException(String.format("ips is invalid. %s", ips));
                } else if (pos > 0) {
                    // IP/掩码格式
                    int bits = Integer.parseInt(ips.substring(pos + 1));
                    if (bits < 1 || bits > 32) {
                        throw new IllegalArgumentException(String.format("ips is invalid. %s", ips));
                    }
                    long mask = (int) toLong(MASKES[bits - 1]);
                    begin = toLong(ips.substring(0, pos)) & mask;
                    end = begin + ~((int) mask);
                } else {
                    // 单个IP
                    begin = toLong(ips.substring(0, pos));
                    end = begin;
                }
            }
        }

        public long getBegin() {
            return begin;
        }

        public long getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return toIp(begin) + "-" + toIp(end);
        }

        /**
         * 是否包含指定IP
         *
         * @param ip IP
         * @return 布尔值
         */
        public boolean contains(final String ip) {
            long value = toLong(ip);
            return value >= begin && value <= end;
        }
    }

    /**
     * 局域网
     */
    public static class Lan {
        // 多个网段
        private List<Segment> segments = new ArrayList<Segment>();

        public Lan(String ips) {
            if (ips != null && !ips.isEmpty()) {
                StringTokenizer tokenizer = new StringTokenizer(ips, ",;", false);
                String segment;
                while (tokenizer.hasMoreTokens()) {
                    segment = tokenizer.nextToken();
                    if (segment != null && !segment.isEmpty()) {
                        segments.add(new Segment(segment));
                    }
                }
            }
        }

        public List<Segment> getSegments() {
            return segments;
        }

        /**
         * 是否包含指定IP
         *
         * @param ip IP
         * @return 布尔值
         */
        public boolean contains(String ip) {
            if (ip == null || ip.isEmpty()) {
                return false;
            }
            if (segments != null) {
                for (Segment segment : segments) {
                    if (segment.contains(ip)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(100);
            int count = 0;
            for (Segment segment : segments) {
                if (count++ > 0) {
                    builder.append(';');
                }
                builder.append(segment.toString());
            }
            return builder.toString();
        }
    }

}
