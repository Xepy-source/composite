package com.ccrental.composite.rental.apis.vos;

public class CarRentInfoVo {
    private final String name;
    private final int price;
    private final int ins;

    public CarRentInfoVo(String name, int price, int ins) {
        this.name = name;
        this.price = price;
        this.ins = ins;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getIns() {
        return ins;
    }
}
