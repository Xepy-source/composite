package com.ccrental.composite.rental.apis.containers;

import com.ccrental.composite.rental.apis.enums.RentalGetResult;
import com.ccrental.composite.rental.apis.vos.RentalVo;

import java.util.ArrayList;

public class CarListContainer {
    private final RentalGetResult rentalGetResult;
    private final ArrayList<RentalVo> cars;

    public CarListContainer(RentalGetResult rentalGetResult) {
        this(rentalGetResult, null);
    }

    public CarListContainer(RentalGetResult rentalGetResult, ArrayList<RentalVo> cars) {
        this.rentalGetResult = rentalGetResult;
        this.cars = cars;
    }

    public RentalGetResult getRentalGetResult() {
        return rentalGetResult;
    }

    public ArrayList<RentalVo> getCars() {
        return cars;
    }
}
