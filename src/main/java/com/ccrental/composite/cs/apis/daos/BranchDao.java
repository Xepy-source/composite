package com.ccrental.composite.cs.apis.daos;

import com.ccrental.composite.cs.apis.vos.AddBranchVo;
import com.ccrental.composite.cs.apis.vos.GetBranchVo;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class BranchDao {
    public ArrayList<GetBranchVo> getBranchesAll(Connection connection, String code) throws SQLException {
        ArrayList<GetBranchVo> branches = new ArrayList<>();
        String query = "SELECT   `branch_name` AS `name`, " +
                "`branch_address` AS `address`, " +
                "`branch_contact` AS `contact`, " +
                "`branch_operation` AS `operation`, " +
                "`branch_coordinate_x` AS `co_x`, " +
                "`branch_coordinate_y` AS `co_y`, " +
                "`branch_approach_bus` AS `bus`, " +
                "`branch_approach_subway` AS `subway` " +
                " FROM `cc_rental`.`branches` WHERE `branch_code` LIKE '0%' ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    GetBranchVo getBranchVo = new GetBranchVo(
                            resultSet.getString("name"),
                            resultSet.getString("address"),
                            resultSet.getString("contact"),
                            resultSet.getString("operation"),
                            resultSet.getString("co_x"),
                            resultSet.getString("co_y"),
                            resultSet.getString("bus"),
                            resultSet.getString("subway")
                    );
                    branches.add(getBranchVo);
                }
            }
        }
        return branches;
    }

    public ArrayList<GetBranchVo> getBranches(Connection connection, String code) throws SQLException {
        ArrayList<GetBranchVo> branches = new ArrayList<>();
        String query = "SELECT   `branch_name` AS `name`, " +
                "`branch_address` AS `address`, " +
                "`branch_contact` AS `contact`, " +
                "`branch_operation` AS `operation`, " +
                "`branch_coordinate_x` AS `co_x`, " +
                "`branch_coordinate_y` AS `co_y`, " +
                "`branch_approach_bus` AS `bus`, " +
                "`branch_approach_subway` AS `subway` " +
                " FROM `cc_rental`.`branches` WHERE `branch_code` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    GetBranchVo getBranchVo = new GetBranchVo(
                            resultSet.getString("name"),
                            resultSet.getString("address"),
                            resultSet.getString("contact"),
                            resultSet.getString("operation"),
                            resultSet.getString("co_x"),
                            resultSet.getString("co_y"),
                            resultSet.getString("bus"),
                            resultSet.getString("subway")
                    );
                    branches.add(getBranchVo);
                }
            }
        }
        return branches;
    }

    public int addBranch(Connection connection, AddBranchVo addBranchVo) throws SQLException {
        String query = "INSERT INTO `cc_rental`.`branches`" +
                "(`branch_name`, `branch_address`, `branch_contact`, `branch_code`," +
                " `branch_operation`, `branch_coordinate_x`, `branch_coordinate_y`, " +
                "`branch_approach_bus`, `branch_approach_subway`) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, addBranchVo.getName());
            preparedStatement.setString(2, addBranchVo.getAddress());
            preparedStatement.setString(3, addBranchVo.getContact());
            preparedStatement.setString(4, addBranchVo.getCode());
            preparedStatement.setString(5, addBranchVo.getOperation());
            preparedStatement.setString(6, addBranchVo.getCoordinate_x());
            preparedStatement.setString(7, addBranchVo.getCoordinate_y());
            preparedStatement.setString(8, addBranchVo.getApproach_bus());
            preparedStatement.setString(9, addBranchVo.getApproach_subway());
            preparedStatement.execute();
        }
        return this.addBranchCheck(connection, addBranchVo);
    }

    public int addBranchCheck(Connection connection, AddBranchVo addBranchVo) throws SQLException {
        int count = 0;
        String query = "select count(`branch_index`) as `count` from `cc_rental`.`branches` where `branch_name`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, addBranchVo.getName());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        }
        return count;
    }

    public int branchNameOverlapCheck(Connection connection, AddBranchVo addBranchVo) throws SQLException {
        int count = 0;
        String query = "select count(`branch_index`) as `count` from `cc_rental`.`branches` where `branch_name`=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, addBranchVo.getName());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        }
        return count;
    }

    public int branchContactOverlapCheck(Connection connection, AddBranchVo addBranchVo) throws SQLException {
        int count = 0;
        String query = "select count(`branch_index`) as `count` from `cc_rental`.`branches` where `branch_contact`=?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, addBranchVo.getContact());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        }
        return count;
    }
}
