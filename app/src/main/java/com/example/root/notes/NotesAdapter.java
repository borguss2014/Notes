package com.example.root.notes;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ROOT on 3/21/2017.
 */

class ViewHolder
{
    TextView title;
    TextView content;
    TextView date;

    LinearLayout titleContentLayout;
    LinearLayout contentLayout;
    LinearLayout photoLayout;
    LinearLayout parentLayout;
}

class NotesAdapter extends ArrayAdapter<Note>{

    private ViewHolder holder;
    private ArrayList<Integer> mSelectedItems;
    private final int WRAP_CONTENT_LENGTH = 35;

    NotesAdapter(Context context, int resource, List<Note> objects) {
        super(context, resource, objects);
        holder = new ViewHolder();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        mSelectedItems = MainActivity.retrieveSelectedItems();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.notes_adapter_row, null);
        }

        holder.title = (TextView) convertView.findViewById(R.id.adapter_notes_title);
        holder.content = (TextView) convertView.findViewById(R.id.adapter_notes_content);
        holder.date = (TextView) convertView.findViewById(R.id.adapter_notes_date);

        holder.titleContentLayout = (LinearLayout) convertView.findViewById(R.id.adapter_notes_title_content_layout);
        holder.photoLayout = (LinearLayout) convertView.findViewById(R.id.adapter_notes_photo_layout);
        holder.parentLayout = (LinearLayout) convertView.findViewById(R.id.adapter_notes_parent_layout);

        convertView.setTag(holder);

        Note note = getItem(position);
        if (note != null)
        {
            Log.d("ADAPTER", note.getFileName());

            holder.title.setText(note.getTitle());

            if (note.getContent().length() > WRAP_CONTENT_LENGTH) {
                holder.content.setText(note.getContent().substring(0, WRAP_CONTENT_LENGTH) + "...");
            } else {
                holder.content.setText(note.getContent());
            }


            int creationDay, creationMonth, creationYear,
            creationHour, creationMinute, creationSecond;

            int modificationDay, modificationMonth, modificationYear,
                    modificationHour, modificationMinute, modificationSecond;

            int currentDay, currentMonth, currentYear,
            currentHour, currentMinute, currentSecond;

            String date = "DATE PLACEHOLDER";

            DateTime currentDate = Utilities.getCurrentDateTime();
            currentDay = currentDate.getDay();
            currentMonth = currentDate.getMonth();
            currentYear = currentDate.getYear();

            currentHour = currentDate.getHour();
            currentMinute = currentDate.getMinute();
            currentSecond = currentDate.getSeconds();

            if(!note.getLastModifiedDate().isDateSet())
            {
                creationDay = note.getCreationDate().getDay();
                creationMonth = note.getCreationDate().getMonth();
                creationYear = note.getCreationDate().getYear();

                creationHour = note.getCreationDate().getHour();
                creationMinute = note.getCreationDate().getMinute();
                creationSecond = note.getCreationDate().getSeconds();

                if((currentSecond - creationSecond) < 60 && currentMinute == creationMinute)
                {
                    date = "Created seconds ago";
                }
                else if(currentMinute > creationMinute && currentHour == creationHour)
                {
                    int time = currentMinute - creationMinute;
                    date = "Created " + Integer.toString(time) + " minutes ago";
                }
                else if(currentHour > creationHour && currentDay == creationDay)
                {
                    int time = currentHour - creationHour;
                    date = "Created " + Integer.toString(time) + " hours ago";
                }
                else if(currentDay > creationDay && currentMonth == creationMonth)
                {
                    int time = currentDay - creationDay;
                    date = "Created " + Integer.toString(time) + " days ago";
                }
                else if(currentMonth > creationMonth)
                {
                    date = Integer.toString(creationMonth) + ":"
                            + Integer.toString(creationDay) + ":"
                            + Integer.toString(creationYear);
                }
            }
            //TODO : DYNAMIC MODIFICATION DATE/TIME
            else
            {
                modificationDay = note.getLastModifiedDate().getDay();
                modificationMonth = note.getLastModifiedDate().getMonth();
                modificationYear = note.getLastModifiedDate().getYear();

                modificationHour = note.getLastModifiedDate().getHour();
                modificationMinute = note.getLastModifiedDate().getMinute();
                modificationSecond = note.getLastModifiedDate().getSeconds();

                date = "Modified: " + Integer.toString(modificationMonth) + ":"
                        + Integer.toString(modificationDay) + ":"
                        + Integer.toString(modificationYear);
            }

            holder.date.setText(date);
            holder.date.setTextColor(Color.GREEN);
        }

        if(MainActivity.selectModeStatus())
        {
            if(mSelectedItems.contains(position))
            {
                holder.titleContentLayout.setBackgroundColor(Color.BLUE);
                holder.photoLayout.setBackgroundColor(Color.BLUE);
            }
            else
            {
                holder.titleContentLayout.setBackgroundColor(Color.WHITE);
                holder.photoLayout.setBackgroundColor(Color.WHITE);
            }
        }
        else if(!MainActivity.selectModeStatus() && mSelectedItems.size() >= 0)
        {
            holder.titleContentLayout.setBackgroundColor(Color.WHITE);
            holder.photoLayout.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }
}
