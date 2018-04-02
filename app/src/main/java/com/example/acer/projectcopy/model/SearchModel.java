package com.example.acer.projectcopy.model;

/**
 * Created by Acer on 4/1/2018.
 */

public class SearchModel {

    private long mId;
    private String mImageURL;
    private String mText;
    private String mOtherText;

    public SearchModel() {
    }

    public SearchModel(long id, String imageURL, String text, String otherText) {
        mId = id;
        mImageURL = imageURL;
        mText = text;
        mOtherText = otherText;
    }


    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String imageURL) {
        mImageURL = imageURL;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    @Override
    public String toString() {
        return mText;
    }

    public String getOtherText() {
        return mOtherText;
    }

    public void setOtherText(String otherText) {
        mOtherText = otherText;
    }
}
