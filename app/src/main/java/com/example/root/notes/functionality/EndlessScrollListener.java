package com.example.root.notes.functionality;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * TODO: Add a class header comment!
 */

public abstract class EndlessScrollListener implements ListView.OnScrollListener
{
    private final int buffer_threshold;
    private int nr_front_invisible_items;
    private int invisible_remaining_items;

    private boolean loading;

    private int previous_total_item_count;


    public EndlessScrollListener()
    {
        this.buffer_threshold = 5;
    }

    public EndlessScrollListener(int buffer_threshold)
    {
        this.buffer_threshold = buffer_threshold;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        nr_front_invisible_items = firstVisibleItem; // number of front invisible items = index of the first visible item

        invisible_remaining_items = totalItemCount - (visibleItemCount + nr_front_invisible_items);

        if(!loading && (invisible_remaining_items < buffer_threshold) && totalItemCount != 0) {

            previous_total_item_count = totalItemCount;

            loading = true;

            onLoadData();

            Log.d("SCROLL_LISTENER", "LOADING NEW DATA");
        }

        if(loading)
        {
            int newTotalCount = previous_total_item_count + buffer_threshold;

            Log.d("SCROLL_LISTENER", "Total count : " + Integer.toString(totalItemCount));
            Log.d("SCROLL_LISTENER", "Expected total count : " + Integer.toString(newTotalCount));

            if(totalItemCount == newTotalCount)
            {
                loading = false;
                Log.d("SCROLL_LISTENER", "NEW DATA LOADED");
            }
            else
            {
                Log.d("SCROLL_LISTENER", "Data not loaded yet");
            }

        }

    }

    public abstract void onLoadData();
}
