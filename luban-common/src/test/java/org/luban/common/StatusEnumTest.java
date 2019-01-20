package org.luban.common;

import org.luban.common.enums.IndexedEnum;
import org.luban.common.enums.IndexedEnum;

import java.util.List;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/27
 * Time: 8:49
 */
public enum StatusEnumTest implements IndexedEnum {

    /**
     * 未审核
     */
    UN_REVIEWED(1),
    /**
     * 未通过
     */
    UN_PASSED(2),
    /**
     * 冻结
     */
    FROZEN(3),
    /**
     * 通过
     */
    PASSED(4);

    /**
     * 枚举元素列表
     */
    private static final List<StatusEnumTest> INDEXS = Util.toIndexes(StatusEnumTest.values());

    private final int index;

    StatusEnumTest(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return 0;
    }

    /**
     * 根据索引获取对应的类型{@link StatusEnumTest}
     *
     * @param index
     * @return
     */
    public static StatusEnumTest indexOf(final int index) {
        return Util.valueOf(INDEXS, index);
    }
}
