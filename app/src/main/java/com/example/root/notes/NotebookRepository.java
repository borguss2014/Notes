package com.example.root.notes;

import com.example.root.notes.model.Notebook;

import java.util.List;

import io.reactivex.Single;

/**
 * TODO: Add a class header comment!
 */

public interface NotebookRepository
{
    Single<List<Notebook>> getNotebooks();
    Single<Long> addNotebook(Notebook notebook);
    Single<Long> updateNotebook(Notebook notebook);
    Single<Long> deleteNotebook(Notebook notebook);

    Single<Long> deleteAllNotesFromNotebook(int notebookId);

    Single<Notebook> retrieveNotebookByName(String name);

    int retrieveDefaultNotebookID();
    void insertDefaultNotebookID(int notebookID);
}
