package com.example.root.notes;

import android.arch.lifecycle.Lifecycle;

/**
 * TODO: Add a class header comment!
 */

public interface BasePresenter
{
    void attachLifecycle(Lifecycle lifecycle);
    void detachLifecycle(Lifecycle lifecycle);

    int getDefaultNotebookID();
    void updateDefaultNotebookID(int notebookID);
}
