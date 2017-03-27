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
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView mListNotes;

    ArrayList<Note> notes;
    NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setBackgroundColor(Color.LTGRAY);

        mListNotes = (ListView) findViewById(R.id.main_notes_list_view);
        //Utilities.deleteAllFiles(getApplicationContext());
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
                Note note = (Note) parent.getItemAtPosition(position);
                Log.d("MAIN ON CLICK", note.getFileName());
                Log.d("MAIN CLICK CR DATE", note.getCreationDate().toString());
                Log.d("MAIN CLICK MOD DATE", note.getLastModifiedDate().toString());

                HashMap<String, String> noteData = new HashMap<String, String>();
                noteData.put(Utilities.TITLE,           note.getTitle());
                noteData.put(Utilities.CONTENT,         note.getContent());
                noteData.put(Utilities.FILENAME,        note.getFileName());

                Intent intent = new Intent(view.getContext(), NoteActivity.class);
                intent.putExtra(Utilities.MAIN_DATA, noteData);
                intent.putExtra(Utilities.CREATION_DATE, note.getCreationDate());
                intent.putExtra(Utilities.MODIFIED_DATE, note.getLastModifiedDate());
                startActivity(intent);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNote() {
        Intent createNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
        startActivity(createNoteIntent);
    }
}
