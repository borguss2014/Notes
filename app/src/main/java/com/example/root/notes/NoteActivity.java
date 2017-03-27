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
import android.widget.Toast;

import java.util.HashMap;

public class NoteActivity extends AppCompatActivity {

    private EditText            mEditTextTitle;
    private DottedLineEditText  mDLEditTextContent;

    private boolean             mEdit;
    private boolean             mNewNote;
    private boolean             mNoteAltered;

    private Note                mReceivedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mEdit = false;
        mNoteAltered = false;

        mEditTextTitle = (EditText) findViewById(R.id.note_et_title);
        mDLEditTextContent = (DottedLineEditText) findViewById(R.id.note_et_content);

        Intent intent = getIntent();
        HashMap<String, String> noteData = (HashMap<String, String>) intent.getSerializableExtra(Utilities.MAIN_DATA);

        if(noteData != null)
        {
            mEditTextTitle.setText(noteData.get(Utilities.TITLE));
            mDLEditTextContent.setText(noteData.get(Utilities.CONTENT));

            enableEditText(mEditTextTitle, false);
            enableEditText(mDLEditTextContent, false);

            mNewNote = false;

//            MenuItem item = (MenuItem) findViewById(R.id.action_notes_save_note);
//            item.setEnabled(false);

            Note newNote = new Note();
            newNote.setTitle(noteData.get(Utilities.TITLE));
            newNote.setContent(noteData.get(Utilities.CONTENT));
            newNote.setFileName(noteData.get(Utilities.FILENAME));
            newNote.setCreationDate((DateTime) intent.getSerializableExtra(Utilities.CREATION_DATE));
            newNote.setLastModifiedDate((DateTime) intent.getSerializableExtra(Utilities.MODIFIED_DATE));

            mReceivedNote = newNote;
        }
        else
        {
            mNewNote = true;
        }

        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("OnTextChanged", "Text changed title");

                if(mNewNote == false) { //If it's not a new note
                    if (notesComparison(mReceivedNote, new Note(mEditTextTitle.getText().toString(), mDLEditTextContent.getText().toString()))) {
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

                if(mNewNote == false) { //If it's not a new note
                    if (notesComparison(mReceivedNote, new Note(mEditTextTitle.getText().toString(), mDLEditTextContent.getText().toString()))) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(mNewNote)
        {
            inflater.inflate(R.menu.activity_notes_new_note_menu, menu);
            mNoteAltered = true;
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
                if(note_title.isEmpty() && note_content.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Note empty ! Add content before saving",  Toast.LENGTH_SHORT).show();
                    return true;
                }

                if(note_title.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter a title before saving",  Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if(note_content.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter content before saving",  Toast.LENGTH_SHORT).show();
                    return true;
                }

                if(mNewNote)
                {
                    long date = System.currentTimeMillis();

                    Note note = new Note(date, note_title, note_content);

                    saveNote(note, false);
                }
                else
                {
                    mReceivedNote.setTitle(mEditTextTitle.getText().toString());
                    mReceivedNote.setContent(mDLEditTextContent.getText().toString());
                    saveNote(mReceivedNote, true);
                }

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
                boolean fileDeleted = deleteNote(mReceivedNote);
                if(fileDeleted)
                {
                    Toast.makeText(getApplicationContext(), "Note succesfully deleted" , Toast.LENGTH_SHORT).show();
                    return true;
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "An error occured. Note couldn't be deleted" , Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            default: return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_notes_save_note);

        if(mNoteAltered)
        {
            //Allow user to save the modified note
            item.setEnabled(true);
        }
        else
        {
            item.setEnabled(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    private void saveNote(Note note, boolean overwrite)
    {
        Log.d("SAVE NOTE", "SAVING");
        Utilities.saveFile(getApplicationContext(), note, overwrite);
        finish();
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

    private boolean deleteNote(Note note)
    {
        boolean fileDeleted = Utilities.deleteFile(getApplicationContext(), note.getFileName());
        finish();
        return fileDeleted;
    }

    private boolean notesComparison(Note originalNote, Note newNote)
    {
        boolean altered = false;

        //Title has changed
        if(!originalNote.getTitle().equals(newNote.getTitle()))
        {
            altered = true;
        }

        //Content has changed
        if(!originalNote.getContent().equals(newNote.getContent()))
        {
            altered = true;
        }

        return altered;
    }
}
