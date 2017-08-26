package com.example.root.notes.views;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.root.notes.async_tasks.notebook.AddNotebookDBTask;
import com.example.root.notes.async_tasks.notebook.PurgeNotebooksDBTask;
import com.example.root.notes.database.AppDatabase;
import com.example.root.notes.database.DatabaseCreator;
import com.example.root.notes.database.NotebookViewModel;
import com.example.root.notes.database.QueryResultLiveData;
import com.example.root.notes.util.Attributes;
import com.example.root.notes.model.Notebook;
import com.example.root.notes.functionality.NotebooksAdapter;
import com.example.root.notes.R;
import com.example.root.notes.async_tasks.notebook.LoadNotebooksTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NotebooksView extends AppCompatActivity implements LifecycleRegistryOwner
{

    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    private WeakReference<NotebooksView>    context;
    private AppDatabase                     appDatabase;
    private RecyclerView                    mNotebooksView;
    private String                          notebooksDirPath;
    private LoadNotebooksTask               loadAllNotebooks;
    private String                          dialogNotebookName;
    private NotebookViewModel               mNotebooksViewModel;
    private QueryResultLiveData             mQueryResultLiveData;
    private NotebooksAdapter                mNotebooksViewAdapter;

    //private static ArrayList<Note> mNotesTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebooks_list);

        Log.d("DEBUG", "NOTEBOOKS_ON_CREATE");

        setTitle("Notebooks");

        context = new WeakReference<>(this);

        FloatingActionButton floatingActionButton = findViewById(R.id.floating_action_add_notebook);
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                createNotebook();
            }
        });

        DatabaseCreator dbCreator = DatabaseCreator.getInstance();

        dbCreator.isDatabaseCreated().observe(NotebooksView.this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(@Nullable Boolean dbCreated)
            {
                if (dbCreated != null && dbCreated)
                {
                    Log.d("DatabaseCreator", "Database created");

                    appDatabase = dbCreator.getDatabase();
                }
            }
        });

//        NotebookViewModel.Factory notebookViewModelFactory = new NotebookViewModel.Factory(
//                getApplication(), notebooksLst
//        );

//        mNotebooksViewModel = ViewModelProviders.of(this, notebookViewModelFactory).get(NotebookViewModel.class);
        mNotebooksViewModel = ViewModelProviders.of(this).get(NotebookViewModel.class);
        mNotebooksViewModel.getNotebooksList().observe(NotebooksView.this, new Observer<List<Notebook>>()
        {
            @Override
            public void onChanged(@Nullable List<Notebook> notebooks)
            {
                Log.d("NotebooksViewModel", "Data changed ... refreshing notebooks adapter");

                mNotebooksViewAdapter.clear();
                mNotebooksViewAdapter.addItems(notebooks);
                mNotebooksViewAdapter.notifyDataSetChanged();
            }
        });

        mQueryResultLiveData = new QueryResultLiveData();
        mQueryResultLiveData.observe(NotebooksView.this, new Observer<Object>() {
            @Override
            public void onChanged(@Nullable Object queryResult) {
                if(queryResult != null && !queryResult.equals(-1))
                {
                    Snackbar.make(mNotebooksView, "Notebook added", Snackbar.LENGTH_LONG)
                            .setAction("Open notebook", new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {

                                }
                            }).show();
                }
                else if(queryResult != null && queryResult.equals(-1))
                {
                    Snackbar.make(mNotebooksView, "Error : Notebook insertion rejected", Snackbar.LENGTH_LONG)
                            .setAction("Retry", new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {

                                }
                            }).show();
                }
                else if(queryResult == null)
                {
                    Snackbar.make(mNotebooksView, "Error : An issue has occured", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        //notebooksDirPath = getApplicationContext().getFilesDir().toString();

        getWindow().getDecorView().setBackgroundColor(Color.argb(255,224,224,224));

        mNotebooksView = findViewById(R.id.notebooks_list_view);

        mNotebooksViewAdapter = new NotebooksAdapter(getApplicationContext(), new ArrayList<Notebook>());
        mNotebooksView.setAdapter(mNotebooksViewAdapter);

        mNotebooksView.setLayoutManager(new LinearLayoutManager(this));


//        loadAllNotebooks = new LoadNotebooksTask(notebooksDirPath);
//        loadAllNotebooks.setNotebooksList(mNotebooks);
//        loadAllNotebooks.setNotebooksAdapter(mNotebooksViewAdapter);

        mNotebooksView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && floatingActionButton.isShown()) {
                    floatingActionButton.hide();
                }

                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    floatingActionButton.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        mNotebooksViewAdapter.setClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int itemPosition = mNotebooksView.indexOfChild(view);

                Notebook notebook = mNotebooksViewAdapter.getItemAtPosition(itemPosition);

                //setTempNotes(notebook.getNotes());

                Intent notesView = new Intent(getApplicationContext(), NotesView.class);
                notesView.putExtra(Attributes.ActivityMessageType.NOTEBOOK_FOR_ACTIVITY, notebook.getId());

                startActivityForResult(notesView, Attributes.ActivityMessageType.NOTES_LIST_ACTIVITY);
            }
        });

        mNotebooksViewAdapter.setLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                return false;
            }
        });

