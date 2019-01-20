package org.luban.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: shijingui
 * Date: 2016/9/5
 */
public class LRUMapTest {

    @Test
    public void test1() {
        LRUMap lruMap = new LRUMap(10);
        lruMap.put("name", "jingui");
        lruMap.put("sex", "jingui");
        lruMap.put("address", "jingui");
        lruMap.put("interest", "jingui");
        lruMap.put("age", "jingui");
        lruMap.put("height", "jingui");
        lruMap.put("birthday", "jingui");
        lruMap.put("wish", "jingui");
        lruMap.put("play", "jingui");
        lruMap.put("plan", "jingui");
        lruMap.put("hair", "jingui");
        assertEquals(10, lruMap.size());
    }
}
