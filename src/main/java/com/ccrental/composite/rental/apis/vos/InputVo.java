package com.ccrental.composite.rental.apis.vos;

import java.sql.Date;
import java.sql.Time;

public class InputVo {
    private final Date fromDate;
    private final Time fromTime;
    private final Date toDate;
    private final Time toTime;
    private final String branchDiv;

    public InputVo(Date fromDate, Time fromTime, Date toDate, Time toTime, String branchDiv) {
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
        this.branchDiv = branchDiv;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Time getFromTime() {
        return fromTime;
    }

    public Date getToDate() {
        return toDate;
    }

    public Time getToTime() {
        return toTime;
    }

    public String getBranchDiv() {
        return branchDiv;
    }
}
