package com.cotrack.global;

import com.cloudant.client.api.query.QueryResult;
import com.cloudant.client.api.query.Selector;
import com.cotrack.models.AssetDetails;
import com.cotrack.utils.CloudantAssetUtils;
import com.cotrack.utils.CloudantServiceUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static com.cloudant.client.api.query.Expression.eq;

public class AssetDataHolder implements Serializable {

    private String asset_id;
    private String asset_type;
    private String asset_title;
    private String asset_description;
    private int asset_available_services_count;
    private String asset_count_key;


    private static List<AssetDataHolder> allInstances;

    public static List<AssetDataHolder> getAllInstances() {
        if (allInstances == null) {
            allInstances = new ArrayList<>();
            QueryResult<AssetDetails> queryResult = CloudantAssetUtils.getAllData();
            for (AssetDetails assetDetails : queryResult.getDocs()) {
                AssetDataHolder holder = new AssetDataHolder();
                holder.setAsset_id(assetDetails.asset_id);
                holder.setAsset_title(assetDetails.asset_title);
                holder.setAsset_description(assetDetails.asset_description);
                holder.setAsset_type(assetDetails.asset_type);
                holder.setAsset_count_key(assetDetails.getAsset_count_key());
                holder.setAsset_available_services_count(CloudantServiceUtils.queryData(eq("asset_id", assetDetails.get_id())).getDocs().size());
                allInstances.add(holder);
            }
        }
        return allInstances;
    }

    public static List<AssetDataHolder> refreshAllInstances() {
        allInstances = new ArrayList<>();
        QueryResult<AssetDetails> queryResult = CloudantAssetUtils.getAllData();
        System.out.println("Result size:" + queryResult.getDocs().size());
        for (AssetDetails assetDetails : queryResult.getDocs()) {
            System.out.println("Retreived id:" + assetDetails._id);
            AssetDataHolder holder = new AssetDataHolder();
            holder.setAsset_id(assetDetails.asset_id);
            holder.setAsset_title(assetDetails.asset_title);
            holder.setAsset_description(assetDetails.asset_description);
            holder.setAsset_count_key(assetDetails.getAsset_count_key());
            holder.setAsset_type(assetDetails.asset_type);
            allInstances.add(holder);
        }
        return allInstances;
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

    public int getAsset_available_services_count() {
        return asset_available_services_count;
    }

    public void setAsset_available_services_count(int asset_available_services_count) {
        this.asset_available_services_count = asset_available_services_count;
    }

    public String getAsset_count_key() {
        return asset_count_key;
    }

    public void setAsset_count_key(String asset_count_key) {
        this.asset_count_key = asset_count_key;
    }
}
