package com.ccrental.composite.cs.apis.daos;

import com.ccrental.composite.cs.apis.enums.NoticeResult;
import com.ccrental.composite.cs.apis.vos.AddNoticeVo;
import com.ccrental.composite.cs.apis.vos.GetNoticeVo;
import com.ccrental.composite.cs.apis.vos.ModifyNoticeVo;
import com.ccrental.composite.cs.apis.vos.SearchNoticeVo;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class NoticeDao {
    public int getTotalNotices(Connection connection) throws SQLException {
        int count = 0;
        String query = "SELECT COUNT(`notice_index`) AS `count` FROM `cc_rental`.`notices`";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        }
        return count;
    }

    public ArrayList<GetNoticeVo> getNotices(Connection connection, int page) throws SQLException {
        ArrayList<GetNoticeVo> notices = new ArrayList<>();
        String query = "SELECT `notice_index` AS `index`, " +
                              "`notice_title` AS `title`, " +
                              "`notice_content` AS `content`, " +
                              "`notice_written_at` AS `writtenAt` " +
                              "FROM `cc_rental`.`notices` " +
                              "ORDER BY `notice_index` DESC limit ?,10";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, (page - 1) * 10);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    GetNoticeVo notice = new GetNoticeVo(
                            resultSet.getString("index"),
                            resultSet.getString("title"),
                            resultSet.getString("content"),
                            resultSet.getString("writtenAt"));
                    notices.add(notice);
                }
            }
        }
        return notices;
    }

    public GetNoticeVo readNotice(Connection connection, int index) throws SQLException {
        GetNoticeVo notice = null;
        String query = "SELECT `notice_index` AS `index`, " +
                              "`notice_title` AS `title`, " +
                              "`notice_content` AS `content`, " +
                              "`notice_written_at` AS `writtenAt` " +
                              "FROM `cc_rental`.`notices` WHERE `notice_index`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, index);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    notice = new GetNoticeVo(
                            resultSet.getString("index"),
                            resultSet.getString("title"),
                            resultSet.getString("content"),
                            resultSet.getString("writtenAt")
                    );
                }
            }
        }
        return notice;
    }

    public NoticeResult deleteNotice(Connection connection, int index) throws SQLException {
        NoticeResult noticeResult;
        String query = "DELETE FROM `cc_rental`.`notices` WHERE `notice_index`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, index);
            if (preparedStatement.executeUpdate() == 1) {
                noticeResult = NoticeResult.SUCCESS;
            } else {
                noticeResult = NoticeResult.FAILURE;
            }
        }
        return noticeResult;
    }

    public NoticeResult modifyNotice(Connection connection, ModifyNoticeVo modifyNoticeVo) throws SQLException {
        NoticeResult noticeResult;
        String query = "UPDATE `cc_rental`.`notices` SET `notice_title`=?, `notice_content`=?, `notice_written_at`=NOW() WHERE `notice_index`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, modifyNoticeVo.getTitle());
            preparedStatement.setString(2, modifyNoticeVo.getContent());
            preparedStatement.setInt(3, modifyNoticeVo.getIndex());
            if (preparedStatement.executeUpdate() == 1) {
                noticeResult = NoticeResult.SUCCESS;
            } else {
                noticeResult = NoticeResult.FAILURE;
            }
        }
        return noticeResult;
    }

    public NoticeResult writeNotice(Connection connection, AddNoticeVo addNoticeVo) throws SQLException {
        NoticeResult noticeResult;
        String query = "INSERT INTO `cc_rental`.`notices` (`notice_title`, `notice_content`, `notice_written_at`) " +
                       "VALUES (?, ?, NOW())";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, addNoticeVo.getTitle());
            preparedStatement.setString(2, addNoticeVo.getContent());
            if (preparedStatement.executeUpdate() == 1) {
                noticeResult = NoticeResult.SUCCESS;
            } else {
                noticeResult = NoticeResult.FAILURE;
            }
        }

        return noticeResult;
    }

    public int getSearchAllNotices(Connection connection, SearchNoticeVo searchNoticeVo) throws SQLException {
        int count = 0;
        String query = "SELECT COUNT(`notice_index`) AS `count` FROM `cc_rental`.`notices` WHERE `notice_title` LIKE ? OR `notice_content` LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%"+searchNoticeVo.getKeyword()+"%");
            preparedStatement.setString(2, "%"+searchNoticeVo.getKeyword()+"%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        }
        return count;
    }

    public int getSearchTitleNotices(Connection connection, SearchNoticeVo searchNoticeVo) throws SQLException {
        int count = 0;
        String query = "SELECT COUNT(`notice_index`) AS `count` FROM `cc_rental`.`notices` WHERE `notice_title` LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%"+searchNoticeVo.getKeyword()+"%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        }
        return count;
    }

    public int getSearchContentNotices(Connection connection, SearchNoticeVo searchNoticeVo) throws SQLException {
        int count = 0;
        String query = "SELECT COUNT(`notice_index`) AS `count` FROM `cc_rental`.`notices` WHERE `notice_content` LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%"+searchNoticeVo.getKeyword()+"%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        }
        return count;
    }

    public ArrayList<GetNoticeVo> getAllSearchNotices(Connection connection, SearchNoticeVo searchNoticeVo, int page) throws SQLException {
        ArrayList<GetNoticeVo> notices = new ArrayList<>();
        String query = "SELECT `notice_index` AS `index`, " +
                              "`notice_title` AS `title`, " +
                              "`notice_content` AS `content`, " +
                              "`notice_written_at` AS `writtenAt` " +
                              "FROM `cc_rental`.`notices` " +
                              "WHERE `notice_title` LIKE ? OR `notice_content` LIKE ? " +
                              "ORDER BY `notice_index` DESC limit ?,10";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%"+searchNoticeVo.getKeyword()+"%");
            preparedStatement.setString(2, "%"+searchNoticeVo.getKeyword()+"%");
            preparedStatement.setInt(3, (page - 1) * 10);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    GetNoticeVo notice = new GetNoticeVo(
                            resultSet.getString("index"),
                            resultSet.getString("title"),
                            resultSet.getString("content"),
                            resultSet.getString("writtenAt"));
                    notices.add(notice);
                }
            }
        }
        return notices;
    }

    public ArrayList<GetNoticeVo> getTitleSearchNotices(Connection connection, SearchNoticeVo searchNoticeVo, int page) throws SQLException {
        ArrayList<GetNoticeVo> notices = new ArrayList<>();
        String query = "SELECT `notice_index` AS `index`, " +
                              "`notice_title` AS `title`, " +
                              "`notice_content` AS `content`, " +
                              "`notice_written_at` AS `writtenAt` " +
                              "FROM `cc_rental`.`notices` " +
                              "WHERE `notice_title` LIKE ? " +
                              "ORDER BY `notice_index` DESC limit ?,10";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%"+searchNoticeVo.getKeyword()+"%");
            preparedStatement.setInt(2, (page - 1) * 10);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    GetNoticeVo notice = new GetNoticeVo(
                            resultSet.getString("index"),
                            resultSet.getString("title"),
                            resultSet.getString("content"),
                            resultSet.getString("writtenAt"));
                    notices.add(notice);
                }
            }
        }
        return notices;
    }

    public ArrayList<GetNoticeVo> getContentSearchNotices(Connection connection, SearchNoticeVo searchNoticeVo, int page) throws SQLException {
        ArrayList<GetNoticeVo> notices = new ArrayList<>();
        String query = "SELECT `notice_index` AS `index`, " +
                              "`notice_title` AS `title`, " +
                              "`notice_content` AS `content`, " +
                              "`notice_written_at` AS `writtenAt` " +
                              "FROM `cc_rental`.`notices` " +
                              "WHERE `notice_content` LIKE ? " +
                              "ORDER BY `notice_index` DESC limit ?,10";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%"+searchNoticeVo.getKeyword()+"%");
            preparedStatement.setInt(2, (page - 1) * 10);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    GetNoticeVo notice = new GetNoticeVo(
                            resultSet.getString("index"),
                            resultSet.getString("title"),
                            resultSet.getString("content"),
                            resultSet.getString("writtenAt"));
                    notices.add(notice);
                }
            }
        }
        return notices;
    }
}
