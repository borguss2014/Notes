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
    private AppDatabase mAppDatabase;
    private LiveData<List<Note>> notesList;

    public NoteViewModel(Application application, int notebookId)
    {
        super(application);

        mAppDatabase = AppDatabase.getDatabase(this.getApplication());
        notesList = mAppDatabase.noteModel().getNotesForNotebook(notebookId);
    }

    public LiveData<List<Note>> getNotesList()
    {
        return notesList;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory
    {

        @NonNull
        private final Application mApplication;

        private int mNotebookId;

        public Factory(@NonNull Application application, int notebookId)
        {
            mApplication = application;
            mNotebookId = notebookId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass)
        {
            //noinspection unchecked
            return (T) new NoteViewModel(mApplication, mNotebookId);
        }
    }
}
