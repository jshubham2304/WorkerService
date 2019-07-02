package com.service.Worker.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.service.Worker.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.media.CamcorderProfile.get;

public class Registeration extends AppCompatActivity {
    public String seletedItem =null;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.aadhar)
    EditText aadhar;
    @BindView(R.id.age)
    EditText age;
    @BindView(R.id.education)
    EditText education;
    @BindView(R.id.select)
    TextView select;
    @BindView(R.id.address)
    EditText address;
    ListView listView;
    ArrayAdapter<String> adapter;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.confirm)
    Button confirm;
    DatabaseReference mref,data_ref;
    FirebaseAuth auth;
    List<String> items = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = auth.getCurrentUser();
        final String a = user.getUid().toString();
        name.setText(user.getDisplayName());
        email.setText(user.getEmail());
        mref = FirebaseDatabase.getInstance().getReference().child("UrbanClap");
        data_ref = mref.child("available services");
        data_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot dataSnapshot1  :  dataSnapshot.getChildren())
                {
                    items.add(dataSnapshot1.getKey());

//                    Toast.makeText(Registeration.this, ""+dataSnapshot1.getKey(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        txtTitle.setVisibility(View.VISIBLE);
        select.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Registeration.this);
                final String[] List= items.toArray(new String[0]);
                builder.setTitle("This is list choice Category");
                builder.setSingleChoiceItems(
                         List, // Items list
                        -1, // Index of checked item (-1 = no selection)
                        new DialogInterface.OnClickListener() // Item click listener
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Get the alert dialog selected item's text
                                seletedItem =(String)items.get(i);

                            }
                        });

                builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        select.setText(seletedItem);
                    }
                });
                builder.show();

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences("myDetails", MODE_PRIVATE).edit();

                editor.putString("name", name.getText().toString());
                editor.putString("phone", phone.getText().toString());
                editor.putString("email", email.getText().toString());
                editor.putString("aadhar", aadhar.getText().toString());
                editor.putString("age", age.getText().toString());
                editor.putString("education",education.getText().toString());
                editor.putString("category", select.getText().toString());
                editor.putString("address",address.getText().toString());


                editor.apply();

                mref = mref.child("serviceProvider").child("worker");
                mref.child(a).child("image").setValue(user.getPhotoUrl().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mref.child(a).child("email").setValue(user.getEmail().toString());
                        mref.child(a).child("name").setValue(user.getDisplayName().toString());
                        mref.child(a).child("phone").setValue(phone.getText().toString());
                        mref.child(a).child("aadhar").setValue(aadhar.getText().toString());
                        mref.child(a).child("age").setValue(age.getText().toString());
                        mref.child(a).child("education").setValue(education.getText().toString());
                        mref.child(a).child("address").setValue(address.getText().toString());
                        mref.child(a).child("category").setValue(select.getText().toString());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(SplashActivity.this, "Photo is not uploaded", Toast.LENGTH_SHORT).show();
                    }
                });

                finish();
            }
        });
    }

}
