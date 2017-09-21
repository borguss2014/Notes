package com.example.root.notes;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

import com.example.root.notes.model.Notebook;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO: Add a class header comment!
 */

public class NotebooksDisplayPresenter implements LifecycleObserver, NotebookPresenter
{
    private NotebookRepository repository;
    private NotebooksDisplayView view;

    private Scheduler mainScheduler;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public NotebooksDisplayPresenter(NotebooksDisplayView view, NotebookRepository repository, Scheduler mainScheduler)
    {
        this.view          = view;
        this.repository    = repository;
        this.mainScheduler = mainScheduler;
    }

    @Override
    public void loadNotebooks()
    {
        compositeDisposable.add(repository.getNotebooks()
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<Notebook>>()
                {
                    @Override
                    public void onSuccess(List<Notebook> notebooks)
                    {
                        if(notebooks.isEmpty())
                        {
                            view.displayNoNotebooks();
                        }
                        else
                        {
                            view.displayNotebooks(notebooks);
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
    public void addNotebook(Notebook notebook)
    {
        compositeDisposable.add(repository.addNotebook(notebook)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<Long>()
                {
                    @Override
                    public void onSuccess(Long aLong)
                    {
                        if(aLong == -1)
                        {
                            view.displayNoNotebookAdded();
                            return;
                        }

                        getNotebookByName(notebook.getName());
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
    public void updateNotebook(Notebook notebook)
    {
        compositeDisposable.add(repository.updateNotebook(notebook)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<Long>()
                {
                    @Override
                    public void onSuccess(Long aLong)
                    {
                        if(aLong == -1)
                        {
                            view.displayNoNotebookUpdated();
                            return;
                        }

                        view.displayNotebookUpdated();
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
    public void deleteNotebook(Notebook notebook)
    {
        compositeDisposable.add(repository.deleteNotebook(notebook)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<Long>()
                {
                    @Override
                    public void onSuccess(Long aLong)
                    {
                        if(aLong == -1)
                        {
                            view.displayNoNotebookDeleted();
                            return;
                        }

                        deleteAllNotesForNotebook(notebook.getId());
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
    public void deleteAllNotesForNotebook(int notebookId)
    {
        compositeDisposable.add(repository.deleteAllNotesFromNotebook(notebookId)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<Long>()
                {
                    @Override
                    public void onSuccess(Long aLong)
                    {
                        if(aLong != -1)
                        {
                            view.displayNotebookDeleted();
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }
                }));
    }

    @Override
    public void getNotebookByName(String name)
    {
        compositeDisposable.add(repository.retrieveNotebookByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<Notebook>()
               {
                   @Override
                   public void onSuccess(Notebook notebook)
                   {
                       view.displayNotebookAdded(notebook);
                   }

                   @Override
                   public void onError(Throwable e)
                   {

                   }
               })
        );
    }

    @Override
    final public void attachLifecycle(Lifecycle lifecycle)
    {
        lifecycle.addObserver(this);
    }

    @Override
    final public void detachLifecycle(Lifecycle lifecycle)
    {
        lifecycle.removeObserver(this);
    }

    @Override
    public void unsubscribe()
    {
        compositeDisposable.clear();
    }

    @Override
    public int getDefaultNotebookID()
    {
        return repository.retrieveDefaultNotebookID();
    }

    @Override
    public void updateDefaultNotebookID(int notebookID)
    {
        repository.insertDefaultNotebookID(notebookID);
    }
}
