package com.ccrental.composite.rental.apis.vos;

public class CarVo {
    private final int branchIndex;
    private final int carIndex;
    private final String carName;
    private final String carType;
    private final int carCount;
    private final String branchDiv;

    public CarVo(int branchIndex, int carIndex, String carName, String carType, int carCount, String branchDiv) {
        this.branchIndex = branchIndex;
        this.carIndex = carIndex;
        this.carName = carName;
        this.carType = carType;
        this.carCount = carCount;
        this.branchDiv = branchDiv;
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

    public int getCarCount() {
        return carCount;
    }

    public String getBranchDiv() {
        return branchDiv;
    }
}
