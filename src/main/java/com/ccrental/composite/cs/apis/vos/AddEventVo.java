package com.ccrental.composite.cs.apis.vos;

public class AddEventVo {
    private final String title;
    private final String image;
    private final String startDate;
    private final String endDate;
    private final boolean isNormalized;

    public AddEventVo(String title, String image, String startDate, String endDate) {
        if (title.equals("") || image.equals("") || startDate.equals("")) {
            this.title = null;
            this.image = null;
            this.startDate = null;
            this.endDate = null;
            this.isNormalized = false;
        } else {
            this.title = title;
            this.image = image;
            this.startDate = startDate;
            this.endDate = endDate;
            this.isNormalized = true;
        }
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

    public boolean isNormalized() {
        return isNormalized;
    }
}
