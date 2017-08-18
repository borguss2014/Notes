package com.example.root.notes.async_tasks.notebook;

import android.os.AsyncTask;

import com.example.root.notes.database.AppDatabase;
import com.example.root.notes.model.Notebook;

/**
 * TODO: Add a class header comment!
 */

public class AddNotebookDBTask extends AsyncTask<Notebook, Void, Void>
{
    private final AppDatabase mAppDatabase;

    public AddNotebookDBTask(AppDatabase appDatabase)
    {
        mAppDatabase = appDatabase;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Notebook... notebook)
    {
        mAppDatabase.notebookModel().addNotebook(notebook[0]);
        return null;
    }
}
