package com.example.root.notes;

import java.io.Serializable;

/**
 * Created by ROOT on 3/24/2017.
 */

public class DateTime implements Serializable{
    private int mHour;
    private int mMinute;
    private int mSeconds;

    public DateTime()
    {
        this.mHour      = -1;
        this.mMinute    = -1;
        this.mSeconds   = -1;
    }

    public DateTime(int mHour, int mMinute, int mSeconds) {
        this.mHour      = mHour;
        this.mMinute    = mMinute;
        this.mSeconds   = mSeconds;
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

    @Override
    public String toString() {
        return "DateTime{" +
                "mHour=" + mHour +
                ", mMinute=" + mMinute +
                ", mSeconds=" + mSeconds +
                '}';
    }
}
