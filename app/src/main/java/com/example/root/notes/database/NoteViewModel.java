package com.example.root.notes.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.root.notes.model.Note;

import java.util.List;

/**
 * TODO: Add a class header comment!
 */

public class NoteViewModel extends AndroidViewModel
{
    private List<Note> mNotesList;

    public NoteViewModel(Application application, List<Note> notesList)
    {
        super(application);
    }

    public List<Note> getNotesList()
    {
        return mNotesList;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory
    {

        @NonNull
        private final Application mApplication;

        private List<Note> mNotesList;

        public Factory(@NonNull Application application, List<Note> notesList)
        {
            mApplication = application;
            mNotesList = notesList;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass)
        {
            //noinspection unchecked
            return (T) new NoteViewModel(mApplication, mNotesList);
        }
    }
}
