package com.example.root.notes.views;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.notes.NotebookDisplayRepository;
import com.example.root.notes.NotebooksDisplayPresenter;
import com.example.root.notes.NotebooksDisplayView;
import com.example.root.notes.PresenterViewModel;
import com.example.root.notes.NotebookRepository;
import com.example.root.notes.RecyclerItemListener;
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

    private NotebooksAdapter                                mNotebooksViewAdapter;
    private Notebook                                        mLastInsertedNotebook;

    private InputMethodManager                              inputManager;

    private NotebooksDisplayPresenter                       mPresenter;

    private PresenterViewModel<NotebooksDisplayPresenter>   mPresenterViewModel;

    @BindView(R.id.floating_action_add_notebook)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.notebooks_list_view)
    RecyclerView mNotebooksView;

    @BindView(R.id.notebooks_list_empty)
    TextView mEmptyNotebooksListMessage;

    @BindView(R.id.notebooks_list_loading)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebooks_list);
        ButterKnife.bind(this);

        setTitle("Notebooks");

        getWindow().getDecorView().setBackgroundColor(Color.argb(255,224,224,224));

        inputManager = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);

        mPresenterViewModel = ViewModelProviders.of(this).get(PresenterViewModel.class);

        setViewState(Attributes.ViewState.LOADING);

        if(mPresenterViewModel.getPresenter() == null)
        {
            Log.d("NotebooksDisplay", "Presenter null ... preparing for presenter init");

            AppDatabase appDatabase = DatabaseCreator.getInstance().getDatabase();

            initializePresenter(appDatabase);
        }
        else
        {
            Log.d("NotebooksDisplay", "Presenter found ... recreating");

            mPresenter = mPresenterViewModel.getPresenter();
            mPresenter.attachLifecycle(getLifecycle());
        }

        setupView();

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

        Log.d("PresenterInit", "Presenter initialized ... loading notebooks");

        mPresenter.loadNotebooks();
    }

    @OnClick(R.id.floating_action_add_notebook)
    public void onClickAction(View view)
    {
        Log.d("OnClickNotebookActivity", "Clicking " + getResources().getResourceEntryName(view.getId()));

        int viewId = view.getId();

        if(viewId == R.id.floating_action_add_notebook)
        {
            createNotebookDialog();
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
    public Object onRetainCustomNonConfigurationInstance()
    {
        Log.d("NotebooksDisplay", "Retaining adapter data");
        return mNotebooksViewAdapter.getInternalData();
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
                createNotebookDialog();
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

    private void openNotebook(Notebook notebook)
    {
        Intent resultIntent = new Intent();

        resultIntent.putExtra(Attributes.ActivityMessageType.NOTEBOOK_FOR_ACTIVITY, notebook.getId());

        int resultCode = Attributes.ActivityResultMessageType.RETURN_NOTEBOOK_ACTIVITY_RESULT;

        setResult(resultCode, resultIntent);

        finish();
    }

    @Override
    public void setupView()
    {
        List<Notebook> notebooksList = (List<Notebook>) getLastCustomNonConfigurationInstance();

        if(notebooksList == null)
        {
            notebooksList = new ArrayList<>();
        }

        mNotebooksViewAdapter = new NotebooksAdapter(NotebooksDisplay.this, notebooksList, mPresenter, new NotebooksAdapter.RecyclerViewClickInterface()
        {
            @Override
            public void onItemClicked(int position, String tag, View view)
            {
                Notebook notebook = mNotebooksViewAdapter.getItemAtPosition(position);

                if(tag.equals(NotebooksAdapter.Constants.NotebookOverflowOptions))
                {
                    PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                    popupMenu.getMenuInflater().inflate(R.menu.notebooks_overflow_menu, popupMenu.getMenu());

                    if(mNotebooksViewAdapter.getItemCount() == 1 &&
                            notebook.getId() == mPresenter.getDefaultNotebookID())
                    {
                        popupMenu.getMenu().findItem(R.id.notebook_overflow_delete).setEnabled(false);
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {
                            switch(item.getItemId())
                            {
                                case R.id.notebook_overflow_edit:
                                {
                                    displayDialog("Edit notebook", "Ok",
                                            "Cancel", Attributes.NotebookOverflowAction.MODE_EDIT, notebook);
                                    break;
                                }
                                case R.id.notebook_overflow_delete:
                                {
                                    displayDialog("Delete notebook", "Accept",
                                            "Cancel", Attributes.NotebookOverflowAction.MODE_DELETE, notebook);
                                    break;
                                }
                            }
                            return false;
                        }
                    });

                    popupMenu.show();
                }
                else
                {
                    openNotebook(notebook);
                }
            }
        });

        mNotebooksView.setAdapter(mNotebooksViewAdapter);
        mNotebooksView.setLayoutManager(new LinearLayoutManager(this));

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
    }

    @Override
    public LifecycleRegistry getLifecycle()
    {
        return mRegistry;
    }

    @Override
    public void displayNotebooks(List<Notebook> notebookList)
    {
        mNotebooksViewAdapter.clear();
        mNotebooksViewAdapter.addItems(notebookList);

        setViewState(Attributes.ViewState.LOADED);
    }

    @Override
    public void displayNoNotebooks()
    {
        setViewState(Attributes.ViewState.EMPTY);
    }

    @Override
    public void displayNotebookAdded(Notebook notebook)
    {

        mNotebooksViewAdapter.addItem(notebook);

        mLastInsertedNotebook = notebook;

        Snackbar.make(mNotebooksView, "Notebook added", Snackbar.LENGTH_LONG)
                .setAction("Open notebook", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        openNotebook(notebook);
                    }
                }).show();

        if(mNotebooksViewAdapter.getItemCount() == 0)
        {
            setViewState(Attributes.ViewState.LOADED);
        }
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

    @Override
    public void displayNotebookUpdated()
    {
        Toast.makeText(getApplicationContext(), "Notebook updated", Toast.LENGTH_SHORT).show();

        mPresenter.loadNotebooks();
    }

    @Override
    public void displayNoNotebookUpdated()
    {
        Toast.makeText(getApplicationContext(), "Issue occurred: notebook not updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayNotebookDeleted()
    {
        Toast.makeText(getApplicationContext(), "Notebook deleted", Toast.LENGTH_SHORT).show();

        mPresenter.loadNotebooks();

    }

    @Override
    public void displayNoNotebookDeleted()
    {
        Toast.makeText(getApplicationContext(), "Issue occurred: notebook not deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initializePresenter(AppDatabase appDatabase)
    {
//        NotebookRepository repository = new NotebookDisplayRepository(appDatabase,
//                getApplicationContext().getSharedPreferences("com.example.root.notes", Context.MODE_PRIVATE));

        NotebookRepository repository = new NotebookDisplayRepository(appDatabase,
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        NotebooksDisplayPresenter presenter = new NotebooksDisplayPresenter(this, repository, AndroidSchedulers.mainThread());

        mPresenterViewModel.setPresenter(presenter);
        mPresenter = presenter;
    }

    private void setViewState(int state)
    {
        switch (state)
        {
            case Attributes.ViewState.EMPTY:
            {
                mNotebooksView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);

                mEmptyNotebooksListMessage.setVisibility(View.VISIBLE);
                break;
            }
            case Attributes.ViewState.LOADING:
            {
                mNotebooksView.setVisibility(View.GONE);
                mEmptyNotebooksListMessage.setVisibility(View.GONE);

                mProgressBar.setVisibility(View.VISIBLE);
                break;
            }
            case Attributes.ViewState.LOADED:
            {
                mEmptyNotebooksListMessage.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);

                mNotebooksView.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void displayDialog(String dialogTitle, String okButton, String cancelButton, int mode, Notebook notebook)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));

        final EditText input = new EditText(getApplicationContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(notebook.getName());

        if(mode == Attributes.NotebookOverflowAction.MODE_EDIT)
        {
            alertDialogBuilder.setView(input);
        }
        else if(mode == Attributes.NotebookOverflowAction.MODE_DELETE)
        {
            final TextView view = new TextView(getApplicationContext());
            view.setText("Are you sure you want to delete this notebook?");
            view.setGravity(Gravity.CENTER);

            alertDialogBuilder.setView(view);
        }

        alertDialogBuilder.setTitle(dialogTitle);

        alertDialogBuilder.setPositiveButton(okButton, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(mode == Attributes.NotebookOverflowAction.MODE_EDIT)
                {
                    String dialogNotebookName = input.getText().toString();

                    if(mNotebooksViewAdapter.contains(dialogNotebookName))
                    {
                        Toast.makeText(getApplicationContext(), "Notebook already exists. Choose another name!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(dialogNotebookName.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "Notebook name cannot be empty!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    notebook.setName(dialogNotebookName);

                    //mLastInsertedNotebook = new Notebook(dialogNotebookName);

                    mPresenter.updateNotebook(notebook);

                    inputManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
                }
                else if(mode == Attributes.NotebookOverflowAction.MODE_DELETE)
                {
                    int defaultNotebook = mPresenter.getDefaultNotebookID();

                    if(notebook.getId() == defaultNotebook && mNotebooksViewAdapter.getItemCount() > 1)
                    {
                        int newDefaultNotebookID = mNotebooksViewAdapter.getItemAtPosition(1).getId();

                        mPresenter.updateDefaultNotebookID(newDefaultNotebookID);

                        Log.d("ADAPTER", "New default notebook id : " + Integer.toString(mPresenter.getDefaultNotebookID()));
                    }
                    mPresenter.deleteNotebook(notebook);
                }
            }
        });
        alertDialogBuilder.setNegativeButton(cancelButton, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {
                if(mode == Attributes.NotebookOverflowAction.MODE_EDIT)
                {
                    inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });

        alertDialogBuilder.show();

        if(mode == Attributes.NotebookOverflowAction.MODE_EDIT)
        {
            input.requestFocus();
            input.setSelection(input.getText().length());

            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void createNotebookDialog()
    {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(input);
        alertDialogBuilder.setTitle("Notebook name");

        alertDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String dialogNotebookName = input.getText().toString();

                if(mNotebooksViewAdapter.contains(dialogNotebookName))
                {
                    Toast.makeText(getApplicationContext(), "Notebook already exists. Choose another name!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(dialogNotebookName.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Notebook name cannot be empty!", Toast.LENGTH_LONG).show();
                    return;
                }

                mLastInsertedNotebook = new Notebook(dialogNotebookName);

                mPresenter.addNotebook(mLastInsertedNotebook);

                inputManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.show();

        input.requestFocus();

        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}