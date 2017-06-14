package com.example.root.notes;

import android.os.AsyncTask;
import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Spoiala Cristian on 6/12/2017.
 */

class AddNotebookTask extends AsyncTask<String, String, Void>
{

    private final WeakReference<MainActivity> mActivity;
    private Notebook notebook;
    private MainActivity activity;

    AddNotebookTask(MainActivity activity, Notebook notebook)
    {
        mActivity = new WeakReference<>(activity);
        this.notebook = notebook;
        this.activity = mActivity.get();
    }

    @Override
    protected void onPreExecute()
    {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    protected Void doInBackground(String... params)
    {
        activity.getNotebooks().add(notebook);

        Collections.sort(activity.getNotebooks(), new Comparator<Notebook>() {
            @Override
            public int compare(Notebook o1, Notebook o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        Utilities.createDirectory(activity, notebook.getName());

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        activity.getNotebooksViewAdapter().notifyDataSetChanged();
    }
}