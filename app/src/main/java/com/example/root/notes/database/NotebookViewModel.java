package com.example.root.notes.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.root.notes.model.Notebook;

import java.util.List;

/**
 * TODO: Add a class header comment!
 */

public class NotebookViewModel extends AndroidViewModel
{
    private final MutableLiveData<List<Notebook>> notebooksList;

    public NotebookViewModel(Application application, MutableLiveData<List<Notebook>> notebooksList)
    {
        super(application);

        this.notebooksList = notebooksList;
    }

    public MutableLiveData<List<Notebook>> getNotebooksList()
    {
        return notebooksList;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final MutableLiveData<List<Notebook>> notebooksList;

        public Factory(@NonNull Application application, MutableLiveData<List<Notebook>> notebooksList) {
            mApplication = application;
            this.notebooksList = notebooksList;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new NotebookViewModel(mApplication, notebooksList);
        }
    }
}
