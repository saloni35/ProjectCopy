package com.example.acer.projectcopy.model;

/**
 * Created by Acer on 4/2/2018.
 */

public class PlanModel {

    private long mId;
    private String title;
    private String description;

    public PlanModel() {

    }

    public PlanModel(long id, String title, String description) {
        this.mId = id;
        this.title = title;
        this.description = description;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
