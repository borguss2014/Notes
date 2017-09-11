package com.example.root.notes;

import com.example.root.notes.model.Note;
import com.example.root.notes.model.Notebook;

import java.util.List;

import io.reactivex.Single;

/**
 * TODO: Add a class header comment!
 */

public interface NoteRepository
{
    Single<List<Note>> retrieveNotesForNotebookWithLimit(int nrOfNotes, int offset, int notebookId);
    Single<List<Note>> retrieveNotesForNotebook(int notebookId);

    Single<List<Note>> retrieveAllNotes();

    Single<Long> insertNote(Note note);
    Single<Integer> updateNote(Note note);
    Single<Integer> removeNote(Note note);

    Single<Long> insertDefaultNotebook(Notebook notebook);
    Single<Notebook> retrieveNotebookByName(String name);
}
