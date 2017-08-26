package com.example.root.notes.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.root.notes.DateConverter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * TODO: Add a class header comment!
 */

@Entity(tableName = Note.TABLE_NAME)
public class Note implements Serializable
{
    public final static String TABLE_NAME = "notes";
    public final static String NOTE_NAME = "note_title";
    public final static String NOTE_CONTENT = "note_content";
    public final static String NOTEBOOK_ID = "notebook_id";
    public final static String NOTE_CREATION_DATE = "note_creation_date";
    public final static String NOTE_MODIFICATION_DATE = "note_modification_date";

    @PrimaryKey(autoGenerate = true)
    private int         id;

    @ColumnInfo(name = Note.NOTE_NAME)
    private String      mTitle;

    @ColumnInfo(name = Note.NOTE_CONTENT)
    private String      mContent;

    @Ignore
    private String      mFileName;

    @ColumnInfo(name = Note.NOTEBOOK_ID)
    private int      mNotebookId;

    @ColumnInfo(name = Note.NOTE_CREATION_DATE)
    @TypeConverters(DateConverter.class)
    private LocalDateTime mCreationDate;

    @ColumnInfo(name = Note.NOTE_MODIFICATION_DATE)
    @TypeConverters(DateConverter.class)
    private LocalDateTime mModificationDate;


    @Ignore
    public Note()
    {
        mFileName          = "";

        mNotebookId        = -1;

        mTitle             = "placeholder";
        mContent           = "placeholder";

        mCreationDate      = null;
        mModificationDate  = null;
    }

    public Note(String title, String content, LocalDateTime creationDate, LocalDateTime modificationDate, int notebookId)
    {
        mFileName         = "";

        mNotebookId       = notebookId;

        mTitle            = title;
        mContent          = content;

        mCreationDate     = creationDate;
        mModificationDate = modificationDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getFileName()
    {
        return mFileName;
    }

    public void setFileName(String filename)
    {
        mFileName = filename;
    }

    public void setCreationDate(LocalDateTime cDate)
    {
        mCreationDate = cDate;
    }

    public LocalDateTime getCreationDate()
    {
        return mCreationDate;
    }

    public void setModificationDate(LocalDateTime mDate)
    {
        mModificationDate = mDate;
    }

    public LocalDateTime getModificationDate()
    {
        return mModificationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public int getNotebookId()
    {
        return mNotebookId;
    }

    public void setNotebookId(int mNotebookId)
    {
        this.mNotebookId = mNotebookId;
    }

    public boolean isEqual(Note note)
    {
        return mTitle.equals(note.getTitle()) && mContent.equals(note.getContent());
    }
}