//        mNotebooksView.setOnScrollListener(new EndlessScrollListener()
//        {
//            @Override
//            public void onLoadData()
//            {
//                final int[] done = {0};
//                Runnable genDir = new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        for(int i=0; i<5; i++)
//                        {
//                            Random random = new Random();
//
//                            Notebook newNotebook = new Notebook(Integer.toString(random.nextInt()));
//                            String notebookPath = notebooksDirPath.concat(File.separator.concat(newNotebook.getName()));
//
//                            if(mNotebooks != null)
//                            {
//                                //Add notebook to the adapter list and resort
//                                mNotebooks.add(newNotebook);
//
//                                Collections.sort(mNotebooks, new Comparator<Notebook>()
//                                {
//                                    @Override
//                                    public int compare(Notebook o1, Notebook o2) {
//                                        return o1.getName().compareTo(o2.getName());
//                                    }
//                                });
//                            }
//
//                            //Save the notebook as a directory in persistent storage
//                            Utilities.createDirectory(notebookPath);
//                        }
//                        done[0] = 1;
//                    }
//                };
//
//                Thread t = new Thread(genDir);
//                t.setPriority(Thread.MIN_PRIORITY);
//                t.start();
//
//                while(done[0] == 0)
//                {
//                }
//                mNotebooksViewAdapter.notifyDataSetChanged();
//
//                Log.d("SCROLL_LISTENER", "FINISHED LOADING DATA");
//            }
//        });

        //loadAllNotebooks.execute();

        dbCreator.createDb(getApplicationContext());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
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
    protected void onDestroy()
    {
        super.onDestroy();

        Log.d("DEBUG", "ON_DESTROY");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("DEBUG", "ON_RESTART");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notebooks_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_add_notebook:
                createNotebook();
                return true;
            case R.id.action_purge_notebooks:
                //Utilities.deleteAllFolders(context.get());
                new PurgeNotebooksDBTask(appDatabase.notebookModel()).execute();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void createNotebook()
    {
        dialogNotebookName = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set notebook name");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialogNotebookName = input.getText().toString();

                if(dialogNotebookName.equals(""))
                {
                    dialogNotebookName = "Untitled";
                }


//                String notebookPath = notebooksDirPath.concat(File.separator.concat(newNotebook.getName()));
//
//                AddNotebookFileTask addNotebook = new AddNotebookFileTask(newNotebook, notebookPath);
//                addNotebook.setNotebooksList(mNotebooks.getValue());
//                addNotebook.setNotebooksAdapter(mNotebooksViewAdapter);
//
//                addNotebook.execute();

                Notebook newNotebook = new Notebook(dialogNotebookName);
                new AddNotebookDBTask(appDatabase.notebookModel(), mQueryResultLiveData).execute(newNotebook);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public List<Notebook> getNotebooks()
    {
        return mNotebooksViewModel.getNotebooksList().getValue();
    }

    public NotebooksAdapter getNotebooksViewAdapter()
    {
        return mNotebooksViewAdapter;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }

//    public static void setTempNotes(ArrayList<Note> tempNotes)
//    {
//        mNotesTemp = tempNotes;
//    }
//
//    public static ArrayList<Note> getTempNotes()
//    {
//        return mNotesTemp;
//    }
}
