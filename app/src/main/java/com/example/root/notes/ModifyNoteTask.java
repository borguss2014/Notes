package com.example.root.notes;

import android.os.AsyncTask;
import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.Collections;

/**
 * Created by Spoiala Cristian on 6/2/2017.
 */

class ModifyNoteTask extends AsyncTask<String, String, Void>
{

    private final WeakReference<MainActivity> mActivity;

    ModifyNoteTask(MainActivity activity)
    {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute()
    {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    protected Void doInBackground(String... params) {

        MainActivity activity = mActivity.get();

        Handler handler = activity.getHandler();

        if(activity.getReceivedNote() != null)
        {
            if(activity.getCurrentlyClickedNote() != Utilities.NO_NOTE_CLICKED)
            {
                activity.getNotes().remove(activity.getNotes().get(activity.getCurrentlyClickedNote()));
                activity.getNotes().add(activity.getReceivedNote());

                Collections.sort(activity.getNotes(), Comparison.getCurrentComparator());

                Utilities.saveFile(activity.getApplicationContext(), activity.getReceivedNote());

                handler.sendEmptyMessage(Utilities.HANDLER_MESSAGE_NOTE_MODIFIED);
            }
        }

        return null;
    }
}
