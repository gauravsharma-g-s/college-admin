package com.example.android.collegeadmin.data;

/*
This class contains the notice information
We can create object of a single notice and updates its info in database

 */

public class noticeInfo {
    private String noticeName;
    private String noticeUrl;
   private String  noticeDate;

    public noticeInfo(){}

    public noticeInfo(String noticeName, String noticeUrl,String noticeDate) {
        this.noticeName = noticeName;
        this.noticeUrl = noticeUrl;
        this.noticeDate = noticeDate;

    }

    public String getNoticeName() {
        return noticeName;
    }

    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName;
    }

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }


}
