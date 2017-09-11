package com.example.root.notes;

import com.example.root.notes.database.AppDatabase;
import com.example.root.notes.model.Note;
import com.example.root.notes.model.Notebook;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * TODO: Add a class header comment!
 */

public class NoteDisplayRepository implements NoteRepository
{
    private AppDatabase mAppDatabase;

    public NoteDisplayRepository(AppDatabase mAppDatabase)
    {
        this.mAppDatabase = mAppDatabase;
    }


    @Override
    public Single<List<Note>> retrieveNotesForNotebookWithLimit(int nrOfNotes, int offset, int notebookId)
    {

        return Single.fromCallable(new Callable<List<Note>>()
        {
            @Override
            public List<Note> call() throws Exception
            {
                return mAppDatabase.noteModel().getNotesForNotebookWithLimit(nrOfNotes, offset, notebookId);
            }
        });
    }

    @Override
    public Single<List<Note>> retrieveNotesForNotebook(int notebookId)
    {
        return Single.fromCallable(new Callable<List<Note>>()
        {
            @Override
            public List<Note> call() throws Exception
            {
                return mAppDatabase.noteModel().getNotesForNotebook(notebookId);
            }
        });
    }

    @Override
    public Single<List<Note>> retrieveAllNotes()
    {
        return Single.fromCallable(new Callable<List<Note>>()
        {
            @Override
            public List<Note> call() throws Exception
            {
                return mAppDatabase.noteModel().getAllNotes();
            }
        });
    }

    @Override
    public Single<Long> insertNote(Note note)
    {
        return Single.fromCallable(new Callable<Long>()
        {
            @Override
            public Long call() throws Exception
            {
                return mAppDatabase.noteModel().addNote(note);
            }
        });
    }

    @Override
    public Single<Integer> updateNote(Note note)
    {
        return Single.fromCallable(new Callable<Integer>()
        {
            @Override
            public Integer call() throws Exception
            {
                return mAppDatabase.noteModel().updateNote(note);
            }
        });
    }

    @Override
    public Single<Integer> removeNote(Note note)
    {
        return Single.fromCallable(new Callable<Integer>()
        {
            @Override
            public Integer call() throws Exception
            {
                return mAppDatabase.noteModel().deleteNote(note);
            }
        });
    }

    @Override
    public Single<Long> insertDefaultNotebook(Notebook notebook)
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
}
