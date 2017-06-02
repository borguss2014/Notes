package com.example.root.notes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
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

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{
    ListView mListNotes;

    private ArrayList<Note> mNotes;
    private static NotesAdapter mNotesAdapter;

    private static boolean mSelectMode;
    private static ArrayList<Integer> mSelectedItems;

    private static int mCurrentlyClickedNote;

    private static Note mReceivedNote;

    private Context mContext;

    private static class IOHandler extends Handler
    {
        private final WeakReference<MainActivity> mActivity;

        private IOHandler(MainActivity activity)
        {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();

            if(activity == null) return;

            switch(msg.what)
            {
                case Utilities.NOTE_ADDED:
                {
                    Log.d("HANDLER", "NEW NOTE ADDED");

                    mReceivedNote = null;
                    mNotesAdapter.notifyDataSetChanged();
                    break;
                }
                case Utilities.NOTE_MODIFIED:
                {
                    mReceivedNote = null;
                    mCurrentlyClickedNote = Utilities.NO_NOTE_CLICKED;
                    mNotesAdapter.notifyDataSetChanged();
                    break;
                }
                case Utilities.NOTE_DELETED:
                {
                    boolean isDeleted = (boolean) msg.obj;

                    if(isDeleted)
                    {
                        Toast.makeText(activity.getApplicationContext(), "Note successfully deleted" , Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(activity.getApplicationContext(), "An error occurred. Note couldn't be deleted" , Toast.LENGTH_SHORT).show();
                    }

                    mCurrentlyClickedNote = Utilities.NO_NOTE_CLICKED;
                    mNotesAdapter.notifyDataSetChanged();
                    break;
                }
            }

        }
    }

    private final IOHandler mHandler = new IOHandler(this);

    private Runnable createTestNotes = new Runnable() {
        @Override
        public void run()
        {
            Utilities.createTestNotes(mContext, 8000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("DEBUG", "ON_CREATE");

        mContext = this;

        getWindow().getDecorView().setBackgroundColor(Color.argb(255,224,224,224));

        mListNotes = (ListView) findViewById(R.id.main_notes_list_view);
        mSelectedItems = new ArrayList<>();
        mSelectMode = false;

        mCurrentlyClickedNote = Utilities.NO_NOTE_CLICKED;

        Comparison.initComparators();
        Comparison.setCurrentComparator(Comparison.getCompareByTitle());
        //Comparison.setOrderDescending();

        mNotes = new ArrayList<>();
        mNotesAdapter = new NotesAdapter(getApplicationContext(), R.layout.notes_adapter_row, mNotes);


        mListNotes.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!mSelectMode)
                {
                    mCurrentlyClickedNote = position;

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

        mListNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
            mListNotes.setAdapter(mNotesAdapter);
        }
        else
        {
            LoadFilesTask loadAllNotes = new LoadFilesTask(this);
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
                    Note note = (Note) mListNotes.getItemAtPosition(mSelectedItems.get(pos));
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

        if(requestCode == Utilities.NOTE_ACTIVITY)
        {
            switch(resultCode)
            {
                case Utilities.NEW_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "New note result");

                    mReceivedNote = (Note) data.getSerializableExtra("NEW_NOTE");

                    AddNoteOperation addNote = new AddNoteOperation(this, mReceivedNote);

                    Thread noteAdditionThread = new Thread(addNote);
                    noteAdditionThread.start();
                    noteAdditionThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);

                    break;
                }
                case Utilities.OVERWRITE_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "Overwrite note result");

                    mReceivedNote = (Note) data.getSerializableExtra("MODIFIED_NOTE");

                    ModifyNoteOperation modifyNote = new ModifyNoteOperation(this, mReceivedNote);

                    Thread noteModificationThread = new Thread(modifyNote);
                    noteModificationThread.start();
                    noteModificationThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);

                    break;
                }
                case Utilities.DELETE_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "Delete note result");

                    DeleteNoteOperation deleteNote = new DeleteNoteOperation(this);

                    Thread noteDeletionThread = new Thread(deleteNote);
                    noteDeletionThread.start();
                    noteDeletionThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);

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
        return mCurrentlyClickedNote;
    }

    public NotesAdapter getAdapter()
    {
        return mNotesAdapter;
    }
}
