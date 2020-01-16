package com.example.navigationhaazrai;

public class ViewAttendanceItem {
    private  String mSubject, mTotal, mPresent, mAbsent, mPercent;

    public ViewAttendanceItem(String subject, String total, String present, String absent, String percent)
    {
        mSubject = subject;
        mTotal = total;
        mPresent = present;
        mAbsent = absent;
        mPercent = percent;
    }

    public String getmSubject() {
        return mSubject;
    }

    public String getmTotal() {
        return mTotal;
    }

    public String getmPresent() {
        return mPresent;
    }

    public String getmAbsent() {
        return mAbsent;
    }

    public String getmPercent() {
        return mPercent;
    }

}

