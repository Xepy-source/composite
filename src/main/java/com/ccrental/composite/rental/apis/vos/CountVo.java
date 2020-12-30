package com.ccrental.composite.rental.apis.vos;

public class CountVo {
    private final int branchIndex;
    private final int carIndex;
    private final String carName;
    private final String carType;
    private final int totalCar;
    private final int usingCar;

    public CountVo(int branchIndex, int carIndex, String carName, String carType, int totalCar, int usingCar) {
        this.branchIndex = branchIndex;
        this.carIndex = carIndex;
        this.carName = carName;
        this.carType = carType;
        this.totalCar = totalCar;
        this.usingCar = usingCar;
    }

    public int getBranchIndex() {
        return branchIndex;
    }

    public int getCarIndex() {
        return carIndex;
    }

    public String getCarName() {
        return carName;
    }

    public String getCarType() {
        return carType;
    }

    public int getTotalCar() {
        return totalCar;
    }

    public int getUsingCar() {
        return usingCar;
    }
}
