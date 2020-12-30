package com.ccrental.composite.cs.apis.services;

import com.ccrental.composite.cs.apis.daos.EventDao;
import com.ccrental.composite.cs.apis.enums.EventResult;
import com.ccrental.composite.cs.apis.vos.AddEventVo;
import com.ccrental.composite.cs.apis.vos.GetEventVo;
import com.ccrental.composite.cs.apis.vos.ModifyEventVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class EventService {
    private final DataSource dataSource;
    private final EventDao EventDao;

    @Autowired
    public EventService(DataSource dataSource, EventDao EventDao) {
        this.dataSource = dataSource;
        this.EventDao = EventDao;
    }

    public byte[] getEventImage(int index) throws SQLException, IOException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.EventDao.getEventImage(connection, index);
        }
    }

    public ArrayList<GetEventVo> getEvents() throws SQLException, IOException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.EventDao.getEvents(connection);
        }
    }

    public EventResult deleteEvent(int index) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.EventDao.deleteEvent(connection, index);
        }
    }

    public EventResult modifyEvent(ModifyEventVo modifyEventVo) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.EventDao.modifyEvent(connection, modifyEventVo);
        }
    }

    public EventResult writeEvent(AddEventVo addEventVo) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {
            return this.EventDao.writeEvent(connection, addEventVo);
        }
    }
}
