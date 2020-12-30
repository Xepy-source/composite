package com.ccrental.composite.cs.apis.vos;

public class GetEventVo {
    private final int index;
    private final String title;
    private final String written_at;
    private final String startDate;
    private final String endDate;

    public GetEventVo(String index, String title, String written_at, String startDate, String endDate) {
        int indexNum;
        try {
            indexNum = Integer.parseInt(index);
        } catch (NumberFormatException ignored) {
            indexNum = -1;
        }
        this.index = indexNum;
        this.title = title;
        this.written_at = written_at;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getWritten_at() {
        return written_at;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
