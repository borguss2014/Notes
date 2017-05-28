package com.example.root.notes;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;


/**
 * Created by ROOT on 3/21/2017.
 */

public class Utilities {

    private static String EXTENSION = ".bin";

    public static String MAIN_DATA = "main_data";

    public static String TITLE     = "title";
    public static String CONTENT   = "content";
    public static String FILENAME  = "filename";

    public static String CREATION_DATE = "creation_date";
    public static String MODIFIED_DATE = "modified_date";

    public static String DEBUG     = "DEBUG";

    public static final int DATE_BEFORE = 1000;
    public static final int DATE_EQUAL  = 1001;
    public static final int DATE_AFTER  = 1002;
    public static final int DATE_COMPARE_ERROR  = -1;

    @IntDef({DATE_BEFORE, DATE_EQUAL, DATE_AFTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DateEquality {
    }

    @TargetApi(Build.VERSION_CODES.N)
    public static boolean saveFile(Context context, Note note, boolean overwrite)
    {
        String fileName;

        DateTime date = getCurrentDateTime();

        if(overwrite)
        {
            File dir = context.getFilesDir();

            for(File file : dir.listFiles())
            {
                if(file.getName().equals(note.getFileName()))
                {
                    if(file.delete())
                    {
                        Log.d("FILENAME CHECK", "FILE " + note.getFileName() + "SUCCESSFULLY DELETED");
                    }
                    else
                    {
                        Log.d("FILENAME CHECK", "ERROR: COULDN'T DELETE FILE");
                    }
                    break;
                }
            }

            fileName = note.getFileName();

            note.setLastModifiedDate(date);
        }
        else
        {
            fileName = String.valueOf(note.getDate()) + EXTENSION;

            note.setFileName(fileName);
            note.setCreationDate(date);
        }


        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(note);

            oos.close();
            oos.flush();
            fos.close();

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static ArrayList<Note> loadAllFiles(Context context)
    {
        File dir = context.getFilesDir();
        ArrayList<Note> notes = new ArrayList<>();

        FileInputStream fis;
        ObjectInputStream ois;

        try {
            if(dir.listFiles().length != 0) {
                for (File file : dir.listFiles()) {
                    if (file.getName().endsWith(EXTENSION)) {
                        fis = context.openFileInput(file.getName());
                        ois = new ObjectInputStream(fis);

                        Note note = (Note) ois.readObject();
                        Log.d("LOAD", note.getFileName());

                        notes.add(note);

                        ois.close();
                        fis.close();
                    }
                }
            }
            else
            {
                Toast.makeText(context, "No notes have been written", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return notes;
    }

    public static void deleteAllFiles(Context context)
    {
        File dir = context.getFilesDir();

        for(File file : dir.listFiles())
        {
            file.delete();
        }
    }

    public static boolean deleteFile(Context context, final String fileName)
    {
        File dir = context.getFilesDir();
        boolean fileDeleted = false;

        for(File file : dir.listFiles())
        {
            if(file.getName().equals(fileName))
            {
                Log.d("FILENAME CHECK", "FOUND FILE, DELETING...");
                file.delete();
                fileDeleted = true;
                break;
            }
        }

        return fileDeleted;
    }

    public static DateTime getCurrentDateTime()
    {
//
//        // get the supported ids for GMT-08:00 (Pacific Standard Time)
//        String[] ids = TimeZone.getAvailableIDs(-8 * 60 * 60 * 1000);
//        // if no ids were returned, something is wrong. get out.
//        if (ids.length == 0)
//            System.exit(0);
//
//        // begin output
//        System.out.println("Current Time");
//
//        // create a Pacific Standard Time time zone
//        SimpleTimeZone pdt = new SimpleTimeZone(-8 * 60 * 60 * 1000, ids[0]);
//
//        // set up rules for Daylight Saving Time
//        pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
//        pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);

        // create a GregorianCalendar with the Pacific Daylight time zone
        // and the current date and time
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        Date date = new Date();
        calendar.setTime(date);

        DateTime dateTime = new DateTime();
        dateTime.setSeconds(calendar.get(Calendar.SECOND));
        dateTime.setMinute(calendar.get(Calendar.MINUTE));
        dateTime.setHour(calendar.get(Calendar.HOUR_OF_DAY));

        dateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        dateTime.setMonth(calendar.get(Calendar.MONTH) + 1);
        dateTime.setYear(calendar.get(Calendar.YEAR));

        return dateTime;
    }

    public static ElapsedTime elapsedTime(String d1,String d2)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long difference = 0;

        Date startDate = null;
        Date endDate = null;

        try
        {
            startDate = sdf.parse(d1);
            endDate = sdf.parse(d2);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


        if (endDate != null && startDate != null) {
            difference = endDate.getTime() - startDate.getTime();
        }


        long elapsedDays = difference / daysInMilli;
        difference = difference % daysInMilli;

        long elapsedHours = difference / hoursInMilli;
        difference = difference % hoursInMilli;

        long elapsedMinutes = difference / minutesInMilli;
        difference = difference % minutesInMilli;

        long elapsedSeconds = difference / secondsInMilli;

        return new ElapsedTime(elapsedDays, elapsedHours,
                elapsedMinutes, elapsedSeconds);
    }

}