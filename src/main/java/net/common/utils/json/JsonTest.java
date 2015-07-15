package net.common.utils.json;

import com.alibaba.fastjson.JSON;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/7/15
 * Time: 14:16
 */
public class JsonTest {


    public static void main(String[] args) {
        User user = new User();
        user.setAge(123);
        user.setUserName("krisjin");

        String userString = JSON.toJSONString(user);
        User retUser = JSON.parseObject(userString, User.class);

        System.out.println(userString);

    }

    static class User {
        private String userName;
        private int age;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
