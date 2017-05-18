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
        holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.adapter_notes_content_layout);
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
