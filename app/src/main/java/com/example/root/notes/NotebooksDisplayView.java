package com.example.root.notes;

import com.example.root.notes.model.Notebook;

import java.util.List;

/**
 * TODO: Add a class header comment!
 */

public interface NotebooksDisplayView extends BaseView
{
    void displayNotebooks(List<Notebook> notebookList);
    void displayNoNotebooks();

    void displayNotebookAdded(Notebook notebook);
    void displayNoNotebookAdded();
}
