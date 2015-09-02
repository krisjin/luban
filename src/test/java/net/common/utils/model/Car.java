package net.common.utils.model;

/**
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/9/2
 * Time: 10:22
 */
public class Car {

    private double price;

    private String color;

    private String brand;

    public Car(String brand, double price, String color) {
        this.brand = brand;
        this.price = price;
        this.color = color;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

}
