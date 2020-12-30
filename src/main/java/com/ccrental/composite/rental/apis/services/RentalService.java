package com.ccrental.composite.rental.apis.services;


import com.ccrental.composite.common.vos.UserVo;
import com.ccrental.composite.rental.apis.containers.CarListContainer;
import com.ccrental.composite.rental.apis.containers.RentalResultContainer;
import com.ccrental.composite.rental.apis.daos.RentalDao;
import com.ccrental.composite.rental.apis.enums.RentalGetResult;
import com.ccrental.composite.rental.apis.vos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


@Service
public class RentalService {
    private final DataSource dataSource;
    private final RentalDao rentalDao;

    @Autowired
    public RentalService(DataSource dataSource, RentalDao rentalDao) {
        this.dataSource = dataSource;
        this.rentalDao = rentalDao;
    }

    // 예약전
    public CarListContainer getCarList(InputVo inputVo) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {

            ArrayList<RentalVo> cars = new ArrayList<>();

            this.rentalDao.removeGroupBy(connection);
            ArrayList<CarVo> availableCarList = this.rentalDao.availableCarList(connection, inputVo);
            ArrayList<DivVo> divList = this.rentalDao.branchDiv(connection);

            for (CarVo availableCar : availableCarList) {
                for (DivVo branchDiv : divList) {
                    if(branchDiv.getCarDiv().equals(inputVo.getBranchDiv())){
                        if (availableCar.getCarCount() == 0 ) {
                            cars.add(new RentalVo(
                                    inputVo.getBranchDiv(),
                                    availableCar.getBranchIndex(),
                                    availableCar.getCarIndex(),
                                    availableCar.getCarName(),
                                    availableCar.getCarType(),
                                    false)
                            );
                            break;
                        } else {
                            cars.add(new RentalVo(
                                    inputVo.getBranchDiv(),
                                    availableCar.getBranchIndex(),
                                    availableCar.getCarIndex(),
                                    availableCar.getCarName(),
                                    availableCar.getCarType(),
                                    true)
                            );
                            break;
                        }
                    } else{
                        cars.add(new RentalVo(
                                inputVo.getBranchDiv(),
                                availableCar.getBranchIndex(),
                                availableCar.getCarIndex(),
                                availableCar.getCarName(),
                                availableCar.getCarType(),
                                true)
                        );
                        break;
                    }

                }

            }
            System.out.println(cars.size());
            return new CarListContainer(RentalGetResult.SUCCESS, cars);
        }


    }


    // 예약후
    public RentalResultContainer getRental(AddRentalVo addRentalVo) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            ArrayList<CountVo> counts = this.rentalDao.getRental(connection, addRentalVo);
            int totalCar = this.rentalDao.totalCar(connection, addRentalVo);
            int availableCar = this.rentalDao.availableCar(connection, addRentalVo);
            if (availableCar == 0) {
                return new RentalResultContainer(RentalGetResult.SOLDOUT, counts);
            } else {
                this.rentalDao.insertRental(connection, addRentalVo);
                return new RentalResultContainer(RentalGetResult.SUCCESS, counts);
            }
        }
    }

    public String getBranchInfo(int branchIndex) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.rentalDao.selectBranchName(connection, branchIndex);
        }
    }

    public CarRentInfoVo getCarInfo(int carIndex) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.rentalDao.selectCarName(connection, carIndex);
        }
    }

    public int getRentalIndex(UserVo userVo) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            int rentalIndex;
            if (userVo == null) {
                rentalIndex = 0;
                return rentalIndex;
            } else {
                return this.rentalDao.getRentalIndex(connection);
            }
        }
    }

    public ArrayList<MyRentalVo> getMyRental(int index) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()){
            return this.rentalDao.getMyRental(connection, index);
        }
    }

}
