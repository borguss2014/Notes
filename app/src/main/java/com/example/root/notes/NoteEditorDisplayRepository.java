package com.example.root.notes;

import com.example.root.notes.database.AppDatabase;
import com.example.root.notes.model.Notebook;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * TODO: Add a class header comment!
 */

public class NoteEditorDisplayRepository implements NoteEditorRepository
{
    private AppDatabase mAppDatabase;

    public NoteEditorDisplayRepository(AppDatabase appDatabase)
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
    public Single<Notebook> retrieveNotebookById(int id)
    {
        return Single.fromCallable(new Callable<Notebook>()
        {
            @Override
            public Notebook call() throws Exception
            {
                return mAppDatabase.notebookModel().getNotebookById(id);
            }
        });
    }
}
