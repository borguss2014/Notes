package com.example.root.notes.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.root.notes.NotesLiveData;
import com.example.root.notes.model.Note;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Add a class header comment!
 */

@Entity(tableName = Notebook.TABLE_NAME)
public class Notebook implements Serializable
{
    @Ignore
    public final static String TABLE_NAME = "notebooks";

    @Ignore
    public final static String NOTEBOOK_NAME = "notebook_name";

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = Notebook.NOTEBOOK_NAME)
    private String mName;

    @Ignore
    private MutableLiveData<List<Note>> mNotes = new MutableLiveData<>();

    @Ignore
    public Notebook()
    {
        mName = "";
    }

    public Notebook(String name)
    {
        mName = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        mNotes.getValue().add(note);

    }

    public void removeNote(Note note)
    {
        mNotes.getValue().remove(note);
    }

    public MutableLiveData<List<Note>> getNotes()
    {
        return mNotes;
    }

    public void setNotes(List<Note> notes)
    {
        mNotes.setValue(notes);
    }
}
