package com.cotrack.adaptors;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cotrack.R;
import com.cotrack.fragments.ChatFragment;
import com.cotrack.fragments.HomeFragment;
import com.cotrack.fragments.ServiceFragment;
import com.cotrack.models.ServiceDetailsModel;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends BaseAdapter {
    List<ServiceDetailsModel> serviceDetailsModels;
    Context context;
    int[] imageId;
    private static LayoutInflater inflater = null;

    public ServiceAdapter(Context context, List<ServiceDetailsModel> serviceDetailsModels) {
        this.context = context;
        this.serviceDetailsModels = serviceDetailsModels;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return serviceDetailsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView serviceType;
        TextView serviceCount;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.layout_service_list, null);
        holder.serviceType = (TextView) rowView.findViewById(R.id.serviceType);
        holder.serviceCount = (TextView) rowView.findViewById(R.id.serviceCount);
        holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
        String serviceType = serviceDetailsModels.get(position).getServiceType();
        int serviceCount = serviceDetailsModels.get(position).getServiceCount();
        holder.serviceType.setText(serviceType);
        holder.serviceCount.setText("Available Services: " + serviceCount);
        if(serviceCount==0) {
            holder.serviceCount.setTextColor(Color.RED);
        }
        switch (serviceType.toUpperCase()) {
            case "AMBULANCE":
                holder.img.setImageResource(R.drawable.ambulance_icon);
                break;
            case "DOCTOR":
                holder.img.setImageResource(R.drawable.doctor_icon);
                break;
            case "PATHOLOGY":
                holder.img.setImageResource(R.drawable.pathology_icon);
                break;
            case "DISINFECTANT":
                holder.img.setImageResource(R.drawable.disinfect_icon);
                break;
            case "MEDICINE":
                holder.img.setImageResource(R.drawable.medicine_icon);
                break;
            case "HOSPITAL":
                holder.img.setImageResource(R.drawable.hospital_icon);
                break;
            default:
                holder.img.setImageResource(R.drawable.medical_icon);
        }
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Clicked " + serviceDetailsModels.get(position).getServiceType(), Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }
}
