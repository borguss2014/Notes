package com.example.root.notes;

import android.util.SparseArray;

/**
 * Created by Spoiala Cristian on 6/7/2017.
 */

class Attributes {


    static final int NO_NOTE_CLICKED = 1000;

    //bno : binary note
    static final String NOTE_FILE_EXTENSION = ".bno";

    enum ActivityMessageType
    {
        NOTE_FOR_ACTIVITY(0),
        NOTE_FROM_ACTIVITY(1),
        NOTE_EDITOR_ACTIVITY(2);

        private int code;

        ActivityMessageType(int code)
        {
            this.code = code;
        }

        private static final SparseArray<ActivityMessageType> codeMap = new SparseArray<ActivityMessageType>();

        static {
            for (ActivityMessageType type : ActivityMessageType.values()) {
                codeMap.put(type.getCode(), type);
            }
        }

        public static ActivityMessageType fromCode(int code) {
            return codeMap.get(code);
        }

        public int getCode(){
            return this.code;
        }

        public String toString()
        {
            return Integer.toString(code);
        }
    }

    enum ActivityResultMessageType
    {
        NEW_NOTE_ACTIVITY_RESULT(0),
        OVERWRITE_NOTE_ACTIVITY_RESULT(1),
        DELETE_NOTE_ACTIVITY_RESULT(2);

        private int code;

        ActivityResultMessageType(int code)
        {
            this.code = code;
        }

        private static final SparseArray<ActivityResultMessageType> codeMap = new SparseArray<ActivityResultMessageType>();

        static {
            for (ActivityResultMessageType type : ActivityResultMessageType.values()) {
                codeMap.put(type.getCode(), type);
            }
        }

        public static ActivityResultMessageType fromCode(int code) {
            return codeMap.get(code);
        }

        public int getCode(){
            return this.code;
        }
    }

    enum HandlerMessageType
    {
        HANDLER_MESSAGE_NOTE_ADDED(1000),
        HANDLER_MESSAGE_NOTE_MODIFIED(1),
        HANDLER_MESSAGE_NOTE_DELETED(2);

        HandlerMessageType(int code)
        {
            this.code = code;
        }

        private int code;

        private static final SparseArray<HandlerMessageType> codeMap = new SparseArray<HandlerMessageType>();

        static {
            for (HandlerMessageType type : HandlerMessageType.values()) {
                codeMap.put(type.getCode(), type);
            }
        }

        public static HandlerMessageType fromCode(int code) {
            return codeMap.get(code);
        }

        public int getCode(){
            return this.code;
        }

    }
}
