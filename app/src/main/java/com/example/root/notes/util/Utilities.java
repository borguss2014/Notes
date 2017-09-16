package com.example.root.notes.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;

import com.example.root.notes.DateTime;
import com.example.root.notes.ElapsedTime;
import com.example.root.notes.model.Note;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


/**
 * TODO: Add a class header comment!
 */

public class Utilities {

    public static boolean saveToFile(FileOutputStream fileOutputStream, Note note)
    {
        if (note == null)
        {
            return false;
        }

        //Persist note data in a binary file
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);

            oos.writeObject(note);

            return true;
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static void createDirectory(String folderPath)
    {
        File folder = new File(folderPath);

        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            Log.d("CREATE DIRECTORY", "FOLDER CREATED");
        } else {
            Log.d("CREATE DIRECTORY", "FOLDER CREATION FAILED");
        }
    }

    public static void deleteAllFiles(Context context)
    {
        File dir = context.getFilesDir();

        for(File file : dir.listFiles())
        {
            file.delete();
        }
    }

    public static void deleteAllFolders(Context context)
    {
        boolean deleted = true;
        FilenameFilter filesFilter = new FilenameFilter()
        {
            public boolean accept(File file, String name)
            {
                return file.isDirectory();
            }
        };

        File dir = context.getFilesDir();

        File[] folders = dir.listFiles(filesFilter);

        for(File folder : folders)
        {
            String[] children = folder.list();
            for (String child : children)
            {
                File file = new File(folder, child);

                if(!file.delete())
                {
                    deleted = false;
                }

                if(!deleted)
                {
                    Log.d("DELETE_ALL_FOLDERS", "Warning: One or more files couldn't be deleted");
                }

                deleted = true;
            }

            if(!folder.delete())
            {
                deleted = false;
            }
        }

        if(!deleted)
        {
            Log.d("DELETE_ALL_FOLDERS", "Warning: One or more folders couldn't be deleted");
        }
    }

    public static boolean deleteFile(String filePath)
    {
        File file = new File(filePath);

        if(file.exists())
        {
            Log.d("FILENAME CHECK", "FOUND FILE, DELETING...");
            return file.delete();
        }
        else
        {
            return false;
        }
    }

    public static void createTestNotes(int nrNotes, String notesDirPath) throws FileNotFoundException
    {
        Note tempNote;
        FileOutputStream fileOutputStream;

        for(int i=0; i<nrNotes; i++)
        {
            String noteUniqueFilename = generateUniqueFilename(Attributes.NOTE_FILE_EXTENSION);

            //tempNote = new Note("test" + Integer.toString(i), "Test note " + Integer.toString(i));
            //tempNote.setFileName(noteUniqueFilename);

            String noteFilePath = notesDirPath.concat(File.separator.concat(noteUniqueFilename));

            fileOutputStream = new FileOutputStream(noteFilePath);

            //Utilities.saveToFile(fileOutputStream, tempNote);
        }
    }

//    public static DateTime getCurrentDateTime(Calendar calendar)
//    {
//        DateTime dateTime = new DateTime();
//        dateTime.setSeconds(calendar.get(Calendar.SECOND));
//        dateTime.setMinute(calendar.get(Calendar.MINUTE));
//        dateTime.setHour(calendar.get(Calendar.HOUR_OF_DAY));
//
//        dateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
//        dateTime.setMonth(calendar.get(Calendar.MONTH) + 1);
//        dateTime.setYear(calendar.get(Calendar.YEAR));
//
//        return dateTime;
//    }

    public static ElapsedTime elapsedTime(String d1, String d2)
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

    public static String generateUniqueFilename(String extension)
    {
        return UUID.randomUUID().toString() + extension;
    }

    public static CharSequence getRelativeTimespanString(Context context, long time, long minResolution, long transitionResolution) {
        long currentTime = new Date().getTime();
        long timeDiff = currentTime - time;

        if (timeDiff < transitionResolution) {
            return DateUtils.getRelativeTimeSpanString(time, currentTime, minResolution);
        } else {
            return DateUtils.formatDateTime(context, time, 0);
        }
    }

//    @TargetApi(Build.VERSION_CODES.O)
//    public static LocalDateTime getCurrentDateTime()
//    {
//        return LocalDateTime.now();
//    }

}