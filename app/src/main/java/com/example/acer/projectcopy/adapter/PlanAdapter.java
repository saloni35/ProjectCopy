package com.example.acer.projectcopy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.acer.projectcopy.R;
import com.example.acer.projectcopy.model.PlanModel;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.util.Swappable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Acer on 4/2/2018.
 */

public class PlanAdapter extends BaseAdapter implements Swappable, OnDismissCallback {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<PlanModel> mPlanModelList;
    private boolean mShouldShowDragAndDropIcon;

    public PlanAdapter(Context context, ArrayList<PlanModel> planModelList, boolean shouldShowDragAndDropIcon) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPlanModelList = planModelList;
        mShouldShowDragAndDropIcon = shouldShowDragAndDropIcon;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return mPlanModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPlanModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mPlanModelList.get(position).getId();
    }

    @Override
    public void swapItems(int positionOne, int positionTwo) {
        Collections.swap(mPlanModelList, positionOne, positionTwo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.plan_list_item_layout, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.plan_item_title);
            holder.description = (TextView) convertView.findViewById(R.id.plan_item_description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PlanModel dm = mPlanModelList.get(position);

        holder.title.setText(dm.getTitle());
        holder.description.setText(dm.getDescription());

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
        mPlanModelList.remove(position);
    }

    private static class ViewHolder {
        public TextView title;
        public TextView description;
    }
}
