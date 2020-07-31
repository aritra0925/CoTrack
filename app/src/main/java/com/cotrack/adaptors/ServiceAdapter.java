package com.cotrack.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import com.cotrack.R;
import com.cotrack.global.AssetDataHolder;
import com.cotrack.helpers.OnItemClick;
import java.util.List;

public class ServiceAdapter extends BaseAdapter{
    List<AssetDataHolder> serviceDetailsModels;
    Context context;
    int[] imageId;
    private static LayoutInflater inflater = null;
    private OnItemClick itemClick;
    public OnItemClick getItemClick() {
        return itemClick;
    }

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public ServiceAdapter(Context context, List<AssetDataHolder> serviceDetailsModels) {
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

    public class Holder implements CardView.OnClickListener {
        TextView serviceType;
        TextView serviceCount;
        ImageView img;
        CardView cardView;
        int postion;

        public Holder(TextView serviceType,
                      TextView serviceCount,
                      ImageView img,
                      CardView cardView,
                      int position) {
            this.serviceType = serviceType;
            this.serviceCount = serviceCount;
            this.img = img;
            this.cardView = cardView;
            this.postion = position;
            cardView.setClickable(true);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClick != null) {
                itemClick.onItemClicked(postion);
            }
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = inflater.inflate(R.layout.layout_service_list, null);

        CardView cardViewComponent = (CardView) rowView.findViewById(R.id.cardView1);
        TextView serviceTypeComponent = (TextView) rowView.findViewById(R.id.serviceType);
        TextView serviceCountComponent = (TextView) rowView.findViewById(R.id.serviceCount);
        ImageView imgComponent = (ImageView) rowView.findViewById(R.id.imageView1);

        Holder holder = new Holder(serviceTypeComponent, serviceCountComponent, imgComponent, cardViewComponent, position);

        String serviceName = serviceDetailsModels.get(position).getAsset_type();
        String serviceType = serviceDetailsModels.get(position).getAsset_title();
        int serviceCount = serviceDetailsModels.get(position).getAsset_available_services_count();
        holder.serviceType.setText(serviceType);
        holder.serviceCount.setText("Available Services: " + serviceCount);
        if (serviceCount == 0) {
            holder.serviceCount.setTextColor(ContextCompat.getColor(context, R.color.primary_dark));
        }
        switch (serviceName.toUpperCase()) {
            case "AMBULANCE":
                holder.img.setImageResource(R.drawable.ambulance_icon);
                break;
            case "DOCTOR":
                holder.img.setImageResource(R.drawable.doctor_icon);
                break;
            case "PATHOLOGY":
                holder.img.setImageResource(R.drawable.pathology_icon);
                break;
            case "DISINFECT":
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
        return rowView;
    }
}
