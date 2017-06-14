package com.example.root.notes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Spoiala Cristian on 6/10/2017.
 */

class LoadNotebooksTask extends AsyncTask<String, String, Void>
{
    private final WeakReference<MainActivity> mActivity;
    private boolean mRunning;

    LoadNotebooksTask(MainActivity activity)
    {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute()
    {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mRunning = true;
    }

    @Override
    protected Void doInBackground(String... params)
    {
        Context context = mActivity.get().getApplicationContext();

        FilenameFilter filesFilter = new FilenameFilter()
        {
            public boolean accept(File file, String name)
            {
                return file.isDirectory();
            }
        };

        File dir = context.getFilesDir();

        File[] files = dir.listFiles(filesFilter);

        for (File file : files)
        {
            if(mRunning)
            {
                Notebook notebook = new Notebook(file.getName());
                mActivity.get().getNotebooks().add(notebook);

                Log.d("NOTEBOOKS_LOAD_TASK", "Notebook name: " + notebook.getName());
            }
        }

        Collections.sort(mActivity.get().getNotebooks(), new Comparator<Notebook>() {
            @Override
            public int compare(Notebook o1, Notebook o2) {
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

        mActivity.get().getNotebooksViewAdapter().notifyDataSetChanged();
    }
}
