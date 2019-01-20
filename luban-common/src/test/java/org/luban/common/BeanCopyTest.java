package org.luban.common;

import org.luban.common.model.PersonBean2;
import org.luban.common.model.PersonBean1;
import org.junit.Test;

/**
 * User: shijingui
 * Date: 2016/1/22
 */
public class BeanCopyTest {

    @Test
    public void test1() {
        PersonBean1 personBean1 = new PersonBean1();
        personBean1.setAccount("xiaoming");
        personBean1.setAddress("beijing");
        personBean1.setSex(1);
        personBean1.setUsername("huangxiaoming");

        PersonBean2 personBean2 = new PersonBean2();

        BeanCopierUtils.copyProperties(personBean1, personBean2);
        System.out.println(222);

    }
}
