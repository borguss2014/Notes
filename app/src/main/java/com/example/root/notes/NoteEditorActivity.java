package com.example.root.notes;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class NoteEditorActivity extends AppCompatActivity {

    private EditText            mEditTextTitle;
    private DottedLineEditText  mDLEditTextContent;

    private boolean             mEdit;
    private boolean             mNewNote;
    private boolean             mNoteAltered;

    private Note                mReceivedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        mEdit = false;
        mNoteAltered = false;

        mEditTextTitle = (EditText) findViewById(R.id.note_et_title);
        mDLEditTextContent = (DottedLineEditText) findViewById(R.id.note_et_content);

        mReceivedNote = (Note) getIntent().getSerializableExtra(Attributes.ActivityMessageType.NOTE_FROM_ACTIVITY.toString());

        if(mReceivedNote != null)
        {
            Log.d("NOTE_ACTIVITY", "NOT NEW NOTE");
            mNewNote = false;

            mEditTextTitle.setText(mReceivedNote.getTitle());
            mDLEditTextContent.setText(mReceivedNote.getContent());

            enableEditText(mEditTextTitle, false);
            enableEditText(mDLEditTextContent, false);
        }
        else
        {
            Log.d("NOTE_ACTIVITY", "NEW NOTE");
            mNewNote = true;

            mReceivedNote = new Note();
            mReceivedNote.setFileName(Utilities.generateUniqueFilename(Attributes.NOTE_FILE_EXTENSION));
        }

        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("OnTextChanged", "Text changed title");

                if(!mNewNote) {
                    Note currentContent = new Note(mEditTextTitle.getText().toString(), mDLEditTextContent.getText().toString());
                    if (!mReceivedNote.isEqual(currentContent)) {
                        //Note altered
                        mNoteAltered = true;
                        invalidateOptionsMenu();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDLEditTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("OnTextChanged", "Text changed content");

                if(!mNewNote)
                {
                    Note currentContent = new Note(mEditTextTitle.getText().toString(), mDLEditTextContent.getText().toString());
                    if (!mReceivedNote.isEqual(currentContent)) {
                        //Note altered
                        mNoteAltered = true;
                        invalidateOptionsMenu();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
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
    public boolean onOptionsItemSelected(MenuItem item) {

        String note_title = mEditTextTitle.getText().toString().trim();
        String note_content = mDLEditTextContent.getText().toString().trim();

        switch(item.getItemId())
        {
            case R.id.action_notes_save_note:
            {
                Log.d("NOTE_ACTIVITY", "SAVING NOTE");

                Calendar calendar  = new GregorianCalendar(TimeZone.getDefault());
                calendar.setTime(new Date());

                DateTime currentDate = Utilities.getCurrentDateTime(calendar);

                int resultCode;

                if(note_title.isEmpty())
                {
                    mReceivedNote.setTitle("Untitled");
                }
                else
                {
                    mReceivedNote.setTitle(note_title);
                }

                if(mNewNote)
                {
                    mReceivedNote.setCreationDate(currentDate);

                    resultCode = Attributes.ActivityResultMessageType.NEW_NOTE_ACTIVITY_RESULT;
                }
                else
                {
                    mReceivedNote.setLastModifiedDate(currentDate);

                    resultCode = Attributes.ActivityResultMessageType.OVERWRITE_NOTE_ACTIVITY_RESULT;
                }

                mReceivedNote.setContent(note_content);

                Intent resultIntent = new Intent();

                resultIntent.putExtra(Attributes.ActivityMessageType.NOTE_FOR_ACTIVITY, mReceivedNote);

                setResult(resultCode, resultIntent);

                finish();
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
}
