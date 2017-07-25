package com.example.root.notes.async_tasks.note;

import android.os.AsyncTask;
import android.os.Handler;

import com.example.root.notes.util.Attributes;
import com.example.root.notes.util.Comparison;
import com.example.root.notes.Note;
import com.example.root.notes.util.Utilities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * TODO: Add a class header comment!
 */

public class AddNoteFileTask extends AsyncTask<String, String, Void>
{
    private Note note;
    private Handler         handler;
    private String          notePath;
    private ArrayList<Note> notesList;

    public AddNoteFileTask(Note note, String notePath)
    {
        this.note       = note;
        this.notePath   = notePath;
    }

    @Override
    protected void onPreExecute()
    {
        //Set thread priority as background so it
        // won't fight with the UI thread for resources
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    protected Void doInBackground(String... params)
    {
        if(note == null)
        {
            return null;
        }

        if(notesList != null)
        {
            //Add the note to the adapter's list
            //and resort the list with the current comparator
            notesList.add(note);
            Collections.sort(notesList, Comparison.getCurrentComparator());
        }

        try
        {
            //Save the new note to persistent storage
            Utilities.saveToFile(new FileOutputStream(notePath), note);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        if(handler != null)
        {
            //Signal the handler that a note has been added
            handler.sendEmptyMessage(Attributes.HandlerMessageType.HANDLER_MESSAGE_NOTE_ADDED);
        }

        return null;
    }

    public void setHandler(Handler handler)
    {
        this.handler = handler;
    }

    public void setNotesList(ArrayList<Note> notesList)
    {
        this.notesList = notesList;
    }
}