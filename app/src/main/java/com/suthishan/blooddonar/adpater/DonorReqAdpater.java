package com.suthishan.blooddonar.adpater;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.suthishan.blooddonar.R;
import com.suthishan.blooddonar.model.DonorModel;
import com.suthishan.blooddonar.utils.CallInterface;
import com.suthishan.blooddonar.utils.REquestBloodInterface;

import java.util.List;

public class DonorReqAdpater extends RecyclerView.Adapter<DonorReqAdpater.ViewHolder> {

    Activity applicationContext;
    CallInterface callInterface;
    REquestBloodInterface rEquestBloodInterface;
    List<DonorModel.Donor_data> donor_dataList;

    public DonorReqAdpater(List<DonorModel.Donor_data> donor_data, Activity applicationContext,
                           CallInterface callInterface, REquestBloodInterface rEquestBloodInterface) {
        this.applicationContext =applicationContext;
        this.donor_dataList =donor_data;
        this.callInterface=  callInterface;
        this.rEquestBloodInterface = rEquestBloodInterface;
    }

    @Override
    public DonorReqAdpater.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_blood,parent,false);
        return new DonorReqAdpater.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final DonorReqAdpater.ViewHolder holder, final int position) {

        final DonorModel.Donor_data donor_data = donor_dataList.get(position);
        if(donor_data.getDname().equalsIgnoreCase("null")){
            holder.txt_donor_name.setText("-");
        }else{
            holder.txt_donor_name.setText(donor_data.getDname());
        }
        if(donor_data.getBloodgroup().equalsIgnoreCase("null")){
            holder.txt_donor_blood_group.setText("-");
        }else{
            holder.txt_donor_blood_group.setText(donor_data.getBloodgroup());
        }
        if(donor_data.getMobile().equalsIgnoreCase("null")){
            holder.txt_donor_phone.setText("-");
        }else{
            holder.txt_donor_phone.setText(donor_data.getMobile());
        }
        if(donor_data.getStatus().equalsIgnoreCase("null")){
            holder.txt_donor_location.setText("-");
        }else{
            holder.txt_donor_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q="+donor_data.getLatitude()+","+donor_data.getLongitude());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(applicationContext.getPackageManager()) != null) {
                        applicationContext. startActivity(mapIntent);
                    }
                }
            });
        }

        if(donor_data.getMobile().equalsIgnoreCase("null")){
            holder.txt_donor_call.setText("-");
        }else{
            holder.txt_donor_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(applicationContext.getApplicationContext(),"make call to"+nearByHospitalModel.getPhcMobile()+"", Toast.LENGTH_LONG).show();
                    callInterface.makeCall(donor_data.getMobile());

                }
            });
        }
        holder.request_donor_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rEquestBloodInterface.requestBlood(v,position);

                Log.d("CLICK-->" , String.valueOf(rEquestBloodInterface));

//                Toast.makeText(v.getContext(), "Button Click" + position, Toast.LENGTH_SHORT).show();
            }
        });

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
        return donor_dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txt_donor_call, txt_donor_name, txt_donor_blood_group, txt_donor_phone, txt_donor_location;
        ImageButton request_donor_blood;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_donor_name = itemView.findViewById(R.id.txt_donor_name);
            txt_donor_blood_group = itemView.findViewById(R.id.txt_donor_blood_group);
            txt_donor_phone = itemView.findViewById(R.id.txt_donor_phone);
            txt_donor_location = itemView.findViewById(R.id.txt_donor_location);
            txt_donor_call = itemView.findViewById(R.id.txt_donor_call);
            request_donor_blood = itemView.findViewById(R.id.request_donor_blood);
        }
    }


}
