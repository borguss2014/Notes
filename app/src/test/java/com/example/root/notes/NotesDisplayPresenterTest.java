package com.example.root.notes;

import com.example.root.notes.model.Note;

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

public class NotesDisplayPresenterTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private NotesDisplayView view;

    @Mock
    private NoteRepository repository;

    @Mock
    private Note testNote;

    private NotesDisplayPresenter presenter;

    private final List<Note> MANY_NOTES = Arrays.asList(new Note(), new Note(), new Note());

    private int testNotebookId;

    @Before
    public void setUp() throws Exception
    {
        presenter = new NotesDisplayPresenter(view, repository, Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>()
        {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception
            {
                return Schedulers.trampoline();
            }
        });

        testNotebookId = 100;
    }

    @After
    public void cleanUp()
    {
        RxJavaPlugins.reset();
    }

    @Test
    public void shouldLoadNotesToViewWithLimit()
    {
        Mockito.when(repository.retrieveNotesForNotebookWithLimit(MANY_NOTES.size(), 0, testNotebookId))
                .thenReturn(Single.just(MANY_NOTES));

        presenter.getNotesForNotebookWithLimit(MANY_NOTES.size(), 0, testNotebookId);

        Mockito.verify(view).displayNotes(MANY_NOTES);
    }

    @Test
    public void shouldHandleNoNotesLoadedWithLimit()
    {
        Mockito.when(repository.retrieveNotesForNotebookWithLimit(MANY_NOTES.size(), 0, testNotebookId))
                .thenReturn(Single.just(Collections.emptyList()));

        presenter.getNotesForNotebookWithLimit(MANY_NOTES.size(), 0, testNotebookId);

        Mockito.verify(view).displayNoNotes();
    }

    @Test
    public void shouldLoadNotesToViewForNotebook()
    {
        Mockito.when(repository.retrieveNotesForNotebook(testNotebookId))
                .thenReturn(Single.just(MANY_NOTES));

        presenter.getNotesForNotebook(testNotebookId);

        Mockito.verify(view).displayNotes(MANY_NOTES);
    }

    @Test
    public void shouldHandleNoNotesLoadedForNotebook()
    {
        Mockito.when(repository.retrieveNotesForNotebook(testNotebookId))
                .thenReturn(Single.just(Collections.emptyList()));

        presenter.getNotesForNotebook(testNotebookId);

        Mockito.verify(view).displayNoNotes();
    }

    @Test
    public void shouldLoadAllNotesToView()
    {
        Mockito.when(repository.retrieveAllNotes())
                .thenReturn(Single.just(MANY_NOTES));

        presenter.getAllNotes();

        Mockito.verify(view).displayNotes(MANY_NOTES);
    }

    @Test
    public void shouldHandleNoNotesLoaded()
    {
        Mockito.when(repository.retrieveAllNotes())
                .thenReturn(Single.just(Collections.emptyList()));

        presenter.getAllNotes();

        Mockito.verify(view).displayNoNotes();
    }

    @Test
    public void shouldAddNoteToView()
    {
        Mockito.when(repository.insertNote(testNote))
                .thenReturn(Single.just((long)1));

        presenter.addNote(testNote);

        Mockito.verify(view).displayNoteAdded(testNote);
    }

    @Test
    public void shouldHandleNoteNotAdded()
    {
        Mockito.when(repository.insertNote(testNote))
                .thenReturn(Single.just((long)-1));

        presenter.addNote(testNote);

        Mockito.verify(view).displayNoteNotAdded(testNote);
    }

    @Test
    public void shouldUpdateNoteInView()
    {
        Mockito.when(repository.updateNote(testNote))
                .thenReturn(Single.just(1));

        presenter.updateNote(testNote);

        Mockito.verify(view).displayNoteUpdated(testNote);
    }

    @Test
    public void shouldHandleNoteNotUpdated()
    {
        Mockito.when(repository.updateNote(testNote))
                .thenReturn(Single.just(-1));

        presenter.updateNote(testNote);

        Mockito.verify(view).displayNoteNotUpdated(testNote);
    }

    @Test
    public void shouldDeleteNoteInView()
    {
        Mockito.when(repository.removeNote(testNote))
                .thenReturn(Single.just(1));

        presenter.deleteNote(testNote);

        Mockito.verify(view).displayNoteDeleted(testNote);
    }

    @Test
    public void shouldHandleNoteNotDeleted()
    {
        Mockito.when(repository.removeNote(testNote))
                .thenReturn(Single.just(-1));

        presenter.deleteNote(testNote);

        Mockito.verify(view).displayNoteNotDeleted(testNote);
    }

}
