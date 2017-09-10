package com.example.root.notes.util;

/**
 * TODO: Add a class header comment!
 */

public final class Attributes
{
    private Attributes()
    {
    }

    public static final int NO_NOTE_CLICKED = -1;

    //bno : binary note
    public static final String NOTE_FILE_EXTENSION = ".bno";

    public static class ActivityMessageType
    {
        public static final int NOTE_EDITOR_ACTIVITY = 0;
        public static final int NOTES_LIST_ACTIVITY = 0;

        public static final String NOTE_FROM_ACTIVITY = "NOTE_FROM_ACTIVITY";
        public static final String NOTE_FOR_ACTIVITY = "NOTE_FOR_ACTIVITY";

        public static final String NOTEBOOK_FROM_ACTIVITY = "NOTEBOOK_FROM_ACTIVITY";
        public static final String NOTEBOOK_FOR_ACTIVITY = "NOTEBOOK_FOR_ACTIVITY";
    }

    public static class ActivityResultMessageType
    {
        public static final int NEW_NOTE_ACTIVITY_RESULT = 0;
        public static final int OVERWRITE_NOTE_ACTIVITY_RESULT = 1;
        public static final int DELETE_NOTE_ACTIVITY_RESULT = 2;
    }

    public static class HandlerMessageType
    {
        public static final int HANDLER_MESSAGE_NOTE_ADDED = 0;
        public static final int HANDLER_MESSAGE_NOTE_MODIFIED = 1;
        public static final int HANDLER_MESSAGE_NOTE_DELETED = 2;
    }
}
