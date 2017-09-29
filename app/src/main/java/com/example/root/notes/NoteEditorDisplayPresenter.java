package com.example.root.notes;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;

import com.example.root.notes.model.Notebook;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO: Add a class header comment!
 */

public class NoteEditorDisplayPresenter implements NoteEditorPresenter, LifecycleObserver
{
    private NoteEditorRepository repository;
    private NoteEditorDisplayView view;

    private Scheduler mainScheduler;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public NoteEditorDisplayPresenter(NoteEditorDisplayView view, NoteEditorRepository repository, Scheduler mainScheduler)
    {
        this.view = view;
        this.repository = repository;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void loadNotebooksInDialog()
    {
        compositeDisposable.add(repository.getNotebooks()
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<Notebook>>()
                {
                    @Override
                    public void onSuccess(List<Notebook> notebooks)
                    {
                        view.displayNotebooks(notebooks);
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
    public void getNotebookById(int id)
    {
        compositeDisposable.add(repository.retrieveNotebookById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<Notebook>()
                {
                    @Override
                    public void onSuccess(Notebook notebook)
                    {
                        view.displayCurrentNotebook(notebook);
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
    public void attachLifecycle(Lifecycle lifecycle)
    {

    }

    @Override
    public void detachLifecycle(Lifecycle lifecycle)
    {

    }

    @Override
    public int getDefaultNotebookID()
    {
        return 0;
    }

    @Override
    public void updateDefaultNotebookID(int notebookID)
    {

    }
}
