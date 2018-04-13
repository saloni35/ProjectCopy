package com.example.acer.projectcopy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.acer.projectcopy.view.AnimatedExpandableListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import static com.example.acer.projectcopy.PlanTabFragment.padLeftSpaces;


public class MyPlansFragment extends Fragment {
    private Activity mActivity;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;


    public MyPlansFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mActivity));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_plans, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<GroupItem> items = new ArrayList<GroupItem>();
        items = fillData(items);

        adapter = new ExampleAdapter(mActivity);
        adapter.setData(items);

        listView = (AnimatedExpandableListView) mActivity.findViewById(R.id.show_plan_list_view);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

        // Set indicator (arrow) to the right
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                50, r.getDisplayMetrics());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            listView.setIndicatorBounds(width - px, width);
        } else {
            listView.setIndicatorBoundsRelative(width - px, width);
        }
    }

    private List<GroupItem> fillData(final List<GroupItem> items) {

        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Plans");
        databaseRef.keepSynced(true);

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("authorId").getValue().toString().equals(uid)) {
                        Plan plan = data.getValue(Plan.class);
                        GroupItem item = new GroupItem();
                        item.title = plan.getTitle();
                        item.description = plan.getDescription();
                        ChildItem child;
                        child = new ChildItem();
                        child.all_places = plan.getPlacesToTravel();
                        child.budget = Integer.toString(plan.getBudget());
                        child.destination = plan.getDestination();
                        child.no_of_days = Integer.toBinaryString(plan.getNoOfDays());
                        child.partner_desc = plan.getDescriptionOfTravelPartner();
                        child.source = plan.getSource();
                        child.trip_type = plan.getTripType();
                        item.items.add(child);
                        items.add(item);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        return items;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private static class GroupItem {
        String title;
        String description;
        String icon_edit;
        String icon_delete;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String source;
        String destination;
        String budget;
        String all_places;
        String partner_desc;
        String trip_type;
        String no_of_days;
    }

    private static class ChildHolder {
        TextView source;
        TextView destination;
        TextView budget;
        TextView all_places;
        TextView partner_desc;
        TextView trip_type;
        TextView no_of_days;
    }

    private static class GroupHolder {
        TextView title;
        TextView description;
    }

    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition,
                                     boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(
                        R.layout.show_plan_list_item_child, parent,
                        false);
                holder.source = (TextView) convertView
                        .findViewById(R.id.show_plan_child_source);
                holder.destination = (TextView) convertView
                        .findViewById(R.id.show_plan_child_destination);
                holder.all_places = (TextView) convertView
                        .findViewById(R.id.show_plan_child_all_places);
                holder.budget = (TextView) convertView
                        .findViewById(R.id.show_plan_child_budget);
                holder.no_of_days = (TextView) convertView
                        .findViewById(R.id.show_plan_child_days);
                holder.partner_desc = (TextView) convertView
                        .findViewById(R.id.show_plan_child_partner_desc);
                holder.trip_type = (TextView) convertView
                        .findViewById(R.id.show_plan_child_trip_type);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            String so = "Source";
            String de = "Destination";
            String pd = "Partner Description";
            String no = "No of days to travel";
            String bu = "Budget";
            String tt = "Trip Type";
            String p = "All places";

            holder.source.setText(padLeftSpaces(so, 22) + ":   " + item.source);
            holder.destination.setText(padLeftSpaces(de, 22) + ":   " + item.destination);
            holder.partner_desc.setText(padLeftSpaces(pd, 22) + ":   " + item.partner_desc);
            holder.no_of_days.setText(padLeftSpaces(no, 22) + ":   " + item.no_of_days);
            holder.budget.setText(padLeftSpaces(bu, 22) + ":   " + item.budget);
            holder.trip_type.setText(padLeftSpaces(tt, 22) + ":   " + item.trip_type);
            holder.all_places.setText(padLeftSpaces(p, 22) + ":   " + item.all_places);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(
                        R.layout.show_plan_list_item, parent, false);
                holder.title = (TextView) convertView
                        .findViewById(R.id.show_plan_header_title);
                holder.description = (TextView) convertView
                        .findViewById(R.id.show_plan_header_description);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            holder.description.setText(item.description);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }
    }
}
