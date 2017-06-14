package com.example.root.notes;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Spoiala Cristian on 6/9/2017.
 */

class ViewHolderNotebooks
{
    TextView title;

    LinearLayout titleContentLayout;
    LinearLayout photoLayout;
    LinearLayout parentLayout;
}

class NotebooksAdapter extends ArrayAdapter<Notebook> {

    private ViewHolderNotebooks holder;
    private LayoutInflater inflater;

    NotebooksAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Notebook> objects)
    {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(inflater == null)
        {
            inflater= LayoutInflater.from(getContext());
        }

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.notes_adapter_row, null);
            holder = new ViewHolderNotebooks();

            holder.title = (TextView) convertView.findViewById(R.id.adapter_notes_title);

            holder.titleContentLayout = (LinearLayout) convertView.findViewById(R.id.adapter_notebooks_title_content_layout);
            holder.photoLayout = (LinearLayout) convertView.findViewById(R.id.adapter_notebooks_photo_layout);
            holder.parentLayout = (LinearLayout) convertView.findViewById(R.id.adapter_notebooks_parent_layout);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolderNotebooks) convertView.getTag();
        }

        Notebook notebook = getItem(position);

        holder.title.setText(notebook.getName());

        return convertView;
    }
}
