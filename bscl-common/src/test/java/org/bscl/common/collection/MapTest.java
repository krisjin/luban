package org.bscl.common.collection;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: shijingui
 * Date: 2016/8/24
 */
public class MapTest {

    @Test
    public void test1() {
        Map map = new HashMap();
        map.put("name", "jingui");
        map.put("name2", "shi");
        map.put("name3", "shi");

        System.out.println(map.size());
        System.out.println(map.get("name"));
    }

    @Test
    public void test2() {
        Map map = new TreeMap();
        map.put("name", "jingui");
        map.put("name2", "shi");
        map.put("name3", "shi");

        System.out.println(map.size());
        System.out.println(map.get("name"));
    }

    @Test
    public void test3() {
        Map map = new HashMap();
        map.put(null, "ss");
        map.put(null, "aa");
        System.out.println(map.size());
        System.out.println(map.get(null));

        boolean digit = Character.isDigit('a');
        System.out.println(digit);
    }
}
