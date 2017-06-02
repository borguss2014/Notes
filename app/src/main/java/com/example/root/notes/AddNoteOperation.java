package com.example.root.notes;

import android.app.Activity;
import android.os.Handler;

import java.util.Collections;

/**
 * Created by Spoiala Cristian on 6/2/2017.
 */

class AddNoteOperation implements Runnable{

    private Note mNote;
    private MainActivity mActivity;

    AddNoteOperation(MainActivity activity, Note note)
    {
        mNote       = note;
        mActivity   = activity;
    }

    @Override
    public void run()
    {
        Handler handler = mActivity.getHandler();

        if(mNote != null)
        {
            mActivity.getNotes().add(mNote);

            Collections.sort(mActivity.getNotes(), Comparison.getCurrentComparator());

            Utilities.saveFile(mActivity.getApplicationContext(), mNote, false);

            handler.sendEmptyMessage(Utilities.NOTE_ADDED);
        }
    }

}