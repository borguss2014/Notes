package com.example.root.notes.views;

import android.app.AlertDialog;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.root.notes.NoteDisplayRepository;
import com.example.root.notes.NoteRepository;
import com.example.root.notes.NotesDisplayPresenter;
import com.example.root.notes.NotesDisplayView;
import com.example.root.notes.PresenterViewModel;
import com.example.root.notes.RecyclerItemListener;
import com.example.root.notes.database.AppDatabase;
import com.example.root.notes.database.DatabaseCreator;
import com.example.root.notes.model.Notebook;
import com.example.root.notes.util.Attributes;
import com.example.root.notes.util.Comparison;
import com.example.root.notes.model.Note;
import com.example.root.notes.functionality.NotesAdapter;
import com.example.root.notes.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class NotesDisplay extends AppCompatActivity implements LifecycleRegistryOwner, NotesDisplayView
{
    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    private static boolean              mSelectMode;
    private static ArrayList<Integer>   mSelectedItems;
    private static int                  mClickedNotePosition;

    private Note                        mReceivedNoteFromEditor;
    private NotesAdapter                mNotesViewAdapter;

    private int mSelectedNotebookID;

    private ActionBarDrawerToggle       mDrawerToggle;
    private ArrayAdapter<String>        mDrawerListAdapter;

    private PresenterViewModel<NotesDisplayPresenter> mPresenterViewModel;
    private NotesDisplayPresenter mPresenter;

    @BindView(R.id.floating_action_add_note)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.notes_list_view)
    RecyclerView mNotesView;

    @BindView(R.id.notes_nav_drawer)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.notes_nav_list)
    ListView mDrawerList;

    @BindView(R.id.notes_list_loading)
    ProgressBar mProgressBar;

    @BindView(R.id.notes_list_empty)
    TextView mEmptyNotesListMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        ButterKnife.bind(this);

        Log.d("DEBUG", "NOTES_ON_CREATE");

        setTitle("Notes");

        getWindow().getDecorView().setBackgroundColor(Color.argb(255,224,224,224));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        addDrawerItems();
        setupDrawer();

        setupView();

        mPresenterViewModel = ViewModelProviders.of(this).get(PresenterViewModel.class);

        setViewState(Attributes.ViewState.LOADING);

        if(mPresenterViewModel.getPresenter() == null)
        {
            Log.d("NotesDisplay", "Presenter null ... preparing for DB init");

            initializeDatabase();
        }
        else
        {
            Log.d("NotesDisplay", "Presenter found ... recreating");

            mPresenter = mPresenterViewModel.getPresenter();
            mPresenter.attachLifecycle(getLifecycle());

            mSelectedNotebookID = mPresenter.getDefaultNotebookID();
        }


        mSelectedItems = new ArrayList<>();
        mSelectMode = false;

        mClickedNotePosition = Attributes.NO_NOTE_CLICKED;

        Comparison.initComparators();
        Comparison.setCurrentComparator(Comparison.getCompareByTitle());
        //Comparison.setOrderDescending();
    }

    @OnClick(R.id.floating_action_add_note)
    public void onClickAction(View view)
    {
        Log.d("OnClickNoteActivity", "Clicking " + getResources().getResourceEntryName(view.getId()));

        if(view.getId() == R.id.floating_action_add_note)
        {
            createNote();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {

        super.onSaveInstanceState(outState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance()
    {
        Log.d("NotesDisplay", "Retaining adapter data");
        return mNotesViewAdapter.getInternalData();
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

        mPresenter.detachLifecycle(getLifecycle());
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();

        Log.d("DEBUG", "ON_RESTART");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        switch(item.getItemId())
        {
            case R.id.action_main_add_note:
                createNote();
                return true;
            case R.id.action_main_purge:
                //Utilities.deleteAllFiles(getApplicationContext());

                //new PurgeNotesDBTask(mAppDatabase.noteModel()).execute();

                Log.d("MAIN_ACTIVITY_PURGE", "Notes purged");

                return true;
            case R.id.action_main_select_delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                TextView text = new TextView(getApplicationContext());
                text.setText("Are you sure about deleting these notes?");
                text.setGravity(Gravity.CENTER);

                builder.setView(text);
                builder.setTitle("Confirm notes deletion");
                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        deleteSelectedNotes();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                    }
                });

                builder.setCancelable(true);

                builder.show();

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
                    Log.d("NOTES_ACTIVITY_RESULT", "New note result");

                    mReceivedNoteFromEditor = (Note) data.getSerializableExtra(Attributes.ActivityMessageType.NOTE_FOR_ACTIVITY);

                    mReceivedNoteFromEditor.setNotebookId(mSelectedNotebookID);

                    mPresenter.addNote(mReceivedNoteFromEditor);

                    break;
                }
                case Attributes.ActivityResultMessageType.OVERWRITE_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("NOTES_ACTIVITY_RESULT", "Overwrite note result");

                    mReceivedNoteFromEditor = (Note) data.getSerializableExtra(Attributes.ActivityMessageType.NOTE_FOR_ACTIVITY);
                    Log.d("NOTES_ACTIVITY_RESULT", "Overwrite note id: " + mReceivedNoteFromEditor.getId());
                    //mReceivedNoteFromEditor.setNotebookId(mSelectedNotebookID);

                    mPresenter.updateNote(mReceivedNoteFromEditor);

                    break;
                }
                case Attributes.ActivityResultMessageType.DELETE_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("NOTES_ACTIVITY_RESULT", "Delete note result");

                    Note toBeDeletedNote = mNotesViewAdapter.getItemAtPosition(mClickedNotePosition);

                    mPresenter.deleteNote(toBeDeletedNote);

                    mReceivedNoteFromEditor = toBeDeletedNote;

                    break;
                }
            }

            Log.d("RECEIVED NOTE", "Title: " + mReceivedNoteFromEditor.getTitle());
            Log.d("RECEIVED NOTE", "ID: " + mReceivedNoteFromEditor.getId());
            Log.d("RECEIVED NOTE", "Notebook ID: " + mReceivedNoteFromEditor.getNotebookId());
        }
        else if(requestCode == Attributes.ActivityMessageType.NOTEBOOKS_LIST_ACTIVITY && data != null)
        {
            mSelectedNotebookID = (int) data.getIntExtra(Attributes.ActivityMessageType.NOTEBOOK_FOR_ACTIVITY, -1);

            Log.d("CLICKED_NOTEBOOK", "ID : " + mSelectedNotebookID);

            switch(resultCode)
            {
                case Attributes.ActivityResultMessageType.RETURN_NOTEBOOK_ACTIVITY_RESULT:
                {
                    Log.d("NOTES_ACTIVITY_RESULT", "Notebook returned result");

                    mNotesViewAdapter.clear();

                    mPresenter.getNotesForNotebook(mSelectedNotebookID);

                    setViewState(Attributes.ViewState.LOADING);

                    break;
                }
            }
        }
    }

    private void createNote()
    {
        Note newNote = new Note();

        openNote(newNote);
    }

    public static ArrayList<Integer> retrieveSelectedItems()
    {
        return mSelectedItems;
    }

    public static boolean selectModeStatus()
    {
        return mSelectMode;
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
        mReceivedNoteFromEditor = note;
    }

    public void openNote(Note note)
    {
        Intent noteIntent = new Intent(getApplicationContext(), NoteEditorDisplay.class);
        noteIntent.putExtra(Attributes.ActivityMessageType.NOTE_FROM_ACTIVITY, (Serializable) note);

        startActivityForResult(noteIntent, Attributes.ActivityMessageType.NOTE_EDITOR_ACTIVITY);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }

    @Override
    public void setupView()
    {
        List<Note> notesList = (List<Note>) getLastCustomNonConfigurationInstance();

        if(notesList == null)
        {
            mNotesViewAdapter = new NotesAdapter(getApplicationContext(), new ArrayList<>());
        }
        else
        {
            mNotesViewAdapter = new NotesAdapter(getApplicationContext(), notesList);
        }

        mNotesView.setHasFixedSize(true);
        mNotesView.setItemAnimator(new DefaultItemAnimator());
        mNotesView.setLayoutManager(new LinearLayoutManager(this));
        mNotesView.setAdapter(mNotesViewAdapter);

        mNotesView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mNotesView,
                new RecyclerItemListener.RecyclerTouchListener()
                {
                    @Override
                    public void onClickItem(View v, int position)
                    {
                        if(!mSelectMode)
                        {
                            mClickedNotePosition = position;

                            Note note = mNotesViewAdapter.getItemAtPosition(position);

                            Log.d("NOTE ON CLICK", note.getTitle());
                            Log.d("NOTE ON CLICK", Integer.toString(note.getNotebookId()));

                            openNote(note);
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

                    @Override
                    public void onLongClickItem(View v, int position)
                    {
                        if(!mSelectMode)
                        {
                            Log.d("Select mode long click", Integer.toString(position));
                            mSelectedItems.add(position);
                            mSelectMode = true;
                            invalidateOptionsMenu();
                            mNotesViewAdapter.notifyDataSetChanged();
                        }
                    }
                })
        );
    }

    public void initializeDatabase()
    {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance();

        dbCreator.isDatabaseCreated().observe(NotesDisplay.this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(@Nullable Boolean dbCreated)
            {
                if (dbCreated != null && dbCreated)
                {
                    AppDatabase appDatabase = dbCreator.getDatabase();

//                    appDatabase.noteModel().nukeNotebooks();
//                    appDatabase.noteModel().nukeNotes();

                    Log.d("DatabaseCreator", "Database retrieved ... initializing presenter");
                    initializePresenter(appDatabase);
                }
            }
        });

        dbCreator.createDb(getApplication());
    }

    @Override
    public void initializePresenter(AppDatabase appDatabase)
    {
//        NoteRepository repository = new NoteDisplayRepository(appDatabase,
//                getApplicationContext().getSharedPreferences("com.example.root.notes", Context.MODE_PRIVATE));

        NoteRepository repository = new NoteDisplayRepository(appDatabase,
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        NotesDisplayPresenter presenter = new NotesDisplayPresenter(this, repository, AndroidSchedulers.mainThread());

        mPresenterViewModel.setPresenter(presenter);
        mPresenter = presenter;

        Log.d("PresenterInit", "Presenter initialized ... loading notes");

        //mSelectedNotebookID = appPreferences.getInt(Attributes.AppPreferences.DEFAULT_NOTEBOOK, -1);
        mSelectedNotebookID = mPresenter.getDefaultNotebookID();

        Log.d("PresenterInit", "Default notebook id : " + mSelectedNotebookID);

        if(mSelectedNotebookID == Attributes.AppPreferences.NO_DEFAULT_NOTEBOOK)
        {
            Log.d("PresenterInit", "No default notebook found ... creating");

            Notebook defaultNotebook = new Notebook("Default notebook");

            mPresenter.addDefaultNotebook(defaultNotebook);
        }

        mPresenter.getAllNotes();
    }

    @Override
    public void displayNotes(List<Note> notes)
    {
        mNotesViewAdapter.clear();
        mNotesViewAdapter.addItems(notes);

        setViewState(Attributes.ViewState.LOADED);
    }

    @Override
    public void displayNoNotes()
    {
        setViewState(Attributes.ViewState.EMPTY);
    }

    @Override
    public void displayNoteAdded(Note note)
    {
        mNotesViewAdapter.addItem(note);

        Snackbar.make(mNotesView, "Note added", Snackbar.LENGTH_LONG)
                .setAction("Open note", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        openNote(note);
                    }
                }).show();

        if(mNotesViewAdapter.getItemCount() == 1)
        {
            setViewState(Attributes.ViewState.LOADED);
        }
    }

    @Override
    public void displayNoteNotAdded(Note note)
    {
        Snackbar.make(mNotesView, "Error : Note insertion rejected", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mPresenter.addNote(note);
                    }
                }).show();
    }

    @Override
    public void displayNoteUpdated(Note note)
    {
        Log.d("UPDATED_NOTE", "Note title: " + note.getTitle());
        Log.d("UPDATED_NOTE", "Note id: " + note.getId());
        Log.d("UPDATED_NOTE", "Note notebook id: " + note.getNotebookId());

//        Note toBeUpdatedNote = mNotesViewAdapter.getItemAtPosition(mClickedNotePosition);
//
//        mNotesViewAdapter.deleteItem(toBeUpdatedNote);
//        mNotesViewAdapter.addItem(note);
//        mNotesViewAdapter.notifyDataSetChanged();

        mNotesViewAdapter.deleteItemAtPosition(mClickedNotePosition);
        mNotesViewAdapter.addItem(note);

        Snackbar.make(mNotesView, "Note updated", Snackbar.LENGTH_LONG)
                .setAction("Open note", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        openNote(note);
                    }
                }).show();
    }

    @Override
    public void displayNoteNotUpdated(Note note)
    {
        Snackbar.make(mNotesView, "Error : Note update rejected", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mPresenter.updateNote(note);
                    }
                }).show();
    }

    @Override
    public void displayNoteDeleted(Note note)
    {
        mNotesViewAdapter.deleteItemAtPosition(mClickedNotePosition);

        Snackbar.make(mNotesView, "Note deleted", Snackbar.LENGTH_LONG)
                .setAction("Revert", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mPresenter.addNote(note);
                    }
                }).show();

        if(mNotesViewAdapter.getItemCount() == 0)
        {
            setViewState(Attributes.ViewState.EMPTY);
        }
    }

    @Override
    public void displayNoteNotDeleted(Note note)
    {
        Snackbar.make(mNotesView, "Error : Note deletion rejected", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mPresenter.deleteNote(note);
                    }
                }).show();
    }

    @Override
    public void displayNotesDeleted()
    {
        for(int notePos = 0; notePos< mSelectedItems.size(); notePos++)
        {
            int position = mSelectedItems.get(notePos);

            mNotesViewAdapter.deleteItemAtPosition(position);

            for(int decrementPos = notePos + 1; decrementPos < mSelectedItems.size(); decrementPos++)
            {
                mSelectedItems.set(decrementPos, mSelectedItems.get(decrementPos)-1);
            }
        }

        mSelectedItems.clear();
        mSelectMode = false;
        invalidateOptionsMenu();
        mNotesViewAdapter.notifyDataSetChanged();

        Snackbar.make(mNotesView, "Notes successfully deleted", Snackbar.LENGTH_LONG).show();

        if(mNotesViewAdapter.getItemCount() == 0)
        {
            setViewState(Attributes.ViewState.EMPTY);
        }
    }

    @Override
    public void displayNotesNotDeleted()
    {
        Snackbar.make(mNotesView, "Couldn't delete notes", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        deleteSelectedNotes();
                    }
                }).show();
    }

    private void deleteSelectedNotes()
    {
        Collections.sort(mSelectedItems);

        List<Integer> noteIds = new ArrayList<>();

        for(int i=0; i < mSelectedItems.size(); i++)
        {
            int position = mSelectedItems.get(i);

            noteIds.add(mNotesViewAdapter.getItemAtPosition(position).getId());
        }

        mPresenter.deleteNotesWithIds(noteIds);
    }

    private void addDrawerItems()
    {
        String[] itemsArray = { "All notes", "Notebooks" };
        mDrawerListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemsArray);
        mDrawerList.setAdapter(mDrawerListAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                String item = (String) adapterView.getItemAtPosition(position);

                if(item.equals(Attributes.NavigationDrawerList.NAV_ITEM_ALL_NOTES))
                {
                    mSelectedNotebookID = mPresenter.getDefaultNotebookID();

                    mNotesViewAdapter.clear();

                    mPresenter.getAllNotes();

                    setViewState(Attributes.ViewState.LOADING);
                }
                else if(item.equals(Attributes.NavigationDrawerList.NAV_ITEM_NOTEBOOKS))
                {
                    Intent notebooksActivity = new Intent(getApplicationContext(), NotebooksDisplay.class);
                    startActivityForResult(notebooksActivity , Attributes.ActivityMessageType.NOTEBOOKS_LIST_ACTIVITY);
                }

                mDrawerLayout.closeDrawers();
            }
        });
    }

    private void setupDrawer()
    {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.nav_drawer_open, R.string.nav_drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);

                //getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);

                //getSupportActionBar().setTitle("Notes");
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

    }

    public void saveDefaultNotebook(Notebook notebook)
    {
        int defaultNotebookID = notebook.getId();

        mSelectedNotebookID = defaultNotebookID;

        mPresenter.updateDefaultNotebookID(defaultNotebookID);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setViewState(int state)
    {
        switch (state)
        {
            case Attributes.ViewState.EMPTY:
            {
                mNotesView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);

                mEmptyNotesListMessage.setVisibility(View.VISIBLE);
                break;
            }
            case Attributes.ViewState.LOADING:
            {
                mNotesView.setVisibility(View.GONE);
                mEmptyNotesListMessage.setVisibility(View.GONE);

                mProgressBar.setVisibility(View.VISIBLE);
                break;
            }
            case Attributes.ViewState.LOADED:
            {
                mEmptyNotesListMessage.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);

                mNotesView.setVisibility(View.VISIBLE);
                break;
            }
        }
    }
}
