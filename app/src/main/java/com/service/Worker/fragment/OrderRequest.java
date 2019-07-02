package com.service.Worker.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.service.Worker.R;
import com.service.Worker.model.Details;
import com.service.Worker.model.Ord_Req_Adapter;
import com.service.Worker.ui.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.service.Worker.ui.MainActivity.category;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderRequest extends Fragment {
    ArrayList<Details> dtaRequest = new ArrayList<Details>();
    DatabaseReference mref;
    MainActivity mainActivity;
    @BindView(R.id.recycler_request)
    RecyclerView recyclerRequest;
    Unbinder unbinder;
    FirebaseAuth auth;
    DatabaseReference mdata;
    @BindView(R.id.swipe_load)
    SwipeRefreshLayout swipeLoad;

    public OrderRequest() {
        dtaRequest.clear();
        // Required empty public constructor
    }

    public Ord_Req_Adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_request, container, false);
        unbinder = ButterKnife.bind(this, view);
        auth = FirebaseAuth.getInstance();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerRequest.setLayoutManager(mLayoutManager);
        recyclerRequest.setItemAnimator(new DefaultItemAnimator());
        mref = FirebaseDatabase.getInstance().getReference().child("UrbanClap").child("bookings").child(category);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dtaRequest.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dtaRequest.add(new Details(dataSnapshot.child("Name").getValue().toString(), dataSnapshot.child("Phone Number").getValue().toString(),
                            dataSnapshot.child("Address").getValue().toString(), dataSnapshot.child("Date").getValue().toString(), dataSnapshot.child("Description").getValue().toString()));
                }
                 adapter= new Ord_Req_Adapter(dtaRequest);
                if (dtaRequest.size() != 0) {
                    recyclerRequest.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        swipeLoad.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                            adapter.notifyDataSetChanged();
                            swipeLoad.setRefreshing(false);
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
