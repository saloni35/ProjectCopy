package com.example.acer.projectcopy;

/**
 * Created by Acer on 4/3/2018.
 */

public class DrawerItem {


    public static final int DRAWER_ITEM_TAG_PROFILE = 1;
    public static final int DRAWER_ITEM_TAG_NEWSFEED = 2;
    public static final int DRAWER_ITEM_TAG_FRIENDS = 4;
    public static final int DRAWER_ITEM_TAG_FACEBOOK_FRIENDS = 3;
    public static final int DRAWER_ITEM_TAG_CREATE_PLANS = 5;
    public static final int DRAWER_ITEM_TAG_SHOW_PLANS = 6;
    public static final int DRAWER_ITEM_TAG_CREATE_POST = 7;
    public static final int DRAWER_ITEM_TAG_INVITE_FRIENDS = 8;
    public static final int DRAWER_ITEM_TAG_ADD_ACCOUNT = 9;
    private int icon;
    private int title;
    private int tag;

    public DrawerItem(int icon, int title, int tag) {
        this.icon = icon;
        this.title = title;
        this.tag = tag;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}

