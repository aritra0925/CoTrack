package com.cotrack.adaptors;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.cotrack.R;
import com.cotrack.global.OrderDataHolder;
import com.cotrack.helpers.OnItemClick;
import com.cotrack.models.OrderDetails;
import com.cotrack.models.Test;
import com.cotrack.utils.CloudantOrderUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static android.R.layout.simple_spinner_item;

@SuppressWarnings("deprecation")
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

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

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
        LinearLayout askForUpdateLayout;
        LinearLayout cancelOrderLayout;
        Spinner spinner;
        String orderId;
        FrameLayout progressBarHolder;
        int postion;

        public Holder(TextView serviceTypeComponent,
                      TextView orderStatusComponent,
                      TextView requestedQuantity,
                      TextView serviceId,
                      ImageView img,
                      TextView askForUpdate,
                      TextView cancelOrder,
                      Spinner spinner,
                      LinearLayout askForUpdateLayout,
                      LinearLayout cancelOrderLayout,
                      FrameLayout progressBarHolder,
                      int position,
                      String orderId) {
            this.serviceTypeComponent = serviceTypeComponent;
            this.orderStatusComponent = orderStatusComponent;
            this.requestedQuantity = requestedQuantity;
            this.serviceId = serviceId;
            this.img = img;
            this.askForUpdate = askForUpdate;
            this.cancelOrder = cancelOrder;
            this.askForUpdateLayout = askForUpdateLayout;
            this.cancelOrderLayout = cancelOrderLayout;
            this.spinner = spinner;
            this.postion = position;
            this.progressBarHolder = progressBarHolder;
            this.orderId = orderId;
            askForUpdateLayout.setOnClickListener(new View.OnClickListener() {
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
                    orderDetailsModels.get(position).setOrder_status("Approved");
                    notifyDataSetChanged();
                    askForUpdate.setText("Process Order");
                    askForUpdate.setClickable(true);
                    askForUpdate.setEnabled(false);
                    askForUpdate.setTextColor(view.getResources().getColor(R.color.redBg) );
                    Toast.makeText(context, "Order approved successfully - Order ID: " + orderId, Toast.LENGTH_LONG).show();
                }
            });
            cancelOrderLayout.setClickable(true);
            cancelOrderLayout.setEnabled(true);
            cancelOrderLayout.setOnClickListener(new View.OnClickListener() {
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
                    orderDetailsModels.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Order cancelled successfully Order ID: " + orderId, Toast.LENGTH_LONG).show();
                }
            });
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String selectedStatus = spinner.getSelectedItem().toString();
                    inAnimation = new AlphaAnimation(0f, 1f);
                    inAnimation.setDuration(200);
                    progressBarHolder.setAnimation(inAnimation);
                    progressBarHolder.setVisibility(View.VISIBLE);
                    new DBConnect().execute(orderId, selectedStatus);
                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    progressBarHolder.setAnimation(outAnimation);
                    progressBarHolder.setVisibility(View.GONE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

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
        rowView = inflater.inflate(R.layout.layout_requested_service_list, null);
        TextView requestedQuantity = (TextView) rowView.findViewById(R.id.requestedQuantity);
        TextView serviceId = (TextView) rowView.findViewById(R.id.requestedServiceId);
        TextView cancelOrder = (TextView) rowView.findViewById(R.id.cancelOrder);
        TextView buttonComponent = (TextView) rowView.findViewById(R.id.btn_nextStatus);
        TextView serviceTypeComponent = (TextView) rowView.findViewById(R.id.serviceTypeRequestedService);
        TextView orderStatusComponent = (TextView) rowView.findViewById(R.id.orderStatusRequestedService);
        ImageView imgComponent = (ImageView) rowView.findViewById(R.id.imageViewRequestedService);
        RelativeLayout spinnerHoldeer = (RelativeLayout) rowView.findViewById(R.id.spinnerHolder);
        FrameLayout progressBarHolder = (FrameLayout) rowView.findViewById(R.id.progressBarHolder);
        LinearLayout askForUpdateLayout = (LinearLayout) rowView.findViewById(R.id.nextStatusLayout);
        LinearLayout cancelOrderLayout = (LinearLayout) rowView.findViewById(R.id.cancelOrderLayout);
        Spinner spinner = (Spinner) rowView.findViewById(R.id.testStatusUpdate);
        String orderId = orderDetailsModels.get(position).get_id();
        if(orderDetailsModels.get(position).getOrder_status().equalsIgnoreCase("APPROVED")){
            buttonComponent.setText("Process Order");
            buttonComponent.setClickable(false);
            buttonComponent.setEnabled(false);
            buttonComponent.setTextColor(rowView.getResources().getColor(R.color.redBg) );
        } else {
            buttonComponent.setClickable(true);
            buttonComponent.setEnabled(true);
        }
        Holder holder = new Holder(serviceTypeComponent,
                orderStatusComponent,
                requestedQuantity,
                serviceId,
                imgComponent,
                buttonComponent,
                cancelOrder,
                spinner,
                askForUpdateLayout,
                cancelOrderLayout,
                progressBarHolder,
                position,
                orderId);
        if (!orderDetailsModels.get(position).getService_type().equalsIgnoreCase("PATHOLOGY")) {
            spinnerHoldeer.setVisibility(View.GONE);
            System.out.println("Spinner Visibility: Checked false");
        } else {
            System.out.println("Spinner Visibility: Checked");
            holder.spinner.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rowView.getContext(), R.array.testStatus, simple_spinner_item);
            holder.spinner.setAdapter(adapter);
            Log.d("Spinner Values", orderDetailsModels.get(position).getTests().getTest_status());
            if (orderDetailsModels.get(position).getTests() != null && orderDetailsModels.get(position).getTests().getTest_status() != null) {
                List<String> spinnerArray = new ArrayList<String>(Arrays.asList(rowView.getContext().getResources().getStringArray(R.array.testStatus)));
                spinnerArray.add(orderDetailsModels.get(position).getTests().getTest_status());
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(rowView.getContext(), simple_spinner_item, spinnerArray);
                holder.spinner.setAdapter(adapter);
                try {
                    int spinnerPosition = spinnerAdapter.getPosition(orderDetailsModels.get(position).getTests().getTest_status());
                    if (holder.spinner.getSelectedItemPosition() != spinnerPosition) {
                        holder.spinner.setSelection(spinnerPosition);
                    }
                } catch (Exception ex) {
                    Log.d("Requested Service Adapter", "Exception thrown: " , ex);
                }
            }

        }
        String serviceType = orderDetailsModels.get(position).getService_type();
        String orderStatus = orderDetailsModels.get(position).getOrder_status();
        holder.requestedQuantity.setText(orderDetailsModels.get(position).getPrimaryQuantity());
        holder.serviceId.setText(orderDetailsModels.get(position).getService_id());
        holder.serviceTypeComponent.setText(serviceType);
        holder.orderStatusComponent.setText(orderStatus);
        holder.img.setImageResource(orderDetailsModels.get(position).getImgResource());
        return rowView;
    }

    class DBConnect extends AsyncTask<String, Void, Boolean> {

        private Exception exception;

        /**
         * @param objects
         * @deprecated
         */
        @Override
        protected Boolean doInBackground(String[] objects) {
            OrderDetails orderDetails = CloudantOrderUtils.getWithId(objects[0]);
            boolean flag = false;
            Test test = orderDetails.getTests();
            if (test != null) {
                if(!test.getTest_status().equalsIgnoreCase(objects[1])){
                    flag = true;
                } else {
                    return flag;
                }
                test.setTest_status(objects[1]);
            } else {
                test = new Test();
                test.setTest_type("Invalid Data");
                test.setTest_status(objects[1]);
            }
            orderDetails.setTests(test);
            CloudantOrderUtils.updateDocument(orderDetails);
            OrderDataHolder.refreshAllServiceSpecificDetails();
            Log.d("Req Adaptor", "DB updated succesfully");
            return flag;
        }

        protected void onPostExecute(Boolean feed) {
            if(feed) {
                Toast.makeText(context, "Status updated successfully for order", Toast.LENGTH_LONG).show();
            }

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
            OrderDataHolder.refreshAllServiceSpecificDetails();
            Log.d("Req Adaptor", "Document deleted succesfully");
            return "";
        }

        protected void onPostExecute(String feed) {
            //Toast.makeText(view.getContext(), feed, Toast.LENGTH_LONG).show();
        }
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
            orderDetails.setOrder_status("Approved");
            CloudantOrderUtils.updateDocument(orderDetails);
            OrderDataHolder.refreshAllUserSpecificDetails();
            Log.d("Ord Adaptor", "DB updated succesfully");
            return "";
        }

        protected void onPostExecute(String feed) {
            //Toast.makeText(view.getContext(), feed, Toast.LENGTH_LONG).show();
        }
    }
}
