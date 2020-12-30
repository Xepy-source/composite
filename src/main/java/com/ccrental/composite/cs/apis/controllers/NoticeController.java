package com.ccrental.composite.cs.apis.controllers;

import com.ccrental.composite.common.utillity.Converter;
import com.ccrental.composite.common.vos.UserVo;
import com.ccrental.composite.cs.apis.enums.NoticeResult;
import com.ccrental.composite.cs.apis.services.NoticeService;
import com.ccrental.composite.cs.apis.vos.AddNoticeVo;
import com.ccrental.composite.cs.apis.vos.GetNoticeVo;
import com.ccrental.composite.cs.apis.vos.ModifyNoticeVo;
import com.ccrental.composite.cs.apis.vos.SearchNoticeVo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/apis/cs/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @RequestMapping(value = "get", method = RequestMethod.POST)
    public void getNotices(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(name = "page", defaultValue = "1") String page) throws SQLException, IOException {
        int requestPage = Converter.stringToInt(page, 1);
        int totalNotices = this.noticeService.getTotalNotices();
        int maxPage = totalNotices % 10 == 0 ? totalNotices / 10 : (int) (Math.floor((double) totalNotices / 10) + 1);
        int startPage = (requestPage > 5) ? (requestPage - 4) : 1;
        int endPage = (maxPage > 9) ? (requestPage + 4) : maxPage;

        ArrayList<GetNoticeVo> notices = this.noticeService.getNotices(requestPage);
        JSONObject resultJSON = new JSONObject();

        if(requestPage < 1 || notices == null) {
            resultJSON.put("result", "failure");
        } else {
            resultJSON.put("result", "success");
            JSONArray dataArray = new JSONArray();

            for (GetNoticeVo notice : notices) {
                JSONObject data = new JSONObject();
                data.put("notice_id", notice.getIndex());
                data.put("title", notice.getTitle());
                data.put("written_at", notice.getWritten_at());
                dataArray.put(data);
            }
            resultJSON.put("notices", dataArray);
            resultJSON.put("request_page", requestPage);
            resultJSON.put("start_page", startPage);
            resultJSON.put("end_page", endPage);
            resultJSON.put("max_page", maxPage);
        }

        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }

    @RequestMapping(value = "read", method = RequestMethod.POST)
    public void readNotice(HttpServletRequest request, HttpServletResponse response,
                           @RequestParam(name = "notice_id", defaultValue = "") String noticeId,
                           @RequestParam(name = "page", defaultValue = "1") String page,
                           @RequestParam(name = "category", defaultValue = "") String category,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword) throws SQLException, IOException {
        int index = Converter.stringToInt(noticeId, 1);

        JSONObject resultJSON = new JSONObject();
        GetNoticeVo notice = this.noticeService.readNotice(index);

        if (index == -1 || notice == null) {
            resultJSON.put("result", "failure");
        } else {
            resultJSON.put("result", "success");
            resultJSON.put("title", notice.getTitle());
            resultJSON.put("content", notice.getContent());
            resultJSON.put("written_at", notice.getWritten_at());
            resultJSON.put("page", page);
            if (!category.equals("") && !keyword.equals("")) {
                resultJSON.put("category", category);
                resultJSON.put("keyword", keyword);
            }
        }

        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public void deleteNotice (HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(name = "notice_id", defaultValue = "") String noticeId) throws SQLException, IOException {
        UserVo userVo = Converter.getUserVo(request);
        int index = Converter.stringToInt(noticeId, 1);

        JSONObject resultJSON = new JSONObject();

        if (userVo == null || userVo.getLevel() != 1) {
            resultJSON.put("result", "not_authority");
        } else {
            NoticeResult noticeResult = this.noticeService.deleteNotice(index);
            resultJSON.put("result", noticeResult.name().toLowerCase());
        }

        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }

    @RequestMapping(value = "modify", method = RequestMethod.POST)
    public void modifyNotice (HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(name = "notice_id", defaultValue = "") String noticeId,
                              @RequestParam(name = "title", defaultValue = "") String title,
                              @RequestParam(name = "content", defaultValue = "") String content ) throws SQLException, IOException {
        UserVo userVo = Converter.getUserVo(request);
        int index = Converter.stringToInt(noticeId, 1);
        ModifyNoticeVo modifyNoticeVo = new ModifyNoticeVo(index, title, content);

        JSONObject resultJSON = new JSONObject();

        if (userVo == null || userVo.getLevel() != 1) {
            resultJSON.put("result", "not_authority");
        } else {
            NoticeResult noticeResult = this.noticeService.modifyNotice(modifyNoticeVo);
            resultJSON.put("result", noticeResult.name().toLowerCase());
        }

        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }

    @RequestMapping(value = "write", method = RequestMethod.POST)
    public void writeNotice (HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(name = "title", defaultValue = "") String title,
                             @RequestParam(name = "content",defaultValue = "") String content) throws SQLException, IOException {
        UserVo userVo = Converter.getUserVo(request);
        AddNoticeVo addNoticeVo = new AddNoticeVo(title, content);
        NoticeResult noticeResult;

        if (userVo == null || userVo.getLevel() != 1) {
            noticeResult = NoticeResult.NOT_AUTHORITY;
        } else if (!addNoticeVo.isNormalized()) {
            noticeResult = NoticeResult.NORMALIZED_FAILURE;
        } else {
            noticeResult = this.noticeService.writeNotice(addNoticeVo);
        }

        JSONObject resultJSON = new JSONObject();
        resultJSON.put("result", noticeResult.name().toLowerCase());
        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public void searchNotice (HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(name = "category", defaultValue = "all") String category,
                              @RequestParam(name = "keyword", defaultValue = "") String keyword,
                              @RequestParam(name = "page", defaultValue = "1") String page) throws SQLException, IOException {
        SearchNoticeVo searchNoticeVo = new SearchNoticeVo(category, keyword);
        int requestPage = Converter.stringToInt(page, 1);
        int totalNotices = this.noticeService.getSearchTotalNotices(searchNoticeVo);
        int maxPage = totalNotices % 10 == 0 ? totalNotices / 10 : (int) (Math.floor((double) totalNotices / 10) + 1);
        int startPage = (requestPage > 5) ? (requestPage - 4) : 1;
        int endPage = (maxPage > 9) ? (requestPage + 4) : maxPage;

        ArrayList<GetNoticeVo> notices = this.noticeService.getSearchNotices(searchNoticeVo, requestPage);
        JSONObject resultJSON = new JSONObject();

        if(requestPage < 1 || notices == null) {
            resultJSON.put("result", "failure");
        } else if (maxPage == 0) {
            resultJSON.put("result", "no_result");
        }else {
            resultJSON.put("result", "success");
            JSONArray dataArray = new JSONArray();

            for (GetNoticeVo notice : notices) {
                JSONObject data = new JSONObject();
                data.put("notice_id", notice.getIndex());
                data.put("title", notice.getTitle());
                data.put("written_at", notice.getWritten_at());
                dataArray.put(data);
            }
            resultJSON.put("notices", dataArray);
            resultJSON.put("request_page", requestPage);
            resultJSON.put("start_page", startPage);
            resultJSON.put("end_page", endPage);
            resultJSON.put("max_page", maxPage);
            if (!category.equals("") && !keyword.equals("")) {
                resultJSON.put("category", category);
                resultJSON.put("keyword", keyword);
            }
        }

        response.getWriter().print(resultJSON);
        response.getWriter().close();
    }
}
