package com.ccrental.composite.rental.apis.vos;

import java.sql.Date;
import java.sql.Time;

public class AddRentalVo {
    private final Date rentalFromDate;
    private final Time rentalFromTime;
    private final Date rentalToDate;
    private final Time rentalToTime;
    private final int branchIndex;
    private final int carIndex;
    private final int userIndex;

    public AddRentalVo(Date rentalFromDate, Time rentalFromTime, Date rentalToDate, Time rentalToTime, int branchIndex, int carIndex, int userIndex) {
        this.rentalFromDate = rentalFromDate;
        this.rentalFromTime = rentalFromTime;
        this.rentalToDate = rentalToDate;
        this.rentalToTime = rentalToTime;
        this.branchIndex = branchIndex;
        this.carIndex = carIndex;
        this.userIndex = userIndex;
    }

    public int getUserIndex() {
        return userIndex;
    }

    public Date getRentalFromDate() {
        return rentalFromDate;
    }

    public Time getRentalFromTime() {
        return rentalFromTime;
    }

    public Date getRentalToDate() {
        return rentalToDate;
    }

    public Time getRentalToTime() {
        return rentalToTime;
    }

    public int getBranchIndex() {
        return branchIndex;
    }

    public int getCarIndex() {
        return carIndex;
    }
}
