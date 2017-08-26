package com.example.root.notes.async_tasks.note;

import android.os.AsyncTask;
import android.os.Process;

import com.example.root.notes.NoteDao;
import com.example.root.notes.model.Note;

/**
 * TODO: Add a class header comment!
 */

public class PurgeNotesDBTask extends AsyncTask<Note, Void, Void>
{
    private NoteDao noteDao;

    public PurgeNotesDBTask(NoteDao noteDao)
    {
        this.noteDao = noteDao;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    protected Void doInBackground(Note... note)
    {
        noteDao.deleteAllNotes();
        return null;
    }
}
