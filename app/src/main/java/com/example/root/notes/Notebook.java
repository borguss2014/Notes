package com.example.root.notes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Spoiala Cristian on 5/30/2017.
 */

class Notebook implements Serializable{

    private String mName;
    private ArrayList<Note> mNotes;

    public Notebook()
    {
        mName = "";
        mNotes = new ArrayList<>();
    }

    public Notebook(String name)
    {
        mName = name;
        mNotes = new ArrayList<>();
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

    public ArrayList<Note> getNotes()
    {
        return mNotes;
    }
}
