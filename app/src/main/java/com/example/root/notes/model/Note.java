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

@Entity(tableName = "notes")
public class Note implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    private int         id;

    @ColumnInfo(name = "note_title")
    private String      mTitle;

    @ColumnInfo(name = "note_content")
    private String      mContent;

    @Ignore
    private String      mFileName;

    @ColumnInfo(name = "notebook_id")
    private int      mNotebookId;

    @ColumnInfo(name = "note_creation_date")
    @TypeConverters(DateConverter.class)
    private LocalDateTime mCreationDate;

    @ColumnInfo(name = "note_modification_date")
    @TypeConverters(DateConverter.class)
    private LocalDateTime mModificationDate;


    public Note(String title, String content, LocalDateTime creationDate, LocalDateTime modificationDate, int notebookId)
    {
        mFileName         = "";

        mTitle            = title;
        mContent          = content;
        mNotebookId       = notebookId;
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
