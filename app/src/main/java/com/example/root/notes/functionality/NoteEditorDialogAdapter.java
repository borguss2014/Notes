package com.example.root.notes.functionality;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.notes.R;
import com.example.root.notes.model.Notebook;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

/**
 * TODO: Add a class header comment!
 */

public class NoteEditorDialogAdapter extends RecyclerView.Adapter<NoteEditorDialogAdapter.ViewHolder>
{
    private Context mContext;
    private List<Notebook> mDataSet;

    public NoteEditorDialogAdapter(@NonNull Context context, @NonNull List<Notebook> dataSet)
    {
        mContext = context;
        mDataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.note_editor_dialog_row_title)
        TextView title;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public NoteEditorDialogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_editor_dialog_adapter_row, parent, false);

        NoteEditorDialogAdapter.ViewHolder holder = new NoteEditorDialogAdapter.ViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(NoteEditorDialogAdapter.ViewHolder holder, int position)
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

    public void removeItem(Notebook notebook)
    {
        mDataSet.remove(notebook);
        notifyItemRemoved(mDataSet.indexOf(notebook));
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
