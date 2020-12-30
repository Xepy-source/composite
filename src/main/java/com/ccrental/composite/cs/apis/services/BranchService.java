package com.ccrental.composite.cs.apis.services;

import com.ccrental.composite.cs.apis.daos.BranchDao;
import com.ccrental.composite.cs.apis.enums.AddBranchResult;
import com.ccrental.composite.cs.apis.vos.AddBranchVo;
import com.ccrental.composite.cs.apis.vos.GetBranchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class BranchService {
    private final DataSource dataSource;
    private final BranchDao branchDao;

    @Autowired
    public BranchService(DataSource dataSource, BranchDao branchDao) {
        this.dataSource = dataSource;
        this.branchDao = branchDao;
    }

    public ArrayList<GetBranchVo> getBranch(String code) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            if (code.equals("0")) {
                return this.branchDao.getBranchesAll(connection, code);
            } else {
                return this.branchDao.getBranches(connection, code);
            }

        }
    }

    public AddBranchResult addBranch(AddBranchVo addBranchVo) throws SQLException {
        int count;
        try (Connection connection = this.dataSource.getConnection()) {
            if (this.branchDao.branchNameOverlapCheck(connection, addBranchVo) == 1) {
                return AddBranchResult.NAME_DUPLICATE;
            } else if(this.branchDao.branchContactOverlapCheck(connection, addBranchVo) == 1) {
                return AddBranchResult.CONTACT_DUPLICATE;
            } else {
                count = this.branchDao.addBranch(connection, addBranchVo);
                if (count == 1) {
                    return AddBranchResult.SUCCESS;
                } else {
                    return AddBranchResult.FAILURE;
                }
            }
        }
    }
}
