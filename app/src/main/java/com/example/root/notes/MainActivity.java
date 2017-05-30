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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView mListNotes;

    ArrayList<Note> notes;
    NotesAdapter notesAdapter;

    private static boolean selectMode;
    private static ArrayList<Integer> selectedItems;

    int currentlyClickedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("DEBUG", "ON_CREATE");

        getWindow().getDecorView().setBackgroundColor(Color.argb(255,224,224,224));

        mListNotes = (ListView) findViewById(R.id.main_notes_list_view);
        selectedItems = new ArrayList<>();
        selectMode = false;

        currentlyClickedNote = -1;

        Comparison.initComparators();
        Comparison.setCurrentComparator(Comparison.getCompareByTitle());
        //Comparison.setOrderDescending();

        //Utilities.createTestNotes(getApplicationContext(), 2000);

        notes = Utilities.loadNotes(getApplicationContext());

        Collections.sort(notes, Comparison.getCurrentComparator());

        notesAdapter = new NotesAdapter(getApplicationContext(), R.layout.notes_adapter_row, notes);

        mListNotes.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!selectMode)
                {
                    currentlyClickedNote = position;

                    Note note = (Note) parent.getItemAtPosition(position);
                    Log.d("MAIN ON CLICK", note.getFileName());
                    Log.d("MAIN CLICK CR DATE", note.getCreationDate().toString());
                    Log.d("MAIN CLICK MOD DATE", note.getLastModifiedDate().toString());

                    HashMap<String, String> noteData = new HashMap<>();
                    noteData.put(Utilities.TITLE,           note.getTitle());
                    noteData.put(Utilities.CONTENT,         note.getContent());
                    noteData.put(Utilities.FILENAME,        note.getFileName());

                    int REQ_CODE_CHILD = Utilities.NOTE_ACTIVITY;
                    Intent modifyNoteIntent = new Intent(view.getContext(), NoteActivity.class);
                    modifyNoteIntent.putExtra(Utilities.MAIN_DATA, noteData);
                    modifyNoteIntent.putExtra(Utilities.CREATION_DATE, note.getCreationDate());
                    modifyNoteIntent.putExtra(Utilities.MODIFIED_DATE, note.getLastModifiedDate());
                    startActivityForResult(modifyNoteIntent, REQ_CODE_CHILD);
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

        mListNotes.setAdapter(notesAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("DEBUG", "ON_START");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("DEBUG", "ON_RESUME");

        notesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("DEBUG", "ON_PAUSE");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("DEBUG", "ON_STOP");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("DEBUG", "ON_DESTROY");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("DEBUG", "ON_RESTART");
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
                Log.d("MAIN_ACTIVITY_PURGE", "Notes purged");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Utilities.NOTE_ACTIVITY)
        {
            switch(resultCode)
            {
                case Utilities.NEW_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "New note result");

                    Note newNote = (Note) data.getSerializableExtra("NEW_NOTE");
                    notes.add(newNote);
                    Collections.sort(notes, Comparison.getCurrentComparator());
                    //notesAdapter.notifyDataSetChanged();
                    break;
                }
                case Utilities.OVERWRITE_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "Overwrite note result");

                    Note modifiedNote = (Note) data.getSerializableExtra("MODIFIED_NOTE");

                    if(currentlyClickedNote != -1)
                    {
                        notes.remove(notes.get(currentlyClickedNote));
                        notes.add(modifiedNote);
                        Collections.sort(notes, Comparison.getCurrentComparator());
                        Toast.makeText(getApplicationContext(), "Note modified", Toast.LENGTH_SHORT).show();
                        currentlyClickedNote = -1;
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "ERROR: Couldn't delete note", Toast.LENGTH_SHORT).show();
                    }

                    //notesAdapter.notifyDataSetChanged();
                    break;
                }
                case Utilities.DELETE_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "Delete note result");

                    String deletedFilename = data.getStringExtra("DELETED_NOTE");

                    Note toBeDeletedNote = new Note();
                    toBeDeletedNote.setFileName(deletedFilename);

                    if(currentlyClickedNote != -1)
                    {
                        notes.remove(notes.get(currentlyClickedNote));
                        currentlyClickedNote = -1;
                    }
                    //notesAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    private void createNote() {
        int REQ_CODE_CHILD = Utilities.NOTE_ACTIVITY;
        Intent createNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
        startActivityForResult(createNoteIntent, REQ_CODE_CHILD);
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
