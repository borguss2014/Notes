package com.example.root.notes;

import com.example.root.notes.model.Note;
import com.example.root.notes.model.Notebook;

import java.util.List;

/**
 * TODO: Add a class header comment!
 */

public interface NotePresenter extends BasePresenter
{
    void getNotesForNotebookWithLimit(int nrOfNotes, int offset, int notebookId);
    void getNotesForNotebook(int notebookId);

    void getAllNotes();
    void getNoteById(int noteId);

    void addNote(Note note);
    void updateNote(Note note);
    void deleteNote(Note note);

    void deleteNotesWithIds(List<Integer> noteIds);

    void addDefaultNotebook(Notebook notebook);
    void getNotebookByName(String name);
}
