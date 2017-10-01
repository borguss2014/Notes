package com.example.root.notes.functionality;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.notes.NotebooksDisplayPresenter;
import com.example.root.notes.model.Notebook;
import com.example.root.notes.R;
import com.example.root.notes.util.Attributes;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO: Add a class header comment!
 */

public class NotebooksAdapter extends RecyclerView.Adapter<NotebooksAdapter.ViewHolder>
{

    public static class Constants
    {
        public static String Holder = "Holder";
        public static String NotebookOverflowOptions = "OverflowOptions";
    }

    private Context mContext;
    private List<Notebook> mDataSet;

    private NotebooksDisplayPresenter mPresenter;

    private RecyclerViewClickInterface clickListener;

    public interface RecyclerViewClickInterface
    {
        public void onItemClicked(int position, String tag, View view);
    }

    public NotebooksAdapter(@NonNull Context context, @NonNull List<Notebook> dataSet, NotebooksDisplayPresenter presenter, RecyclerViewClickInterface clickListener)
    {
        mContext = context;
        mDataSet = dataSet;
        mPresenter = presenter;
        this.clickListener = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        @BindView(R.id.adapter_notebooks_title)
        TextView title;

        @BindView(R.id.adapter_notebooks_title_content_layout)
        LinearLayout titleContentLayout;

        @BindView(R.id.adapter_notebooks_photo_layout)
        LinearLayout photoLayout;

        @BindView(R.id.adapter_notebooks_parent_layout)
        LinearLayout parentLayout;

        @BindView(R.id.adapter_notebooks_options)
        Button notebookOverflowOptions;

        private WeakReference<RecyclerViewClickInterface> clickListenerRef;

        public ViewHolder(View itemView, RecyclerViewClickInterface listener)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);

            clickListenerRef = new WeakReference<>(listener);

            itemView.setTag(Constants.Holder);
            itemView.setOnClickListener(this);

            notebookOverflowOptions.setTag(Constants.NotebookOverflowOptions);
            notebookOverflowOptions.setOnClickListener(this);
        }


        @Override
        public void onClick(View view)
        {
            if(clickListenerRef.get() != null)
            {
                clickListenerRef.get().onItemClicked(getAdapterPosition(), view.getTag().toString(), view);
            }
        }

        @Override
        public boolean onLongClick(View view)
        {
            return false;
        }
    }

    @Override
    public NotebooksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notebooks_adapter_row, parent, false);

        NotebooksAdapter.ViewHolder holder = new NotebooksAdapter.ViewHolder(view, clickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(NotebooksAdapter.ViewHolder holder, int position)
    {
        Notebook notebook = mDataSet.get(position);

        holder.title.setText(notebook.getName());
    }

    @Override
    public int getItemCount()
    {
        return mDataSet.size();
    }

    public List<Notebook> getInternalData()
    {
        return mDataSet;
    }

    public Notebook getItemAtPosition(int position)
    {
        return mDataSet.get(position);
    }

    public boolean contains(String notebookName)
    {
        for(Notebook notebook : mDataSet)
        {
            if(notebook.getName().equals(notebookName))
            {
                return true;
            }
        }

        return false;
    }

    public void addItems(List<Notebook> newDataSet)
    {
        mDataSet.addAll(newDataSet);
        notifyDataSetChanged();
    }

    public void addItem(Notebook notebook)
    {
        mDataSet.add(notebook);
        notifyItemInserted(mDataSet.indexOf(notebook));
    }

    public void clear()
    {
        mDataSet.clear();
        notifyDataSetChanged();
    }
}
