package com.example.root.notes;

import com.example.root.notes.model.Note;

/**
 * TODO: Add a class header comment!
 */

public interface NotePresenter extends BasePresenter
{
    void getNotesForNotebookWithLimit(int nrOfNotes, int offset, int notebookId);
    void getNotesForNotebook(int notebookId);

    void getAllNotes();

    void addNote(Note note);
    void updateNote(Note note);
    void deleteNote(Note note);
}
