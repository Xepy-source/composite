package com.ccrental.composite.cs.apis.vos;

public class ModifyEventVo {
    private final int index;
    private final String title;
    private final String image;
    private final String startDate;
    private final String endDate;

    public ModifyEventVo(int index, String title, String image, String startDate, String endDate) {
        this.index = index;
        this.title = title;
        this.image = image;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
