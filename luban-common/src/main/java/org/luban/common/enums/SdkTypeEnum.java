package org.luban.common.enums;


import java.util.List;

public enum SdkTypeEnum implements IndexedEnum {
    iOS(1),
    ANDROID(2),
    WP(3),
    WAP(4),
    API(5);

    private static final List<SdkTypeEnum> INDEXS = Util.toIndexes(SdkTypeEnum.values());

    /**
     * 索引
     */
    private final int index;

    private SdkTypeEnum(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    /**
     * 根据索引获取枚举常量
     *
     * @param index
     * @return
     */
    public static SdkTypeEnum indexOf(final int index) {
        return Util.valueOf(INDEXS, index);
    }
}
