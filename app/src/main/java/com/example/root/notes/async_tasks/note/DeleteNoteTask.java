package com.example.root.notes.async_tasks.note;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.example.root.notes.util.Attributes;
import com.example.root.notes.Note;
import com.example.root.notes.util.Utilities;

import java.util.ArrayList;

/**
 * TODO: Add a class header comment!
 */

public class DeleteNoteTask extends AsyncTask<String, String, Void>
{
    private Handler         handler;
    private String          notePath;
    private ArrayList<Note> notesList;
    private int             clickedNotePosition;

    public DeleteNoteTask(String notePath, int clickedNotePosition)
    {
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
        if(clickedNotePosition == Attributes.NO_NOTE_CLICKED)
        {
            return null;
        }

        Note toBeDeletedNote = notesList.get(clickedNotePosition);

        if(notesList != null)
        {
            notesList.remove(toBeDeletedNote);
        }

        boolean isDeleted = Utilities.deleteFile(notePath);

        if(handler != null)
        {
            Message message = handler.obtainMessage();
            message.what = Attributes.HandlerMessageType.HANDLER_MESSAGE_NOTE_DELETED;
            message.obj = isDeleted;

            handler.sendMessage(message);
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
