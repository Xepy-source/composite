package com.ccrental.composite.rental.apis.containers;

import com.ccrental.composite.rental.apis.enums.RentalGetResult;
import com.ccrental.composite.rental.apis.vos.CountVo;

import java.util.ArrayList;

public class RentalResultContainer {
    private final RentalGetResult rentalGetResult;
    private final ArrayList<CountVo> counts;

    public RentalResultContainer(RentalGetResult rentalGetResult) {
        this(rentalGetResult, null);
    }

    public RentalResultContainer(RentalGetResult rentalGetResult, ArrayList<CountVo> counts) {
        this.rentalGetResult = rentalGetResult;
        this.counts = counts;
    }

    public RentalGetResult getRentalGetResult() {
        return rentalGetResult;
    }

    public ArrayList<CountVo> getCounts() {
        return counts;
    }
}

