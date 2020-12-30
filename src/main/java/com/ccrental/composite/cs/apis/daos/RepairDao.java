package com.ccrental.composite.cs.apis.daos;

import com.ccrental.composite.cs.apis.vos.AddRepairVo;
import com.ccrental.composite.cs.apis.vos.GetRepairVo;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class RepairDao {
    public ArrayList<GetRepairVo> getRepairsAll(Connection connection, String code) throws SQLException {
        ArrayList<GetRepairVo> repairs = new ArrayList<>();
        String query = "SELECT   `repair_name` AS `name`, " +
                "`repair_address` AS `address`, " +
                "`repair_contact` AS `contact`, " +
                "`repair_coordinate_x` AS `co_x`, " +
                "`repair_coordinate_y` AS `co_y` " +
                " FROM `cc_rental`.`repairs` WHERE `repair_code` LIKE '0%'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    GetRepairVo getRepairVo = new GetRepairVo(
                            resultSet.getString("name"),
                            resultSet.getString("address"),
                            resultSet.getString("contact"),
                            resultSet.getString("co_x"),
                            resultSet.getString("co_y")
                    );
                    repairs.add(getRepairVo);
                }
            }
        }
        return repairs;
    }

    public ArrayList<GetRepairVo> getRepairs(Connection connection, String code) throws SQLException {
        ArrayList<GetRepairVo> repairs = new ArrayList<>();
        String query = "SELECT   `repair_name` AS `name`, " +
                "`repair_address` AS `address`, " +
                "`repair_contact` AS `contact`, " +
                "`repair_coordinate_x` AS `co_x`, " +
                "`repair_coordinate_y` AS `co_y` " +
                " FROM `cc_rental`.`repairs` WHERE `repair_code` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    GetRepairVo getRepairVo = new GetRepairVo(
                            resultSet.getString("name"),
                            resultSet.getString("address"),
                            resultSet.getString("contact"),
                            resultSet.getString("co_x"),
                            resultSet.getString("co_y")
                    );
                    repairs.add(getRepairVo);
                }
            }
        }
        return repairs;
    }

    public int addRepair(Connection connection, AddRepairVo addRepairVo) throws SQLException {
        String query = "INSErT INTO `cc_rental`.`repaires`" +
                "(`repair_name`, `repair_address`, `repair_contact`, " +
                "`repair_code`, `repair_coordinate_x`, `repair_coordinate_y`) " +
                "values (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, addRepairVo.getName());
            preparedStatement.setString(2, addRepairVo.getAddress());
            preparedStatement.setString(3, addRepairVo.getContact());
            preparedStatement.setString(4, addRepairVo.getCode());
            preparedStatement.setString(5, addRepairVo.getCoordinate_x());
            preparedStatement.setString(6, addRepairVo.getCoordinate_y());
            preparedStatement.execute();
        }
        return this.addRepairCheck(connection, addRepairVo);
    }

    public int addRepairCheck(Connection connection, AddRepairVo addRepairVo) throws SQLException {
        int count = 0;
        String query = "select count(`repair_index`) as `count` from `cc_rental`.`repaires` where `repair_name`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, addRepairVo.getName());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        }
        return count;
    }

    public int RepairNameOverlapCheck(Connection connection, AddRepairVo addRepairVo) throws SQLException {
        int count = 0;
        String query = "select count(`repair_index`) as `count` from `cc_rental`.`repaires` where `repair_name`=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, addRepairVo.getName());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        }
        return count;
    }

    public int RepairContactOverlapCheck(Connection connection, AddRepairVo addRepairVo) throws SQLException {
        int count = 0;
        String query = "select count(`repair_index`) as `count` from `cc_rental`.`repaires` where `repair_contact`=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, addRepairVo.getContact());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        }
        return count;
    }
}
