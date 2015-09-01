package net.common.utils;

import net.common.utils.uuid.UuidUtil;
import org.junit.Test;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/1
 * Time: 15:11
 */
public class UUIDTest {

    @Test
    public void test() {
        String uuid = UuidUtil.genTerseUuid();
        System.out.println("UUID:" + uuid + "\nLen:" + uuid.length());
    }
}
