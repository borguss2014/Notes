package com.example.root.notes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
{
    private ListView mNotesView;

    private LoadFilesTask loadAllNotes;

    private ArrayList<Note> mNotes;
    private NotesAdapter mNotesAdapter;

    private static boolean mSelectMode;
    private static ArrayList<Integer> mSelectedItems;

    private static int mClickedNotePosition;

    private Note mReceivedNote;

    private final IOHandler mHandler = new IOHandler(this);

    private Runnable createTestNotes = new Runnable() {
        @Override
        public void run()
        {
            Utilities.createTestNotes(getApplicationContext(), 15);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("DEBUG", "ON_CREATE");

        //Utilities.deleteAllFiles(this);

        WeakReference<Context> context = new WeakReference<>(getApplicationContext());

        loadAllNotes = new LoadFilesTask(this);

        getWindow().getDecorView().setBackgroundColor(Color.argb(255,224,224,224));

        mNotesView = (ListView) findViewById(R.id.main_notes_list_view);
        mSelectedItems = new ArrayList<>();
        mSelectMode = false;

        mClickedNotePosition = Utilities.NO_NOTE_CLICKED;

        Comparison.initComparators();
        Comparison.setCurrentComparator(Comparison.getCompareByTitle());
        //Comparison.setOrderDescending();

        mNotes = new ArrayList<>();
        mNotesAdapter = new NotesAdapter(context.get(), R.layout.notes_adapter_row, mNotes);
        mNotesView.setAdapter(mNotesAdapter);

        mNotesView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!mSelectMode)
                {
                    mClickedNotePosition = position;

                    Note note = (Note) parent.getItemAtPosition(position);
                    Log.d("MAIN ON CLICK", note.getFileName());
                    Log.d("MAIN CLICK CR DATE", note.getCreationDate().toString());
                    Log.d("MAIN CLICK MOD DATE", note.getLastModifiedDate().toString());

                    Intent modifyNoteIntent = new Intent(view.getContext(), NoteEditorActivity.class);
                    modifyNoteIntent.putExtra(Utilities.MAIN_DATA, note);

                    startActivityForResult(modifyNoteIntent, Utilities.NOTE_EDITOR_ACTIVITY);
                }
                else
                {
                    Log.d("Select mode click", Integer.toString(position));
                    if(!mSelectedItems.contains(position))
                    {
                        Log.d("Toggle item", "Item at position " + Integer.toString(position) + " selected");
                        mSelectedItems.add(position);
                    }
                    else
                    {
                        Log.d("Toggle item", "Item at position " + Integer.toString(position) + " deselected");
                        mSelectedItems.remove(mSelectedItems.indexOf(position));
                    }
                    mNotesAdapter.notifyDataSetChanged();
                }
            }
        });

        mNotesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(!mSelectMode)
                {
                    Log.d("Select mode long click", Integer.toString(position));
                    mSelectedItems.add(position);
                    mSelectMode = true;
                    invalidateOptionsMenu();
                    mNotesAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        if(savedInstanceState != null)
        {
            Log.d("RESTORE_INSTANCE",  "Restoring notes array list ...");

            int sz = savedInstanceState.getInt("SIZE");

            try
            {
                for(int i=0; i<sz; i++)
                {
                    mNotes.add(((ArrayList<Note>) savedInstanceState.get("NOTES")).get(i));
                }
            }
            catch(IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }

            mNotesAdapter.notifyDataSetChanged();
            mNotesView.setAdapter(mNotesAdapter);
        }
        else
        {
            loadAllNotes.execute();
        }

//        Thread t = new Thread(createTestNotes);
//        t.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
//        t.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("NOTES", mNotes);
        outState.putInt("SIZE", mNotes.size());
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

        //mNotesAdapter.notifyDataSetChanged();
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


        if(loadAllNotes.getStatus() == AsyncTask.Status.RUNNING)
        {
            loadAllNotes.cancel(true);
        }
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
                mNotes.clear();
                mNotesAdapter.notifyDataSetChanged();
                Log.d("MAIN_ACTIVITY_PURGE", "Notes purged");
                return true;
            case R.id.action_main_select_delete:

                Collections.sort(mSelectedItems);

                for(int pos = 0; pos< mSelectedItems.size(); pos++)
                {
                    Note note = (Note) mNotesView.getItemAtPosition(mSelectedItems.get(pos));
                    Utilities.deleteFile(getApplicationContext(), note.getFileName());
                    mNotes.remove(mNotes.indexOf(note));

                    for(int decrement = pos+1; decrement< mSelectedItems.size(); decrement++)
                    {
                        mSelectedItems.set(decrement, mSelectedItems.get(decrement)-1);
                    }
                }
                mSelectedItems.clear();
                mSelectMode = false;
                invalidateOptionsMenu();
                mNotesAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        if(!mSelectMode && menu.size() == 0)
        {
            inflater.inflate(R.menu.main_notes_menu, menu);
        }
        else if(mSelectMode && menu.size() > 0)
        {
            menu.clear();
            inflater.inflate(R.menu.main_notes_select_menu, menu);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Utilities.NOTE_EDITOR_ACTIVITY)
        {
            switch(resultCode)
            {
                case Utilities.NEW_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "New note result");

                    mReceivedNote = (Note) data.getSerializableExtra("NOTE_FROM_EDITOR");

                    AddNoteTask addNote = new AddNoteTask(this);
                    addNote.execute();

                    break;
                }
                case Utilities.OVERWRITE_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "Overwrite note result");

                    mReceivedNote = (Note) data.getSerializableExtra("NOTE_FROM_EDITOR");

                    ModifyNoteTask modifyNote = new ModifyNoteTask(this);
                    modifyNote.execute();

                    break;
                }
                case Utilities.DELETE_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "Delete note result");

                    DeleteNoteTask deleteNote = new DeleteNoteTask(this);
                    deleteNote.execute();

                    break;
                }
            }
        }
    }

    private void createNote() {
        Intent createNoteIntent = new Intent(getApplicationContext(), NoteEditorActivity.class);
        startActivityForResult(createNoteIntent, Utilities.NOTE_EDITOR_ACTIVITY);
    }

    public static ArrayList<Integer> retrieveSelectedItems()
    {
        return mSelectedItems;
    }

    public static boolean selectModeStatus()
    {
        return mSelectMode;
    }

    public IOHandler getHandler()
    {
        return mHandler;
    }

    public ArrayList<Note> getNotes()
    {
        return mNotes;
    }

    public int getCurrentlyClickedNote()
    {
        return mClickedNotePosition;
    }

    public void setCurrentlyClickedNote(int pos)
    {
        mClickedNotePosition = pos;
    }

    public NotesAdapter getAdapter()
    {
        return mNotesAdapter;
    }

    public void setReceivedNote(Note note)
    {
        mReceivedNote = note;
    }

    public Note getReceivedNote()
    {
        return mReceivedNote;
    }

    public ListView getNotesView()
    {
        return mNotesView;
    }
}
