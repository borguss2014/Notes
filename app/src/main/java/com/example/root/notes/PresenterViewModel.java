package com.example.root.notes;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

/**
 * TODO: Add a class header comment!
 */

public class PresenterViewModel<T> extends ViewModel
{
    private T mPresenter;

    public void setPresenter(T presenter)
    {
        if(this.mPresenter == null)
        {
            this.mPresenter = presenter;
        }
    }

    public T getPresenter()
    {
        return mPresenter;
    }

    @Override
    protected void onCleared()
    {
        super.onCleared();

        mPresenter = null;
    }
}
