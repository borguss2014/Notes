package com.example.root.notes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: Add a class header comment!
 */

public class RecyclerItemListener implements RecyclerView.OnItemTouchListener
{
    private RecyclerTouchListener listener;
    private GestureDetector gestureDetector;

    public interface RecyclerTouchListener {
        public void onClickItem(View v, int position) ;
        public void onLongClickItem(View v, int position);
    }

    public RecyclerItemListener(Context context, final RecyclerView recyclerView, final RecyclerTouchListener listener)
    {
        this.listener = listener;
        this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
        {
            @Override
            public boolean onSingleTapUp(MotionEvent e)
            {
                View v = recyclerView.findChildViewUnder(e.getX(), e.getY());
                // Notify the even
                listener.onClickItem(v, recyclerView.getChildAdapterPosition(v));
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e)
            {
                // We find the view
                View v = recyclerView.findChildViewUnder(e.getX(), e.getY());
                // Notify the even
                listener.onLongClickItem(v, recyclerView.getChildAdapterPosition(v));
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
    {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        return ( child != null && gestureDetector.onTouchEvent(e));
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e)
    {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept)
    {

    }
}
