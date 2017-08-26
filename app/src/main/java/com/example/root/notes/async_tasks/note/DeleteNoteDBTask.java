package com.example.root.notes.async_tasks.note;

import android.os.AsyncTask;

import com.example.root.notes.NoteDao;
import com.example.root.notes.database.QueryResultLiveData;
import com.example.root.notes.model.Note;

/**
 * TODO: Add a class header comment!
 */

public class DeleteNoteDBTask extends AsyncTask<Note, Void, Void>
{
    private NoteDao mNoteDao;
    private QueryResultLiveData mObservableData;

    public DeleteNoteDBTask(NoteDao noteDao, QueryResultLiveData observableData)
    {
        mNoteDao = noteDao;
        mObservableData = observableData;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    protected Void doInBackground(Note... note)
    {
        mObservableData.postValue(mNoteDao.deleteNote(note[0]));
        return null;
    }
}
