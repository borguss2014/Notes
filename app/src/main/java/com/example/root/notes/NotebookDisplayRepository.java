package com.example.root.notes;

import com.example.root.notes.database.AppDatabase;
import com.example.root.notes.model.Notebook;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;


/**
 * TODO: Add a class header comment!
 */

public class NotebookDisplayRepository implements NotebookRepository
{
    private AppDatabase mAppDatabase;

    public NotebookDisplayRepository(AppDatabase appDatabase)
    {
        mAppDatabase = appDatabase;
    }

    @Override
    public Single<List<Notebook>> getNotebooks()
    {
        return Single.fromCallable(new Callable<List<Notebook>>()
        {
            @Override
            public List<Notebook> call() throws Exception
            {
                return mAppDatabase.notebookModel().getAllNotebooks();
            }
        });
    }

    @Override
    public Single<Long> addNotebook(Notebook notebook)
    {

        return Single.fromCallable(new Callable<Long>()
        {
            @Override
            public Long call() throws Exception
            {
                return mAppDatabase.notebookModel().addNotebook(notebook);
            }
        });
    }
}
