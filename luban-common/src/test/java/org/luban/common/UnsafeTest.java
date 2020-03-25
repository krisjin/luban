package org.luban.common;

import org.luban.common.model.Car;
import org.luban.common.unsafe.UnsafeObj;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * User:krisjin
 * Date:2019/2/1
 *  
 */
public class UnsafeTest {

    public static void main(String[] args) throws InstantiationException {
        try {
            Unsafe unsafe = UnsafeObj.getUnsafe();
            Car car = (Car) unsafe.allocateInstance(Car.class);

            car.setBrand("dazhong");
            Class carClass = car.getClass();
            Field brandField = carClass.getDeclaredField("brand");
            Field colorField = carClass.getDeclaredField("color");
            Field price = carClass.getDeclaredField("price");

            long brandOffset = unsafe.objectFieldOffset(brandField);
            long colorOffset = unsafe.objectFieldOffset(colorField);
            System.err.println(brandOffset);
            System.err.println(colorOffset);

            //直接往内存地址写数据
            unsafe.putObject(car, unsafe.objectFieldOffset(brandField), "baoma");
//            unsafe.putInt(user, unsafe.objectFieldOffset(age), 101);
            unsafe.putDouble(car, unsafe.objectFieldOffset(price), 100.1);

            System.out.println(car.getBrand() + " " + car.getPrice());
//            unsafe.freeMemory(1213232L);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
