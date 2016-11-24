package org.bscl.common;

import com.alibaba.fastjson.JSON;
import org.bscl.common.model.PersonBean2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: shijingui
 * Date: 2016/11/23
 */
public class JsonTest {
    @Test
    public void test() {
        List<List<Object>> list = new ArrayList<List<Object>>();
        List p = new ArrayList();
        PersonBean2 car = new PersonBean2();
        p.add(car);
        list.add(p);
        String object = JSON.toJSONString(list);
        System.out.println(object);
    }
}
