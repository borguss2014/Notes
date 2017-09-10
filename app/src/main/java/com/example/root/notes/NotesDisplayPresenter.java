package com.example.root.notes;

import android.arch.lifecycle.Lifecycle;

import com.example.root.notes.model.Note;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO: Add a class header comment!
 */

public class NotesDisplayPresenter implements NotePresenter
{
    private NotesDisplayView view;
    private NoteRepository repository;

    private Scheduler mainScheduler;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public NotesDisplayPresenter(NotesDisplayView view, NoteRepository repository, Scheduler mainScheduler)
    {
        this.view = view;
        this.repository = repository;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void attachLifecycle(Lifecycle lifecycle)
    {

    }

    @Override
    public void detachLifecycle(Lifecycle lifecycle)
    {

    }

    @Override
    public void getNotesForNotebookWithLimit(int nrOfNotes, int offset, int notebookId)
    {
        compositeDisposable.add(repository.retrieveNotesForNotebookWithLimit(nrOfNotes, offset, notebookId)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<Note>>()
                {

                    @Override
                    public void onSuccess(List<Note> notes)
                    {
                        if(notes.isEmpty())
                        {
                            view.displayNoNotes();
                        }
                        else
                        {
                            view.displayNotes(notes);
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }
                })
        );
    }

    @Override
    public void getNotesForNotebook(int notebookId)
    {
        compositeDisposable.add(repository.retrieveNotesForNotebook(notebookId)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<Note>>()
                {
                    @Override
                    public void onSuccess(List<Note> notes)
                    {
                        if(notes.isEmpty())
                        {
                            view.displayNoNotes();
                        }
                        else
                        {
                            view.displayNotes(notes);
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }
                })
        );
    }

    @Override
    public void getAllNotes()
    {
        compositeDisposable.add(repository.retrieveAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<Note>>()
                {
                    @Override
                    public void onSuccess(List<Note> notes)
                    {
                        if(notes.isEmpty())
                        {
                            view.displayNoNotes();
                        }
                        else
                        {
                            view.displayNotes(notes);
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }
                })
        );
    }

    @Override
    public void addNote(Note note)
    {
        compositeDisposable.add(repository.insertNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<Long>()
                {
                    @Override
                    public void onSuccess(Long queryResult)
                    {
                        if(queryResult == -1)
                        {
                            view.displayNoteNotAdded(note);
                        }
                        else
                        {
                            view.displayNoteAdded(note);
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }
                })
        );
    }

    @Override
    public void updateNote(Note note)
    {
        compositeDisposable.add(repository.updateNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<Integer>()
                {
                    @Override
                    public void onSuccess(Integer queryResult)
                    {
                        if(queryResult == -1)
                        {
                            view.displayNoteNotUpdated(note);
                        }
                        else
                        {
                            view.displayNoteUpdated(note);
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }
                })
        );
    }

    @Override
    public void deleteNote(Note note)
    {
        compositeDisposable.add(repository.removeNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<Integer>()
                {
                    @Override
                    public void onSuccess(Integer queryResult)
                    {
                        if(queryResult == -1)
                        {
                            view.displayNoteNotDeleted(note);
                        }
                        else
                        {
                            view.displayNoteDeleted(note);
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }
                })
        );
    }
}
