package com.example.root.notes;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.UUID;


/**
 * Created by Spoiala Cristian on 3/21/2017.
 */

class Utilities {

    //bno : binary note
    static final String NOTE_FILE_EXTENSION = ".bno";

    static String MAIN_DATA = "main_data";

    static String TITLE     = "title";
    static String CONTENT   = "content";
    static String FILENAME  = "filename";

    static String CREATION_DATE = "creation_date";
    static String MODIFIED_DATE = "modified_date";

    static final int NEW_NOTE_ACTIVITY_RESULT = 1000;
    static final int OVERWRITE_NOTE_ACTIVITY_RESULT = 1001;
    static final int DELETE_NOTE_ACTIVITY_RESULT = 1002;

    static final int NOTE_ACTIVITY = 1003;


    @TargetApi(Build.VERSION_CODES.N)
    static boolean saveFile(Context context, Note note, boolean overwrite)
    {
        DateTime date = getCurrentDateTime();

        if(overwrite)
        {
            if(note != null)
            {
                note.setLastModifiedDate(date);
            }
            else
            {
                return false;
            }
        }
        else
        {
            if(note != null)
            {
                note.setCreationDate(date);
            }
            else
            {
                return false;
            }
        }

        try
        {
            FileOutputStream fos = context.openFileOutput(note.getFileName(), Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(note);

            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    static ArrayList<Note> loadNotes(Context context)
    {
        File dir = context.getFilesDir();

        ArrayList<Note> notes = new ArrayList<>();

        FilenameFilter filesFilter = new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                return name.endsWith(NOTE_FILE_EXTENSION);
            }
        };

        if(dir.listFiles().length != 0)
        {
            File[] files = dir.listFiles(filesFilter);

            try {
                FileInputStream fis;
                ObjectInputStream ois;

                for (File file : files) {
                    fis = context.openFileInput(file.getName());
                    ois = new ObjectInputStream(fis);

                    Note note = (Note) ois.readObject();
                    Log.d("Utilities-LOAD", note.getFileName());

                    notes.add(note);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(context, "No notes have been written", Toast.LENGTH_SHORT).show();
        }

        return notes;
    }


    static void deleteAllFiles(Context context)
    {
        File dir = context.getFilesDir();

        for(File file : dir.listFiles())
        {
            file.delete();
        }
    }

    static boolean deleteFile(Context context, final String fileName)
    {
        File dir = context.getFilesDir();

        File file = new File(dir + "/" + fileName);

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

    public static void createTestNotes(Context context, int nrNotes)
    {
        String uniqueFilename;

        Note tempNote;
        for(int i=0; i<nrNotes; i++)
        {
            uniqueFilename = generateUniqueFilename(NOTE_FILE_EXTENSION);
            tempNote = new Note("test" + Integer.toString(i), "Test note " + Integer.toString(i));
            tempNote.setFileName(uniqueFilename);
            Utilities.saveFile(context, tempNote, false);
        }
        Toast.makeText(context, "Mock notes loaded", Toast.LENGTH_SHORT).show();
    }

    static DateTime getCurrentDateTime()
    {
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