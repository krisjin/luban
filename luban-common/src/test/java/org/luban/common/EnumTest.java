package org.luban.common;

import org.luban.common.enums.ProcessStatus;
import org.junit.Test;

/**
 * User: krisjin
 * Date: 2016/10/9
 */
public class EnumTest {
    @Test
    public void test1() {
        System.out.println(ProcessStatus.YES.ordinal());
    }
}
