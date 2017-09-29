package com.example.root.notes;

import com.example.root.notes.model.Notebook;

import java.util.List;

import io.reactivex.Single;

/**
 * TODO: Add a class header comment!
 */

public interface NoteEditorRepository
{
    Single<List<Notebook>> getNotebooks();
    Single<Notebook> retrieveNotebookById(int id);
}
