package com.example.root.notes;

import java.io.Serializable;

/**
 * Created by ROOT on 3/21/2017.
 */

public class Note implements Serializable{

    private String      mTitle, mContent;

    private String      mFileName;

    private DateTime    mCreationDate;
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

    String getContent() {
        return mContent;
    }

    void setContent(String mContent) {
        this.mContent = mContent;
    }

    String getFileName()
    {
        return mFileName;
    }

    void setFileName(String filename)
    {
        mFileName = filename;
    }

    void setCreationDate(DateTime cDate)
    {
        mCreationDate = cDate;
    }

    DateTime getCreationDate()
    {
        return mCreationDate;
    }

    void setLastModifiedDate(DateTime mDate)
    {
        mLastModifiedDate = mDate;
    }

    DateTime getLastModifiedDate()
    {
        return mLastModifiedDate;
    }

    boolean isEqual(Note note)
    {
        return mTitle.equals(note.getTitle()) && mContent.equals(note.getContent());
    }
}
