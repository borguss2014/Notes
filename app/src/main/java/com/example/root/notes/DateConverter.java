package com.example.root.notes;

import android.annotation.TargetApi;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * TODO: Add a class header comment!
 */

public class DateConverter
{
    @TypeConverter
    public static Date toDateTime(Long timestamp)
    {
        if(timestamp == null)
        {
            return null;
        }
        else
        {
            return new Date(timestamp);
        }

        //return timestamp == null ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    @TypeConverter
    public static Long toTimestamp(Date date)
    {
        if(date == null)
        {
            return null;
        }
        else
        {
            return date.getTime();
        }

        //return date == null ? null : date.atZone(ZoneId.systemDefault()).toEpochSecond();
    }
}
