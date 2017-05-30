package com.example.root.notes;

/**
 * Created by ROOT on 5/28/2017.
 */

class ElapsedTime {
    private long elapsedDays;
    private long elapsedHours;
    private long elapsedMinutes;
    private long elapsedSeconds;

    ElapsedTime(long days, long hours, long minutes, long seconds)
    {
        elapsedDays = days;
        elapsedHours = hours;
        elapsedMinutes = minutes;
        elapsedSeconds = seconds;
    }

    long getElapsedDays() {
        return elapsedDays;
    }

    public void setElapsedDays(long elapsedDays) {
        this.elapsedDays = elapsedDays;
    }

    long getElapsedHours() {
        return elapsedHours;
    }

    public void setElapsedHours(long elapsedHours) {
        this.elapsedHours = elapsedHours;
    }

    long getElapsedMinutes() {
        return elapsedMinutes;
    }

    public void setElapsedMinutes(long elapsedMinutes) {
        this.elapsedMinutes = elapsedMinutes;
    }

    long getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(long elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    boolean isOneMinuteElapsed()
    {
        return !(elapsedSeconds < 60 && elapsedMinutes == 0 && elapsedHours == 0 && elapsedDays == 0);
    }

    boolean isOneHourElapsed()
    {
        return !(elapsedMinutes < 60 && elapsedHours == 0 && elapsedDays == 0);
    }

    boolean isOneDayElapsed()
    {
        return !(elapsedHours < 24 && elapsedDays == 0);
    }
}
