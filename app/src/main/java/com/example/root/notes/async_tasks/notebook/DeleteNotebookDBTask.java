package com.example.root.notes.async_tasks.notebook;

import android.os.AsyncTask;
import com.example.root.notes.NotebookDao;
import com.example.root.notes.model.Notebook;

/**
 * TODO: Add a class header comment!
 */

public class DeleteNotebookDBTask extends AsyncTask<Notebook, Void, Void>
{
    private NotebookDao notebookDao;

    public DeleteNotebookDBTask(NotebookDao notebookDao)
    {
        this.notebookDao = notebookDao;
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
        notebookDao.deleteNotebook(notebook[0]);
        return null;
    }
}
