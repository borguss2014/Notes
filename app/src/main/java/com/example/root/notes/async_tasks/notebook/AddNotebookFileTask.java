package com.example.root.notes.async_tasks.notebook;

import android.os.AsyncTask;

import com.example.root.notes.Notebook;
import com.example.root.notes.functionality.NotebooksAdapter;
import com.example.root.notes.util.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * TODO: Add a class header comment!
 */

public class AddNotebookFileTask extends AsyncTask<String, String, Void>
{
    private NotebooksAdapter adapter;
    private Notebook notebook;
    private String dirPath;
    private ArrayList<Notebook> notebooksList;

    public AddNotebookFileTask(Notebook notebook, String dirPath)
    {
        this.notebook       = notebook;
        this.dirPath = dirPath;
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
        if(notebooksList != null)
        {
            //Add notebook to the adapter list and resort
            notebooksList.add(notebook);

            Collections.sort(notebooksList, new Comparator<Notebook>()
            {
                @Override
                public int compare(Notebook o1, Notebook o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }

        //Save the notebook as a directory in persistent storage
        Utilities.createDirectory(dirPath);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(adapter != null)
        {
            adapter.notifyDataSetChanged();
        }
    }

    public void setNotebooksList(ArrayList<Notebook> notebooksList)
    {
        this.notebooksList = notebooksList;
    }

    public void setNotebooksAdapter(NotebooksAdapter adapter)
    {
        this.adapter = adapter;
    }
}