package com.example.acer.projectcopy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;


public class CreatePlanFragment extends Fragment {

    private Activity mActivity;
    private EditText planTitle;
    private EditText planDesc;
    private EditText tripType;
    private EditText budget;
    private EditText source;
    private EditText destination;
    private EditText partnerDesc;
    private EditText allPlaces;
    private EditText noOfDays;
    private TextView fromDate;
    private TextView toDate;
    private Button createButton;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private DatabaseReference databaseRefToId;

    private DatePicker datePicker;
    private Calendar calendar;

    private String userId;
    private ProgressDialog loadingBar;
    private DatePickerDialog.OnDateSetListener toDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showtoDate(arg1, arg2, arg3);
                }
            };
    private DatePickerDialog.OnDateSetListener fromDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showfromDate(arg1, arg2, arg3);
                }
            };

    public CreatePlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void updatePlanDetails() {

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Plans");
        databaseRefToId = databaseRef.push();

        Plan newPlan = new Plan();

        newPlan.setAuthorId(userId);
        newPlan.setBudget(Integer.parseInt(budget.getText().toString()));
        newPlan.setCreatedDate(new Date().getTime());
        newPlan.setDescription(planDesc.getText().toString());
        newPlan.setDescriptionOfTravelPartner(partnerDesc.getText().toString());
        newPlan.setDestination(destination.getText().toString());
        newPlan.setId(databaseRefToId.getKey().toString());
        newPlan.setNoOfDays(Integer.parseInt(noOfDays.getText().toString()));
        newPlan.setPlacesToTravel(allPlaces.getText().toString());
        newPlan.setSource(source.getText().toString());
        newPlan.setTitle(planTitle.getText().toString());
        newPlan.setTripType(tripType.getText().toString());

        databaseRefToId.updateChildren(newPlan.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(mActivity, "Created Plan Successfully", Toast.LENGTH_SHORT).show();
                    planTitle.setText("");
                    planDesc.setText("");
                    allPlaces.setText("");
                    budget.setText("");
                    partnerDesc.setText("");
                    tripType.setText("");
                    source.setText("");
                    destination.setText("");
                    noOfDays.setText("");
                    fromDate.setText("");
                    toDate.setText("");
                    planTitle.requestFocus();
                    loadingBar.dismiss();
                } else {
                    Toast.makeText(mActivity, "Please Try Again", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_plan, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        planTitle = (EditText) mActivity.findViewById(R.id.plan_title);
        planDesc = (EditText) mActivity.findViewById(R.id.plan_description);
        allPlaces = (EditText) mActivity.findViewById(R.id.all_places);
        budget = (EditText) mActivity.findViewById(R.id.plan_budget);
        partnerDesc = (EditText) mActivity.findViewById(R.id.plan_partner_description);
        tripType = (EditText) mActivity.findViewById(R.id.plan_trip_type);
        source = (EditText) mActivity.findViewById(R.id.plan_source);
        destination = (EditText) mActivity.findViewById(R.id.plan_destination);
        createButton = (Button) mActivity.findViewById(R.id.create_plan_button);
        noOfDays = (EditText) mActivity.findViewById(R.id.no_of_days);
        fromDate = (TextView) mActivity.findViewById(R.id.plan_from_date);
        toDate = (TextView) mActivity.findViewById(R.id.plan_to_date);
        createButton = (Button) view.findViewById(R.id.create_plan_button);

        calendar = Calendar.getInstance();

        fromDate.setOnClickListener(new View.OnClickListener() {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            @Override
            public void onClick(View view) {
                DatePickerDialog dp = new DatePickerDialog(mActivity,
                        fromDateListener, year, month, day);
                dp.show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            @Override
            public void onClick(View view) {
                DatePickerDialog dp = new DatePickerDialog(mActivity,
                        toDateListener, year, month, day);
                dp.show();

            }
        });


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar = new ProgressDialog(mActivity);
                loadingBar.setTitle("Please Wait !!!");
                loadingBar.setMessage("Creating Plan");
                loadingBar.show();
                updatePlanDetails();
            }
        });
    }

    private void showfromDate(int year, int month, int day) {
        fromDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    private void showtoDate(int year, int month, int day) {
        toDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
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


}
