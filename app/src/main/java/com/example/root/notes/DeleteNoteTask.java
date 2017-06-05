package com.example.root.notes;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Spoiala Cristian on 6/2/2017.
 */

class DeleteNoteTask extends AsyncTask<String, String, Void>
{

    private final WeakReference<MainActivity> mActivity;

    DeleteNoteTask(MainActivity activity)
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

        if(activity.getCurrentlyClickedNote() != Utilities.NO_NOTE_CLICKED)
        {
            Note toBeDeletedNote = activity.getNotes().get(activity.getCurrentlyClickedNote());

            activity.getNotes().remove(toBeDeletedNote);
            boolean isDeleted = Utilities.deleteFile(activity.getApplicationContext(), toBeDeletedNote.getFileName());

            Message message = handler.obtainMessage();
            message.what = Utilities.HANDLER_MESSAGE_NOTE_DELETED;
            message.obj = isDeleted;

            handler.sendMessage(message);
        }

        return null;
    }
}
