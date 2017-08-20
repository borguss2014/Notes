package com.example.root.notes.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import com.example.root.notes.model.Notebook;

import java.util.List;

/**
 * TODO: Add a class header comment!
 */

public class NotebookViewModel extends AndroidViewModel
{
    private LiveData<List<Notebook>> notebooksList;
    private AppDatabase appDatabase;

    public NotebookViewModel(Application application)
    {
        super(application);

        appDatabase = AppDatabase.getDatabase(this.getApplication());
        notebooksList = appDatabase.notebookModel().getAllNotebooks();
    }

    public LiveData<List<Notebook>> getNotebooksList()
    {
        return notebooksList;
    }

//    public static class Factory extends ViewModelProvider.NewInstanceFactory {
//
//        @NonNull
//        private final Application mApplication;
//
//        private final LiveData<List<Notebook>> notebooksList;
//
//        public Factory(@NonNull Application application, LiveData<List<Notebook>> notebooksList) {
//            mApplication = application;
//            this.notebooksList = notebooksList;
//        }
//
//        @Override
//        public <T extends ViewModel> T create(Class<T> modelClass) {
//            //noinspection unchecked
//            return (T) new NotebookViewModel(mApplication, notebooksList);
//        }
//    }
}
