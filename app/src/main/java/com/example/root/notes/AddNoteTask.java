package com.example.root.notes;

import android.os.AsyncTask;
import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.Collections;

/**
 * Created by Spoiala Cristian on 6/2/2017.
 */

class AddNoteTask extends AsyncTask<String, String, Void>
{

    private final WeakReference<NotesView> mActivity;

    AddNoteTask(NotesView activity)
    {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute()
    {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    protected Void doInBackground(String... params)
    {
        NotesView activity = mActivity.get();

        Handler handler = activity.getHandler();

        if(activity.getReceivedNote() != null)
        {
            activity.getNotes().add(activity.getReceivedNote());

            Collections.sort(activity.getNotes(), Comparison.getCurrentComparator());

            Utilities.saveFile(activity.getApplicationContext(), activity.getReceivedNote(), activity.getNotebookName());

            handler.sendEmptyMessage(Attributes.HandlerMessageType.HANDLER_MESSAGE_NOTE_ADDED);
        }

        return null;
    }
}