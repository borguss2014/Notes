package com.example.root.notes.functionality;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.notes.model.Note;
import com.example.root.notes.R;
import com.example.root.notes.util.Utilities;
import com.example.root.notes.views.NotesDisplay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * TODO: Add a class header comment!
 */


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>
{
    private List<Note> mDataSet;
    private Context mContext;
    private List<Integer> mSelectedItems;

    private final int WRAP_CONTENT_LENGTH = 35;
    private View.OnClickListener mClickListener;
    private View.OnLongClickListener mLongClickListener;

    public NotesAdapter(Context context, List<Note> dataSet)
    {
        mContext = context;
        mDataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.adapter_notes_title)
        TextView title;

        @BindView(R.id.adapter_notes_content)
        TextView content;

        @BindView(R.id.adapter_notes_date)
        TextView date;

        @BindView(R.id.adapter_notes_title_content_layout)
        LinearLayout titleContentLayout;

        @BindView(R.id.adapter_notes_photo_layout)
        LinearLayout photoLayout;

        @BindView(R.id.adapter_notes_parent_layout)
        LinearLayout parentLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public TextView getTitleView()
        {
            return title;
        }

        public TextView getContentView()
        {
            return content;
        }

        public TextView getDateView()
        {
            return date;
        }

        public LinearLayout getTitleContentLayoutView()
        {
            return titleContentLayout;
        }

        public LinearLayout getPhotoLayoutView()
        {
            return photoLayout;
        }

        public LinearLayout getParentLayoutView()
        {
            return parentLayout;
        }
    }

    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_adapter_row, parent, false);

        NotesAdapter.ViewHolder holder = new NotesAdapter.ViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mClickListener.onClick(v);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                mLongClickListener.onLongClick(v);
                return false;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(NotesAdapter.ViewHolder holder, int position)
    {
        mSelectedItems = NotesDisplay.retrieveSelectedItems();

        Note note = mDataSet.get(position);

        if (note != null)
        {
            Log.d("ADAPTER", note.getFileName());

            holder.getTitleView().setText(note.getTitle());

            if (note.getContent().length() > WRAP_CONTENT_LENGTH)
            {
                String wrapped_note_description = note.getContent().substring(0, WRAP_CONTENT_LENGTH) + "...";
                holder.getContentView().setText(wrapped_note_description);
            }
            else
            {
                holder.getContentView().setText(note.getContent());
            }

//            String date = "DATE PLACEHOLDER";
//
//            Date currentDate = new Date();
//
//            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//            isoFormat.setTimeZone(TimeZone.getDefault());
//
//            Date formattedCurrentDate = null;
//
//            try {
//                formattedCurrentDate = isoFormat.parse(currentDate.toString());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            Date creationDate = note.getCreationDate();
//            Date modificationDate = note.getModificationDate();
//
//            Calendar calCreationDate = Calendar.getInstance();
//            calCreationDate.setTimeZone(TimeZone.getDefault());
//            calCreationDate.setTime(creationDate);
//
//            Calendar calModificationDate = Calendar.getInstance();
//            calModificationDate.setTimeZone(TimeZone.getDefault());
//            calModificationDate.setTime(creationDate);
//
//            if(note.getModificationDate() == null)
//            {
//                long elapsed = (formattedCurrentDate.getTime() - creationDate.getTime()) / 1000;
//
//                if(elapsed < 60)
//                {
//                    date = "Created seconds ago";
//                }
//                else if(elapsed < 3600)
//                {
//                    date = "Created " + Long.toString(elapsed / 60) + " minutes ago";
//                }
//                else if(elapsed < 86400)
//                {
//                    date = "Created " + Long.toString(elapsed / 3600) + " hours ago";
//                }
//                else if(elapsed > 86400)
//                {
//                    date = Integer.toString(calCreationDate.get(Calendar.MONTH) + 1) + "/"
//                            + Integer.toString(calCreationDate.get(Calendar.DAY_OF_MONTH)) + "/"
//                            + Integer.toString(calCreationDate.get(Calendar.YEAR));
//                }
//            }
//            else
//            {
//                long elapsed = (formattedCurrentDate.getTime() - modificationDate.getTime()) / 1000;
//
//                long minutes = elapsed / 60;
//                long hours = elapsed / 3600;
//                long days = elapsed / 86400;
//
//
//                if(elapsed < 60 &&
//                        minutes == 0 &&
//                        hours == 0 &&
//                        days == 0)
//                {
//                    date = "Modified seconds ago";
//                }
//                else if(minutes < 60 && hours == 0 && days == 0)
//                {
//                    date = "Modified " + Long.toString(minutes) + " minutes ago";
//                }
//                else if(hours < 24 && days == 0)
//                {
//                    date = "Modified " + Long.toString(hours) + " hours ago";
//                }
//                else if(days > 0)
//                {
//                    date = "Modified: " + Integer.toString(calModificationDate.get(Calendar.MONTH) + 1) + "/"
//                            + Integer.toString(calModificationDate.get(Calendar.DAY_OF_MONTH)) + "/"
//                            + Integer.toString(calModificationDate.get(Calendar.YEAR));
//                }
//            }

            if(note.getModificationDate() == null)
            {
                String creationDate = "Created: " + Utilities.getRelativeTimespanString(mContext, note.getCreationDate().getTime(),
                        DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS);
                holder.getDateView().setText(creationDate);

            }
            else
            {
                String modificationDate = "Modified : " + Utilities.getRelativeTimespanString(mContext, note.getModificationDate().getTime(),
                        DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS);
                holder.getDateView().setText(modificationDate);
            }

            holder.getDateView().setTextColor(Color.GREEN);
        }

        if(NotesDisplay.selectModeStatus())
        {
            if(mSelectedItems.contains(position))
            {
                holder.getTitleContentLayoutView().setBackgroundColor(Color.BLUE);
                holder.getPhotoLayoutView().setBackgroundColor(Color.BLUE);
            }
            else
            {
                holder.getTitleContentLayoutView().setBackgroundColor(Color.WHITE);
                holder.getPhotoLayoutView().setBackgroundColor(Color.WHITE);
            }
        }
        else if(!NotesDisplay.selectModeStatus() && mSelectedItems.size() >= 0)
        {
            holder.getTitleContentLayoutView().setBackgroundColor(Color.WHITE);
            holder.getPhotoLayoutView().setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount()
    {
        return mDataSet.size();
    }

    public List<Note> getInternalData()
    {
        return mDataSet;
    }

    public Note getItemAtPosition(int position)
    {
        return mDataSet.get(position);
    }

    public void setClickListener(View.OnClickListener callback)
    {
        mClickListener = callback;
    }

    public void setLongClickListener(View.OnLongClickListener callback )
    {
        mLongClickListener = callback;
    }

    public void addItems(List<Note> newDataset)
    {
        mDataSet = newDataset;
        notifyDataSetChanged();
    }

    public void addItem(Note note)
    {
        mDataSet.add(note);
        notifyDataSetChanged();
    }

    public int getItemPosition(Note note)
    {
        return mDataSet.indexOf(note);
    }

    public void deleteItem(Note note)
    {
        mDataSet.remove(note);
        notifyDataSetChanged();
    }

    public void clear()
    {
        mDataSet.clear();
        notifyDataSetChanged();
    }
}
