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
import android.widget.Toast;

import com.example.root.notes.BaseView;
import com.example.root.notes.NotebookDisplayRepository;
import com.example.root.notes.NotebooksDisplayPresenter;
import com.example.root.notes.NotebooksDisplayView;
import com.example.root.notes.PresenterViewModel;
import com.example.root.notes.NotebookRepository;
import com.example.root.notes.database.AppDatabase;
import com.example.root.notes.database.DatabaseCreator;
import com.example.root.notes.util.Attributes;
import com.example.root.notes.model.Notebook;
import com.example.root.notes.functionality.NotebooksAdapter;
import com.example.root.notes.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class NotebooksDisplay extends AppCompatActivity implements LifecycleRegistryOwner, NotebooksDisplayView
{

    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    private NotebooksAdapter                mNotebooksViewAdapter;
    private Notebook                        mLastInsertedNotebook;
    private AlertDialog.Builder             mAlertDialog;

    private PresenterViewModel mPresenterViewModel;
    private NotebooksDisplayPresenter mPresenter;

    @BindView(R.id.floating_action_add_notebook)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.notebooks_list_view)
    RecyclerView mNotebooksView;

    //    private String                          notebooksDirPath;
    //    private LoadNotebooksTask               loadAllNotebooks;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebooks_list);
        ButterKnife.bind(this);

        setTitle("Notebooks");

        Log.d("DEBUG", "NOTEBOOKS_ON_CREATE");

        mPresenterViewModel = ViewModelProviders.of(this).get(PresenterViewModel.class);

        if(mPresenterViewModel.getPresenter() == null)
        {
            Log.d("NotebooksDisplay", "Presenter null ... preparing for DB init");

            initializeDatabase();
        }
        else
        {
            Log.d("NotebooksDisplay", "Presenter found ... recreating");

            mPresenter = mPresenterViewModel.getPresenter();
            mPresenter.attachLifecycle(getLifecycle());
        }

        mNotebooksViewAdapter = new NotebooksAdapter(getApplicationContext(), new ArrayList<Notebook>());

        mNotebooksView.setAdapter(mNotebooksViewAdapter);
        mNotebooksView.setLayoutManager(new LinearLayoutManager(this));

        mAlertDialog = new AlertDialog.Builder(this);

//        NotebookViewModel.Factory notebookViewModelFactory = new NotebookViewModel.Factory(
//                getApplication(), notebooksLst
//        );

        //notebooksDirPath = getApplicationContext().getFilesDir().toString();

        getWindow().getDecorView().setBackgroundColor(Color.argb(255,224,224,224));


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
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
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

                openNotebook(notebook);
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
    }

    @OnClick(R.id.floating_action_add_notebook)
    public void onClickAction(View view)
    {
        Log.d("OnClickNotebookActivity", "Clicking " + getResources().getResourceEntryName(view.getId()));

        if(view.getId() == R.id.floating_action_add_notebook)
        {
            createNotebook();
        }
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

        mPresenter.unsubscribe();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        Log.d("DEBUG", "ON_DESTROY");

        mPresenter.detachLifecycle(getLifecycle());
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
                //new PurgeNotebooksDBTask(appDatabase.notebookModel()).execute();
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

    private void createNotebook()
    {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        mAlertDialog.setView(input);
        mAlertDialog.setTitle("Set notebook name");

        mAlertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String dialogNotebookName = input.getText().toString();

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

                mLastInsertedNotebook = new Notebook(dialogNotebookName);

                mPresenter.addNotebook(mLastInsertedNotebook);
            }
        });
        mAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        mAlertDialog.show();
    }

    private void openNotebook(Notebook notebook)
    {
        Intent notesView = new Intent(getApplicationContext(), NotesView.class);
        notesView.putExtra(Attributes.ActivityMessageType.NOTEBOOK_FOR_ACTIVITY, notebook.getId());

        startActivityForResult(notesView, Attributes.ActivityMessageType.NOTES_LIST_ACTIVITY);
    }


    public NotebooksAdapter getNotebooksViewAdapter()
    {
        return mNotebooksViewAdapter;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }

    @Override
    public void displayNotebooks(List<Notebook> notebookList)
    {
        mNotebooksViewAdapter.clear();
        mNotebooksViewAdapter.addItems(notebookList);
    }

    @Override
    public void displayNoNotebooks()
    {
        Toast.makeText(this, "No notebooks found !", Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayNotebookAdded(Notebook notebook)
    {

        mNotebooksViewAdapter.addItem(notebook);

        Snackbar.make(mNotebooksView, "Notebook added", Snackbar.LENGTH_LONG)
                .setAction("Open notebook", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        openNotebook(mLastInsertedNotebook);
                    }
                }).show();
    }

    @Override
    public void displayNoNotebookAdded()
    {
        Snackbar.make(mNotebooksView, "Error : Notebook insertion rejected", Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        mPresenter.addNotebook(mLastInsertedNotebook);
                    }
                }).show();
    }

    public void initializeDatabase()
    {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance();

        dbCreator.isDatabaseCreated().observe(NotebooksDisplay.this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(@Nullable Boolean dbCreated)
            {
                if (dbCreated != null && dbCreated)
                {
                    AppDatabase appDatabase = dbCreator.getDatabase();

                    Log.d("DatabaseCreator", "Database retrieved ... initializing presenter");
                    initializePresenter(appDatabase);
                }
            }
        });

        dbCreator.createDb(getApplication());
    }

    public void initializePresenter(AppDatabase appDatabase)
    {
        NotebookRepository repository = new NotebookDisplayRepository(appDatabase);

        NotebooksDisplayPresenter presenter = new NotebooksDisplayPresenter(this, repository, AndroidSchedulers.mainThread());

        mPresenterViewModel.setPresenter(presenter);
        mPresenter = mPresenterViewModel.getPresenter();

        Log.d("DatabaseCreator", "Presenter initialized ... loading notebooks");

        mPresenter.loadNotebooks();
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
