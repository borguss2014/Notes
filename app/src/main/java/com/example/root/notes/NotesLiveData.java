package com.example.root.notes;

import android.arch.lifecycle.LiveData;

import com.example.root.notes.model.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Add a class header comment!
 */

public class NotesLiveData extends LiveData<List<Note>>
{
    private final List<Note> mNotes = new ArrayList<>();

    @Override
    public void setValue(List<Note> value) {
        super.setValue(value);
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }
}
