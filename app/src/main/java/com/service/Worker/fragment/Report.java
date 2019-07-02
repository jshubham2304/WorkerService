package com.service.Worker.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.service.Worker.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Report extends Fragment {
    FirebaseAuth auth;
    DatabaseReference mref;

    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.spinner)
    TextView spinner;
    @BindView(R.id.problem)
    AppCompatEditText problem;
    Unbinder unbinder;
    @BindView(R.id.report_btn)
    Button reportBtn;

    public Report() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        unbinder = ButterKnife.bind(this, view);
        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = auth.getCurrentUser();
        final String a = user.getUid().toString();
        mref = FirebaseDatabase.getInstance().getReference().child("UrbanClap").child("ReportToAdmin").push();

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        if(title.getText().equals("") && problem.getText().equals(""))
        {
            Toast.makeText(getActivity(), "Please fill the feild!!", Toast.LENGTH_SHORT).show();
        }
        else{mref.child("IssuerName").setValue(user.getDisplayName());
            mref.child("IssuerEmail").setValue(user.getEmail());
            mref.child("uid").setValue(user.getUid());
            mref.child("Title").setValue(title.getText().toString());
            mref.child("categoryIssue").setValue(spinner.getText());
            mref.child("desc").setValue(problem.getText().toString());
            }

            }
        });
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<Integer> selectedList = new ArrayList<>();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final String[] selectedItem = new String[1];
                final String[] List = {"Payment Issue", "Order ID", "Payment Unsuccessfull"};
                builder.setTitle("This is list choice Category");
                builder.setSingleChoiceItems(
                        List, // Items list
                        0, // Index of checked item (-1 = no selection)
                        new DialogInterface.OnClickListener() // Item click listener
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Get the alert dialog selected item's text
                                selectedItem[0] = List[i];

                            }
                        });

                builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        spinner.setText(selectedItem[0]);


                    }
                });

                builder.show();


            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
