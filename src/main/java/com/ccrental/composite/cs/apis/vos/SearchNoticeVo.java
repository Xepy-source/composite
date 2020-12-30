package com.ccrental.composite.cs.apis.vos;

public class SearchNoticeVo {
    private final String category;
    private final String keyword;

    public SearchNoticeVo(String category, String keyword) {
        this.category = category;
        this.keyword = keyword;
    }

    public String getCategory() {
        return category;
    }

    public String getKeyword() {
        return keyword;
    }
}
