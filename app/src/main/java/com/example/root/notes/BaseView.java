package com.example.root.notes;

import com.example.root.notes.database.AppDatabase;

/**
 * TODO: Add a class header comment!
 */

public interface BaseView
{
    void setupView();
    void initializePresenter(AppDatabase appDatabase);
}
