package org.bscl.common;

import org.bscl.common.enums.ProcessStatus;
import org.junit.Test;

/**
 * User: shijingui
 * Date: 2016/10/9
 */
public class EnumTest {
    @Test
    public void test1() {
        System.out.println(ProcessStatus.YES.ordinal());
    }
}
