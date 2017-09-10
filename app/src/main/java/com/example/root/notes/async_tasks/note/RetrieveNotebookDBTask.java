package com.example.root.notes.async_tasks.note;

import android.os.AsyncTask;
import android.util.Log;

import com.example.root.notes.NoteDao;
import com.example.root.notes.model.Notebook;

/**
 * TODO: Add a class header comment!
 */

public class RetrieveNotebookDBTask extends AsyncTask<Integer, Void, Notebook>
{
    private NoteDao mNoteDao;

    public RetrieveNotebookDBTask(NoteDao noteDao)
    {
        mNoteDao = noteDao;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    protected Notebook doInBackground(Integer... id)
    {
        Log.d("RetrieveNotebook", "Retrieving...");

        //Notebook retrievedNotebook = mNoteDao.getNotebookById(id[0]);

//        Log.d("NotesOnCreate", "Retrieved notebook name : " + retrievedNotebook.getName());
//
        //return retrievedNotebook;
        return new Notebook();
    }
}
