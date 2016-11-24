package org.bscl.common;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/1
 * Time: 14:25
 */
public class ThreadLocalTest {

    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {
        @Override
        public void set(Connection value) {
            super.set(value);
        }

        @Override
        protected Connection initialValue() {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "username", "password");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return conn;
        }
    };

    public static Connection getConnection() {
        return connectionHolder.get();
    }

    public static void setConnection(Connection conn) {
        connectionHolder.set(conn);
    }

    public static void main(String[] args) {
    }
}

