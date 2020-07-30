package com.cotrack.adaptors;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cotrack.R;
import com.cotrack.global.OrderDataHolder;
import com.cotrack.helpers.OnItemClick;
import com.cotrack.models.OrderDetails;
import com.cotrack.models.Test;
import com.cotrack.utils.CloudantOrderUtils;

import java.util.List;

public class OrdersAdapter  extends BaseAdapter {

    List<OrderDataHolder> orderDetailsModels;
    Context context;
    private static LayoutInflater inflater = null;
    private OnItemClick itemClick;
    public OnItemClick getItemClick() {
        return itemClick;
    }
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

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
        TextView serviceType;
        ImageView img;
        TextView askForUpdate;
        TextView deleteOrder;
        TextView testStatus;
        FrameLayout progressBarHolder;
        int postion;

        public Holder(TextView orderIdComponent,
                      TextView orderStatusComponent,
                      TextView orderedQty,
                      TextView serviceId,
                      TextView serviceType,
                      ImageView img,
                      TextView askForUpdate,
                      TextView deleteOrder,
                      TextView testStatus,
                      FrameLayout progressBarHolder,
                      String orderId,
                      int position) {
            this.orderIdComponent = orderIdComponent;
            this.orderStatusComponent = orderStatusComponent;
            this.img = img;
            this.orderedQty = orderedQty;
            this.askForUpdate = askForUpdate;
            this.deleteOrder = deleteOrder;
            this.postion = position;
            this.serviceId = serviceId;
            this.serviceType = serviceType;
            this.testStatus = testStatus;
            this.progressBarHolder = progressBarHolder;
            askForUpdate.setClickable(true);
            askForUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inAnimation = new AlphaAnimation(0f, 1f);
                    inAnimation.setDuration(200);
                    progressBarHolder.setAnimation(inAnimation);
                    progressBarHolder.setVisibility(View.VISIBLE);
                    new ProcessOrder().execute(orderId);
                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    progressBarHolder.setAnimation(outAnimation);
                    progressBarHolder.setVisibility(View.GONE);
                    Toast.makeText(context, "Updates requested successfully - Order ID: " + orderId, Toast.LENGTH_LONG).show();
                    orderDetailsModels.get(position).setOrder_status("Update Requested");
                    notifyDataSetChanged();
                }
            });
            deleteOrder.setClickable(true);
            deleteOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inAnimation = new AlphaAnimation(0f, 1f);
                    inAnimation.setDuration(200);
                    progressBarHolder.setAnimation(inAnimation);
                    progressBarHolder.setVisibility(View.VISIBLE);
                    new DeleteOrder().execute(orderId);
                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    progressBarHolder.setAnimation(outAnimation);
                    progressBarHolder.setVisibility(View.GONE);
                    Toast.makeText(context, "Order deleted successfully Order ID: " + orderId, Toast.LENGTH_LONG).show();
                    orderDetailsModels.remove(position);
                    notifyDataSetChanged();
                }
            });
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
        TextView serviceType = (TextView) rowView.findViewById(R.id.serviceTypeOrder);
        TextView testStatus = (TextView) rowView.findViewById(R.id.testStatusView);
        ImageView imgComponent = (ImageView) rowView.findViewById(R.id.imageViewOrder);
        FrameLayout progressBarHolder = (FrameLayout) rowView.findViewById(R.id.progressBarHolder);
        String orderId = orderDetailsModels.get(position).get_id();
        Holder holder = new Holder(orderIdComponent, orderStatusComponent, orderedQty, serviceId, serviceType, imgComponent,
                buttonComponent, deleteComponent, testStatus, progressBarHolder, orderId, position);

        String serviceTypeText = orderDetailsModels.get(position).getService_type();

        if(orderDetailsModels.get(position).getOrder_status().equalsIgnoreCase("Update Requested")) {
            buttonComponent.setText("Update Requested");
            buttonComponent.setEnabled(false);
            buttonComponent.setClickable(false);
            buttonComponent.setTextColor(rowView.getResources().getColor(R.color.greenBg));
        }

        if(serviceTypeText.equalsIgnoreCase("PATHOLOGY")){
            if(orderDetailsModels.get(position).getTests() != null
                    && orderDetailsModels.get(position).getTests().getTest_status() != null
                    && orderDetailsModels.get(position).getTests().getTest_type() != null){
                holder.testStatus.setText(orderDetailsModels.get(position).getTests().getTest_type() + " Diagnosis: " + orderDetailsModels.get(position).getTests().getTest_status());
            } else {
                holder.testStatus.setText("Sample" + " Diagnosis: Yet to be updated");
            }
        } else {
                testStatus.setVisibility(View.GONE);
        }
        String orderStatus = orderDetailsModels.get(position).getOrder_status();
        holder.orderedQty.setText(orderDetailsModels.get(position).getPrimaryQuantity());
        holder.serviceId.setText(orderDetailsModels.get(position).getService_id());
        holder.serviceType.setText(serviceTypeText);
        holder.orderIdComponent.setText(orderId);
        holder.orderStatusComponent.setText(orderStatus);
        holder.img.setImageResource(orderDetailsModels.get(position).getImgResource());
        return rowView;
    }

    class ProcessOrder extends AsyncTask<String, Void, String> {

        private Exception exception;

        /**
         * @param objects
         * @deprecated
         */
        @Override
        protected String doInBackground(String[] objects) {
            OrderDetails orderDetails = CloudantOrderUtils.getWithId(objects[0]);
            orderDetails.setOrder_status("Update Requested");
            CloudantOrderUtils.updateDocument(orderDetails);
            OrderDataHolder.refreshAllUserSpecificDetails();
            Log.d("Ord Adaptor", "DB updated succesfully");
            return "";
        }

        protected void onPostExecute(String feed) {
            //Toast.makeText(view.getContext(), feed, Toast.LENGTH_LONG).show();
        }
    }

    class DeleteOrder extends AsyncTask<String, Void, String> {

        private Exception exception;

        /**
         * @param objects
         * @deprecated
         */
        @Override
        protected String doInBackground(String[] objects) {
            OrderDetails orderDetails = CloudantOrderUtils.getWithId(objects[0]);
            CloudantOrderUtils.deleteDocument(orderDetails);
            OrderDataHolder.refreshAllUserSpecificDetails();
            Log.d("Ord Adaptor", "Document deleted succesfully");
            return "";
        }

        protected void onPostExecute(String feed) {
            //Toast.makeText(view.getContext(), feed, Toast.LENGTH_LONG).show();
        }
    }
}
