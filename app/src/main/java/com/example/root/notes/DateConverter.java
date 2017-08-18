package com.example.root.notes;

import android.annotation.TargetApi;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.os.Build;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * TODO: Add a class header comment!
 */

@TargetApi(Build.VERSION_CODES.O)
public class DateConverter
{
    @TypeConverter
    public static LocalDateTime toDateTime(Long timestamp)
    {
        return timestamp == null ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    @TypeConverter
    public static Long toTimestamp(LocalDateTime date)
    {
        return date == null ? null : date.atZone(ZoneId.systemDefault()).toEpochSecond();
    }
}
