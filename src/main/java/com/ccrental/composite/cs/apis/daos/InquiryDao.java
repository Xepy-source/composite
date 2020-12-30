package com.ccrental.composite.cs.apis.daos;

import com.ccrental.composite.common.vos.UserVo;
import com.ccrental.composite.cs.apis.enums.AddInquiryResult;
import com.ccrental.composite.cs.apis.enums.InquiryAnswerResult;
import com.ccrental.composite.cs.apis.vos.AddInquiryVo;
import com.ccrental.composite.cs.apis.vos.GetInquiryVo;
import com.ccrental.composite.cs.apis.vos.InquiryAnswerVo;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class InquiryDao {
    public AddInquiryResult addInquiry(Connection connection, UserVo userVo, AddInquiryVo addInquiryVo) throws SQLException {
        String query = "INSERT INTO `cc_rental`.`inquirys` (`user_index`, `inquiry_title`, `inquiry_date`, `inquiry_content`) " +
                "       VALUES (?, ?, NOW(), ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, userVo.getIndex());
            preparedStatement.setString(2, addInquiryVo.getTitle());
            preparedStatement.setString(3, addInquiryVo.getContent());
            if(preparedStatement.executeUpdate() == 1){
                return AddInquiryResult.SUCCESS;
            } else {
                return AddInquiryResult.FAILURE;
            }
        }
    }

    public ArrayList<GetInquiryVo> getInquiryAll (Connection connection, UserVo userVo) throws SQLException {
        ArrayList<GetInquiryVo> inquirys = new ArrayList<>();
        String query = "SELECT  `inquiry_index` AS `index`, " +
                "               `inquiry_title` AS `title`, " +
                "               `inquiry_date` AS `date`, " +
                "               `inquiry_content` AS `content`, " +
                "               `inquiry_status` AS `status`, " +
                "               `inquiry_answer_content` AS `answerContent`, " +
                "               `inquiry_answer_date` AS `answerDate`" +
                "       FROM `cc_rental`.`inquirys` " +
                "       WHERE `user_index`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userVo.getIndex());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    GetInquiryVo getInquiryVo = new GetInquiryVo(
                            resultSet.getInt("index"),
                            resultSet.getString("title"),
                            resultSet.getString("date"),
                            resultSet.getString("content"),
                            resultSet.getString("status"),
                            resultSet.getString("answerContent"),
                            resultSet.getString("answerDate"));
                    inquirys.add(getInquiryVo);
                }
            }
        }
        return inquirys;
    }

    public InquiryAnswerResult updateInquiryAnswer(Connection connection, InquiryAnswerVo inquiryAnswerVo) throws SQLException {
        String query = "UPDATE `cc_rental`.`inquirys` " +
                "SET `inquiry_status`='DON', `inquiry_answer_content`=?, `inquiry_answer_date`=NOW() " +
                "WHERE `inquiry_index`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, inquiryAnswerVo.getAnswerContent());
            preparedStatement.setInt(2, inquiryAnswerVo.getId());
            if (preparedStatement.executeUpdate() == 1) {
                return InquiryAnswerResult.SUCCESS;
            } else {
                return InquiryAnswerResult.FAILURE;
            }
        }
    }
}
