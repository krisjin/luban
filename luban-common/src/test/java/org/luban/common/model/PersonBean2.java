package org.luban.common.model;

/**
 * User: shijingui
 * Date: 2016/1/22
 */
public class PersonBean2 {
    private String username;

    private String favorit;

    private String address;
    private Car car;
    private Object data;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFavorit() {
        return favorit;
    }

    public void setFavorit(String favorit) {
        this.favorit = favorit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
