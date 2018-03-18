package com.example.acer.project;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ApplyFilterFragment extends Fragment {

    private Button applyFilter;
    private Activity mActivity;
    private ApplyFilterFragment applyFilterFragment;


    public ApplyFilterFragment() {
        // Required empty public constructor
        applyFilterFragment=this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyFilter=(Button) mActivity.findViewById(R.id.apply_filter);
        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.FragmentTransaction fragmentTransaction;
                fragmentTransaction=getFragmentManager().beginTransaction().show(getParentFragment());
                fragmentTransaction.commit();
                fragmentTransaction=getFragmentManager().beginTransaction().hide(applyFilterFragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_filter, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
