package com.service.Worker.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.service.Worker.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class YourOrder extends Fragment {


    public YourOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_your_order, container, false);
    }


}
