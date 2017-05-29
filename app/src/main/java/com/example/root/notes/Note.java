package com.example.root.notes;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by ROOT on 3/21/2017.
 */

public class Note implements Serializable{

    private String mTitle, mContent;

    private String mFileName;

    private DateTime mCreationDate;
    private DateTime mLastModifiedDate;


    public Note()
    {
        mFileName   = "";
    }

    public Note(String title)
    {
        mTitle      = title;
        mContent    = "";
        mFileName   = "";

        mCreationDate       = new DateTime();
        mLastModifiedDate   = new DateTime();
    }

    public Note(String title, String content)
    {
        mTitle      = title;
        mContent    = content;
        mFileName   = "";

        mCreationDate       = new DateTime();
        mLastModifiedDate   = new DateTime();
    }

//    public Note(String title, String content, DateTime creationDate)
//    {
//        mTitle      = title;
//        mContent    = content;
//        mFileName   = "";
//
//        mCreationDate       = creationDate;
//        mLastModifiedDate   = new DateTime();
//    }

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
        Log.d("Note-SET FILENAME", mFileName);
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
}
