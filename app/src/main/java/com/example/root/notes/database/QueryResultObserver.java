package com.example.root.notes.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

/**
 * TODO: Add a class header comment!
 */

public class QueryResultObserver extends AndroidViewModel
{
    private MutableLiveData<Long> mQueryResult = new MutableLiveData<>();

    public QueryResultObserver(Application application)
    {
        super(application);
    }

    public MutableLiveData<Long> getQueryResult()
    {
        return mQueryResult;
    }
}
