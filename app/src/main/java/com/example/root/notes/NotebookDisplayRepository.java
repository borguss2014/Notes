package com.example.root.notes;

import android.content.SharedPreferences;

import com.example.root.notes.database.AppDatabase;
import com.example.root.notes.model.Notebook;
import com.example.root.notes.util.Attributes;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;


/**
 * TODO: Add a class header comment!
 */

public class NotebookDisplayRepository implements NotebookRepository
{
    private AppDatabase mAppDatabase;
    private SharedPreferences mPreferences;

    public NotebookDisplayRepository(AppDatabase appDatabase, SharedPreferences preferences)
    {
        mAppDatabase = appDatabase;
        mPreferences = preferences;
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

    @Override
    public Single<Long> updateNotebook(Notebook notebook)
    {
        return Single.fromCallable(new Callable<Long>()
        {
            @Override
            public Long call() throws Exception
            {
                return (long) mAppDatabase.notebookModel().updateNotebook(notebook);
            }
        });
    }

    @Override
    public Single<Long> deleteNotebook(Notebook notebook)
    {
        return Single.fromCallable(new Callable<Long>()
        {
            @Override
            public Long call() throws Exception
            {
                return (long) mAppDatabase.notebookModel().deleteNotebook(notebook);
            }
        });
    }

    @Override
    public Single<Long> deleteAllNotesFromNotebook(int notebookId)
    {
        return Single.fromCallable(new Callable<Long>()
        {
            @Override
            public Long call() throws Exception
            {
                return (long) mAppDatabase.noteModel().deleteAllNotesFromNotebook(notebookId);
            }
        });
    }

    @Override
    public Single<Notebook> retrieveNotebookByName(String name)
    {
        return Single.fromCallable(new Callable<Notebook>()
        {
            @Override
            public Notebook call() throws Exception
            {
                return mAppDatabase.notebookModel().getNotebookByName(name);
            }
        });
    }

    @Override
    public int retrieveDefaultNotebookID()
    {
        return mPreferences.getInt(Attributes.AppPreferences.DEFAULT_NOTEBOOK, -1);
    }

    @Override
    public void insertDefaultNotebookID(int notebookID)
    {
        mPreferences
                .edit()
                .putInt(Attributes.AppPreferences.DEFAULT_NOTEBOOK, notebookID)
                .apply();
    }
}
