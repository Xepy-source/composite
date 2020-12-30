package com.ccrental.composite.cs.apis.controllers;

import com.ccrental.composite.common.utillity.Converter;
import com.ccrental.composite.common.vos.UserVo;
import com.ccrental.composite.cs.apis.enums.AddInquiryResult;
import com.ccrental.composite.cs.apis.enums.InquiryAnswerResult;
import com.ccrental.composite.cs.apis.services.InquiryService;
import com.ccrental.composite.cs.apis.vos.AddInquiryVo;
import com.ccrental.composite.cs.apis.vos.GetInquiryVo;
import com.ccrental.composite.cs.apis.vos.InquiryAnswerVo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/apis/cs/inquiry")
public class InquiryController {
    private final InquiryService inquiryService;

    @Autowired
    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @RequestMapping(value = "get", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void getInquiry(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        UserVo userVo = Converter.getUserVo(request);
        ArrayList<GetInquiryVo> inquirys = this.inquiryService.getInquiryAll(userVo);
        JSONObject resultJSON = new JSONObject();

        if (userVo == null) {
            resultJSON.put("result", "not_authority");
        } else if (inquirys == null) {
            resultJSON.put("result", "failure");
        } else {
            resultJSON.put("result", "success");
            JSONArray dataArray = new JSONArray();

            for (GetInquiryVo inquiry : inquirys) {
                JSONObject data = new JSONObject();
                data.put("inquiry_id", inquiry.getIndex());
                data.put("title", inquiry.getTitle());
                data.put("date", inquiry.getDate());
                data.put("content", inquiry.getContent());
                if(inquiry.getStatus().equals("PND")) {
                    data.put("status", "답변 대기중");
                } else {
                    data.put("status", "답변 완료");
                }

                JSONArray answerDataArray = new JSONArray();
                JSONObject answerData = new JSONObject();
                answerData.put("answerContent", inquiry.getAnswerContent());
                answerData.put("answerDate", inquiry.getAnswerDate());
                answerDataArray.put(answerData);

                data.put("answer", answerDataArray);
                dataArray.put(data);
            }
            resultJSON.put("inquirys", dataArray);
        }

        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public void addInquiry(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(name = "title", defaultValue = "") String title,
                           @RequestParam(name = "content", defaultValue = "") String content) throws SQLException, IOException {
        UserVo userVo = Converter.getUserVo(request);

        AddInquiryVo addInquiryVo = new AddInquiryVo(title, content);
        AddInquiryResult addInquiryResult;
        if (userVo == null) {
            addInquiryResult = AddInquiryResult.NOT_AUTHORITY;
        } else if (!addInquiryVo.isNormalized()) {
            addInquiryResult = AddInquiryResult.FAILURE;
        } else {
            addInquiryResult = this.inquiryService.addInquiry(userVo, addInquiryVo);
        }

        JSONObject resultJSON = new JSONObject();
        resultJSON.put("result", addInquiryResult.name().toLowerCase());
        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }

    @RequestMapping(value = "answer", method = RequestMethod.POST)
    public void inquiryAnswer(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(name = "inquiry_id", defaultValue = "") String id,
                              @RequestParam(name = "content", defaultValue = "") String content) throws SQLException, IOException {
        UserVo userVo = Converter.getUserVo(request);
        InquiryAnswerVo inquiryAnswerVo = new InquiryAnswerVo(id, content);
        InquiryAnswerResult inquiryAnswerResult;

        if (userVo == null) {
            inquiryAnswerResult = InquiryAnswerResult.NOT_AUTHORITY;
        } else if (userVo.getLevel() != 1) {
            inquiryAnswerResult = InquiryAnswerResult.NOT_AUTHORITY;
        } else {
            inquiryAnswerResult = this.inquiryService.inquiryAnswer(inquiryAnswerVo);
        }

        JSONObject resultJSON = new JSONObject();
        resultJSON.put("result", inquiryAnswerResult.name().toLowerCase());
        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }
}