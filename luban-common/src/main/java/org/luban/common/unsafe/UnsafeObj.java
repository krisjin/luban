package org.luban.common.unsafe;

import sun.misc.Unsafe;
import sun.reflect.CallerSensitive;

import java.lang.reflect.Field;

/**
 * User:shijingui
 * Date:2019/2/1
 *  
 */
public class UnsafeObj {


    @CallerSensitive
    public static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        // 通过反射得到theUnsafe对应的Field对象
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        // 设置该Field为可访问
        field.setAccessible(true);
        // 通过Field得到该Field对应的具体对象，传入null是因为该Field为static的
        Unsafe unsafe = (Unsafe) field.get(null);
        return unsafe;
    }


}
