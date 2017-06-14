package com.example.root.notes;

import java.util.ArrayList;

/**
 * Created by Spoiala Cristian on 5/30/2017.
 */

class Notebook {

    private String mName;
    private ArrayList<Note> mNotes;

    public Notebook()
    {
        mName = "";
    }

    public Notebook(String name)
    {
        mName = name;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public String getName()
    {
        return mName;
    }

    public void addNote(Note note)
    {
        mNotes.add(note);
    }

    public void removeNote(Note note)
    {
        mNotes.remove(note);
    }
}
