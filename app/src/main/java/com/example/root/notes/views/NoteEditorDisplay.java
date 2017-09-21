package com.example.root.notes.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.root.notes.DateTime;
import com.example.root.notes.util.Attributes;
import com.example.root.notes.functionality.DottedLineEditText;
import com.example.root.notes.model.Note;
import com.example.root.notes.R;
import com.example.root.notes.util.Utilities;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteEditorDisplay extends AppCompatActivity
{
    private boolean                 mEdit;
    private boolean                 mNewNote;
    private boolean                 mNoteAltered;

    private Note                    mReceivedNote;

    @BindView(R.id.floating_action_edit_note)
    FloatingActionButton    floatingActionButton;

    @BindView(R.id.note_et_title)
    EditText                mEditTextTitle;

    @BindView(R.id.note_et_content)
    DottedLineEditText      mDLEditTextContent;

    InputMethodManager inputManager;
    android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        ButterKnife.bind(this);

        inputManager = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);

        actionBar=getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mEdit = true;

                enableEditText(mEditTextTitle, true);
                enableEditText(mDLEditTextContent, true);

                floatingActionButton.hide();

                inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                mDLEditTextContent.requestFocus();

                actionBar.setHomeAsUpIndicator(R.drawable.check);
            }
        });

        mEdit = false;
        mNoteAltered = false;

        mReceivedNote = (Note) getIntent().getSerializableExtra(Attributes.ActivityMessageType.NOTE_FROM_ACTIVITY);

        if(mReceivedNote != null && mReceivedNote.getNotebookId() != -1)
        {
            Log.d("NOTE_ACTIVITY", "NOT NEW NOTE");
            mNewNote = false;

            mEditTextTitle.setText(mReceivedNote.getTitle());
            mDLEditTextContent.setText(mReceivedNote.getContent());

            enableEditText(mEditTextTitle, false);
            enableEditText(mDLEditTextContent, false);

            setTitle("Edit note");

        }
        else
        {
            Log.d("NOTE_ACTIVITY", "NEW NOTE");
            mNewNote = true;

            if(mReceivedNote.getNotebookId() == -1)
            {
                mReceivedNote = new Note();
            }

            floatingActionButton.hide();

            setTitle("New note");
        }

        mEditTextTitle.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Log.d("OnTextChanged", "Text changed title");

                if(!mNewNote)
                {
                    if(!mEditTextTitle.getText().toString().equals(mReceivedNote.getTitle()) ||
                            !mDLEditTextContent.getText().toString().equals(mReceivedNote.getContent()))
                    {
                        mNoteAltered = true;
                        invalidateOptionsMenu();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        mDLEditTextContent.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Log.d("OnTextChanged", "Text changed content");

                if(!mEditTextTitle.getText().toString().equals(mReceivedNote.getTitle()) ||
                        !mDLEditTextContent.getText().toString().equals(mReceivedNote.getContent()))
                {
                    mNoteAltered = true;
                    invalidateOptionsMenu();
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(mNewNote)
        {
            inflater.inflate(R.menu.activity_notes_new_note_menu, menu);
        }
        else
        {
            inflater.inflate(R.menu.activity_notes_edit_view_note_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String note_title = mEditTextTitle.getText().toString().trim();
        String note_content = mDLEditTextContent.getText().toString().trim();

        switch(item.getItemId())
        {
            case R.id.action_notes_save_note:
            {
                saveNote(note_title, note_content);
                break;
            }
            case R.id.action_notes_edit_note:
            {
                mEdit = !mEdit;

                enableEditText(mEditTextTitle, mEdit);
                enableEditText(mDLEditTextContent, mEdit);

                if(mEdit)
                {

                    inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
                else
                {

                    inputManager.hideSoftInputFromWindow(mEditTextTitle.getWindowToken(), 0);
                }

                break;
            }
            case R.id.action_notes_delete_note:
            {
                setResult(Attributes.ActivityResultMessageType.DELETE_NOTE_ACTIVITY_RESULT, new Intent());

                finish();
                break;
            }
            case android.R.id.home:
            {
                if(mEdit)
                {
                    mEdit = false;

                    actionBar.setHomeAsUpIndicator(null);

                    floatingActionButton.show();

                    inputManager.hideSoftInputFromWindow(mDLEditTextContent.getWindowToken(), 0);


                    enableEditText(mEditTextTitle, mEdit);
                    enableEditText(mDLEditTextContent, mEdit);
                }
                else
                {
                    if(mNoteAltered)
                    {
                        saveNote(note_title, note_content);
                    }
                    else
                    {
                        finish();
                    }
                }

                break;
            }
            default: return super.onOptionsItemSelected(item);

        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_notes_save_note);

        if(!mNoteAltered)
        {
            item.setEnabled(false);

            return super.onPrepareOptionsMenu(menu);
        }

        item.setEnabled(true);
        floatingActionButton.show();

        return super.onPrepareOptionsMenu(menu);
    }

    private void enableEditText(EditText edit, boolean isEnabled)
    {
        if(isEnabled)
        {
            edit.setEnabled(true);
        }
        else
        {
            edit.setEnabled(false);
            edit.setTextColor(Color.argb(255, 0, 0, 0));
        }
    }

    private void saveNote(String noteTitle, String noteContent)
    {
        Log.d("NOTE_ACTIVITY", "SAVING NOTE");

        Date date = new Date();

//        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        isoFormat.setTimeZone(TimeZone.getDefault());
//
//        Date formated = null;
//
//        try {
//            formated = isoFormat.parse(date.toString());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        int resultCode = -1;

        if (noteTitle.isEmpty())
        {
            mReceivedNote.setTitle("Untitled");
        }
        else
        {
            mReceivedNote.setTitle(noteTitle);
        }


        if (mNewNote)
        {
            mReceivedNote.setCreationDate(date);

            resultCode = Attributes.ActivityResultMessageType.NEW_NOTE_ACTIVITY_RESULT;
        }
        else
        {
            mReceivedNote.setModificationDate(date);

            resultCode = Attributes.ActivityResultMessageType.OVERWRITE_NOTE_ACTIVITY_RESULT;
        }

        Log.d("NOTE_EDITOR_ACTIVITY", "Setting note date");


        mReceivedNote.setContent(noteContent);

        Intent resultIntent = new Intent();

        resultIntent.putExtra(Attributes.ActivityMessageType.NOTE_FOR_ACTIVITY, mReceivedNote);

        setResult(resultCode, resultIntent);

        finish();
    }

    @Override
    public void onBackPressed()
    {
        Log.d("TEST", "BackPressed");

        if(mNoteAltered)
        {
            String note_title = mEditTextTitle.getText().toString().trim();
            String note_content = mDLEditTextContent.getText().toString().trim();

            saveNote(note_title, note_content);
        }

        super.onBackPressed();
    }
}
