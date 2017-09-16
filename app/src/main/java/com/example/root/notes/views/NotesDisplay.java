package com.example.root.notes.views;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.root.notes.NoteDisplayRepository;
import com.example.root.notes.NoteRepository;
import com.example.root.notes.NotebookDao;
import com.example.root.notes.NotebookDisplayRepository;
import com.example.root.notes.NotebookRepository;
import com.example.root.notes.NotebooksDisplayPresenter;
import com.example.root.notes.NotebooksDisplayView;
import com.example.root.notes.NotesDisplayPresenter;
import com.example.root.notes.NotesDisplayView;
import com.example.root.notes.PresenterViewModel;
import com.example.root.notes.database.AppDatabase;
import com.example.root.notes.database.DatabaseCreator;
import com.example.root.notes.database.NoteViewModel;
import com.example.root.notes.model.Notebook;
import com.example.root.notes.util.Attributes;
import com.example.root.notes.util.Comparison;
import com.example.root.notes.model.Note;
import com.example.root.notes.functionality.NotesAdapter;
import com.example.root.notes.R;

import java.io.Serializable;
import java.lang.reflect.Array;
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
    private int mDefaultNotebookID;

    private ActionBarDrawerToggle       mDrawerToggle;
    private ArrayAdapter<String>        mDrawerListAdapter;

    private PresenterViewModel<NotesDisplayPresenter> mPresenterViewModel;
    private NotesDisplayPresenter mPresenter;

    private SharedPreferences appPreferences;

    @BindView(R.id.floating_action_add_note)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.notes_list_view)
    RecyclerView mNotesView;

    @BindView(R.id.notes_nav_drawer)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.notes_nav_list)
    ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        ButterKnife.bind(this);

        Log.d("DEBUG", "NOTES_ON_CREATE");

        setTitle("Notes");

        getWindow().getDecorView().setBackgroundColor(Color.argb(255,224,224,224));

        appPreferences = this.getSharedPreferences("com.example.root.notes", Context.MODE_PRIVATE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        addDrawerItems();
        setupDrawer();

        setupView();

        mPresenterViewModel = ViewModelProviders.of(this).get(PresenterViewModel.class);

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

                Collections.sort(mSelectedItems);

                for(int pos = 0; pos< mSelectedItems.size(); pos++)
                {
                    Note note = mNotesViewAdapter.getItemAtPosition(mSelectedItems.get(pos));

                    //String notePath = notesDirPath.concat(File.separator.concat(note.getFileName()));

                    //Utilities.deleteFile(notePath);

                    //mReceivedNotebook.getNotes().getValue().remove(mReceivedNotebook.getNotes().getValue().indexOf(note));

                    //new DeleteNoteDBTask(mAppDatabase.noteModel(), null).execute(note);

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
            mReceivedNoteFromEditor = (Note) data.getSerializableExtra(Attributes.ActivityMessageType.NOTE_FOR_ACTIVITY);

            switch(resultCode)
            {
                case Attributes.ActivityResultMessageType.NEW_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("NOTES_ACTIVITY_RESULT", "New note result");

                    mReceivedNoteFromEditor.setNotebookId(mDefaultNotebookID);

                    mPresenter.addNote(mReceivedNoteFromEditor);

                    break;
                }
                case Attributes.ActivityResultMessageType.OVERWRITE_NOTE_ACTIVITY_RESULT:
                {
                    Log.d("NOTES_ACTIVITY_RESULT", "Overwrite note result");

                    mReceivedNoteFromEditor.setNotebookId(mDefaultNotebookID);

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
        }
        else if(requestCode == Attributes.ActivityMessageType.NOTEBOOKS_LIST_ACTIVITY && data != null)
        {
            mDefaultNotebookID = (int) data.getIntExtra(Attributes.ActivityMessageType.NOTEBOOK_FOR_ACTIVITY, -1);

            Log.d("DEFAULT_NOTEBOOK_ID", "DEFAULT ID : " + mDefaultNotebookID);

            switch(resultCode)
            {
                case Attributes.ActivityResultMessageType.RETURN_NOTEBOOK_ACTIVITY_RESULT:
                {
                    Log.d("NOTES_ACTIVITY_RESULT", "Notebook returned result");

                    mNotesViewAdapter.clear();

                    mPresenter.getNotesForNotebook(mDefaultNotebookID);

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

        mNotesView.setAdapter(mNotesViewAdapter);
        mNotesView.setLayoutManager(new LinearLayoutManager(this));

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

                    Note note = mNotesViewAdapter.getItemAtPosition(item_position);

                    Log.d("NOTE ON CLICK", note.getTitle());
                    Log.d("NOTE ON CLICK", Integer.toString(note.getNotebookId()));

                    openNote(note);
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
        NoteRepository repository = new NoteDisplayRepository(appDatabase);

        NotesDisplayPresenter presenter = new NotesDisplayPresenter(this, repository, AndroidSchedulers.mainThread());

        mPresenterViewModel.setPresenter(presenter);
        mPresenter = presenter;

        Log.d("PresenterInit", "Presenter initialized ... loading notes");

        mDefaultNotebookID = appPreferences.getInt(Attributes.AppPreferences.DEFAULT_NOTEBOOK, -1);

        Log.d("PresenterInit", "Default notebook id : " + mDefaultNotebookID);

        if(mDefaultNotebookID == -1)
        {
            Log.d("PresenterInit", "No default notebook found ... creating");

            Notebook defaultNotebook = new Notebook("Untitled");

            mPresenter.addDefaultNotebook(defaultNotebook);
        }

        mPresenter.getAllNotes();
    }

    @Override
    public void displayNotes(List<Note> notes)
    {
        mNotesViewAdapter.clear();
        mNotesViewAdapter.addItems(notes);
    }

    @Override
    public void displayNoNotes()
    {
        Toast.makeText(getApplicationContext(), "No notes found!", Toast.LENGTH_LONG).show();
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
        Note toBeUpdatedNote = mNotesViewAdapter.getItemAtPosition(mClickedNotePosition);

        mNotesViewAdapter.deleteItem(toBeUpdatedNote);
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
        Note toBeDeletedNote = mNotesViewAdapter.getItemAtPosition(mClickedNotePosition);

        mNotesViewAdapter.deleteItem(toBeDeletedNote);

        Snackbar.make(mNotesView, "Note deleted", Snackbar.LENGTH_LONG)
                .setAction("Revert", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mPresenter.addNote(note);
                    }
                }).show();
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
                    mDefaultNotebookID = appPreferences.getInt(Attributes.AppPreferences.DEFAULT_NOTEBOOK, -1);

                    mNotesViewAdapter.clear();

                    mPresenter.getAllNotes();
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

                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);

                getSupportActionBar().setTitle("Notes");
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);

    }

    public void saveDefaultNotebook(Notebook notebook)
    {
        int defaultNotebookID = notebook.getId();

        mDefaultNotebookID = defaultNotebookID;

        appPreferences.edit().putInt(Attributes.AppPreferences.DEFAULT_NOTEBOOK, defaultNotebookID)
                .apply();
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
}
