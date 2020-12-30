package com.ccrental.composite.rental.apis.vos;

public class MyRentalVo {
    private final String fromDate;
    private final String fromTime;
    private final String toDate;
    private final String toTime;
    private final int branchIndex;
    private final int carIndex;
    private final int userIndex;

    public MyRentalVo(String fromDate, String fromTime, String toDate, String toTime, int branchIndex, int carIndex, int userIndex) {
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
        this.branchIndex = branchIndex;
        this.carIndex = carIndex;
        this.userIndex = userIndex;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToDate() {
        return toDate;
    }

    public String getToTime() {
        return toTime;
    }

    public int getBranchIndex() {
        return branchIndex;
    }

    public int getCarIndex() {
        return carIndex;
    }

    public int getUserIndex() {
        return userIndex;
    }
}
