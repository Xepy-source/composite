package com.ccrental.composite.cs.apis.vos;

public class GetInquiryVo {
    private final int index;
    private final String title;
    private final String date;
    private final String content;
    private final String status;
    private final String answerContent;
    private final String answerDate;

    public GetInquiryVo(int index, String title, String date, String content, String status, String answerContent, String answerDate) {
        this.index = index;
        this.title = title;
        this.date = date;
        this.content = content;
        this.status = status;
        this.answerContent = answerContent;
        this.answerDate = answerDate;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public String getAnswerDate() {
        return answerDate;
    }
}