package com.example.root.notes;

import android.os.Handler;

import java.util.Collections;

/**
 * Created by Spoiala Cristian on 6/2/2017.
 */

class ModifyNoteOperation implements Runnable {

    private Note mNote;
    private MainActivity mActivity;

    ModifyNoteOperation(MainActivity activity, Note note)
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
            if(mActivity.getCurrentlyClickedNote() != Utilities.NO_NOTE_CLICKED)
            {
                mActivity.getNotes().remove(mActivity.getNotes().get(mActivity.getCurrentlyClickedNote()));
                mActivity.getNotes().add(mNote);

                Collections.sort(mActivity.getNotes(), Comparison.getCurrentComparator());

                Utilities.saveFile(mActivity.getApplicationContext(), mNote, true);

                handler.sendEmptyMessage(Utilities.NOTE_MODIFIED);
            }
        }
    }
}
