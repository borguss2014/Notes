package com.example.root.notes.views;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.root.notes.async_tasks.notebook.AddNotebookDBTask;
import com.example.root.notes.database.AppDatabase;
import com.example.root.notes.database.DatabaseCreator;
import com.example.root.notes.database.NotebookViewModel;
import com.example.root.notes.util.Attributes;
import com.example.root.notes.model.Notebook;
import com.example.root.notes.functionality.NotebooksAdapter;
import com.example.root.notes.R;
import com.example.root.notes.util.Utilities;
import com.example.root.notes.async_tasks.notebook.LoadNotebooksTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NotebooksView extends AppCompatActivity implements LifecycleRegistryOwner
{

    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    private WeakReference<NotebooksView>    context;
    private LiveData<List<Notebook>>        mNotebooks;
    private AppDatabase                     appDatabase;
    private ListView                        mNotebooksView;
    private String                          notebooksDirPath;
    private LoadNotebooksTask               loadAllNotebooks;
    private String                          dialogNotebookName;
    private NotebookViewModel               mNotebooksViewModel;
    private NotebooksAdapter                mNotebooksViewAdapter;

    //private static ArrayList<Note> mNotesTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebooks_list);

        Log.d("DEBUG", "NOTEBOOKS_ON_CREATE");

        context = new WeakReference<>(this);

        DatabaseCreator dbCreator = DatabaseCreator.getInstance();

        dbCreator.isDatabaseCreated().observe(NotebooksView.this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(@Nullable Boolean dbCreated)
            {
                if (dbCreated != null && dbCreated)
                {
                    Log.d("DatabaseCreator", "Change detected ... retrieving notebooks");

                    appDatabase = dbCreator.getDatabase();
                    mNotebooksViewModel.getNotebooksList().setValue(appDatabase.notebookModel().getAllNotebooks().getValue());
                }
            }
        });

        MutableLiveData<List<Notebook>> notebooksLst = new MutableLiveData<>();
        notebooksLst.setValue(new ArrayList<>());

        NotebookViewModel.Factory notebookViewModelFactory = new NotebookViewModel.Factory(
                getApplication(), notebooksLst
        );

        mNotebooksViewModel = ViewModelProviders.of(this, notebookViewModelFactory).get(NotebookViewModel.class);
        mNotebooksViewModel.getNotebooksList().observe(NotebooksView.this, new Observer<List<Notebook>>()
        {
            @Override
            public void onChanged(@Nullable List<Notebook> notebooks)
            {
                Log.d("NotebooksViewModel", "Change detected ... refreshing notebooks adapter");
                mNotebooksViewAdapter.notifyDataSetChanged();
            }
        });

        //notebooksDirPath = getApplicationContext().getFilesDir().toString();

        getWindow().getDecorView().setBackgroundColor(Color.argb(255,224,224,224));

        mNotebooksView = (ListView) findViewById(R.id.notebooks_list_view);

        if(mNotebooksViewModel.getNotebooksList().getValue() != null)
        {
            mNotebooksViewAdapter = new NotebooksAdapter(context.get(), R.layout.notebooks_adapter_row, mNotebooksViewModel.getNotebooksList().getValue());
            mNotebooksView.setAdapter(mNotebooksViewAdapter);
        }

//        loadAllNotebooks = new LoadNotebooksTask(notebooksDirPath);
//        loadAllNotebooks.setNotebooksList(mNotebooks);
//        loadAllNotebooks.setNotebooksAdapter(mNotebooksViewAdapter);

        mNotebooksView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Notebook notebook = (Notebook) parent.getItemAtPosition(position);

                //setTempNotes(notebook.getNotes());

                Intent notesView= new Intent(view.getContext(), NotesView.class);
                notesView.putExtra(Attributes.ActivityMessageType.NOTEBOOK_FOR_ACTIVITY, notebook);

                startActivityForResult(notesView, Attributes.ActivityMessageType.NOTES_LIST_ACTIVITY);
            }
        });

        mNotebooksView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
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
                Utilities.deleteAllFolders(context.get());
                mNotebooks.getValue().clear();
                mNotebooksViewAdapter.notifyDataSetChanged();
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

                Notebook newNotebook = new Notebook(dialogNotebookName);

//                String notebookPath = notebooksDirPath.concat(File.separator.concat(newNotebook.getName()));
//
//                AddNotebookFileTask addNotebook = new AddNotebookFileTask(newNotebook, notebookPath);
//                addNotebook.setNotebooksList(mNotebooks.getValue());
//                addNotebook.setNotebooksAdapter(mNotebooksViewAdapter);
//
//                addNotebook.execute();

                AddNotebookDBTask addNotebook = new AddNotebookDBTask(appDatabase);
                addNotebook.execute(newNotebook);
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
        return mNotebooks.getValue();
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
