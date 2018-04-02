package com.example.acer.projectcopy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acer.projectcopy.adapter.SearchAdapter;
import com.example.acer.projectcopy.model.SearchModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class SearchFriendsFragment extends Fragment {

    public static final String TAG = "Media Search Bar";

    private Activity mActivity;
    private EditText mSearchField;
    private TextView mXMark;
    private View mMicrofon;
    private ListView mListView;
    private ArrayList<SearchModel> searchableArrayList;

    private TextView mName;
    private TextView mNativePlace;
    private TextView mArtist;
    private TextView mGenre;

    private TextView mArrow;
    private LinearLayout mFiltersLayout;
    private SearchFriendsFragment searchFriendsFragment;

    private DatabaseReference databaseReference;
    private ListView listView;

    private boolean searchByName = true;
    private boolean searchByNativePlace = false;

    public SearchFriendsFragment() {
        // Required empty public constructor
        searchFriendsFragment = this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mActivity));

        return inflater.inflate(R.layout.fragment_search_friends, container, false);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mName = (TextView) mActivity.findViewById(R.id.activity_search_bar_name);
        mNativePlace = (TextView) mActivity.findViewById(R.id.activity_search_bar_native_place);
        mArtist = (TextView) mActivity.findViewById(R.id.activity_search_bar_media_artist);
        mGenre = (TextView) mActivity.findViewById(R.id.activity_search_bar_media_genre);

        mArrow = (TextView) mActivity.findViewById(R.id.activity_search_bar_media_arrow);
        mFiltersLayout = (LinearLayout) mActivity.findViewById(R.id.activity_search_bar_media_filters_layout);


        mName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                afterClick(view);
            }
        });
        mNativePlace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                afterClick(view);
            }
        });
        mArtist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                afterClick(view);
            }
        });
        mGenre.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                afterClick(view);
            }
        });
        mArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                afterClick(view);
            }
        });

        mSearchField = (EditText) mActivity.findViewById(R.id.search_field);
        mXMark = (TextView) mActivity.findViewById(R.id.search_x);
        mMicrofon = mActivity.findViewById(R.id.search_microfon);
        mListView = (ListView) mActivity.findViewById(R.id.list_view);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.keepSynced(true);
        final ArrayList<SearchModel> list = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {

                    Users user = childSnapshot.getValue(Users.class);

                    SearchModel searchModel = new SearchModel(i, user.getUser_image(), user.getUser_name(), user.getUser_native_place());

                    list.add(searchModel);
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        searchableArrayList = list;

        mXMark.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                afterClick(view);
            }
        });
        mMicrofon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                afterClick(view);
            }
        });

        mSearchField.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void afterTextChanged(Editable editable) {
                String searchText = editable.toString().trim();
                ArrayList<SearchModel> searchedArray = new ArrayList<SearchModel>();
                for (SearchModel dm : searchableArrayList) {
                    if (dm.getText().toLowerCase()
                            .contains(searchText.toLowerCase())) {
                        searchedArray.add(dm);
                    }
                }
                if (searchText.isEmpty()) {
                    mListView.setAdapter(null);
                    mXMark.setText(R.string.fontello_x_mark);
                } else {
                    mListView.setAdapter(new SearchAdapter(mActivity,
                            searchedArray));
                    mXMark.setText(R.string.fontello_x_mark_masked);
                }
            }
        });
    }

    public void afterClick(View v) {
        switch (v.getId()) {
            case R.id.search_x:
                mSearchField.setText(null);
                break;
            case R.id.search_microfon:
                Toast.makeText(mActivity, "Implement voice search", Toast.LENGTH_LONG).show();
                break;
            case R.id.activity_search_bar_name:
                if (mName.getText() == getString(R.string.material_icon_check_empty)) {
                    mName.setText(getString(R.string.material_icon_checked_full));
                } else {
                    mName.setText(getString(R.string.material_icon_check_empty));
                }
                break;
            case R.id.activity_search_bar_native_place:
                if (mNativePlace.getText() == getString(R.string.material_icon_check_empty)) {
                    mNativePlace.setText(getString(R.string.material_icon_checked_full));
                } else {
                    mNativePlace.setText(getString(R.string.material_icon_check_empty));
                }
                break;
            case R.id.activity_search_bar_media_artist:
                if (mArtist.getText() == getString(R.string.material_icon_check_empty)) {
                    mArtist.setText(getString(R.string.material_icon_checked_full));
                } else {
                    mArtist.setText(getString(R.string.material_icon_check_empty));
                }
                break;
            case R.id.activity_search_bar_media_genre:
                if (mGenre.getText() == getString(R.string.material_icon_check_empty)) {
                    mGenre.setText(getString(R.string.material_icon_checked_full));
                } else {
                    mGenre.setText(getString(R.string.material_icon_check_empty));
                }
                break;
            case R.id.activity_search_bar_media_arrow:
                if (mFiltersLayout.getVisibility() == View.VISIBLE) {
                    mFiltersLayout.setVisibility(View.GONE);
                } else {
                    mFiltersLayout.setVisibility(View.VISIBLE);
                }
                if (mArrow.getText() == getString(R.string.material_icon_chevron_up)) {
                    mArrow.setText(getString(R.string.material_icon_chevron_down));
                } else {
                    mArrow.setText(getString(R.string.material_icon_chevron_up));
                }
                break;
        }
    }
}