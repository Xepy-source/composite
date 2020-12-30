package com.ccrental.composite.cs.apis.services;

import com.ccrental.composite.cs.apis.daos.NoticeDao;
import com.ccrental.composite.cs.apis.enums.NoticeResult;
import com.ccrental.composite.cs.apis.vos.AddNoticeVo;
import com.ccrental.composite.cs.apis.vos.GetNoticeVo;
import com.ccrental.composite.cs.apis.vos.ModifyNoticeVo;
import com.ccrental.composite.cs.apis.vos.SearchNoticeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class NoticeService {
    private final DataSource dataSource;
    private final NoticeDao noticeDao;

    @Autowired
    public NoticeService(DataSource dataSource, NoticeDao noticeDao) {
        this.dataSource = dataSource;
        this.noticeDao = noticeDao;
    }

    public int getTotalNotices() throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.noticeDao.getTotalNotices(connection);
        }
    }

    public ArrayList<GetNoticeVo> getNotices(int page) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.noticeDao.getNotices(connection, page);
        }
    }

    public GetNoticeVo readNotice(int index) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.noticeDao.readNotice(connection, index);
        }
    }

    public NoticeResult deleteNotice(int index) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.noticeDao.deleteNotice(connection, index);
        }
    }

    public NoticeResult modifyNotice(ModifyNoticeVo modifyNoticeVo) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.noticeDao.modifyNotice(connection, modifyNoticeVo);
        }
    }

    public NoticeResult writeNotice(AddNoticeVo addNoticeVo) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.noticeDao.writeNotice(connection, addNoticeVo);
        }
    }

    public int getSearchTotalNotices(SearchNoticeVo searchNoticeVo) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            if (searchNoticeVo.getCategory().equals("all")) {
                return this.noticeDao.getSearchAllNotices(connection, searchNoticeVo);
            } else if (searchNoticeVo.getCategory().equals("title")) {
                return this.noticeDao.getSearchTitleNotices(connection, searchNoticeVo);
            } else if (searchNoticeVo.getCategory().equals("content")) {
                return this.noticeDao.getSearchContentNotices(connection, searchNoticeVo);
            } else {
                return 0;
            }
        }
    }

    public ArrayList<GetNoticeVo> getSearchNotices(SearchNoticeVo searchNoticeVo, int page) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            if (searchNoticeVo.getCategory().equals("all")) {
                return this.noticeDao.getAllSearchNotices(connection, searchNoticeVo, page);
            } else if (searchNoticeVo.getCategory().equals("title")) {
                return this.noticeDao.getTitleSearchNotices(connection, searchNoticeVo, page);
            } else if (searchNoticeVo.getCategory().equals("content")) {
                return this.noticeDao.getContentSearchNotices(connection, searchNoticeVo, page);
            } else {
                return null;
            }
        }
    }
}
