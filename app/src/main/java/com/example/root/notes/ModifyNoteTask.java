package com.example.root.notes;

import android.os.AsyncTask;
import android.os.Handler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Spoiala Cristian on 6/2/2017.
 */

class ModifyNoteTask extends AsyncTask<String, String, Void>
{
    private Note            note;
    private Handler         handler;
    private String          notePath;
    private ArrayList<Note> notesList;
    private int             clickedNotePosition;

    ModifyNoteTask(Note note, String notePath, int clickedNotePosition)
    {
        this.note                   = note;
        this.notePath               = notePath;
        this.clickedNotePosition    = clickedNotePosition;
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
        if(note == null || clickedNotePosition == Attributes.NO_NOTE_CLICKED)
        {
            return null;
        }

        if(notesList != null)
        {
            //Replace the currently clicked note with the modified note in the
            //adapter's list and resort the list with the current comparator
            notesList.remove(notesList.get(clickedNotePosition));
            notesList.add(note);

            Collections.sort(notesList, Comparison.getCurrentComparator());
        }

        try
        {
            //Save the modified note to persistent storage
            Utilities.saveToFile(new FileOutputStream(notePath), note);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        if(handler != null)
        {
            //Signal the handler that a note has been modified
            handler.sendEmptyMessage(Attributes.HandlerMessageType.HANDLER_MESSAGE_NOTE_MODIFIED);
        }

        return null;
    }

    void setHandler(Handler handler)
    {
        this.handler = handler;
    }

    void setNotesList(ArrayList<Note> notesList)
    {
        this.notesList = notesList;
    }
}
