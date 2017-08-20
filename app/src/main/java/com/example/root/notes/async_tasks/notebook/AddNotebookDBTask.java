package com.example.root.notes.async_tasks.notebook;

import android.os.AsyncTask;
import com.example.root.notes.NotebookDao;
import com.example.root.notes.database.QueryResultObserver;
import com.example.root.notes.model.Notebook;

/**
 * TODO: Add a class header comment!
 */

public class AddNotebookDBTask extends AsyncTask<Notebook, Void, Void>
{
    private NotebookDao mNotebookDao;
    private QueryResultObserver mObserver;

    public AddNotebookDBTask(NotebookDao notebookDao, QueryResultObserver observer)
    {
        mNotebookDao = notebookDao;
        mObserver = observer;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    protected Void doInBackground(Notebook... notebook)
    {
        mObserver.getQueryResult().postValue(mNotebookDao.addNotebook(notebook[0]));
        return null;
    }
}
