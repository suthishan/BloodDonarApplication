package com.suthishan.blooddonar.adpater;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suthishan.blooddonar.R;
import com.suthishan.blooddonar.model.DonorModel;
import com.suthishan.blooddonar.model.SeekerModel;
import com.suthishan.blooddonar.utils.CallInterface;

import java.util.List;

public class SeekerAdpater extends RecyclerView.Adapter<SeekerAdpater.ViewHolder> {

    Activity applicationContext;
    CallInterface callInterface;

    List<SeekerModel.Seeker_data> seeker_data1;

    public SeekerAdpater(List<SeekerModel.Seeker_data> seeker_data,
                         Activity applicationContext, CallInterface callInterface) {
        this.applicationContext =applicationContext;
        this.seeker_data1 =seeker_data;
        this.callInterface=  callInterface;
    }

    @Override
    public SeekerAdpater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seeker_list,parent,false);
        return new SeekerAdpater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SeekerAdpater.ViewHolder holder, int position) {

        final SeekerModel.Seeker_data seeker_data = seeker_data1.get(position);
        if(seeker_data.getSname().equalsIgnoreCase("null")){
            holder.txt_seeker_name.setText("-");
        }else{
            holder.txt_seeker_name.setText(seeker_data.getSname());
        }

        if(seeker_data.getMobile().equalsIgnoreCase("null")){
            holder.txt_seeker_phone.setText("-");
        }else{
            holder.txt_seeker_phone.setText(seeker_data.getMobile());
        }
        if(seeker_data.getStatus().equalsIgnoreCase("null")){
            holder.txt_seeker_location.setText("-");
        }else{
            holder.txt_seeker_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+seeker_data.getLatitude()+","+seeker_data.getLongitude());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(applicationContext.getPackageManager()) != null) {
                        applicationContext. startActivity(mapIntent);
                    }
                }
            });
        }

        if(seeker_data.getMobile().equalsIgnoreCase("null")){
            holder.txt_seeker_call.setText("-");
        }else{
            holder.txt_seeker_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(applicationContext.getApplicationContext(),"make call to"+nearByHospitalModel.getPhcMobile()+"", Toast.LENGTH_LONG).show();
                    callInterface.makeCall(seeker_data.getMobile());

                }
            });
        }

        /*if(nearByHospitalModel.getF_latitute().equalsIgnoreCase("null")&&nearByHospitalModel.getF_longititute().equalsIgnoreCase("null")){
            holder.itemView.setVisibility(View.GONE);
        }else{
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(applicationContext.getApplicationContext(),"to view dir to google map", Toast.LENGTH_LONG).show();
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+nearByHospitalModel.getF_latitute()+","+nearByHospitalModel.getF_longititute());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(applicationContext.getPackageManager()) != null) {
                        applicationContext. startActivity(mapIntent);
                    }
                }
            });
        }*/

    }

    @Override
    public int getItemCount() {
        return seeker_data1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txt_seeker_call, txt_seeker_name,
                txt_seeker_phone, txt_seeker_location;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_seeker_name = itemView.findViewById(R.id.txt_seeker_name);

            txt_seeker_phone = itemView.findViewById(R.id.txt_seeker_phone);
            txt_seeker_location = itemView.findViewById(R.id.txt_seeker_location);
            txt_seeker_call = itemView.findViewById(R.id.txt_seeker_call);
        }
    }

}
