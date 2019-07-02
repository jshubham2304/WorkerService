package com.service.Worker.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.service.Worker.R;

import java.util.ArrayList;

import butterknife.BindView;

import static android.support.v7.widget.RecyclerView.ViewHolder;
import static android.view.View.GONE;

public class Ord_Req_Adapter extends RecyclerView.Adapter<Ord_Req_Adapter.MyViewHolder> {
  public   ArrayList<Details> detailList= new ArrayList<Details>();


    public Ord_Req_Adapter(ArrayList<Details> details) {
        this.detailList = details;
    }

    public class MyViewHolder extends ViewHolder {
        public TextView description, date, namePerson,contactPerson,addressPerson;
        public Button detailsPerson,confirm;
        LinearLayout personLin;

        public MyViewHolder(View view) {
            super(view);
            description=(TextView)view.findViewById(R.id.description);
            date=(TextView)view.findViewById(R.id.date);
            namePerson=(TextView)view.findViewById(R.id.name_person);
            contactPerson=(TextView)view.findViewById(R.id.contact_person);
            addressPerson=(TextView)view.findViewById(R.id.address_person);
            personLin =(LinearLayout)view.findViewById(R.id.person_lin);
            detailsPerson=(Button) view.findViewById(R.id.detailsofPerson);
            confirm=(Button)view.findViewById(R.id.confirm);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_order_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {
        Details details = detailList.get(i);
        holder.description.setText(details.getDesc());
        holder.namePerson.setText("Name   :"+details.getName());
        holder.date.setText(details.getDate());
        holder.addressPerson.setText("Address  :"+details.getAddress());
        holder.contactPerson.setText("Number  :"+details.getPhone());
        holder.detailsPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.personLin.getVisibility() == View.GONE)
                {
                    holder.personLin.setVisibility(View.VISIBLE);
                }else{
                    holder.personLin.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }
}