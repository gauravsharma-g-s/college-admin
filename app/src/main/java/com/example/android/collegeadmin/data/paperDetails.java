package com.example.android.collegeadmin.data;

public class paperDetails {
    private String paperName;
    private int paperYear;
    private String paperUrl;

    public paperDetails(String paperName, int paperYear, String paperUrl) {
        this.paperName = paperName;
        this.paperYear = paperYear;
        this.paperUrl = paperUrl;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }


    public int getPaperYear() {
        return paperYear;
    }

    public void setPaperYear(int paperYear) {
        this.paperYear = paperYear;
    }

    public String getPaperUrl() {
        return paperUrl;
    }

    public void setPaperUrl(String paperUrl) {
        this.paperUrl = paperUrl;
    }
}
