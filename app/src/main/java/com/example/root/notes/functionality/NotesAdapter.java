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
import com.example.root.notes.views.NotesView;

import java.util.List;


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
        private final TextView title;
        private final TextView content;
        private final TextView date;

        private final LinearLayout titleContentLayout;
        private final LinearLayout photoLayout;
        private final LinearLayout parentLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.adapter_notes_title);
            content = (TextView) itemView.findViewById(R.id.adapter_notes_content);
            date = (TextView) itemView.findViewById(R.id.adapter_notes_date);

            titleContentLayout = (LinearLayout) itemView.findViewById(R.id.adapter_notes_title_content_layout);
            photoLayout = (LinearLayout) itemView.findViewById(R.id.adapter_notes_photo_layout);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.adapter_notes_parent_layout);
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
        mSelectedItems = NotesView.retrieveSelectedItems();

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
//            Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
//            calendar.setTime(new Date());

//            Date currentDate = Utilities.getCurrentDateTime(calendar);
//            Date creationDate = note.getCreationDate();
//            Date modificationDate = note.getModificationDate();
//
//            ElapsedTime elapsed;

//            if(!note.getModificationDate().isDateSet())
//            {
//
//                elapsed = Utilities.elapsedTime(creationDate.getDateTime(), currentDate.getDateTime());
//
//                if(!elapsed.isOneMinuteElapsed())
//                {
//                    date = "Created seconds ago";
//                }
//                else if(!elapsed.isOneHourElapsed())
//                {
//                    date = "Created " + Long.toString(elapsed.getElapsedMinutes()) + " minutes ago";
//                }
//                else if(!elapsed.isOneDayElapsed())
//                {
//                    date = "Created " + Long.toString(elapsed.getElapsedHours()) + " hours ago";
//                }
//                else if(elapsed.isOneDayElapsed())
//                {
//                    date = Integer.toString(creationDate.getMonth()) + "/"
//                            + Integer.toString(creationDate.getDay()) + "/"
//                            + Integer.toString(creationDate.getYear());
//                }
//            }
//            else
//            {
//                elapsed = Utilities.elapsedTime(modificationDate.getDateTime(), currentDate.getDateTime());
//
//                if(elapsed.getElapsedSeconds() < 60 &&
//                        elapsed.getElapsedMinutes() == 0 &&
//                        elapsed.getElapsedHours() == 0 &&
//                        elapsed.getElapsedDays() == 0)
//                {
//                    date = "Modified seconds ago";
//                }
//                else if(elapsed.getElapsedMinutes() < 60 && elapsed.getElapsedHours() == 0 && elapsed.getElapsedDays() == 0)
//                {
//                    date = "Modified " + Long.toString(elapsed.getElapsedMinutes()) + " minutes ago";
//                }
//                else if(elapsed.getElapsedHours() < 24 && elapsed.getElapsedDays() == 0)
//                {
//                    date = "Modified " + Long.toString(elapsed.getElapsedHours()) + " hours ago";
//                }
//                else if(elapsed.getElapsedDays() > 0)
//                {
//                    date = "Modified: " + Integer.toString(modificationDate.getMonth()) + "/"
//                            + Integer.toString(modificationDate.getDay()) + "/"
//                            + Integer.toString(modificationDate.getYear());
//                }
//            }


            //holder.getDateView().setText(Utilities.getRelativeTimespanString(mContext, note.getCreationDate().getTime(), DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS));
            holder.getDateView().setTextColor(Color.GREEN);
        }

        if(NotesView.selectModeStatus())
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
        else if(!NotesView.selectModeStatus() && mSelectedItems.size() >= 0)
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
        this.mDataSet = newDataset;
        notifyDataSetChanged();
    }
}
