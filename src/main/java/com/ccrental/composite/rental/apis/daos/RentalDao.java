package com.ccrental.composite.rental.apis.daos;

import com.ccrental.composite.rental.apis.vos.*;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class RentalDao {
    // -------------------------------------------------------------------------------------------- get
    // 예약전 각 렌트카 총수
    public ArrayList<CarVo> totalCarList(Connection connection, InputVo inputVo) throws SQLException {
        CarVo carVo = null;
        ArrayList<CarVo> cars = new ArrayList<>();
        String query = "" +
                "SELECT `count`.`branch_index` AS `branchIndex`,\n" +
                "       `count`.`car_index`    AS `carIndex`,\n" +
                "       `cars`.`car_name`      AS `carName`,\n" +
                "       `cars`.`car_class`     AS `carType`,\n" +
                "       `count`.`car_count`    AS `totalCar`,\n" +
                "       `cars`.`car_division`   AS `branchDiv`\n" +
                "FROM `cc_rental`.`counts` AS `count`\n" +
                "         INNER JOIN `cc_rental`.`cars` ON `count`.`car_index` = `cars`.`car_index`\n" +
                "WHERE `cars`.car_division like ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + inputVo.getBranchDiv() + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    carVo = new CarVo(
                            resultSet.getInt("branchIndex"),
                            resultSet.getInt("carIndex"),
                            resultSet.getString("carName"),
                            resultSet.getString("carType"),
                            resultSet.getInt("totalCar"),
                            resultSet.getString("branchDiv")
                    );
                    cars.add(carVo);
                }
            }
            return cars;
        }
    }

    // ONLY FULL GROUP BY 제거
    public void removeGroupBy(Connection connection) throws SQLException {
        String query = "SET sql_mode=(SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY', ''))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.execute();
        }
    }

    // 예약전 각 사용가능 렌트카
    public ArrayList<CarVo> availableCarList(Connection connection, InputVo inputVo) throws SQLException {
        CarVo carVo = null;
        ArrayList<CarVo> cars = new ArrayList<>();

        String query = "" +
                "SELECT `counts`.`branch_index`     AS `branchIndex`,\n" +
                "       `counts`.`car_index`        AS `carIndex`,\n" +
                "       `car`.`car_name`            AS `carName`,\n" +
                "       `car`.`car_class`           AS `carType`,\n" +
                "       `car`.`car_division`        AS `branchDiv`,\n" +
                "       IFNULL(`countTable`.`availableCar`, 0) AS `availableCar`\n" +
                "FROM `cc_rental`.`counts`\n" +
                "         INNER JOIN `cc_rental`.`cars` AS `car` on `counts`.`car_index` = `car`.`car_index`\n" +
                "         LEFT JOIN (SELECT IFNULL(count(rental_index), `counts`.`car_count`) AS `availableCar`, rentals.car_index\n" +
                "                    FROM cc_rental.rentals\n" +
                "                             INNER JOIN cc_rental.counts ON rentals.car_index = counts.car_index\n" +
                "                    WHERE CAST(CONCAT(rental_to_date, ' ', rental_to_time) AS DATETIME) <\n" +
                "                          CAST(CONCAT(?, ' ', ?) AS DATETIME)\n" +
                "                       OR CAST(CONCAT(rental_from_date, ' ', rental_from_time) AS DATETIME) >\n" +
                "                          CAST(CONCAT(?, ' ', ?) AS DATETIME)) AS countTable\n" +
                "                   ON car.car_index = countTable.car_index\n" +
                "WHERE `car`.`car_division` like ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, inputVo.getFromDate());
            preparedStatement.setTime(2, inputVo.getFromTime());
            preparedStatement.setDate(3, inputVo.getToDate());
            preparedStatement.setTime(4, inputVo.getToTime());
            preparedStatement.setString(5, "%" + inputVo.getBranchDiv() + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    carVo = new CarVo(
                            resultSet.getInt("branchIndex"),
                            resultSet.getInt("carIndex"),
                            resultSet.getString("carName"),
                            resultSet.getString("carType"),
                            resultSet.getInt("availableCar"),
                            resultSet.getString("branchDiv"));
                    cars.add(carVo);
                }
            }
            return cars;
        }
    }

    // land vs jeju
    public ArrayList<DivVo> branchDiv(Connection connection) throws SQLException {
        DivVo divVo = null;
        ArrayList<DivVo> divs = new ArrayList<>();

        String query = "" +
                "SELECT rentals.branch_index AS `branchIndex`,\n" +
                "       cars.car_index       AS `carIndex`,\n" +
                "       cars.car_division    AS `carDiv`\n" +
                "FROM cc_rental.rentals\n" +
                "         INNER JOIN cc_rental.cars ON rentals.car_index = cars.car_index\n" +
                "group by branch_index";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    divVo = new DivVo(resultSet.getInt("branchIndex"),
                            resultSet.getInt("branchIndex"),
                            resultSet.getString("carDiv"));
                    divs.add(divVo);
                }
            }
        }
        return divs;
    }

    // ------------------------------------------------------------------------------------------- add
    // Rental 추가
    public void insertRental(Connection connection, AddRentalVo addRentalVo) throws SQLException {
        String query = "" +
                "INSERT INTO `cc_rental`.`rentals`(`rental_from_date`,\n" +
                "                                  `rental_from_time`,\n" +
                "                                  `rental_to_date`,\n" +
                "                                  `rental_to_time`,\n" +
                "                                  `branch_index`,\n" +
                "                                  `car_index`,\n" +
                "                                  `user_index`)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, addRentalVo.getRentalFromDate());
            preparedStatement.setTime(2, addRentalVo.getRentalFromTime());
            preparedStatement.setDate(3, addRentalVo.getRentalToDate());
            preparedStatement.setTime(4, addRentalVo.getRentalToTime());
            preparedStatement.setInt(5, addRentalVo.getBranchIndex());
            preparedStatement.setInt(6, addRentalVo.getCarIndex());
            preparedStatement.setInt(7, addRentalVo.getUserIndex());
            preparedStatement.execute();
        }
    }

    // 렌트카 총수
    public int totalCar(Connection connection, AddRentalVo addRentalVo) throws SQLException {
        int count = 0;
        String query = "" +
                "SELECT car_count AS `totalCar` FROM `cc_rental`.counts AS `count`\n" +
                "WHERE `count`.`branch_index` = ?\n" +
                "  AND `count`.`car_index` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, addRentalVo.getBranchIndex());
            preparedStatement.setInt(2, addRentalVo.getCarIndex());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    count = resultSet.getInt("totalCar");
                }
            }
        }
        return count;
    }

    // 사용가능한 렌트카
    public int availableCar(Connection connection, AddRentalVo addRentalVo) throws SQLException {
        int count = 0;
        String query = "" +
                "SELECT COUNT(`rental_index`) AS `availableCar`\n" +
                "FROM `cc_rental`.`rentals` AS `rental_in`,\n" +
                "     `cc_rental`.`counts` AS `count`\n" +
                "WHERE `count`.`branch_index` = ?\n" +
                "  AND `count`.`car_index` = ?\n" +
                "  AND (CAST(CONCAT(rental_to_date, ' ', rental_to_time) AS DATETIME) <\n" +
                "      CAST(CONCAT(?, ' ', ?) AS DATETIME)\n" +
                "   OR CAST(CONCAT(rental_from_date, ' ', rental_from_time) AS DATETIME) >\n" +
                "      CAST(CONCAT(?, ' ', ?) AS DATETIME))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, addRentalVo.getBranchIndex());
            preparedStatement.setInt(2, addRentalVo.getCarIndex());
            preparedStatement.setDate(3, addRentalVo.getRentalFromDate());
            preparedStatement.setTime(4, addRentalVo.getRentalFromTime());
            preparedStatement.setDate(5, addRentalVo.getRentalToDate());
            preparedStatement.setTime(6, addRentalVo.getRentalToTime());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    count = resultSet.getInt("availableCar");
                }
            }
        }
        return count;
    }

    // Retal 선택 차 리스트 (예약후)
    public ArrayList<CountVo> getRental(Connection connection, AddRentalVo addRentalVo) throws SQLException {
        CountVo countVo = null;
        ArrayList<CountVo> counts = new ArrayList<>();
        String query = "" +
                "SELECT `count`.`branch_index` AS `branchIndex`,\n" +
                "       `count`.`car_index`    AS `carIndex`,\n" +
                "       `car`.`car_name`       AS `carName`,\n" +
                "       `car`.`car_class`      AS `carType`,\n" +
                "       `count`.`car_count`    AS `totalCar`,\n" +
                "       (SELECT COUNT(`rental_index`)\n" +
                "        FROM `cc_rental`.`rentals` AS `rental_in`\n" +
                "        WHERE `rental_in`.`branch_index` = ?\n" +
                "          AND `count`.`car_index` = `rental_in`.`car_index`\n" +
                "          AND CAST(CONCAT(rental_to_date, ' ', rental_to_time) AS DATETIME) <\n" +
                "              CAST(CONCAT(?, ' ', ?) AS DATETIME)\n" +
                "           OR CAST(CONCAT(rental_from_date, ' ', rental_from_time) AS DATETIME) >\n" +
                "              CAST(CONCAT(?, ' ', ?) AS DATETIME))          AS `availableCar`\n" +
                "FROM `cc_rental`.`counts` AS `count`\n" +
                "         INNER JOIN `cc_rental`.`rentals` AS `rental`\n" +
                "                    ON `count`.`branch_index` = `rental`.`branch_index` AND `count`.`car_index` = ?\n" +
                "         LEFT JOIN `cc_rental`.`cars` AS `car`\n" +
                "                   ON `count`.`car_index` = `car`.`car_index`\n" +
                "GROUP BY `count`.`count_index`";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, addRentalVo.getBranchIndex());
            preparedStatement.setDate(2, addRentalVo.getRentalFromDate());
            preparedStatement.setTime(3, addRentalVo.getRentalFromTime());
            preparedStatement.setDate(4, addRentalVo.getRentalToDate());
            preparedStatement.setTime(5, addRentalVo.getRentalToTime());
            preparedStatement.setInt(6, addRentalVo.getCarIndex());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    countVo = new CountVo(
                            resultSet.getInt("branchIndex"),
                            resultSet.getInt("carIndex"),
                            resultSet.getString("carName"),
                            resultSet.getString("carType"),
                            resultSet.getInt("totalCar"),
                            resultSet.getInt("availableCar")
                    );
                    counts.add(countVo);
                }
            }
            return counts;
        }
    }

    public String selectBranchName(Connection connection, int branchIndex) throws SQLException {
        String name = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT branch_name AS `branchName` FROM `cc_rental`.`branches` WHERE `branch_index` = ?")) {
            preparedStatement.setInt(1, branchIndex);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    name = resultSet.getString("branchName");
                }
            }
        }
        return name;
    }

    public CarRentInfoVo selectCarName(Connection connection, int carIndex) throws SQLException {
        CarRentInfoVo carRentInfoVo = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT car_name AS `carName`, car_basic_1 AS `price`, car_insurance_1 AS `ins` FROM `cc_rental`.`cars` WHERE `car_index` = ?")) {
            preparedStatement.setInt(1, carIndex);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    carRentInfoVo = new CarRentInfoVo(
                            resultSet.getString("carName"),
                            resultSet.getInt("price"),
                            resultSet.getInt("ins")
                    );
                }
            }
        }
        return carRentInfoVo;
    }

    public int getRentalIndex(Connection connection) throws SQLException {
        int index = 0;
        String query = "" +
                "SELECT LAST_INSERT_ID() AS `lastIndex`,\n" +
                "       count(*)         AS `count`\n" +
                "FROM cc_rental.rentals\n" +
                "GROUP BY lastIndex";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    index = resultSet.getInt("lastIndex");
                }
            }
        }
        return index;
    }

    public ArrayList<MyRentalVo> getMyRental(Connection connection, int index) throws SQLException {
        ArrayList<MyRentalVo> rentals = new ArrayList<>();
        String query = "" +
                "SELECT rental_from_date AS `fromDate`,\n" +
                "       rental_from_time AS `fromTime`,\n" +
                "       rental_to_date   AS `toDate`,\n" +
                "       rental_to_time   AS `toTime`,\n" +
                "       branch_index     AS `branchIndex`,\n" +
                "       car_index        AS `carIndex`,\n" +
                "       user_index       AS `userIndex`\n" +
                "FROM cc_rental.rentals WHERE `user_index`=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, index);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    MyRentalVo rentalVo = new MyRentalVo(
                            resultSet.getString("fromDate"),
                            resultSet.getString("fromTime"),
                            resultSet.getString("toDate"),
                            resultSet.getString("toTime"),
                            resultSet.getInt("branchIndex"),
                            resultSet.getInt("carIndex"),
                            resultSet.getInt("userIndex"));
                    rentals.add(rentalVo);
                }
            }
        }
        return rentals;
    }
}