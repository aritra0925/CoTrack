package com.cotrack.global;

import com.cloudant.client.api.query.QueryResult;
import com.cotrack.R;
import com.cotrack.models.ServiceDetails;
import com.cotrack.utils.CloudantServiceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceProviderDataHolder {

    private String _id;
    private String type;
    private String service_id;
    private String service_name;
    private String service_description;
    private String asset_id;
    private String sr_id;
    private String city;
    private String address_line;
    private String state;
    private String postal_code;
    private String contact;
    private boolean isNew;
    private boolean isLoading;
    private int imageResource;
    private static Map<String, List<ServiceProviderDataHolder>> serviceSpecificDetails;

    private static List<ServiceProviderDataHolder> allInstances;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_description() {
        return service_description;
    }

    public void setService_description(String service_description) {
        this.service_description = service_description;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
    }

    public String getSr_id() {
        return sr_id;
    }

    public void setSr_id(String sr_id) {
        this.sr_id = sr_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress_line() {
        return address_line;
    }

    public void setAddress_line(String address_line) {
        this.address_line = address_line;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public static List<ServiceProviderDataHolder> getAllInstances() {
        if (allInstances == null) {
            allInstances = new ArrayList<>();
            QueryResult<ServiceDetails> queryResult = CloudantServiceUtils.getAllData();
            for (ServiceDetails serviceDetails : queryResult.getDocs()) {
                ServiceProviderDataHolder holder = new ServiceProviderDataHolder();
                holder.set_id(serviceDetails.get_id());
                holder.setAddress_line(serviceDetails.getAddress_line());
                holder.setAsset_id(serviceDetails.getAsset_id());
                holder.setCity(serviceDetails.getCity());
                holder.setContact(serviceDetails.getContact());
                holder.setPostal_code(serviceDetails.getPostal_code());
                holder.setService_description(holder.getService_description());
                holder.setService_id(serviceDetails.getService_id());
                holder.setService_name(serviceDetails.getService_name());
                holder.setSr_id(serviceDetails.getSr_id());
                holder.setState(serviceDetails.getType());
                holder.setType(serviceDetails.getType());
                switch (serviceDetails.getService_name().toUpperCase()) {
                    case "AMBULANCE":
                        holder.setImageResource(R.drawable.ambulance_icon);
                        break;
                    case "DOCTOR":
                        holder.setImageResource(R.drawable.doctor_icon);
                        break;
                    case "PATHOLOGY":
                        holder.setImageResource(R.drawable.pathology_icon);
                        break;
                    case "DISINFECT":
                        holder.setImageResource(R.drawable.disinfect_icon);
                        break;
                    case "MEDICINE":
                        holder.setImageResource(R.drawable.medicine_icon);
                        break;
                    case "HOSPITAL":
                        holder.setImageResource(R.drawable.hospital_icon);
                        break;
                    default:
                        holder.setImageResource(R.drawable.medical_icon);
                }
                allInstances.add(holder);
                holder.setNew(Boolean.parseBoolean(serviceDetails.isNew()));
                holder.setLoading(Boolean.parseBoolean(serviceDetails.isLoading()));
            }
        }
        return allInstances;
    }

    public static Map<String, List<ServiceProviderDataHolder>> getAllServiceSpecificDetails() {
        if(serviceSpecificDetails == null) {
            allInstances = new ArrayList<>();
            serviceSpecificDetails = new HashMap<>();
            QueryResult<ServiceDetails> queryResult = CloudantServiceUtils.getAllData();
            for (ServiceDetails serviceDetails : queryResult.getDocs()) {
                String asset_id = serviceDetails.getAsset_id();
                ServiceProviderDataHolder holder = new ServiceProviderDataHolder();
                holder.set_id(serviceDetails.get_id());
                holder.setAddress_line(serviceDetails.getAddress_line());
                holder.setAsset_id(asset_id);
                holder.setCity(serviceDetails.getCity());
                holder.setContact(serviceDetails.getContact());
                holder.setPostal_code(serviceDetails.getPostal_code());
                holder.setService_description(holder.getService_description());
                holder.setService_id(serviceDetails.getService_id());
                holder.setService_name(serviceDetails.getService_name());
                holder.setSr_id(serviceDetails.getSr_id());
                holder.setState(serviceDetails.getType());
                holder.setType(serviceDetails.getType());
                holder.setNew(Boolean.parseBoolean(serviceDetails.isNew()));
                holder.setLoading(Boolean.parseBoolean(serviceDetails.isLoading()));
                switch (serviceDetails.getService_name().toUpperCase()) {
                    case "AMBULANCE":
                        holder.setImageResource(R.drawable.ambulance_icon);
                        break;
                    case "DOCTOR":
                        holder.setImageResource(R.drawable.doctor_icon);
                        break;
                    case "PATHOLOGY":
                        holder.setImageResource(R.drawable.pathology_icon);
                        break;
                    case "DISINFECT":
                        holder.setImageResource(R.drawable.disinfect_icon);
                        break;
                    case "MEDICINE":
                        holder.setImageResource(R.drawable.medicine_icon);
                        break;
                    case "HOSPITAL":
                        holder.setImageResource(R.drawable.hospital_icon);
                        break;
                    default:
                        holder.setImageResource(R.drawable.medical_icon);
                }
                allInstances.add(holder);

               // Grouping by service category
                if(serviceSpecificDetails.containsKey(asset_id)){
                    List<ServiceProviderDataHolder> serviceProviderDataHolders = serviceSpecificDetails.get(asset_id);
                    serviceProviderDataHolders.add(holder);
                    serviceSpecificDetails.replace(asset_id, serviceProviderDataHolders);
                } else {
                    List<ServiceProviderDataHolder> serviceProviderDataHolders = new ArrayList<>();
                    serviceProviderDataHolders.add(holder);
                    serviceSpecificDetails.put(asset_id, serviceProviderDataHolders);
                }
            }
        }
        return serviceSpecificDetails;
    }

    public static Map<String, List<ServiceProviderDataHolder>> refreshAllServiceSpecificDetails() {
        allInstances = new ArrayList<>();
        serviceSpecificDetails = new HashMap<>();
        QueryResult<ServiceDetails> queryResult = CloudantServiceUtils.getAllData();
        for (ServiceDetails serviceDetails : queryResult.getDocs()) {
            String asset_id = serviceDetails.getAsset_id();
            ServiceProviderDataHolder holder = new ServiceProviderDataHolder();
            holder.set_id(serviceDetails.get_id());
            holder.setAddress_line(serviceDetails.getAddress_line());
            holder.setAsset_id(asset_id);
            holder.setCity(serviceDetails.getCity());
            holder.setContact(serviceDetails.getContact());
            holder.setPostal_code(serviceDetails.getPostal_code());
            holder.setService_description(holder.getService_description());
            holder.setService_id(serviceDetails.getService_id());
            holder.setService_name(serviceDetails.getService_name());
            holder.setSr_id(serviceDetails.getSr_id());
            holder.setState(serviceDetails.getType());
            holder.setType(serviceDetails.getType());
            holder.setNew(Boolean.parseBoolean(serviceDetails.isNew()));
            holder.setLoading(Boolean.parseBoolean(serviceDetails.isLoading()));
            switch (serviceDetails.getService_name().toUpperCase()) {
                case "AMBULANCE":
                    holder.setImageResource(R.drawable.ambulance_icon);
                    break;
                case "DOCTOR":
                    holder.setImageResource(R.drawable.doctor_icon);
                    break;
                case "PATHOLOGY":
                    holder.setImageResource(R.drawable.pathology_icon);
                    break;
                case "DISINFECT":
                    holder.setImageResource(R.drawable.disinfect_icon);
                    break;
                case "MEDICINE":
                    holder.setImageResource(R.drawable.medicine_icon);
                    break;
                case "HOSPITAL":
                    holder.setImageResource(R.drawable.hospital_icon);
                    break;
                default:
                    holder.setImageResource(R.drawable.medical_icon);
            }
            allInstances.add(holder);

            // Grouping by service category
            if(serviceSpecificDetails.containsKey(asset_id)){
                List<ServiceProviderDataHolder> serviceProviderDataHolders = serviceSpecificDetails.get(asset_id);
                serviceProviderDataHolders.add(holder);
                serviceSpecificDetails.replace(asset_id, serviceProviderDataHolders);
            } else {
                List<ServiceProviderDataHolder> serviceProviderDataHolders = new ArrayList<>();
                serviceProviderDataHolders.add(holder);
                serviceSpecificDetails.put(asset_id, serviceProviderDataHolders);
            }
        }
        return serviceSpecificDetails;
    }

    public static List<ServiceProviderDataHolder> getSpecificServiceDetails(String asset_id){
        if(serviceSpecificDetails == null){
            getAllServiceSpecificDetails();
        }
        return serviceSpecificDetails.get(asset_id);
    }

    public static List<ServiceProviderDataHolder> refreshAllInstances() {
        allInstances = new ArrayList<>();
        QueryResult<ServiceDetails> queryResult = CloudantServiceUtils.getAllData();
        for (ServiceDetails serviceDetails : queryResult.getDocs()) {
            ServiceProviderDataHolder holder = new ServiceProviderDataHolder();
            holder.set_id(serviceDetails.get_id());
            holder.setAddress_line(serviceDetails.getAddress_line());
            holder.setAsset_id(serviceDetails.getAsset_id());
            holder.setCity(serviceDetails.getCity());
            holder.setContact(serviceDetails.getContact());
            holder.setPostal_code(serviceDetails.getPostal_code());
            holder.setService_description(holder.getService_description());
            holder.setService_id(serviceDetails.getService_id());
            holder.setService_name(serviceDetails.getService_name());
            holder.setSr_id(serviceDetails.getSr_id());
            holder.setState(serviceDetails.getType());
            holder.setType(serviceDetails.getType());
            holder.setNew(Boolean.parseBoolean(serviceDetails.isNew()));
            holder.setLoading(Boolean.parseBoolean(serviceDetails.isLoading()));
            switch (serviceDetails.getService_name().toUpperCase()) {
                case "AMBULANCE":
                    holder.setImageResource(R.drawable.ambulance_icon);
                    break;
                case "DOCTOR":
                    holder.setImageResource(R.drawable.doctor_icon);
                    break;
                case "PATHOLOGY":
                    holder.setImageResource(R.drawable.pathology_icon);
                    break;
                case "DISINFECT":
                    holder.setImageResource(R.drawable.disinfect_icon);
                    break;
                case "MEDICINE":
                    holder.setImageResource(R.drawable.medicine_icon);
                    break;
                case "HOSPITAL":
                    holder.setImageResource(R.drawable.hospital_icon);
                    break;
                default:
                    holder.setImageResource(R.drawable.medical_icon);
            }
            allInstances.add(holder);
        }
        return allInstances;
    }

}
