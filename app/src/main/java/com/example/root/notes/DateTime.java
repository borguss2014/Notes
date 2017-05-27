package com.example.root.notes;

import java.io.Serializable;

/**
 * Created by ROOT on 3/24/2017.
 */

public class DateTime implements Serializable{
    private int mHour;
    private int mMinute;
    private int mSeconds;

    private int mDay;
    private int mMonth;
    private int mYear;

    public DateTime()
    {
        this.mHour      = -1;
        this.mMinute    = -1;
        this.mSeconds   = -1;

        this.mDay       = -1;
        this.mMonth     = -1;
        this.mYear      = -1;
    }

    public int getHour() {
        return mHour;
    }

    public void setHour(int mHour) {
        this.mHour = mHour;
    }

    public int getMinute() {
        return mMinute;
    }

    public void setMinute(int mMinute) {
        this.mMinute = mMinute;
    }

    public int getSeconds() {
        return mSeconds;
    }

    public void setSeconds(int mSeconds) {
        this.mSeconds = mSeconds;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int day) {
        this.mDay = day;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        this.mMonth = month;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        this.mYear = year;
    }

    public boolean isDateSet()
    {
        if(mDay == -1 && mMonth == -1 && mYear == -1)
        {
            return false;
        }
        return true;
    }

    public boolean isTimeSet()
    {
        if(mHour == -1 && mMinute == -1 && mSeconds == -1)
        {
            return false;
        }
        return true;
    }

    public String getDate()
    {
        return mMonth + "/" + mDay + "/" + mYear;
    }

    public String getTime()
    {
        return mHour + ":" + mMinute + ":" + mSeconds;
    }

    public String getDateTime()
    {
        return mMonth + "/" + mDay + "/" + mYear + " " +
                mHour + ":" + mMinute + ":" + mSeconds;
    }

    @Override
    public String toString() {
        return "DateTime{" +
                "[Hour=" + mHour + ", Minutes=" + mMinute + ", Seconds=" + mSeconds + "]," +
                "[Month=" + mMonth + ", Days=" + mDay + ", Year=" + mYear + "]" +
                "}";
    }
}