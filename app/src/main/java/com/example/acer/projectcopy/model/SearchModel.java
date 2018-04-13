package com.example.acer.projectcopy.model;

/**
 * Created by Acer on 4/1/2018.
 */

public class SearchModel {

    private long mId;
    private String mUserId;
    private String mImageURL;
    private String mText;
    private String mOtherText;
    private String mNativeText;
    private String mAgeText;
    private String mHobbies;

    public SearchModel() {
    }

    public SearchModel(long id, String userId, String imageURL, String text, String otherText) {
        mId = id;
        mUserId = userId;
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

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getNativeText() {
        return mNativeText;
    }

    public void setNativeText(String mNativeText) {
        this.mNativeText = mNativeText;
    }

    public String getAgeText() {
        return mAgeText;
    }

    public void setAgeText(String mAgeText) {
        this.mAgeText = mAgeText;
    }

    public String getHobbies() {
        return mHobbies;
    }

    public void setHobbies(String mHobbies) {
        this.mHobbies = mHobbies;
    }
}
