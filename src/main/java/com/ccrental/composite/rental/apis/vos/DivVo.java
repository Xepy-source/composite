package com.ccrental.composite.rental.apis.vos;

public class DivVo {
    private final int branchIndex;
    private final int carIndex;
    private final String carDiv;

    public DivVo(int branchIndex, int carIndex, String carDiv) {
        this.branchIndex = branchIndex;
        this.carIndex = carIndex;
        this.carDiv = carDiv;
    }

    public int getBranchIndex() {
        return branchIndex;
    }

    public int getCarIndex() {
        return carIndex;
    }

    public String getCarDiv() {
        return carDiv;
    }
}
