package com.ccrental.composite.cs.apis.daos;

import com.ccrental.composite.cs.apis.enums.EventResult;
import com.ccrental.composite.cs.apis.vos.AddEventVo;
import com.ccrental.composite.cs.apis.vos.GetEventVo;
import com.ccrental.composite.cs.apis.vos.ModifyEventVo;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class EventDao {
    public byte[] getEventImage(Connection connection, int index) throws SQLException, IOException {
        byte[] resultImage = null;
        String query = "SELECT `event_image` AS `image` FROM `cc_rental`.`events` WHERE `event_index` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, index);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String imageData = resultSet.getString("image").split(",")[1];
                    byte[] imageBytes = DatatypeConverter.parseBase64Binary(imageData);
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(image, "jpg", byteArrayOutputStream);
                    resultImage = byteArrayOutputStream.toByteArray();
                }
            }
        }
        return resultImage;
    }

    public ArrayList<GetEventVo> getEvents(Connection connection) throws SQLException, IOException {
        ArrayList<GetEventVo> notices = new ArrayList<>();
        String query = "SELECT `event_index` AS `index`, " +
                                "`event_title` AS `title`, " +
                                "`event_written_at` AS `writtenAt`," +
                                "`event_start_date` AS `startDate`," +
                                "`event_end_date` AS `endDate` " +
                                "FROM `cc_rental`.`events` " +
                                "ORDER BY `event_index` DESC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    GetEventVo event = new GetEventVo(
                            resultSet.getString("index"),
                            resultSet.getString("title"),
                            resultSet.getString("writtenAt"),
                            resultSet.getString("startDate"),
                            resultSet.getString("endDate"));
                    notices.add(event);
                }
            }
        }
        return notices;
    }

    public EventResult deleteEvent(Connection connection, int index) throws SQLException {
        EventResult eventResult;
        String query = "DELETE FROM `cc_rental`.`events` WHERE `event_index`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, index);
            if (preparedStatement.executeUpdate() == 1) {
                eventResult = EventResult.SUCCESS;
            } else {
                eventResult = EventResult.FAILURE;
            }
        }
        return eventResult;
    }

    public EventResult modifyEvent(Connection connection, ModifyEventVo modifyEventVo) throws SQLException {
        EventResult eventResult;
        String query = "UPDATE `cc_rental`.`events` SET `event_title`=?, `event_image`=?, `event_written_at`=NOW(), `event_start_date`=?, `event_end_date`=? WHERE `event_index`=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, modifyEventVo.getTitle());
            preparedStatement.setString(2, modifyEventVo.getImage());
            preparedStatement.setString(3, modifyEventVo.getStartDate());
            preparedStatement.setString(4, modifyEventVo.getEndDate());
            preparedStatement.setInt(5, modifyEventVo.getIndex());
            if (preparedStatement.executeUpdate() == 1) {
                eventResult = EventResult.SUCCESS;
            } else {
                eventResult = EventResult.FAILURE;
            }
        }
        return eventResult;
    }

    public EventResult writeEvent(Connection connection, AddEventVo addeventVo) throws SQLException {
        EventResult eventResult;
        String query = "INSERT INTO `cc_rental`.`events` (`event_title`, `event_image`, `event_written_at`, `event_start_date`, `event_end_date`) " +
                "VALUES (?, ?, NOW(), ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, addeventVo.getTitle());
            preparedStatement.setString(2, addeventVo.getImage());
            preparedStatement.setString(3, addeventVo.getStartDate());
            preparedStatement.setString(4, addeventVo.getEndDate());
            if (preparedStatement.executeUpdate() == 1) {
                eventResult = EventResult.SUCCESS;
            } else {
                eventResult = EventResult.FAILURE;
            }
        }

        return eventResult;
    }
}
