package net.common.utils.mac;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import net.common.utils.enums.SdkTypeEnum;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

/**
 * Mac工具.
 * <p/>
 * User: xiaoqing.hu@mail-inc.com
 * Date: 12-8-27
 * Time: 上午10:40
 */
public final class MacUtil {

    private static final String MAC_NONE = "";
    private static final Pattern REGEX_MAC = Pattern.compile("^([0-9]|[A-F]){12}$");
    private static final Pattern REGEX_ORI_MAC = Pattern.compile("[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}");
    private static final char CHAR_BLANK = ' ';
    private static final char CHAR_PLUS = '+';

    private MacUtil() {
    }

    public static String getMacOrigin(String mac) {
        if (!REGEX_MAC.matcher(mac).matches()) {
            return MAC_NONE;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(mac.substring(0, 2));
        sb.append(":");
        sb.append(mac.substring(2, 4));
        sb.append(":");
        sb.append(mac.substring(4, 6));
        sb.append(":");
        sb.append(mac.substring(6, 8));
        sb.append(":");
        sb.append(mac.substring(8, 10));
        sb.append(":");
        sb.append(mac.substring(10, 12));
        return sb.toString();
    }

    /**
     * 解析mac
     *
     * @param tid     请求参数中的tid字段
     * @param uuid1   请求参数中的uuid1字段
     * @param sdkType 请求参数中sdk_type字段
     * @return
     */
    public static String getMac(String tid, String uuid1, SdkTypeEnum sdkType) {
        String mac = StringUtils.trimToEmpty(tid);
        // 解密mac
        if (sdkType == SdkTypeEnum.iOS && StringUtils.isNotBlank(mac)) {
            // 修正base64 "+"被转为" "问题
            mac = fixBase64(mac);
            // 解码
            mac = decode(mac, uuid1);
        }
        return mac;
    }


    /**
     * 修正Base64加号问题
     *
     * @param str base64后可能出现" "串
     * @return 返回将" "转为"+"的串
     */
    public static String fixBase64(String str) {
        return StringUtils.replaceChars(str, CHAR_BLANK, CHAR_PLUS);
    }

    /**
     * 编码
     *
     * @param source 原始串
     * @param key    加密key
     * @return 编码后的串
     */
    public static String encode(String source, String key) {
        Preconditions.checkNotNull(source, "source");
        Preconditions.checkNotNull(key, "key");

        // 异或操作,且base64
        byte[] bytes = Base64.encodeBase64(transform(source.getBytes(Charsets.UTF_8), key.getBytes(Charsets.UTF_8)));
        // Base64
        return StringUtils.trim(new String(bytes, Charsets.UTF_8));
    }

    /**
     * 解码, 结果为大写
     *
     * @param source 原始串
     * @param key    加密key
     * @return 编码后的串
     */
    public static String decode(String source, String key) {
        Preconditions.checkNotNull(source, "source");
        Preconditions.checkNotNull(key, "key");

        // base64,且异或操作
        byte[] bytes = transform(Base64.decodeBase64(source), key.getBytes(Charsets.UTF_8));
        String mac = StringUtils.trim(new String(bytes, Charsets.UTF_8).toUpperCase());
        if (REGEX_MAC.matcher(mac).matches()) {
            return mac;
        }
        return MAC_NONE;
    }

    /**
     * 转换, 异或操作
     * <p>
     * <code>source</code>与<code>keys</code>相同索引位进行异或操作
     * </p>
     *
     * @param source 原始串
     * @param keys   加密key
     * @return 串
     */
    private static byte[] transform(byte[] source, byte[] keys) {
        Preconditions.checkPositionIndex(source.length, keys.length);
        byte[] bytes = new byte[source.length];

        for (int i = 0, n = source.length; i < n; i++) {
            bytes[i] = (byte) (source[i] ^ keys[i]);
        }

        return bytes;
    }

    /**
     * 转换, 异或操作
     * <p>
     * <code>source</code>与<code>keys</code>每字节进行异或操作
     * </p>
     *
     * @param source 原始串
     * @param keys   加密key
     * @return 串
     */
    /*
    private static byte[] transform2(byte[] source, byte[] keys) {
        byte[] bytes = new byte[source.length];

        for (byte key : keys) {
            for (int i = 0, n = source.length; i < n; i++) {
                bytes[i] = (byte) (source[i] ^ key);
            }
            source = bytes;
        }

        return bytes;
    }
    */

    /**
     * 获取截取完的MAC
     *
     * @param originMac
     * @return
     */
    public static String getTrimMac(String originMac) {
        if (originMac == null) {
            return MAC_NONE;
        }
        if (!REGEX_ORI_MAC.matcher(originMac).matches()) {
            return MAC_NONE;
        }
        StringBuffer sb = new StringBuffer();
        String[] mac = StringUtils.split(originMac, ":");
        for (String tmp : mac) {
            sb.append(tmp);
        }
        return StringUtils.upperCase(sb.toString());
    }

}
