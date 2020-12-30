package com.ccrental.composite.cs.apis.services;

import com.ccrental.composite.common.vos.UserVo;
import com.ccrental.composite.cs.apis.daos.InquiryDao;
import com.ccrental.composite.cs.apis.enums.AddInquiryResult;
import com.ccrental.composite.cs.apis.enums.InquiryAnswerResult;
import com.ccrental.composite.cs.apis.vos.AddInquiryVo;
import com.ccrental.composite.cs.apis.vos.GetInquiryVo;
import com.ccrental.composite.cs.apis.vos.InquiryAnswerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class InquiryService {
    private final DataSource dataSource;
    private final InquiryDao inquiryDao;

    @Autowired
    public InquiryService(DataSource dataSource, InquiryDao inquiryDao) {
        this.dataSource = dataSource;
        this.inquiryDao = inquiryDao;
    }

    public AddInquiryResult addInquiry(UserVo userVo, AddInquiryVo addInquiryVo) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
             return this.inquiryDao.addInquiry(connection, userVo, addInquiryVo);
        }
    }

    public ArrayList<GetInquiryVo> getInquiryAll (UserVo userVo) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.inquiryDao.getInquiryAll(connection, userVo);
        }
    }

    public InquiryAnswerResult inquiryAnswer (InquiryAnswerVo inquiryAnswerVo) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.inquiryDao.updateInquiryAnswer(connection, inquiryAnswerVo);
        }
    }
}
