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
        public static final int NOTES_LIST_ACTIVITY = 1;
        public static final int NOTEBOOKS_LIST_ACTIVITY = 2;
        public static final int MOVE_NOTE_DIALOG = 3;
        public static final int NEW_NOTE_LOCATION = 4;

        public static final String NOTE_FROM_ACTIVITY = "NOTE_FROM_ACTIVITY";
        public static final String NOTE_FOR_ACTIVITY = "NOTE_FOR_ACTIVITY";

        public static final String NOTEBOOK_FROM_ACTIVITY = "NOTEBOOK_FROM_ACTIVITY";
        public static final String NOTEBOOK_FOR_ACTIVITY = "NOTEBOOK_FOR_ACTIVITY";
        public static final String MOVE_NOTE_DIALOG_DATA = "MOVE_NOTE_DIALOG_DATA";
    }

    public static class ActivityResultMessageType
    {
        public static final int NEW_NOTE_ACTIVITY_RESULT = 0;
        public static final int OVERWRITE_NOTE_ACTIVITY_RESULT = 1;
        public static final int DELETE_NOTE_ACTIVITY_RESULT = 2;

        public static final int RETURN_NOTEBOOK_ACTIVITY_RESULT = 3;
    }

    public static class HandlerMessageType
    {
        public static final int HANDLER_MESSAGE_NOTE_ADDED = 0;
        public static final int HANDLER_MESSAGE_NOTE_MODIFIED = 1;
        public static final int HANDLER_MESSAGE_NOTE_DELETED = 2;
    }

    public static class NavigationDrawerList
    {
        public static final String NAV_ITEM_ALL_NOTES = "All notes";
        public static final String NAV_ITEM_NOTEBOOKS = "Notebooks";
    }

    public static class AppPreferences
    {
        public static final String DEFAULT_NOTEBOOK = "DEFAULT_NOTEBOOK";
        public static final int NO_DEFAULT_NOTEBOOK = -1;
    }

    public static class NotebookOverflowAction
    {
        public static final int MODE_EDIT = 0;
        public static final int MODE_DELETE = 1;
    }

    public static class ViewState
    {
        public static final int EMPTY = 0;
        public static final int LOADING = 1;
        public static final int LOADED = 2;
    }
}
