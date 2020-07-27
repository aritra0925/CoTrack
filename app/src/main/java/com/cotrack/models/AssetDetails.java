package com.cotrack.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssetDetails {

    @JsonProperty(value = "_id")
    public String _id;
    @JsonProperty(value = "asset_id")
    public String asset_id;
    @JsonProperty(value = "asset_type")
    public String asset_type;
    @JsonProperty(value = "asset_title")
    public String asset_title;
    @JsonProperty(value = "asset_description")
    public String asset_description;
    @JsonProperty(value = "asset_count_key")
    public String asset_count_key;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getAsset_title() {
        return asset_title;
    }

    public void setAsset_title(String asset_title) {
        this.asset_title = asset_title;
    }

    public String getAsset_description() {
        return asset_description;
    }

    public void setAsset_description(String asset_description) {
        this.asset_description = asset_description;
    }

    public String getAsset_count_key() {
        return asset_count_key;
    }

    public void setAsset_count_key(String asset_count_key) {
        this.asset_count_key = asset_count_key;
    }

    @Override
    public String toString() {
        return "Service{" +
                "_id='" + _id + '\'' +
                ", asset_id='" + asset_id + '\'' +
                ", asset_type='" + asset_type + '\'' +
                ", asset_title='" + asset_title + '\'' +
                ", asset_description='" + asset_description + '\'' +
                ", asset_count_key='" + asset_count_key + '\'' +
                '}';
    }
}
