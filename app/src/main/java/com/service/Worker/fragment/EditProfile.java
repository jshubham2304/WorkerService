package com.service.Worker.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.service.Worker.ui.MainActivity;
import com.service.Worker.ui.Registeration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfile extends Fragment {
    List<String> items = new ArrayList<String>();
    DatabaseReference mref,data_ref;
    FirebaseAuth auth;
    String S_name, S_phone, S_email, S_aadhar, S_age, S_education, S_category, S_address;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
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
    @BindView(R.id.confirm)
    Button confirm;
    Unbinder unbinder;
    @BindView(R.id.imageSelect)
    FloatingActionButton imageSelect;
    public String seletedItem;
    public EditProfile() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = auth.getCurrentUser();
        final String a = user.getUid().toString();
        mref = FirebaseDatabase.getInstance().getReference().child("UrbanClap");
        data_ref = mref.child("available services");
        SharedPreferences prefs = getActivity().getSharedPreferences("myDetails", MODE_PRIVATE);

        S_name = prefs.getString("name", null);
        S_phone = prefs.getString("phone", null);
        S_email = prefs.getString("email", null);
        S_aadhar = prefs.getString("aadhar", null);
        S_age = prefs.getString("age", null);//
        S_education = prefs.getString("education", null);
        S_category = prefs.getString("category", null);
        S_address = prefs.getString("address", null);
        if (name != null) {
            name.setText(user.getDisplayName());
            phone.setText(S_phone);
            email.setText(user.getEmail());
            address.setText(S_address);
            age.setText(S_age);
            education.setText(S_education);
            select.setText(S_category);
            aadhar.setText(S_aadhar);


        }
        imageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });
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

                final ArrayList<Integer> selectedList = new ArrayList<>();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("myDetails", MODE_PRIVATE).edit();

                editor.putString("name", name.getText().toString());
                editor.putString("phone", phone.getText().toString());
                editor.putString("email", email.getText().toString());
                editor.putString("aadhar", aadhar.getText().toString());
                editor.putString("age", age.getText().toString());
                editor.putString("education", education.getText().toString());
                editor.putString("category", select.getText().toString());
                editor.putString("address", address.getText().toString());


                editor.apply();

//                mref = mref.child("serviceProvider").child("worker");
                mref.child("serviceProvider").child("worker").child(a).child("image").setValue(user.getPhotoUrl().toString());
                mref.child("serviceProvider").child("worker").child(a).child("email").setValue(user.getEmail().toString());
                mref.child("serviceProvider").child("worker").child(a).child("name").setValue(user.getDisplayName().toString());
                mref.child("serviceProvider").child("worker").child(a).child("phone").setValue(phone.getText().toString());
                mref.child("serviceProvider").child("worker").child(a).child("aadhar").setValue(aadhar.getText().toString());
                mref.child("serviceProvider").child("worker").child(a).child("age").setValue(age.getText().toString());
                mref.child("serviceProvider").child("worker").child(a).child("education").setValue(education.getText().toString());
                mref.child("serviceProvider").child("worker").child(a).child("address").setValue(address.getText().toString());
                mref.child("serviceProvider").child("worker").child(a).child("category").setValue(select.getText().toString());
                startActivity(new Intent(getActivity(), MainActivity.class));

            }
        });

        return view;
    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, 121);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 121:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    image.setImageURI(selectedImage);
                    break;
            }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
