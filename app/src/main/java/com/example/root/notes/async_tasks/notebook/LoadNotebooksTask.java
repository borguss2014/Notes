package com.example.root.notes.async_tasks.notebook;

import android.os.AsyncTask;
import android.util.Log;

import com.example.root.notes.Notebook;
import com.example.root.notes.functionality.NotebooksAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * TODO: Add a class header comment!
 */

public class LoadNotebooksTask extends AsyncTask<String, String, Void>
{
    private volatile boolean mRunning;

    private NotebooksAdapter adapter;
    private String              notebooksPath;
    private ArrayList<Notebook> notebooksList;

    public LoadNotebooksTask(String notebooksPath)
    {
        this.notebooksPath = notebooksPath;
    }

    @Override
    protected void onPreExecute()
    {
        mRunning = true;

        //Set thread priority as background so it
        // won't fight with the UI thread for resources
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    protected Void doInBackground(String... params)
    {
        if(notebooksList == null)
        {
            try
            {
                throw new Exception("Notebooks list not set");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        FilenameFilter dirFilter = new FilenameFilter()
        {
            public boolean accept(File file, String name)
            {
                return file.isDirectory();
            }
        };

        File notebooksDirectory = new File(notebooksPath);

        File[] files = notebooksDirectory.listFiles(dirFilter);

        for (File file : files)
        {
            if(mRunning)
            {
                Notebook notebook = new Notebook(file.getName());
                notebooksList.add(notebook);

                Log.d("NOTEBOOKS_LOAD_TASK", "Notebook name: " + notebook.getName());
            }
        }

        Collections.sort(notebooksList, new Comparator<Notebook>()
        {
            @Override
            public int compare(Notebook o1, Notebook o2)
            {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return null;
    }

    @Override
    protected void onCancelled()
    {
        super.onCancelled();

        mRunning = false;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);

        if(adapter == null)
        {
            try
            {
                throw new Exception("Notes list not set");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return;
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void setNotebooksAdapter(NotebooksAdapter adapter)
    {
        this.adapter = adapter;
    }

    public void setNotebooksList(ArrayList<Notebook> notebooksList)
    {
        this.notebooksList = notebooksList;
    }
}
