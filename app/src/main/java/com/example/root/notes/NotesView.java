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

public class NotesView extends AppCompatActivity
{
    private ListView mNotesView;

    private LoadNotesTask loadAllNotes;

    private ArrayList<Note> mNotes;
    private NotesAdapter mNotesViewAdapter;

    private static boolean mSelectMode;
    private static ArrayList<Integer> mSelectedItems;

    private static int mClickedNotePosition;

    private Note mReceivedNote;

    private String mNotebookName;

    private final IOHandler mHandler = new IOHandler(this);

    private Runnable createTestNotes = new Runnable() {
        @Override
        public void run()
        {
            Utilities.createTestNotes(getApplicationContext(), 15, mNotebookName);
        }
    };

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        Log.d("DEBUG", "NOTES_ON_CREATE");

        mNotebookName = getIntent().getStringExtra(Attributes.ActivityMessageType.NOTEBOOK_FOR_ACTIVITY);

        //Utilities.deleteAllFiles(this);

        WeakReference<Context> context = new WeakReference<>(getApplicationContext());

        loadAllNotes = new LoadNotesTask(this);

        getWindow().getDecorView().setBackgroundColor(Color.argb(255,224,224,224));

        mNotesView = (ListView) findViewById(R.id.notes_list_view);
        mSelectedItems = new ArrayList<>();
        mSelectMode = false;

        mClickedNotePosition = Attributes.NO_NOTE_CLICKED;

        Comparison.initComparators();
        Comparison.setCurrentComparator(Comparison.getCompareByTitle());
        //Comparison.setOrderDescending();

        mNotes = new ArrayList<>();
        mNotesViewAdapter = new NotesAdapter(context.get(), R.layout.notes_adapter_row, mNotes);
        mNotesView.setAdapter(mNotesViewAdapter);

        mNotesView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!mSelectMode)
                {
                    mClickedNotePosition = position;

                    Note note = (Note) parent.getItemAtPosition(position);
                    Log.d("NOTE ON CLICK", note.getFileName());
                    Log.d("NOTE CLICK CR DATE", note.getCreationDate().toString());
                    Log.d("NOTE CLICK MOD DATE", note.getLastModifiedDate().toString());

                    Intent modifyNoteIntent = new Intent(view.getContext(), NoteEditorActivity.class);
                    modifyNoteIntent.putExtra(Attributes.ActivityMessageType.NOTE_FROM_ACTIVITY, note);

                    startActivityForResult(modifyNoteIntent, Attributes.ActivityMessageType.NOTE_EDITOR_ACTIVITY);
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
                    mNotesViewAdapter.notifyDataSetChanged();
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
                    mNotesViewAdapter.notifyDataSetChanged();
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

            mNotesViewAdapter.notifyDataSetChanged();
            mNotesView.setAdapter(mNotesViewAdapter);
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

        //mNotesViewAdapter.notifyDataSetChanged();
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
        inflater.inflate(R.menu.notes_menu, menu);
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
                mNotesViewAdapter.notifyDataSetChanged();
                Log.d("MAIN_ACTIVITY_PURGE", "Notes purged");
                return true;
            case R.id.action_main_select_delete:

                Collections.sort(mSelectedItems);

                for(int pos = 0; pos< mSelectedItems.size(); pos++)
                {
                    Note note = (Note) mNotesView.getItemAtPosition(mSelectedItems.get(pos));
                    Utilities.deleteFile(getApplicationContext(), note.getFileName(), mNotebookName);
                    mNotes.remove(mNotes.indexOf(note));

                    for(int decrement = pos+1; decrement< mSelectedItems.size(); decrement++)
                    {
                        mSelectedItems.set(decrement, mSelectedItems.get(decrement)-1);
                    }
                }
                mSelectedItems.clear();
                mSelectMode = false;
                invalidateOptionsMenu();
                mNotesViewAdapter.notifyDataSetChanged();
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
            inflater.inflate(R.menu.notes_menu, menu);
        }
        else if(mSelectMode && menu.size() > 0)
        {
            menu.clear();
            inflater.inflate(R.menu.notes_select_menu, menu);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Attributes.ActivityMessageType.NOTE_EDITOR_ACTIVITY && data != null)
        {
            switch(resultCode)
            {
                case Attributes.ActivityResultMessageType.NEW_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "New note result");

                    mReceivedNote = (Note) data.getSerializableExtra(Attributes.ActivityMessageType.NOTE_FOR_ACTIVITY);

                    AddNoteTask addNote = new AddNoteTask(this);
                    addNote.execute();

                    break;
                }
                case Attributes.ActivityResultMessageType.OVERWRITE_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "Overwrite note result");

                    mReceivedNote = (Note) data.getSerializableExtra(Attributes.ActivityMessageType.NOTE_FOR_ACTIVITY);

                    ModifyNoteTask modifyNote = new ModifyNoteTask(this);
                    modifyNote.execute();

                    break;
                }
                case Attributes.ActivityResultMessageType.DELETE_NOTE_ACTIVITY_RESULT:
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
        startActivityForResult(createNoteIntent, Attributes.ActivityMessageType.NOTE_EDITOR_ACTIVITY);
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
        return mNotesViewAdapter;
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

    public String getNotebookName()
    {
        return mNotebookName;
    }
}
