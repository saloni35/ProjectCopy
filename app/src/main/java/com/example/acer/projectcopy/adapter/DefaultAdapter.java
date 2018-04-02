package com.example.acer.projectcopy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.acer.projectcopy.R;
import com.example.acer.projectcopy.model.DummyModel;
import com.example.acer.projectcopy.util.ImageUtil;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.util.Swappable;

import java.util.ArrayList;
import java.util.Collections;

public class DefaultAdapter extends BaseAdapter implements Swappable, OnDismissCallback {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<DummyModel> mDummyModelList;
    private boolean mShouldShowDragAndDropIcon;

    public DefaultAdapter(Context context, ArrayList<DummyModel> dummyModelList, boolean shouldShowDragAndDropIcon) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDummyModelList = dummyModelList;
        mShouldShowDragAndDropIcon = shouldShowDragAndDropIcon;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return mDummyModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDummyModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mDummyModelList.get(position).getId();
    }

    @Override
    public void swapItems(int positionOne, int positionTwo) {
        Collections.swap(mDummyModelList, positionOne, positionTwo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.facebook_friends_list_item_layout, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.icon = (TextView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DummyModel dm = mDummyModelList.get(position);

        ImageUtil.displayRoundImage(holder.image, dm.getImageURL(), null);
        holder.text.setText(dm.getText());
        if (mShouldShowDragAndDropIcon) {
            holder.icon.setText(R.string.fontello_drag_and_drop);
        } else {
            holder.icon.setText(dm.getIconRes());
        }
        return convertView;
    }

    @Override
    public void onDismiss(@NonNull final ViewGroup listView,
                          @NonNull final int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            remove(position);
        }
    }

    public void remove(int position) {
        mDummyModelList.remove(position);
    }

    private static class ViewHolder {
        public ImageView image;
        public /*Roboto*/ TextView text;
        public /*Fontello*/ TextView icon;
    }
}
