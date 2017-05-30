package com.example.root.notes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Spoiala Cristian on 5/30/2017.
 */

class Comparison {

    private static Comparator<Note> compareByFilename;
    private static Comparator<Note> compareByCreationDate;
    private static Comparator<Note> compareByModificationDate;
    private static Comparator<Note> compareByTitle;

    private static Comparator<Note> mCurrent;
    private static boolean mOrderAscending = true;

    static void initComparators()
    {
        compareByFilename = new java.util.Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                if(mOrderAscending)
                {
                    return o1.getFileName().compareTo(o2.getFileName());
                }
                else{
                    return o2.getFileName().compareTo(o1.getFileName());
                }
            }
        };

        compareByTitle = new java.util.Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                if(mOrderAscending)
                {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
                else
                {
                    return o2.getTitle().compareTo(o1.getTitle());
                }
            }
        };

        compareByCreationDate = new java.util.Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                int result = -1000;

                try
                {
                    Date firstDate = sdf.parse(o1.getCreationDate().getDateTime());
                    Date secondDate = sdf.parse(o2.getCreationDate().getDateTime());

                    if(mOrderAscending)
                    {
                        result = firstDate.compareTo(secondDate);
                    }
                    else
                    {
                        result = secondDate.compareTo(firstDate);
                    }
                }
                catch (ParseException | NullPointerException e)
                {
                    e.printStackTrace();
                }

                return result;
            }
        };

        compareByModificationDate = new java.util.Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                int result = -1000;

                try
                {
                    Date firstDate = sdf.parse(o1.getLastModifiedDate().getDateTime());
                    Date secondDate = sdf.parse(o2.getLastModifiedDate().getDateTime());

                    if(mOrderAscending)
                    {
                        result = firstDate.compareTo(secondDate);
                    }
                    else
                    {
                        result = secondDate.compareTo(firstDate);
                    }
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }

                return result;
            }
        };
    }

    static void setCurrentComparator(Comparator<Note> comparator)
    {
        mCurrent = comparator;
    }

    static Comparator<Note> getCurrentComparator()
    {
        return mCurrent;
    }

    static Comparator<Note> getCompareByFilename() {
        return compareByFilename;
    }

    public static Comparator<Note> getCompareByCreationDate() {
        return compareByCreationDate;
    }

    public static Comparator<Note> getCompareByModificationDate() {
        return compareByModificationDate;
    }

    static Comparator<Note> getCompareByTitle() {
        return compareByTitle;
    }

    public static void setOrderAscending()
    {
        mOrderAscending = true;
    }

    static void setOrderDescending()
    {
        mOrderAscending = false;
    }
}
