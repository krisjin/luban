package org.bscl.common;

import com.alibaba.fastjson.JSON;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/6/4
 * Time: 21:00
 */
public class FastjsonTest {


    public static void main(String[] args) {
        User user = new User();
        user.setId(121L);
        user.setName("kris");
        String str = JSON.toJSONString(user);
        System.out.println(str);
    }


    static class User {

        private long id;

        private String name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
