package com.android_developer.jaipal.sim;

public class InspectionNoteDataModel {
    public String filename,inspectedBy,time, stationCode, division;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String name) {
        this.filename = name;
    }
    public String getInspectedBy() {
        return inspectedBy;
    }

    public void setInspectedBy(String name) {
        this.inspectedBy = name;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String name) {
        this.time = name;
    }
    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String name) {
        this.stationCode = name;
    }

    public void setDivision(String division) {
        this.division = division;
    }
    public String getDivision() {
        return division;
    }
}
