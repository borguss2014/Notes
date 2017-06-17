package com.example.root.notes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Spoiala Cristian on 6/10/2017.
 */

class LoadNotebooksTask extends AsyncTask<String, String, Void>
{
    private volatile boolean mRunning;

    private NotebooksAdapter    adapter;
    private String              notebooksPath;
    private ArrayList<Notebook> notebooksList;

    LoadNotebooksTask(String notebooksPath)
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

    void setNotebooksAdapter(NotebooksAdapter adapter)
    {
        this.adapter = adapter;
    }

    void setNotebooksList(ArrayList<Notebook> notebooksList)
    {
        this.notebooksList = notebooksList;
    }
}
