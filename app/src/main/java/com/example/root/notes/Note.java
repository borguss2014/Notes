package com.example.root.notes;

import java.io.Serializable;

/**
 * TODO: Add a class header comment!
 */

public class Note implements Serializable
{
    private String      mTitle, mContent;

    private String      mFileName;

    private DateTime mCreationDate;
    private DateTime    mLastModifiedDate;

    public Note()
    {
        mFileName   = "";

        mCreationDate = new DateTime();
        mLastModifiedDate = new DateTime();
    }

    public Note(String title)
    {
        mTitle      = title;
        mContent    = "";
        mFileName   = "";

        mCreationDate = new DateTime();
        mLastModifiedDate = new DateTime();
    }

    public Note(String title, String content)
    {
        mTitle      = title;
        mContent    = content;
        mFileName   = "";

        mCreationDate = new DateTime();
        mLastModifiedDate = new DateTime();
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

    public void setCreationDate(DateTime cDate)
    {
        mCreationDate = cDate;
    }

    public DateTime getCreationDate()
    {
        return mCreationDate;
    }

    public void setLastModifiedDate(DateTime mDate)
    {
        mLastModifiedDate = mDate;
    }

    public DateTime getLastModifiedDate()
    {
        return mLastModifiedDate;
    }

    public boolean isEqual(Note note)
    {
        return mTitle.equals(note.getTitle()) && mContent.equals(note.getContent());
    }
}
