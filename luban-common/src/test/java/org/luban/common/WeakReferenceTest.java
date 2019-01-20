package org.luban.common;

import org.bscl.common.model.Car;
import org.junit.Test;
import org.luban.common.model.Car;

import java.lang.ref.WeakReference;

/**
 * User : shijingui
 * Date: 2015/9/2
 * Time: 10:26
 */
public class WeakReferenceTest {

    /**
     * 使用WeakReference
     */
    @Test
    public void test() {
        Car car = new Car("Audi", 380000d, "Golden");
        WeakReference<Car> weakReference = new WeakReference<Car>(car);
        int i = 0;
        while (true) {
//            System.out.println("在这里做强引用的使用" + car);
            if (weakReference.get() != null) {
                i++;
                System.out.println("对象是存活状态，循环了" + i + "次--" + weakReference);
            } else {
                System.out.println("对象被回收了!对象状态--" + weakReference.get());
                break;
            }
        }
    }


    @Test
    public void test2() {
        Car car = new Car("Audi", 380000d, "Golden");
        int i = 0;
        while (true) {
            if (car != null) {
                i++;
                if (i == 100000) car = null;
                System.out.println("对象是存活状态，循环了" + i + "次--" + car);
            } else {
                System.out.println("对象被回收了!对象状态--" + car);
                break;
            }
        }
    }

}
