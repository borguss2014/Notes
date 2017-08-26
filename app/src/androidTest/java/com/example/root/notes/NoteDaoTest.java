package com.example.root.notes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.root.notes.database.AppDatabase;
import com.example.root.notes.model.Note;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * TODO: Add a class header comment!
 */

@RunWith(AndroidJUnit4.class)
public class NoteDaoTest
{
    private NoteDao mNoteDao;
    private AppDatabase mDb;

    @Before
    public void createDb()
    {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mNoteDao = mDb.noteModel();
    }


    @Test
    public void addNote() throws Exception
    {
        int note_id = 1000;
        String note_title = "test_note";

        Note note = new Note();
        note.setTitle(note_title);
        note.setId(note_id);

        long result = mNoteDao.addNote(note);
        assertNotEquals(result, -1);

        Note byNoteID = mNoteDao.getNote(note_id);
        assertNotEquals(byNoteID, null);

        assertThat(byNoteID.getTitle(), equalTo(note_title));
    }

    @Test
    public void getNotesForNotebook() throws Exception
    {
        int notebook_id = 1000;

        boolean note_condition_1 = false;
        boolean note_condition_2 = false;

        String note_title1 = "test_note1";
        String note_title2 = "test_note2";

        Note note1 = new Note();
        note1.setTitle(note_title1);
        note1.setNotebookId(notebook_id);

        Note note2 = new Note();
        note2.setTitle(note_title2);
        note2.setNotebookId(notebook_id);

        mNoteDao.addNote(note1);
        mNoteDao.addNote(note2);

        List<Note> notesList = getValue(mNoteDao.getNotesForNotebook(notebook_id));

        assertNotEquals(notesList, null);
        assertNotEquals(notesList.size(), 0);

        for(Note note : notesList)
        {
            if(note.getTitle().equals(note_title1))
            {
                note_condition_1 = true;
            }

            if(note.getTitle().equals(note_title2))
            {
                note_condition_2 = true;
            }
        }

        assertTrue(note_condition_1);
        assertTrue(note_condition_2);
    }

    @After
    public void closeDb() throws IOException
    {
        mDb.close();
    }

    @Test
    public void deleteAllNotes() throws Exception
    {
        int numberOfNotes = 3;

        for(int i=0; i<numberOfNotes; i++)
        {
            Note note = new Note();
            note.setTitle("Note" + Integer.toString(i));

            mNoteDao.addNote(note);
        }

        List<Note> notesList = getValue(mNoteDao.getAllNotes());

        assertNotEquals(notesList, null);
        assertNotEquals(notesList.size(), 0);

        int numberOfDeletedNotes = mNoteDao.deleteAllNotes();
        assertThat(numberOfDeletedNotes, equalTo(numberOfNotes));

        notesList = getValue(mNoteDao.getAllNotes());

        assertNotEquals(notesList, null);
        assertEquals(notesList.size(), 0);
    }

    @Test
    public void deleteNote() throws Exception
    {
        int note_id = 1001;

        String note_title = "Test";

        Note note = new Note();
        note.setTitle(note_title);
        note.setId(note_id);

        mNoteDao.addNote(note);

        Note receivedNote = mNoteDao.getNote(note_id);
        assertNotEquals(receivedNote, null);
        assertEquals(receivedNote.getTitle(), note_title);

        int queryResult = mNoteDao.deleteNote(receivedNote);
        assertEquals(queryResult, 1);

        receivedNote = mNoteDao.getNote(note_id);
        assertSame(receivedNote, null);
    }

    @Test
    public void updateNote() throws Exception
    {
        int note_id = 1001;

        String note_title = "Test";
        String note_new_title = "TestUpdated";

        Note note = new Note();
        note.setTitle(note_title);
        note.setId(note_id);

        mNoteDao.addNote(note);

        note.setTitle(note_new_title);

        int queryResult = mNoteDao.updateNote(note);
        assertEquals(queryResult, 1);

        Note receivedNote = mNoteDao.getNote(note_id);
        assertNotEquals(receivedNote, null);
        assertNotEquals(receivedNote.getTitle(), note_title);
        assertEquals(receivedNote.getTitle(), note_new_title);
    }

    /**
     * This is used to make sure the method waits till data is available from the observer.
     */
    public static <T> T getValue(LiveData<T> liveData) throws InterruptedException
    {
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };
        liveData.observeForever(observer);
        latch.await(2, TimeUnit.SECONDS);
        //noinspection unchecked
        return (T) data[0];
    }

}