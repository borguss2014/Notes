package com.example.root.notes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;
import java.util.Collections;

/**
 * Created by Spoiala Cristian on 6/2/2017.
 */

class LoadFilesTask extends AsyncTask<String, String, Void>
{

    private final WeakReference<MainActivity> mActivity;
    private boolean mRunning;

    LoadFilesTask(MainActivity activity)
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

        File dir = context.getFilesDir();

        FilenameFilter filesFilter = new FilenameFilter()
        {
            public boolean accept(File dir, String name)
            {
                return name.endsWith(Utilities.NOTE_FILE_EXTENSION);
            }
        };

        File[] files = dir.listFiles(filesFilter);
        if(files.length != 0) {

            int fileCount = 0;

            try {
                FileInputStream fis;
                ObjectInputStream ois;

                Log.d("UTILITIES_LOAD_NOTES", "Loading notes...");

                for (File file : files)
                {
                    if(mRunning)
                    {
                        fileCount++;

                        int progress = (fileCount * 100)/files.length;

                        fis = context.openFileInput(file.getName());
                        ois = new ObjectInputStream(fis);

                        Note note = (Note) ois.readObject();
                        //Log.d("Utilities-LOAD", note.getFileName());

                        mActivity.get().getNotes().add(note);

                        publishProgress(Integer.toString(progress));
                    }
                    else
                    {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            Log.d("UTILITIES_LOAD_NOTES", "Notes loaded from disk");
        }

        Collections.sort(mActivity.get().getNotes(), Comparison.getCurrentComparator());
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values)
    {
        super.onProgressUpdate(values);

        Log.d("UTILITIES_LOAD_NOTES", values[0]);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        mRunning = false;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);

        mActivity.get().getAdapter().notifyDataSetChanged();
        //mActivity.get().getNotesView().setAdapter(mActivity.get().getAdapter());
    }
}
