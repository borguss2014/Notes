package com.example.root.notes;

import android.util.SparseArray;

/**
 * Created by Spoiala Cristian on 6/7/2017.
 */

class Attributes {

    static final int NO_NOTE_CLICKED = -1;

    //bno : binary note
    static final String NOTE_FILE_EXTENSION = ".bno";

    class ActivityMessageType
    {
        static final int NOTE_EDITOR_ACTIVITY = 0;
        static final int NOTES_LIST_ACTIVITY = 0;

        static final String NOTE_FROM_ACTIVITY = "NOTE_FROM_ACTIVITY";
        static final String NOTE_FOR_ACTIVITY = "NOTE_FOR_ACTIVITY";

        static final String NOTEBOOK_FROM_ACTIVITY = "NOTEBOOK_FROM_ACTIVITY";
        static final String NOTEBOOK_FOR_ACTIVITY = "NOTEBOOK_FOR_ACTIVITY";
    }

    class ActivityResultMessageType
    {
        static final int NEW_NOTE_ACTIVITY_RESULT = 0;
        static final int OVERWRITE_NOTE_ACTIVITY_RESULT = 1;
        static final int DELETE_NOTE_ACTIVITY_RESULT = 2;
    }

    class HandlerMessageType
    {
        static final int HANDLER_MESSAGE_NOTE_ADDED = 0;
        static final int HANDLER_MESSAGE_NOTE_MODIFIED = 1;
        static final int HANDLER_MESSAGE_NOTE_DELETED = 2;
    }
}
