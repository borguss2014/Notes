package com.example.root.notes.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.root.notes.model.Note;

import java.util.List;

/**
 * TODO: Add a class header comment!
 */

public class NoteViewModel extends AndroidViewModel
{
    private final LiveData<List<Note>> notesList;

    public NoteViewModel(Application application, LiveData<List<Note>> notesList)
    {
        super(application);

        this.notesList = notesList;
    }

    public LiveData<List<Note>> getNotesList()
    {
        return notesList;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final LiveData<List<Note>> notesList;

        public Factory(@NonNull Application application, LiveData<List<Note>> notesList) {
            mApplication = application;
            this.notesList = notesList;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new NoteViewModel(mApplication, notesList);
        }
    }
}
