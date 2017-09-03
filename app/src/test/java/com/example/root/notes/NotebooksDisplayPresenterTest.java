package com.example.root.notes;

import com.example.root.notes.model.Notebook;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

/**
 * TODO: Add a class header comment!
 */

public class NotebooksDisplayPresenterTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private NotebooksDisplayView view;

    @Mock
    private NotebookRepository repository;

    @Mock
    private Notebook testNotebook;

    private NotebooksDisplayPresenter presenter;

    private final List<Notebook> MANY_NOTEBOOKS = Arrays.asList(new Notebook(), new Notebook(), new Notebook());

    @Before
    public void setUp() throws Exception
    {
        String testNotebookName = "Test";

        testNotebook.setName(testNotebookName);

        presenter = new NotebooksDisplayPresenter(view, repository, Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>()
        {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception
            {
                return Schedulers.trampoline();
            }
        });
    }

    @After
    public void cleanUp()
    {
        RxJavaPlugins.reset();
    }

    @Test
    public void shouldPassLoadNotebooksToView()
    {
        Mockito.when(repository.getNotebooks()).thenReturn(Single.just(MANY_NOTEBOOKS));

        presenter.loadNotebooks();

        Mockito.verify(view).displayNotebooks(MANY_NOTEBOOKS);
    }

    @Test
    public void shouldHandleNoNotebooksLoaded()
    {
        Mockito.when(repository.getNotebooks()).thenReturn(Single.just(Collections.emptyList()));

        presenter.loadNotebooks();

        Mockito.verify(view).displayNoNotebooks();
    }

    @Test
    public void shouldPassAddNotebookToRepository()
    {
        Mockito.when(repository.addNotebook(testNotebook)).thenReturn(Single.just((long)1));

        presenter.addNotebook(testNotebook);

        Mockito.verify(view).displayNotebookAdded(testNotebook);
    }

    @Test
    public void shouldHandleNoNotebookAdded()
    {
        Mockito.when(repository.addNotebook(testNotebook)).thenReturn(Single.just((long)-1));

        presenter.addNotebook(testNotebook);

        Mockito.verify(view).displayNoNotebookAdded();
    }
}
