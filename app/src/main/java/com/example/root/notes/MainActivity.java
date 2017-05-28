package com.example.root.notes;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView mListNotes;

    ArrayList<Note> notes;
    NotesAdapter notesAdapter;

    private static boolean selectMode;
    private static ArrayList<Integer> selectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setBackgroundColor(Color.argb(255,224,224,224));

        mListNotes = (ListView) findViewById(R.id.main_notes_list_view);
        selectedItems = new ArrayList<>();
        selectMode = false;
        
        //Utilities.createTestNotes(getApplicationContext(), 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("RESUME TEST", "CALLING ON RESUME");

        mListNotes.setAdapter(null);
        notes = Utilities.loadAllFiles(getApplicationContext());

        notesAdapter = new NotesAdapter(getApplicationContext(), R.layout.notes_adapter_row, notes);
        mListNotes.setAdapter(notesAdapter);

        mListNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!selectMode)
                {
                    Note note = (Note) parent.getItemAtPosition(position);
                    Log.d("MAIN ON CLICK", note.getFileName());
                    Log.d("MAIN CLICK CR DATE", note.getCreationDate().toString());
                    Log.d("MAIN CLICK MOD DATE", note.getLastModifiedDate().toString());

                    HashMap<String, String> noteData = new HashMap<>();
                    noteData.put(Utilities.TITLE,           note.getTitle());
                    noteData.put(Utilities.CONTENT,         note.getContent());
                    noteData.put(Utilities.FILENAME,        note.getFileName());

                    Intent intent = new Intent(view.getContext(), NoteActivity.class);
                    intent.putExtra(Utilities.MAIN_DATA, noteData);
                    intent.putExtra(Utilities.CREATION_DATE, note.getCreationDate());
                    intent.putExtra(Utilities.MODIFIED_DATE, note.getLastModifiedDate());
                    startActivity(intent);
                }
                else
                {
                    Log.d("Select mode click", Integer.toString(position));
                    if(!selectedItems.contains(position))
                    {
                        Log.d("Toggle item", "Item at position " + Integer.toString(position) + " selected");
                        selectedItems.add(position);
                    }
                    else
                    {
                        Log.d("Toggle item", "Item at position " + Integer.toString(position) + " deselected");
                        selectedItems.remove(selectedItems.indexOf(position));
                    }
                    notesAdapter.notifyDataSetChanged();
                }
            }
        });

        mListNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(!selectMode)
                {
                    Log.d("Select mode long click", Integer.toString(position));
                    selectedItems.add(position);
                    selectMode = true;
                    invalidateOptionsMenu();
                    notesAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_main_add_note:
                createNote();
                return true;
            case R.id.action_main_purge:
                Utilities.deleteAllFiles(getApplicationContext());
                notes.clear();
                notesAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_main_select_delete:

                Collections.sort(selectedItems);
                for(int pos=0; pos<selectedItems.size(); pos++)
                {
                    Note note = (Note) mListNotes.getItemAtPosition(selectedItems.get(pos));
                    Utilities.deleteFile(getApplicationContext(), note.getFileName());
                    notes.remove(notes.indexOf(note));

                    for(int decrement=pos+1; decrement<selectedItems.size(); decrement++)
                    {
                        selectedItems.set(decrement, selectedItems.get(decrement)-1);
                    }
                }
                selectedItems.clear();
                selectMode = false;
                invalidateOptionsMenu();
                notesAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        if(!selectMode && menu.size() == 0)
        {
            inflater.inflate(R.menu.main_notes_menu, menu);
        }
        else if(selectMode && menu.size() > 0)
        {
            menu.clear();
            inflater.inflate(R.menu.main_notes_select_menu, menu);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private void createNote() {
        Intent createNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
        startActivity(createNoteIntent);
    }

    public static ArrayList<Integer> retrieveSelectedItems()
    {
        return selectedItems;
    }

    public static boolean selectModeStatus()
    {
        return selectMode;
    }
}
