package com.example.root.notes;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Spoiala Cristian on 7/17/2017.
 */

public abstract class EndlessScrollListener implements ListView.OnScrollListener
{
    private final int buffer_threshold;
    private int front_invisible_items;
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
        front_invisible_items = firstVisibleItem;
        invisible_remaining_items = totalItemCount - (visibleItemCount + front_invisible_items);

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
