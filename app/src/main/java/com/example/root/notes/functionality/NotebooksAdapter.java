package com.example.root.notes.functionality;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.notes.model.Notebook;
import com.example.root.notes.R;

import java.util.List;

/**
 * TODO: Add a class header comment!
 */

public class NotebooksAdapter extends RecyclerView.Adapter<NotebooksAdapter.ViewHolder>
{

    private Context mContext;
    private List<Notebook> mDataSet;

    private View.OnClickListener mClickListener;
    private View.OnLongClickListener mLongClickListener;

    public NotebooksAdapter(@NonNull Context context, @NonNull List<Notebook> dataSet)
    {
        mContext = context;
        mDataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView title;

        private final LinearLayout titleContentLayout;
        private final LinearLayout photoLayout;
        private final LinearLayout parentLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.adapter_notebooks_title);

            titleContentLayout = (LinearLayout) itemView.findViewById(R.id.adapter_notebooks_title_content_layout);
            photoLayout = (LinearLayout) itemView.findViewById(R.id.adapter_notebooks_photo_layout);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.adapter_notebooks_parent_layout);
        }

        public TextView getTitle() {
            return title;
        }

        public LinearLayout getTitleContentLayout() {
            return titleContentLayout;
        }

        public LinearLayout getPhotoLayout() {
            return photoLayout;
        }

        public LinearLayout getParentLayout() {
            return parentLayout;
        }
    }

    @Override
    public NotebooksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notebooks_adapter_row, parent, false);

        NotebooksAdapter.ViewHolder holder = new NotebooksAdapter.ViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mClickListener.onClick(view);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                mLongClickListener.onLongClick(view);
                return false;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(NotebooksAdapter.ViewHolder holder, int position)
    {
        Notebook notebook = mDataSet.get(position);

        holder.getTitle().setText(notebook.getName());
    }

    @Override
    public int getItemCount()
    {
        return mDataSet.size();
    }

    public Notebook getItemAtPosition(int position)
    {
        return mDataSet.get(position);
    }

    public void setClickListener(View.OnClickListener callback)
    {
        mClickListener = callback;
    }

    public void setLongClickListener(View.OnLongClickListener callback)
    {
        mLongClickListener = callback;
    }

    public void addItems(List<Notebook> newDataSet)
    {
        mDataSet.addAll(newDataSet);
    }

    public void clear()
    {
        mDataSet.clear();
    }
}
