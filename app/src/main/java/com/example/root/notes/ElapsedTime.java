package com.example.root.notes;

/**
 * Created by ROOT on 5/28/2017.
 */

public class ElapsedTime {
    private long elapsedDays;
    private long elapsedHours;
    private long elapsedMinutes;
    private long elapsedSeconds;

    public ElapsedTime(long days, long hours, long minutes, long seconds)
    {
        elapsedDays = days;
        elapsedHours = hours;
        elapsedMinutes = minutes;
        elapsedSeconds = seconds;
    }

    public long getElapsedDays() {
        return elapsedDays;
    }

    public void setElapsedDays(long elapsedDays) {
        this.elapsedDays = elapsedDays;
    }

    public long getElapsedHours() {
        return elapsedHours;
    }

    public void setElapsedHours(long elapsedHours) {
        this.elapsedHours = elapsedHours;
    }

    public long getElapsedMinutes() {
        return elapsedMinutes;
    }

    public void setElapsedMinutes(long elapsedMinutes) {
        this.elapsedMinutes = elapsedMinutes;
    }

    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(long elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }
}
