package com.funbox.ecommerce.funbox;

/**
 * Created by Наиль on 29.07.2015.
 */
public class FieldCsv {
    private String title = "";
    private String price = "";
    private int number = 0;

    public FieldCsv(String title, String price, int number) {
        this.title = title;
        this.price = price;
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public int getNumber() {
        return number;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void increment() {
        this.number = this.number - 1;
    }
}
