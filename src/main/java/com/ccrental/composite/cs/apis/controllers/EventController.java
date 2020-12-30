package com.ccrental.composite.cs.apis.controllers;

import com.ccrental.composite.common.utillity.Converter;
import com.ccrental.composite.common.vos.UserVo;
import com.ccrental.composite.cs.apis.enums.EventResult;
import com.ccrental.composite.cs.apis.services.EventService;
import com.ccrental.composite.cs.apis.vos.AddEventVo;
import com.ccrental.composite.cs.apis.vos.GetEventVo;
import com.ccrental.composite.cs.apis.vos.ModifyEventVo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/apis/cs/event")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(value = "get_image", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] getEventImage(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(name = "event_id", defaultValue = "") String eventId) throws SQLException, IOException {
        int index = Converter.stringToInt(eventId, 1);
        if (index > 0) {
            return this.eventService.getEventImage(index);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "get", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void getEvent(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ParseException {

        ArrayList<GetEventVo> events = this.eventService.getEvents();
        JSONObject resultJSON = new JSONObject();

        if(events == null) {
            resultJSON.put("result", "failure");
        } else {
            resultJSON.put("result", "success");
            JSONArray dataArray = new JSONArray();

            for (GetEventVo event : events) {
                JSONObject data = new JSONObject();
                data.put("event_id", event.getIndex());
                data.put("title", event.getTitle());
                data.put("image", "/apis/cs/event/get_image?event_id="+event.getIndex());
                data.put("written_at", event.getWritten_at());
                data.put("start_date", event.getStartDate());
                data.put("end_date", event.getEndDate());

                Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(event.getEndDate());
                Date nowDate = new Date();
                long diff = nowDate.getTime() - endDate.getTime();
                long decimalDay = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                data.put("decimal_day", decimalDay);
                dataArray.put(data);
            }
            resultJSON.put("events", dataArray);
        }

        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteEvent(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(name = "event_id", defaultValue = "") String eventId) throws SQLException, IOException {
        UserVo userVo = Converter.getUserVo(request);
        int index = Converter.stringToInt(eventId, 1);

        JSONObject resultJSON = new JSONObject();

        if (userVo == null || userVo.getLevel() != 1) {
            resultJSON.put("result", "not_authority");
        } else {
            EventResult eventResult = this.eventService.deleteEvent(index);
            resultJSON.put("result", eventResult.name().toLowerCase());
        }

        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }

    @RequestMapping(value = "modify", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void modifyEvent(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(name = "event_id", defaultValue = "") String eventId,
                            @RequestParam(name = "title", defaultValue = "") String title,
                            @RequestParam(name = "image", defaultValue = "") MultipartFile image,
                            @RequestParam(name = "start_date", defaultValue = "") String startDate,
                            @RequestParam(name = "end_date", defaultValue = "") String endDate) throws SQLException, IOException {
        UserVo userVo = Converter.getUserVo(request);
        int index = Converter.stringToInt(eventId, 1);
        String imageData = Converter.imageToString(image);
        ModifyEventVo modifyEventVo = new ModifyEventVo(index, title, imageData, startDate, endDate);

        JSONObject resultJSON = new JSONObject();

        if (userVo == null || userVo.getLevel() != 1) {
            resultJSON.put("result", "not_authority");
        } else {
            EventResult EventResult = this.eventService.modifyEvent(modifyEventVo);
            resultJSON.put("result", EventResult.name().toLowerCase());
        }

        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }

    @RequestMapping(value = "write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void writeEvent(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(name = "title", defaultValue = "") String title,
                           @RequestParam(name = "image", defaultValue = "") MultipartFile image,
                           @RequestParam(name = "start_date", defaultValue = "") String startDate,
                           @RequestParam(name = "end_date", defaultValue = "") String endDate) throws SQLException, IOException {
        UserVo userVo = Converter.getUserVo(request);
        String imageData = Converter.imageToString(image);
        AddEventVo addEventVo = new AddEventVo(title, imageData, startDate, endDate);
        EventResult eventResult;

        if (userVo == null || userVo.getLevel() != 1) {
            eventResult = EventResult.NOT_AUTHORITY;
        } else if (!addEventVo.isNormalized()) {
            eventResult = EventResult.NORMALIZED_FAILURE;
        } else {
            eventResult = this.eventService.writeEvent(addEventVo);
        }

        JSONObject resultJSON = new JSONObject();
        resultJSON.put("result", eventResult.name().toLowerCase());
        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }
}
