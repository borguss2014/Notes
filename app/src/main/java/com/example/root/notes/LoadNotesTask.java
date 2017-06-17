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
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Spoiala Cristian on 6/2/2017.
 */

class LoadNotesTask extends AsyncTask<String, String, Void>
{
    private volatile boolean mRunning;

    private NotesAdapter        adapter;
    private ArrayList<Note>     notesList;
    private String              notebookPath;

    LoadNotesTask(String notebookPath)
    {
       this.notebookPath = notebookPath;
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
        if(notesList == null)
        {
            try
            {
                throw new Exception("Notes list not set");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        File notebookDirectory = new File(notebookPath);

        Log.d("LOAD_FILES_TASK", "Directory: " + notebookDirectory.toString());

        FilenameFilter filesFilter = new FilenameFilter()
        {
            public boolean accept(File file, String name)
            {
                return name.endsWith(Attributes.NOTE_FILE_EXTENSION);
            }
        };

        File[] files = notebookDirectory.listFiles(filesFilter);

        int nrOfFiles = 0;

        try
        {
            nrOfFiles = files.length;
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        if(nrOfFiles != 0)
        {
            int fileCount = 0;

            try
            {
                FileInputStream fis;
                ObjectInputStream ois;

                Log.d("LOAD_FILES_TASK", "Loading notes...");

                for (File file : files)
                {
                    if(mRunning)
                    {
                        fileCount++;

                        int progress = (fileCount * 100)/files.length;

                        fis = new FileInputStream(file.getPath());
                        ois = new ObjectInputStream(fis);

                        Note note = (Note) ois.readObject();
                        //Log.d("Utilities-LOAD", note.getFileName());

                        notesList.add(note);

                        publishProgress(Integer.toString(progress));
                    }
                    else
                    {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }

            Log.d("LOAD_FILES_TASK", "Notes loaded from disk");
        }
        else
        {
            Log.d("LOAD_FILES_TASK", "No notes found");
        }

        Collections.sort(notesList, Comparison.getCurrentComparator());
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values)
    {
        super.onProgressUpdate(values);

        Log.d("UTILITIES_LOAD_NOTES", values[0]);
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
                throw new Exception("Adapter not set");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return;
            }
        }

        adapter.notifyDataSetChanged();
    }

    void setNotesAdapter(NotesAdapter adapter)
    {
        this.adapter = adapter;
    }

    void setNotesList(ArrayList<Note> notesList)
    {
        this.notesList = notesList;
    }
}