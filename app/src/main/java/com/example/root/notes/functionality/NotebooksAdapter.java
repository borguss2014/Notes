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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO: Add a class header comment!
 */

public class NotebooksAdapter extends RecyclerView.Adapter<NotebooksAdapter.ViewHolder>
{

    private Context mContext;
    private List<Notebook> mDataSet;

    private View.OnClickListener mClickListener;
    private View.OnLongClickListener mLongClickListener;

    private PopupMenu popupMenu;

    private NotebooksDisplayPresenter mPresenter;

    public NotebooksAdapter(@NonNull Context context, @NonNull List<Notebook> dataSet, NotebooksDisplayPresenter presenter)
    {
        mContext = context;
        mDataSet = dataSet;
        mPresenter = presenter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
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

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
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

        public Button getOverflowButton()
        {
            return notebookOverflowOptions;
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

        holder.notebookOverflowOptions.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                popupMenu = new PopupMenu(mContext, view);
                popupMenu.getMenuInflater().inflate(R.menu.notebooks_overflow_menu, popupMenu.getMenu());

                if(mDataSet.size() == 1 &&
                        notebook.getId() == mPresenter.getDefaultNotebookID())
                {
                    popupMenu.getMenu().findItem(R.id.notebook_overflow_delete).setEnabled(false);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch(item.getItemId())
                        {
                            case R.id.notebook_overflow_edit:
                            {
                                displayDialog("Edit notebook", "Ok",
                                        "Cancel", Attributes.NotebookOverflowAction.MODE_EDIT, notebook);
                                break;
                            }
                            case R.id.notebook_overflow_delete:
                            {
                                displayDialog("Delete notebook", "Accept",
                                        "Cancel", Attributes.NotebookOverflowAction.MODE_DELETE, notebook);
                                break;
                            }
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
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

    public void setClickListener(View.OnClickListener callback)
    {
        mClickListener = callback;
    }

    public void setLongClickListener(View.OnLongClickListener callback)
    {
        mLongClickListener = callback;
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
        notifyDataSetChanged();
    }

    public void clear()
    {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    private void displayDialog(String dialogTitle, String okButton, String cancelButton, int mode, Notebook notebook)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.myDialog));

        final EditText input = new EditText(mContext);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        if(mode == Attributes.NotebookOverflowAction.MODE_EDIT)
        {
            alertDialogBuilder.setView(input);
        }
        else if(mode == Attributes.NotebookOverflowAction.MODE_DELETE)
        {
            final TextView view = new TextView(mContext);
            view.setText("Are you sure you want to delete this notebook?");

            alertDialogBuilder.setView(view);
        }

        alertDialogBuilder.setTitle(dialogTitle);

        alertDialogBuilder.setPositiveButton(okButton, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(mode == Attributes.NotebookOverflowAction.MODE_EDIT)
                {
                    String dialogNotebookName = input.getText().toString();

                    if(contains(dialogNotebookName))
                    {
                        Toast.makeText(mContext, "Notebook already exists. Choose another name!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(dialogNotebookName.equals(""))
                    {
                        Toast.makeText(mContext, "Notebook name cannot be empty!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    notebook.setName(dialogNotebookName);

                    //mLastInsertedNotebook = new Notebook(dialogNotebookName);

                    mPresenter.updateNotebook(notebook);
                }
                else if(mode == Attributes.NotebookOverflowAction.MODE_DELETE)
                {
                    int defaultNotebook = mPresenter.getDefaultNotebookID();

                    if(notebook.getId() == defaultNotebook && mDataSet.size() > 1)
                    {
                        int newDefaultNotebookID = mDataSet.get(1).getId();

                        mPresenter.updateDefaultNotebookID(newDefaultNotebookID);

                        Log.d("ADAPTER", "New default notebook id : " + Integer.toString(mPresenter.getDefaultNotebookID()));
                    }
                    mPresenter.deleteNotebook(notebook);
                }
            }
        });
        alertDialogBuilder.setNegativeButton(cancelButton, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        alertDialogBuilder.show();
    }
}
