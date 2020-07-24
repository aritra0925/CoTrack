package com.cotrack.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Random;

public class Orders {
    @JsonProperty(value = "_id")
    private String _id;

    @JsonProperty(value = "_rev")
    private String _rev = null;

    @JsonProperty(value = "order_id")
    private String order_id = "O" + Integer.toString(new Random().nextInt(9000));

    @JsonProperty(value = "provider_id")
    private String provider_id;

    @JsonProperty(value = "asset_id")
    private String asset_id;

    @JsonProperty(value = "service_id")
    private String service_id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public Orders(String _id, String _rev, String provider_id, String asset_id, String service_id) {
        this._id = _id;
        this._rev = _rev;
        this.provider_id = provider_id;
        this.asset_id = asset_id;
        this.service_id = service_id;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "_id='" + _id + '\'' +
                ", _rev='" + _rev + '\'' +
                ", order_id='" + order_id + '\'' +
                ", provider_id='" + provider_id + '\'' +
                ", asset_id='" + asset_id + '\'' +
                ", service_id='" + service_id + '\'' +
                '}';
    }
}
