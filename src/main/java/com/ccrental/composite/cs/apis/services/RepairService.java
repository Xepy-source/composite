package com.ccrental.composite.cs.apis.services;

import com.ccrental.composite.cs.apis.daos.RepairDao;
import com.ccrental.composite.cs.apis.enums.AddRepairResult;
import com.ccrental.composite.cs.apis.vos.AddRepairVo;
import com.ccrental.composite.cs.apis.vos.GetRepairVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class RepairService {
    private final DataSource dataSource;
    private final RepairDao repairDao;

    @Autowired
    public RepairService(DataSource dataSource, RepairDao repairDao) {
        this.dataSource = dataSource;
        this.repairDao = repairDao;
    }

    public ArrayList<GetRepairVo> getRepair(String code) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            if (code.equals("0")) {
                return this.repairDao.getRepairsAll(connection, code);
            } else {
                return this.repairDao.getRepairs(connection, code);
            }
        }
    }

    public AddRepairResult addRepair(AddRepairVo addRepairVo) throws SQLException {
        int count;
        try (Connection connection = this.dataSource.getConnection()) {
            if (this.repairDao.RepairNameOverlapCheck(connection, addRepairVo) == 1) {
                return AddRepairResult.NAME_DUPLICATE;
            } else if (this.repairDao.RepairContactOverlapCheck(connection, addRepairVo) == 1) {
                return AddRepairResult.CONTACT_DUPLICATE;
            } else {
                count = this.repairDao.addRepair(connection, addRepairVo);
                if (count == 1) {
                    return AddRepairResult.SUCCESS;
                } else {
                    return AddRepairResult.FAILURE;
                }
            }
        }
    }
}
