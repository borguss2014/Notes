package com.example.root.notes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

public class NotesView extends AppCompatActivity
{
    private static boolean              mSelectMode;
    private static ArrayList<Integer>   mSelectedItems;
    private static int                  mClickedNotePosition;

    private RecyclerView                mNotesView;
    private String                      notesDirPath;
    private LoadNotesTask               loadAllNotes;
    private Note                        mReceivedNote;
    private Notebook                    mReceivedNotebook;
    private NotesAdapter                mNotesViewAdapter;

    private final IOHandler             mHandler = new IOHandler(this);

    private Runnable createTestNotes = new Runnable()
    {
        @Override
        public void run()
        {
            try {
                Utilities.createTestNotes(15, notesDirPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        Log.d("DEBUG", "NOTES_ON_CREATE");

        mReceivedNotebook = (Notebook) getIntent()
                .getSerializableExtra(Attributes.ActivityMessageType.NOTEBOOK_FOR_ACTIVITY);

        notesDirPath = getApplicationContext().getFilesDir().toString()
                .concat(File.separator.concat(mReceivedNotebook.getName()));

        //Utilities.deleteAllFiles(this);

        WeakReference<Context> context = new WeakReference<>(getApplicationContext());

        getWindow().getDecorView().setBackgroundColor(Color.argb(255,224,224,224));

        mSelectedItems = new ArrayList<>();
        mSelectMode = false;

        mClickedNotePosition = Attributes.NO_NOTE_CLICKED;

        Comparison.initComparators();
        Comparison.setCurrentComparator(Comparison.getCompareByTitle());
        //Comparison.setOrderDescending();

        mNotesView = (RecyclerView) findViewById(R.id.notes_list_view);

        mNotesViewAdapter = new NotesAdapter(mReceivedNotebook.getNotes());
        mNotesView.setAdapter(mNotesViewAdapter);

        mNotesView.setLayoutManager(new LinearLayoutManager(this));

        loadAllNotes = new LoadNotesTask(0, 4, notesDirPath);
        loadAllNotes.setNotesList(mReceivedNotebook.getNotes());
        loadAllNotes.setNotesAdapter(mNotesViewAdapter);

        mNotesViewAdapter.setClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Currently clicked item
                int item_position = mNotesView.indexOfChild(v);

                if(!mSelectMode)
                {
                    mClickedNotePosition = item_position;

                    Note note = (Note) mNotesViewAdapter.getItemAtPosition(item_position);
                    Log.d("NOTE ON CLICK", note.getFileName());
                    Log.d("NOTE CLICK CR DATE", note.getCreationDate().toString());
                    Log.d("NOTE CLICK MOD DATE", note.getLastModifiedDate().toString());

                    Intent modifyNoteIntent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                    modifyNoteIntent.putExtra(Attributes.ActivityMessageType.NOTE_FROM_ACTIVITY, note);

                    startActivityForResult(modifyNoteIntent, Attributes.ActivityMessageType.NOTE_EDITOR_ACTIVITY);
                }
                else
                {
                    Log.d("Select mode click", Integer.toString(item_position));
                    if(!mSelectedItems.contains(item_position))
                    {
                        Log.d("Toggle item", "Item at position " + Integer.toString(item_position) + " selected");
                        mSelectedItems.add(item_position);
                    }
                    else
                    {
                        Log.d("Toggle item", "Item at position " + Integer.toString(item_position) + " deselected");
                        mSelectedItems.remove(mSelectedItems.indexOf(item_position));
                    }
                    mNotesViewAdapter.notifyDataSetChanged();
                }
            }
        });

        mNotesViewAdapter.setLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                //Currently clicked item
                int item_position = mNotesView.indexOfChild(v);

                if(!mSelectMode)
                {
                    Log.d("Select mode long click", Integer.toString(item_position));
                    mSelectedItems.add(item_position);
                    mSelectMode = true;
                    invalidateOptionsMenu();
                    mNotesViewAdapter.notifyDataSetChanged();
                }

                return false;
            }
        });


        //        Log.d("MAX_NBR_HEIGHT", Integer.toString(mNotebooksView.getChildAt(0).getHeight()));
//        Log.d("MAX_NBR_VIEW_HEIGHT", Integer.toString(mNotebooksView.getHeight()));
//
//        Log.d("MAX_NBR_POSSIBLE_ITEMS", Integer.toString(mNotebooksView.getHeight()/mNotebooksView.getChildAt(0).getHeight()));

//        int listview_max_visible_items = mNotesView.getHeight() / mNotesView.getChildAt(0).getHeight();

        if(savedInstanceState != null)
        {
            Log.d("RESTORE_INSTANCE",  "Restoring notes array list ...");

            int sz = savedInstanceState.getInt("SIZE");

            try
            {
                for(int i=0; i<sz; i++)
                {
                    mReceivedNotebook.getNotes().add(((ArrayList<Note>) savedInstanceState.get("NOTES")).get(i));
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

        outState.putSerializable("NOTES", mReceivedNotebook.getNotes());
        outState.putInt("SIZE", mReceivedNotebook.getNotes().size());
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
                mReceivedNotebook.getNotes().clear();
                mNotesViewAdapter.notifyDataSetChanged();
                Log.d("MAIN_ACTIVITY_PURGE", "Notes purged");
                return true;
            case R.id.action_main_select_delete:

                Collections.sort(mSelectedItems);

                for(int pos = 0; pos< mSelectedItems.size(); pos++)
                {
                    Note note = mNotesViewAdapter.getItemAtPosition(mSelectedItems.get(pos));

                    String notePath = notesDirPath.concat(File.separator.concat(note.getFileName()));

                    Utilities.deleteFile(notePath);

                    mReceivedNotebook.getNotes().remove(mReceivedNotebook.getNotes().indexOf(note));

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

        String notePath = "";

        if(requestCode == Attributes.ActivityMessageType.NOTE_EDITOR_ACTIVITY && data != null)
        {
            mReceivedNote = (Note) data.getSerializableExtra(Attributes.ActivityMessageType.NOTE_FOR_ACTIVITY);

            if(mReceivedNote != null)
            {
                notePath = notesDirPath.concat(File.separator.concat(mReceivedNote.getFileName()));
            }

            switch(resultCode)
            {
                case Attributes.ActivityResultMessageType.NEW_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "New note result");

                    AddNoteTask addNote = new AddNoteTask(mReceivedNote, notePath);
                    addNote.setNotesList(mReceivedNotebook.getNotes());
                    addNote.setHandler(mHandler);

                    addNote.execute();

                    break;
                }
                case Attributes.ActivityResultMessageType.OVERWRITE_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "Overwrite note result");

                    ModifyNoteTask modifyNote = new ModifyNoteTask(mReceivedNote, notePath, mClickedNotePosition);
                    modifyNote.setNotesList(mReceivedNotebook.getNotes());
                    modifyNote.setHandler(mHandler);

                    modifyNote.execute();

                    break;
                }
                case Attributes.ActivityResultMessageType.DELETE_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("MAIN_ACTIVITY_RESULT", "Delete note result");

                    Note toBeDeletedNote = mReceivedNotebook.getNotes().get(mClickedNotePosition);

                    notePath = notesDirPath.concat(File.separator.concat(toBeDeletedNote.getFileName()));

                    DeleteNoteTask deleteNote = new DeleteNoteTask(notePath, mClickedNotePosition);
                    deleteNote.setNotesList(mReceivedNotebook.getNotes());
                    deleteNote.setHandler(mHandler);

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

    public RecyclerView getNotesView()
    {
        return mNotesView;
    }
}
