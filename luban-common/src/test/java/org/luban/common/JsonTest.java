package org.luban.common;

import com.alibaba.fastjson.JSON;
import org.bscl.common.model.Car;
import org.bscl.common.model.Child;
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
        List<PersonBean2> list = new ArrayList<PersonBean2>();
        PersonBean2 person = new PersonBean2();
        person.setAddress("北京朝阳");
        person.setUsername("shijingui");
        Car car = new Car();
        car.setBrand("豹猫");
        car.setPrice(111);
        person.setCar(car);
        List<Child> childs= new ArrayList<Child>();

        Child child= new Child();
        child.setAge(11);
        child.setName("张三");
        childs.add(child);
        Child child1= new Child();
        child1.setAge(11);
        child1.setName("张三");
        childs.add(child1);

        person.setData(childs);
        list.add(person);


        String object = JSON.toJSONString(person);
        System.out.println(object);

//        int a = 1;
//        int b = 0;
//        double ccc = (double) a / (double) b;
//        BigDecimal num = new BigDecimal(ccc);
//        num = num.setScale(2, RoundingMode.HALF_UP);
//
//
//        System.out.println(num.toString());
    }

    @Test
    public void test3(){
        String s1="{\"data\":[{\"cateCode\":\"3276\",\"cateName\":\"政治/军事\"},{\"cateCode\":\"3289\",\"cateName\":\"中小学教辅\"},{\"cateCode\":\"3273\",\"cateName\":\"历史\"},{\"cateCode\":\"3274\",\"cateName\":\"哲学/宗教\"}," +
                "{\"cateCode\":\"3275\",\"cateName\":\"国学/古籍\"},{\"cateCode\":\"3291\",\"cateName\":\"外语学习\"}],\"success\":\"true\",\"msg\":\"null\"}\n";
        String s="{\"id\":\"110\", \"class\":\"com.jd.testjsf.ExampleObj\"}";
        Object c =JSON.parseObject(s1);

    }
}
