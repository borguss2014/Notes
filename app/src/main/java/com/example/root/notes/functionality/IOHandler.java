package com.example.root.notes.functionality;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.root.notes.util.Attributes;
import com.example.root.notes.views.NotesDisplay;

import java.lang.ref.WeakReference;

/**
 * TODO: Add a class header comment!
 */

public class IOHandler extends Handler
{
    private final WeakReference<NotesDisplay> mActivity;

    public IOHandler(NotesDisplay activity)
    {
        mActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg)
    {
        Log.d("HANDLER", "IN_HANDLER");

        NotesDisplay activity = mActivity.get();

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