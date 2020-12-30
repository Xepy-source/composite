package com.ccrental.composite.cs.apis.vos;

public class InquiryAnswerVo {
    private final int id;
    private final String answerContent;

    public InquiryAnswerVo(String id, String answerContent) {
        int idNum;
        try {
            idNum = Integer.parseInt(id);
        } catch (NumberFormatException ignored) {
            idNum = -1;
        }
        this.id = idNum;
        this.answerContent = answerContent;
    }

    public int getId() {
        return id;
    }

    public String getAnswerContent() {
        return answerContent;
    }
}
