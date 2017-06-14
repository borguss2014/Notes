package com.example.root.notes;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.UUID;


/**
 * Created by Spoiala Cristian on 3/21/2017.
 */

class Utilities {

    @TargetApi(Build.VERSION_CODES.N)
    static boolean saveFile(Context context, Note note, String folderName)
    {
        if(note != null) {
            try {

                File dir = new File(context.getFilesDir().toString().concat(File.separator.concat(folderName)));

                FileOutputStream fos = new FileOutputStream(dir.toString().concat(File.separator.concat(note.getFileName())));
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(note);

                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    static void createDirectory(Context context, String notebookName)
    {
        File folder = new File(context.getFilesDir() + File.separator + notebookName);

        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            // Do something on success
            Log.d("CREATE DIRECTORY", "FOLDER CREATED");
        } else {
            // Do something else on failure
            Log.d("CREATE DIRECTORY", "FOLDER CREATION FAILED");
        }
    }

    static void deleteAllFiles(Context context)
    {
        File dir = context.getFilesDir();

        for(File file : dir.listFiles())
        {
            file.delete();
        }
    }

    static void deleteAllFolders(Context context)
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

    static boolean deleteFile(Context context, final String fileName, String folderName)
    {
        File dir = new File(context.getFilesDir().toString().concat(File.separator.concat(folderName)));

        File file = new File(dir.toString().concat(File.separator.concat(fileName)));

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

    static void createTestNotes(Context context, int nrNotes, String folderName)
    {
        Note tempNote;
        for(int i=0; i<nrNotes; i++)
        {
            tempNote = new Note("test" + Integer.toString(i), "Test note " + Integer.toString(i));
            tempNote.setFileName(generateUniqueFilename(Attributes.NOTE_FILE_EXTENSION));
            Utilities.saveFile(context, tempNote, folderName);
        }
    }

    static DateTime getCurrentDateTime()
    {
        Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
        calendar.setTime(new Date());

        DateTime dateTime = new DateTime();
        dateTime.setSeconds(calendar.get(Calendar.SECOND));
        dateTime.setMinute(calendar.get(Calendar.MINUTE));
        dateTime.setHour(calendar.get(Calendar.HOUR_OF_DAY));

        dateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        dateTime.setMonth(calendar.get(Calendar.MONTH) + 1);
        dateTime.setYear(calendar.get(Calendar.YEAR));

        return dateTime;
    }

    static ElapsedTime elapsedTime(String d1,String d2)
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

    static String generateUniqueFilename(String extension)
    {
        return UUID.randomUUID().toString() + extension;
    }

}