package com.example.root.notes;

import com.example.root.notes.model.Notebook;

import java.util.List;

/**
 * TODO: Add a class header comment!
 */

public interface NoteEditorDisplayView
{
    void displayNotebooks(List<Notebook> notebooksList);
    void displayCurrentNotebook(Notebook notebook);
}
