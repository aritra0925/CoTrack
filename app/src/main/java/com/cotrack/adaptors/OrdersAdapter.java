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

public class OrdersAdapter  extends BaseAdapter {

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

    public OrdersAdapter(Context context, List<OrderDataHolder> orderDetailsModels) {
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

    public class Holder implements View.OnClickListener {
        TextView orderIdComponent;
        TextView orderStatusComponent;
        TextView orderedQty;
        TextView serviceId;
        ImageView img;
        TextView askForUpdate;
        TextView deleteOrder;
        int postion;

        public Holder(TextView orderIdComponent,
                      TextView orderStatusComponent,
                      TextView orderedQty,
                      TextView serviceId,
                      ImageView img,
                      TextView askForUpdate,
                      TextView deleteOrder,
                      int position) {
            this.orderIdComponent = orderIdComponent;
            this.orderStatusComponent = orderStatusComponent;
            this.img = img;
            this.orderedQty = orderedQty;
            this.askForUpdate = askForUpdate;
            this.deleteOrder = deleteOrder;
            this.postion = position;
            this.serviceId = serviceId;
            askForUpdate.setClickable(true);
            askForUpdate.setOnClickListener(this);
            deleteOrder.setOnClickListener(this);
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
        rowView = inflater.inflate(R.layout.layout_order_list, null);

        TextView deleteComponent = (TextView) rowView.findViewById(R.id.removeOrder);
        TextView buttonComponent = (TextView) rowView.findViewById(R.id.btn_askForUpdate);
        TextView orderIdComponent = (TextView) rowView.findViewById(R.id.orderId);
        TextView orderStatusComponent = (TextView) rowView.findViewById(R.id.orderStatus);
        TextView orderedQty = (TextView) rowView.findViewById(R.id.orderedQuantity);
        TextView serviceId = (TextView) rowView.findViewById(R.id.serviceId);
        ImageView imgComponent = (ImageView) rowView.findViewById(R.id.imageViewOrder);

        Holder holder = new Holder(orderIdComponent, orderStatusComponent, orderedQty, serviceId, imgComponent, buttonComponent, deleteComponent, position);

        String orderId = orderDetailsModels.get(position).get_id();
        String orderStatus = orderDetailsModels.get(position).getOrder_status();
        holder.orderedQty.setText(orderDetailsModels.get(position).getPrimaryQuantity());
        holder.serviceId.setText(orderDetailsModels.get(position).getService_id());
        holder.orderIdComponent.setText(orderId);
        holder.orderStatusComponent.setText(orderStatus);
        holder.img.setImageResource(orderDetailsModels.get(position).getImgResource());
        return rowView;
    }
}
