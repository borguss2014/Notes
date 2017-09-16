package com.example.root.notes.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.root.notes.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Add a class header comment!
 */

public class NoteViewModel extends AndroidViewModel
{
    private List<Note> mNotesList;

    public NoteViewModel(Application application)
    {
        super(application);
    }

    public List<Note> getNotesList()
    {
        return mNotesList;
    }

    public void setNotesList(List<Note> notes)
    {
        mNotesList = notes;
    }
}
