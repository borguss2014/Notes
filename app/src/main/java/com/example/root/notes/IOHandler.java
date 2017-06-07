package com.example.root.notes;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * Created by Spoiala Cristian on 6/5/2017.
 */

class IOHandler extends Handler
{
    private final WeakReference<MainActivity> mActivity;

    IOHandler(MainActivity activity)
    {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        Log.d("HANDLER", "IN_HANDLER");

        MainActivity activity = mActivity.get();

        if(activity == null) return;

        Attributes.HandlerMessageType message = Attributes.HandlerMessageType.fromCode(msg.what);

        switch(message)
        {
            case HANDLER_MESSAGE_NOTE_ADDED:
            {
                Log.d("HANDLER", "NEW NOTE ADDED");

                activity.setReceivedNote(null);
                activity.getAdapter().notifyDataSetChanged();
                break;
            }
            case HANDLER_MESSAGE_NOTE_MODIFIED:
            {
                activity.setReceivedNote(null);
                activity.setCurrentlyClickedNote(Attributes.NO_NOTE_CLICKED);
                activity.getAdapter().notifyDataSetChanged();
                break;
            }
            case HANDLER_MESSAGE_NOTE_DELETED:
            {
                boolean isDeleted = (boolean) msg.obj;

                if(isDeleted)
                {
                    Toast.makeText(activity.getApplicationContext(), "Note successfully deleted" , Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(activity.getApplicationContext(), "An error occurred. Note couldn't be deleted" , Toast.LENGTH_SHORT).show();
                }

                activity.setCurrentlyClickedNote(Attributes.NO_NOTE_CLICKED);
                activity.getAdapter().notifyDataSetChanged();
                break;
            }
        }

    }
}