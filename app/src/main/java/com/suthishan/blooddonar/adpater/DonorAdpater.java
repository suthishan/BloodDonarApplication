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
import com.suthishan.blooddonar.utils.CallInterface;

import java.util.List;

public class DonorAdpater extends RecyclerView.Adapter<DonorAdpater.ViewHolder> {

    Activity applicationContext;
    CallInterface callInterface;
    List<DonorModel.Donor_data> donor_dataList;

    public DonorAdpater(List<DonorModel.Donor_data> donor_data, Activity applicationContext, CallInterface callInterface) {
        this.applicationContext =applicationContext;
        this.donor_dataList =donor_data;
        this.callInterface=  callInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

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
            holder.txt_donor_location.setText(donor_data.getStatus());
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

        public ViewHolder(View itemView) {
            super(itemView);
            txt_donor_name = itemView.findViewById(R.id.txt_donor_name);
            txt_donor_blood_group = itemView.findViewById(R.id.txt_donor_blood_group);
            txt_donor_phone = itemView.findViewById(R.id.txt_donor_phone);
            txt_donor_location = itemView.findViewById(R.id.txt_donor_location);
            txt_donor_call = itemView.findViewById(R.id.txt_donor_call);
        }
    }

}
