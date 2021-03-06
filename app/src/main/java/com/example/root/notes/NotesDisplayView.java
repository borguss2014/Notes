package com.example.root.notes;

import com.example.root.notes.model.Note;
import com.example.root.notes.model.Notebook;

import java.util.List;

/**
 * TODO: Add a class header comment!
 */

public interface NotesDisplayView extends BaseView
{
    void displayNotes(List<Note> notes);
    void displayNoNotes();

    void displayNoteAdded(Note note);
    void displayNoteNotAdded(Note note);

    void displayNoteUpdated(Note note);
    void displayNoteNotUpdated(Note note);

    void displayNoteDeleted(Note note);
    void displayNoteNotDeleted(Note note);

    void displayNotesDeleted();
    void displayNotesNotDeleted();

    void saveDefaultNotebook(Notebook notebook);
}
