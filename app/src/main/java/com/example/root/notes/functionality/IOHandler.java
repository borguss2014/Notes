package com.example.root.notes.functionality;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.root.notes.util.Attributes;
import com.example.root.notes.views.NotesView;

import java.lang.ref.WeakReference;

/**
 * TODO: Add a class header comment!
 */

public class IOHandler extends Handler
{
    private final WeakReference<NotesView> mActivity;

    public IOHandler(NotesView activity)
    {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg)
    {
        Log.d("HANDLER", "IN_HANDLER");

        NotesView activity = mActivity.get();

        if(activity == null) return;

        switch(msg.what)
        {
            case Attributes.HandlerMessageType.HANDLER_MESSAGE_NOTE_ADDED:
            {
                Log.d("HANDLER", "NEW NOTE ADDED");

                activity.setReceivedNote(null);
                activity.getAdapter().notifyDataSetChanged();
                break;
            }
            case Attributes.HandlerMessageType.HANDLER_MESSAGE_NOTE_MODIFIED:
            {
                activity.setReceivedNote(null);
                activity.setCurrentlyClickedNote(Attributes.NO_NOTE_CLICKED);
                activity.getAdapter().notifyDataSetChanged();
                break;
            }
            case Attributes.HandlerMessageType.HANDLER_MESSAGE_NOTE_DELETED:
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