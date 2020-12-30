package com.ccrental.composite.cs.apis.vos;

public class GetNoticeVo {
    private final int index;
    private final String title;
    private final String content;
    private final String written_at;

    public GetNoticeVo(String index, String title, String content, String written_at) {
        int indexNum;
        try {
            indexNum = Integer.parseInt(index);
        } catch (NumberFormatException ignored) {
            indexNum = -1;
        }
        this.index = indexNum;
        this.title = title;
        this.content = content;
        this.written_at = written_at;
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

    public String getWritten_at() {
        return written_at;
    }
}
