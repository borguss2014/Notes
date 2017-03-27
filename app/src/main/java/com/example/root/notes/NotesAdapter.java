package com.example.root.notes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;


/**
 * Created by ROOT on 3/21/2017.
 */

class ViewHolder
{
    TextView title;
    TextView content;
}

public class NotesAdapter extends ArrayAdapter<Note>{

    private ViewHolder holder;
    private final int WRAP_CONTENT_LENGTH = 35;

    public NotesAdapter(Context context, int resource, List<Note> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = LayoutInflater.from(getContext());

        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.notes_adapter_row, null);
        }

        holder = new ViewHolder();

        holder.title    = (TextView) convertView.findViewById(R.id.adapter_notes_title);
        holder.content  = (TextView) convertView.findViewById(R.id.adapter_notes_content);
        convertView.setTag(holder);

        Note note = getItem(position);
        Log.d("ADAPTER", note.getFileName());

        holder.title.setText(note.getTitle());

        if(note.getContent().length() > WRAP_CONTENT_LENGTH)
        {
            holder.content.setText(note.getContent().substring(0, WRAP_CONTENT_LENGTH) + "...");
        }
        else
        {
            holder.content.setText(note.getContent());
        }

        return convertView;
    }
}
