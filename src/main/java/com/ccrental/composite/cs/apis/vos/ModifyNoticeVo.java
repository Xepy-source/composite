package com.ccrental.composite.cs.apis.vos;

public class ModifyNoticeVo {
    private final int index;
    private final String title;
    private final String content;

    public ModifyNoticeVo(int index, String title, String content) {
        this.index = index;
        this.title = title;
        this.content = content;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
