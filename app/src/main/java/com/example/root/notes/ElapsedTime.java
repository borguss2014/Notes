package com.example.root.notes;

/**
 * TODO: Add a class header comment!
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

    public boolean isOneMinuteElapsed()
    {
        return !(elapsedSeconds < 60 && elapsedMinutes == 0 && elapsedHours == 0 && elapsedDays == 0);
    }

    public boolean isOneHourElapsed()
    {
        return !(elapsedMinutes < 60 && elapsedHours == 0 && elapsedDays == 0);
    }

    public boolean isOneDayElapsed()
    {
        return !(elapsedHours < 24 && elapsedDays == 0);
    }
}
