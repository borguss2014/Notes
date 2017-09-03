package com.example.root.notes.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.root.notes.util.Attributes;
import com.example.root.notes.functionality.DottedLineEditText;
import com.example.root.notes.model.Note;
import com.example.root.notes.R;
import com.example.root.notes.util.Utilities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class NoteEditorView extends AppCompatActivity
{
    private EditText                mEditTextTitle;
    private DottedLineEditText      mDLEditTextContent;

    private boolean                 mEdit;
    private boolean                 mNewNote;
    private boolean                 mNoteAltered;

    private Note                    mReceivedNote;

    private FloatingActionButton    floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        floatingActionButton = findViewById(R.id.floating_action_save_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String note_title = mEditTextTitle.getText().toString().trim();
                String note_content = mDLEditTextContent.getText().toString().trim();

                saveNote(note_title, note_content);
            }
        });

        mEdit = false;
        mNoteAltered = false;

        mEditTextTitle = (EditText) findViewById(R.id.note_et_title);
        mDLEditTextContent = (DottedLineEditText) findViewById(R.id.note_et_content);

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

            floatingActionButton.hide();
        }
        else
        {
            Log.d("NOTE_ACTIVITY", "NEW NOTE");
            mNewNote = true;

            if(mReceivedNote.getNotebookId() == -1)
            {
                mReceivedNote = new Note();
            }
//            mReceivedNote.setFileName(Utilities.generateUniqueFilename(Attributes.NOTE_FILE_EXTENSION));

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
                        //Note altered
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
                    //Note altered
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
                return true;
            }
            case R.id.action_notes_edit_note:
            {
                mEdit = !mEdit;

                enableEditText(mEditTextTitle, mEdit);
                enableEditText(mDLEditTextContent, mEdit);

                return true;
            }
            case R.id.action_notes_delete_note:
            {
                setResult(Attributes.ActivityResultMessageType.DELETE_NOTE_ACTIVITY_RESULT, new Intent());

                finish();
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_notes_save_note);

        if(mNewNote)
        {
            item.setEnabled(true);
        }
        else
        {
            if(mNoteAltered)
            {
                //Allow user to save the modified note
                item.setEnabled(true);
                floatingActionButton.show();

            }
            else
            {
                item.setEnabled(false);
            }
        }

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

        int resultCode = -1;

        if (noteTitle.isEmpty())
        {
            mReceivedNote.setTitle("Untitled");
        }
        else
        {
            mReceivedNote.setTitle(noteTitle);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            LocalDateTime currentDate = Utilities.getCurrentDateTime();

            if (mNewNote)
            {
                mReceivedNote.setCreationDate(currentDate);

                resultCode = Attributes.ActivityResultMessageType.NEW_NOTE_ACTIVITY_RESULT;
            }
            else
            {
                mReceivedNote.setModificationDate(currentDate);

                resultCode = Attributes.ActivityResultMessageType.OVERWRITE_NOTE_ACTIVITY_RESULT;
            }

            Log.d("NOTE_EDITOR_ACTIVITY", "Setting note date");
        }

        mReceivedNote.setContent(noteContent);

        Intent resultIntent = new Intent();

        resultIntent.putExtra(Attributes.ActivityMessageType.NOTE_FOR_ACTIVITY, mReceivedNote);

        Log.d("NOTE_EDITOR_ACTIVITY", "Setting note date");

        setResult(resultCode, resultIntent);

        finish();
    }
}
