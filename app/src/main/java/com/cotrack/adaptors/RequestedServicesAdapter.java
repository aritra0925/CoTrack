package com.cotrack.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cotrack.R;
import com.cotrack.global.OrderDataHolder;
import com.cotrack.helpers.OnItemClick;

import java.util.List;

public class RequestedServicesAdapter extends BaseAdapter {

    List<OrderDataHolder> orderDetailsModels;
    Context context;
    private static LayoutInflater inflater = null;
    private OnItemClick itemClick;

    public OnItemClick getItemClick() {
        return itemClick;
    }

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public RequestedServicesAdapter(Context context, List<OrderDataHolder> orderDetailsModels) {
        this.context = context;
        this.orderDetailsModels = orderDetailsModels;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderDetailsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder implements Button.OnClickListener {
        TextView serviceTypeComponent;
        TextView orderStatusComponent;
        TextView requestedQuantity;
        TextView serviceId;
        ImageView img;
        TextView askForUpdate;
        TextView cancelOrder;
        int postion;

        public Holder(TextView serviceTypeComponent,
                      TextView orderStatusComponent,
                      TextView requestedQuantity,
                      TextView serviceId,
                      ImageView img,
                      TextView askForUpdate,
                      TextView cancelOrder,
                      int position) {
            this.serviceTypeComponent = serviceTypeComponent;
            this.orderStatusComponent = orderStatusComponent;
            this.requestedQuantity = requestedQuantity;
            this.serviceId = serviceId;
            this.img = img;
            this.askForUpdate = askForUpdate;
            this.cancelOrder = cancelOrder;
            this.postion = position;
            askForUpdate.setClickable(true);
            askForUpdate.setOnClickListener(this);
            cancelOrder.setOnClickListener(this);
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
        rowView = inflater.inflate(R.layout.layout_requested_service_list, null);

        TextView requestedQuantity = (TextView) rowView.findViewById(R.id.requestedQuantity);
        TextView serviceId = (TextView) rowView.findViewById(R.id.requestedServiceId);
        TextView cancelOrder = (TextView) rowView.findViewById(R.id.cancelOrder);
        TextView buttonComponent = (TextView) rowView.findViewById(R.id.btn_nextStatus);
        TextView serviceTypeComponent = (TextView) rowView.findViewById(R.id.serviceTypeRequestedService);
        TextView orderStatusComponent = (TextView) rowView.findViewById(R.id.orderStatusRequestedService);
        ImageView imgComponent = (ImageView) rowView.findViewById(R.id.imageViewRequestedService);

        Holder holder = new Holder(serviceTypeComponent,
                orderStatusComponent,
                requestedQuantity,
                serviceId,
                imgComponent,
                buttonComponent,
                cancelOrder,
                position);

        String serviceType = orderDetailsModels.get(position).getService_type();
        String orderStatus = orderDetailsModels.get(position).getOrder_status();
        holder.requestedQuantity.setText(orderDetailsModels.get(position).getPrimaryQuantity());
        holder.serviceId.setText(orderDetailsModels.get(position).getService_id());
        holder.serviceTypeComponent.setText(serviceType);
        holder.orderStatusComponent.setText(orderStatus);
        holder.img.setImageResource(orderDetailsModels.get(position).getImgResource());
        return rowView;
    }
}
