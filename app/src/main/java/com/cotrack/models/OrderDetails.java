package com.cotrack.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderDetails {
    @JsonProperty(value = "_id")
    public String _id;
    @JsonProperty(value = "_rev")
    public String _rev = null;
    @JsonProperty(value = "service_id")
    public String service_id;
    @JsonProperty(value = "order_status")
    public String order_status;
    @JsonProperty(value = "service_type")
    public String service_type;
    @JsonProperty(value = "user_id")
    public String user_id;
    @JsonProperty(value = "primary_quantity")
    public String primary_quantity;
    @JsonProperty(value = "scheduled_appointment")
    public String scheduled_appointment;

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

    public String getPrimary_quantity() {
        return primary_quantity;
    }

    public void setPrimary_quantity(String primary_quantity) {
        this.primary_quantity = primary_quantity;
    }

    public String getScheduled_appointment() {
        return scheduled_appointment;
    }

    public void setScheduled_appointment(String scheduled_appointment) {
        this.scheduled_appointment = scheduled_appointment;
    }
}
