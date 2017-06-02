package com.example.root.notes;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Spoiala Cristian on 6/2/2017.
 */

class DeleteNoteOperation implements Runnable{

    private MainActivity mActivity;

    DeleteNoteOperation(MainActivity activity)
    {
        mActivity = activity;
    }

    @Override
    public void run()
    {
        Handler handler = mActivity.getHandler();

        if(mActivity.getCurrentlyClickedNote() != Utilities.NO_NOTE_CLICKED)
        {
            Note toBeDeletedNote = mActivity.getNotes().get(mActivity.getCurrentlyClickedNote());

            mActivity.getNotes().remove(toBeDeletedNote);
            boolean isDeleted = Utilities.deleteFile(mActivity.getApplicationContext(), toBeDeletedNote.getFileName());

            Message message = handler.obtainMessage();
            message.what = Utilities.NOTE_DELETED;
            message.obj = isDeleted;

            handler.sendMessage(message);
        }
    }

}
