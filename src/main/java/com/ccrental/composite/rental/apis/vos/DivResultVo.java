package com.ccrental.composite.rental.apis.vos;

public class DivResultVo {
    private final int carIndex;
    private final String carType;
    private final String carName;
    private final String carDiv;
    private final int branchIndex;

    public DivResultVo(int carIndex, String carType, String carName, String carDiv, int branchIndex) {
        this.carIndex = carIndex;
        this.carType = carType;
        this.carName = carName;
        this.carDiv = carDiv;
        this.branchIndex = branchIndex;
    }

    public int getCarIndex() {
        return carIndex;
    }

    public String getCarType() {
        return carType;
    }

    public String getCarName() {
        return carName;
    }

    public String getCarDiv() {
        return carDiv;
    }

    public int getBranchIndex() {
        return branchIndex;
    }
}
