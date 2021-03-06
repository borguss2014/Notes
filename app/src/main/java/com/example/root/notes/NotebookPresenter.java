package com.example.root.notes;

import com.example.root.notes.model.Notebook;

/**
 * TODO: Add a class header comment!
 */

public interface NotebookPresenter extends BasePresenter
{
    void loadNotebooks();
    void addNotebook(Notebook notebook);
    void updateNotebook(Notebook notebook);
    void deleteNotebook(Notebook notebook);
    void getNotebookByName(String name);

    void deleteAllNotesForNotebook(int notebookId);

    void unsubscribe();
}
