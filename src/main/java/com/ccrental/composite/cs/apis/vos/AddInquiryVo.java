package com.ccrental.composite.cs.apis.vos;

public class AddInquiryVo {
    private final String title;
    private final String content;
    private final boolean isNormalized;

    public AddInquiryVo(String title, String content) {
        if (title.equals("") || content.equals("")) {
            this.title = null;
            this.content = null;
            this.isNormalized = false;
        } else {
            this.title = title;
            this.content = content;
            this.isNormalized = true;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isNormalized() {
        return isNormalized;
    }
}
