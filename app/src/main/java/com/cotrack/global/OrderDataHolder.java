package com.cotrack.global;

import android.util.Log;

import com.cloudant.client.api.query.QueryResult;
import com.cotrack.R;
import com.cotrack.models.OrderDetails;
import com.cotrack.utils.CloudantOrderUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDataHolder {

    public String _id;
    public String service_id;
    public String order_status;
    public String service_type;
    public String user_id;
    public int imgResource;
    public String primaryQuantity;
    public String scheduledAppointment;

    private static List<OrderDataHolder> allInstances;
    private static Map<String, List<OrderDataHolder>> userSpecificOrderDetails;
    private static Map<String, List<OrderDataHolder>> serviceSpecificOrderDetails;
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public String getPrimaryQuantity() {
        return primaryQuantity;
    }

    public void setPrimaryQuantity(String primaryQuantity) {
        this.primaryQuantity = primaryQuantity;
    }

    public String getScheduledAppointment() {
        return scheduledAppointment;
    }

    public void setScheduledAppointment(String scheduledAppointment) {
        this.scheduledAppointment = scheduledAppointment;
    }

    public static List<OrderDataHolder> getAllInstances() {
        if (allInstances == null) {
            allInstances = new ArrayList<>();
            QueryResult<OrderDetails> queryResult = CloudantOrderUtils.getAllData();
            for (OrderDetails orderDetails : queryResult.getDocs()) {
                OrderDataHolder holder = new OrderDataHolder();
                holder.set_id(orderDetails.get_id());
                holder.setOrder_status(orderDetails.getOrder_status());
                holder.setService_id(orderDetails.getService_id());
                holder.setService_type(orderDetails.getService_type());
                holder.setUser_id(orderDetails.getUser_id());
                holder.setPrimaryQuantity(orderDetails.getPrimary_quantity());
                holder.setScheduledAppointment(orderDetails.getScheduled_appointment());
                switch (orderDetails.getService_type().toUpperCase()) {
                    case "AMBULANCE":
                        holder.setImgResource(R.drawable.ambulance_icon);
                        break;
                    case "DOCTOR":
                        holder.setImgResource(R.drawable.doctor_icon);
                        break;
                    case "PATHOLOGY":
                        holder.setImgResource(R.drawable.pathology_icon);
                        break;
                    case "DISINFECT":
                        holder.setImgResource(R.drawable.disinfect_icon);
                        break;
                    case "MEDICINE":
                        holder.setImgResource(R.drawable.medicine_icon);
                        break;
                    case "HOSPITAL":
                        holder.setImgResource(R.drawable.hospital_icon);
                        break;
                    default:
                        holder.setImgResource(R.drawable.medical_icon);
                }
                allInstances.add(holder);
            }
        }
        return allInstances;
    }

    public static List<OrderDataHolder> refreshAllInstances() {
        allInstances = new ArrayList<>();
        QueryResult<OrderDetails> queryResult = CloudantOrderUtils.getAllData();
        for (OrderDetails orderDetails : queryResult.getDocs()) {
            OrderDataHolder holder = new OrderDataHolder();
            holder.set_id(orderDetails.get_id());
            holder.setOrder_status(orderDetails.getOrder_status());
            holder.setService_id(orderDetails.getService_id());
            holder.setService_type(orderDetails.getService_type());
            holder.setUser_id(orderDetails.getUser_id());
            holder.setPrimaryQuantity(orderDetails.getPrimary_quantity());
            holder.setScheduledAppointment(orderDetails.getScheduled_appointment());
            switch (orderDetails.getService_type().toUpperCase()) {
                case "AMBULANCE":
                    holder.setImgResource(R.drawable.ambulance_icon);
                    break;
                case "DOCTOR":
                    holder.setImgResource(R.drawable.doctor_icon);
                    break;
                case "PATHOLOGY":
                    holder.setImgResource(R.drawable.pathology_icon);
                    break;
                case "DISINFECT":
                    holder.setImgResource(R.drawable.disinfect_icon);
                    break;
                case "MEDICINE":
                    holder.setImgResource(R.drawable.medicine_icon);
                    break;
                case "HOSPITAL":
                    holder.setImgResource(R.drawable.hospital_icon);
                    break;
                default:
                    holder.setImgResource(R.drawable.medical_icon);
            }
            allInstances.add(holder);
        }
        return allInstances;
    }

    public static Map<String, List<OrderDataHolder>> getAllUserSpecificDetails() {
        if (userSpecificOrderDetails == null) {
            allInstances = new ArrayList<>();
            userSpecificOrderDetails = new HashMap<>();
            QueryResult<OrderDetails> queryResult = CloudantOrderUtils.getAllData();
            for (OrderDetails orderDetails : queryResult.getDocs()) {
                String user_id = orderDetails.getUser_id();
                OrderDataHolder holder = new OrderDataHolder();
                holder.set_id(orderDetails.get_id());
                holder.setOrder_status(orderDetails.getOrder_status());
                holder.setService_id(orderDetails.getService_id());
                Log.d("Service ID", orderDetails.getService_id());
                holder.setService_type(orderDetails.getService_type());
                holder.setUser_id(orderDetails.getUser_id());
                holder.setPrimaryQuantity(orderDetails.getPrimary_quantity());
                holder.setScheduledAppointment(orderDetails.getScheduled_appointment());
                switch (orderDetails.getService_type().toUpperCase()) {
                    case "AMBULANCE":
                        holder.setImgResource(R.drawable.ambulance_icon);
                        break;
                    case "DOCTOR":
                        holder.setImgResource(R.drawable.doctor_icon);
                        break;
                    case "PATHOLOGY":
                        holder.setImgResource(R.drawable.pathology_icon);
                        break;
                    case "DISINFECT":
                        holder.setImgResource(R.drawable.disinfect_icon);
                        break;
                    case "MEDICINE":
                        holder.setImgResource(R.drawable.medicine_icon);
                        break;
                    case "HOSPITAL":
                        holder.setImgResource(R.drawable.hospital_icon);
                        break;
                    default:
                        holder.setImgResource(R.drawable.medical_icon);
                }
                allInstances.add(holder);

                // Grouping by service category
                if (userSpecificOrderDetails.containsKey(user_id)) {
                    List<OrderDataHolder> orderDataHolder = userSpecificOrderDetails.get(user_id);
                    orderDataHolder.add(holder);
                    userSpecificOrderDetails.replace(user_id, orderDataHolder);
                } else {
                    List<OrderDataHolder> orderDataHolder = new ArrayList<>();
                    orderDataHolder.add(holder);
                    userSpecificOrderDetails.put(user_id, orderDataHolder);
                }
            }
        }
        return userSpecificOrderDetails;
    }

    public static Map<String, List<OrderDataHolder>> refreshAllUserSpecificDetails() {
        allInstances = new ArrayList<>();
        userSpecificOrderDetails = new HashMap<>();
        QueryResult<OrderDetails> queryResult = CloudantOrderUtils.getAllData();
        for (OrderDetails orderDetails : queryResult.getDocs()) {
            String user_id = orderDetails.getUser_id();
            OrderDataHolder holder = new OrderDataHolder();
            holder.set_id(orderDetails.get_id());
            holder.setOrder_status(orderDetails.getOrder_status());
            holder.setService_id(orderDetails.getService_id());
            holder.setService_type(orderDetails.getService_type());
            holder.setUser_id(orderDetails.getUser_id());
            holder.setPrimaryQuantity(orderDetails.getPrimary_quantity());
            holder.setScheduledAppointment(orderDetails.getScheduled_appointment());
            switch (orderDetails.getService_type().toUpperCase()) {
                case "AMBULANCE":
                    holder.setImgResource(R.drawable.ambulance_icon);
                    break;
                case "DOCTOR":
                    holder.setImgResource(R.drawable.doctor_icon);
                    break;
                case "PATHOLOGY":
                    holder.setImgResource(R.drawable.pathology_icon);
                    break;
                case "DISINFECT":
                    holder.setImgResource(R.drawable.disinfect_icon);
                    break;
                case "MEDICINE":
                    holder.setImgResource(R.drawable.medicine_icon);
                    break;
                case "HOSPITAL":
                    holder.setImgResource(R.drawable.hospital_icon);
                    break;
                default:
                    holder.setImgResource(R.drawable.medical_icon);
            }
            allInstances.add(holder);

            // Grouping by service category
            if (userSpecificOrderDetails.containsKey(user_id)) {
                List<OrderDataHolder> orderDataHolder = userSpecificOrderDetails.get(user_id);
                orderDataHolder.add(holder);
                userSpecificOrderDetails.replace(user_id, orderDataHolder);
            } else {
                List<OrderDataHolder> orderDataHolder = new ArrayList<>();
                orderDataHolder.add(holder);
                userSpecificOrderDetails.put(user_id, orderDataHolder);
            }
        }
        return userSpecificOrderDetails;
    }

    public static Map<String, List<OrderDataHolder>> getAllServiceSpecificDetails() {
        if (serviceSpecificOrderDetails == null) {
            allInstances = new ArrayList<>();
            serviceSpecificOrderDetails = new HashMap<>();
            QueryResult<OrderDetails> queryResult = CloudantOrderUtils.getAllData();
            for (OrderDetails orderDetails : queryResult.getDocs()) {
                String service_id = orderDetails.getService_id();
                OrderDataHolder holder = new OrderDataHolder();
                holder.set_id(orderDetails.get_id());
                holder.setOrder_status(orderDetails.getOrder_status());
                holder.setService_id(orderDetails.getService_id());
                holder.setService_type(orderDetails.getService_type());
                holder.setUser_id(orderDetails.getUser_id());
                holder.setPrimaryQuantity(orderDetails.getPrimary_quantity());
                holder.setScheduledAppointment(orderDetails.getScheduled_appointment());
                switch (orderDetails.getService_type().toUpperCase()) {
                    case "AMBULANCE":
                        holder.setImgResource(R.drawable.ambulance_icon);
                        break;
                    case "DOCTOR":
                        holder.setImgResource(R.drawable.doctor_icon);
                        break;
                    case "PATHOLOGY":
                        holder.setImgResource(R.drawable.pathology_icon);
                        break;
                    case "DISINFECT":
                        holder.setImgResource(R.drawable.disinfect_icon);
                        break;
                    case "MEDICINE":
                        holder.setImgResource(R.drawable.medicine_icon);
                        break;
                    case "HOSPITAL":
                        holder.setImgResource(R.drawable.hospital_icon);
                        break;
                    default:
                        holder.setImgResource(R.drawable.medical_icon);
                }
                allInstances.add(holder);

                // Grouping by service category
                if (serviceSpecificOrderDetails.containsKey(service_id)) {
                    List<OrderDataHolder> orderDataHolder = serviceSpecificOrderDetails.get(service_id);
                    orderDataHolder.add(holder);
                    serviceSpecificOrderDetails.replace(service_id, orderDataHolder);
                } else {
                    List<OrderDataHolder> orderDataHolder = new ArrayList<>();
                    orderDataHolder.add(holder);
                    serviceSpecificOrderDetails.put(service_id, orderDataHolder);
                }
            }
        }
        return serviceSpecificOrderDetails;
    }

    public static Map<String, List<OrderDataHolder>> refreshAllServiceSpecificDetails() {
        allInstances = new ArrayList<>();
        serviceSpecificOrderDetails = new HashMap<>();
        QueryResult<OrderDetails> queryResult = CloudantOrderUtils.getAllData();
        for (OrderDetails orderDetails : queryResult.getDocs()) {
            String service_id = orderDetails.getService_id();
            OrderDataHolder holder = new OrderDataHolder();
            holder.set_id(orderDetails.get_id());
            holder.setOrder_status(orderDetails.getOrder_status());
            holder.setService_id(orderDetails.getService_id());
            holder.setService_type(orderDetails.getService_type());
            holder.setUser_id(orderDetails.getUser_id());
            holder.setPrimaryQuantity(orderDetails.getPrimary_quantity());
            holder.setScheduledAppointment(orderDetails.getScheduled_appointment());
            switch (orderDetails.getService_type().toUpperCase()) {
                case "AMBULANCE":
                    holder.setImgResource(R.drawable.ambulance_icon);
                    break;
                case "DOCTOR":
                    holder.setImgResource(R.drawable.doctor_icon);
                    break;
                case "PATHOLOGY":
                    holder.setImgResource(R.drawable.pathology_icon);
                    break;
                case "DISINFECT":
                    holder.setImgResource(R.drawable.disinfect_icon);
                    break;
                case "MEDICINE":
                    holder.setImgResource(R.drawable.medicine_icon);
                    break;
                case "HOSPITAL":
                    holder.setImgResource(R.drawable.hospital_icon);
                    break;
                default:
                    holder.setImgResource(R.drawable.medical_icon);
            }
            allInstances.add(holder);

            // Grouping by service category
            if (serviceSpecificOrderDetails.containsKey(service_id)) {
                List<OrderDataHolder> orderDataHolder = serviceSpecificOrderDetails.get(service_id);
                orderDataHolder.add(holder);
                serviceSpecificOrderDetails.replace(service_id, orderDataHolder);
            } else {
                List<OrderDataHolder> orderDataHolder = new ArrayList<>();
                orderDataHolder.add(holder);
                serviceSpecificOrderDetails.put(service_id, orderDataHolder);
            }
        }
        return serviceSpecificOrderDetails;
    }
}
