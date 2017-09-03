package com.example.root.notes;

import android.arch.lifecycle.ViewModel;

/**
 * TODO: Add a class header comment!
 */

public class PresenterViewModel extends ViewModel
{
    private NotebooksDisplayPresenter mPresenter;

    public void setPresenter(NotebooksDisplayPresenter presenter)
    {
        if(this.mPresenter == null)
        {
            this.mPresenter = presenter;
        }
    }

    public NotebooksDisplayPresenter getPresenter()
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
