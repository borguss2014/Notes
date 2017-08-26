package com.example.root.notes.async_tasks.notebook;

import android.os.AsyncTask;
import com.example.root.notes.NotebookDao;
import com.example.root.notes.database.QueryResultLiveData;
import com.example.root.notes.model.Notebook;

/**
 * TODO: Add a class header comment!
 */

public class AddNotebookDBTask extends AsyncTask<Notebook, Void, Void>
{
    private NotebookDao mNotebookDao;
    private QueryResultLiveData mObservableData;

    public AddNotebookDBTask(NotebookDao notebookDao, QueryResultLiveData observableData)
    {
        mNotebookDao = notebookDao;
        mObservableData = observableData;
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
        mObservableData.postValue(mNotebookDao.addNotebook(notebook[0]));
        return null;
    }
}
