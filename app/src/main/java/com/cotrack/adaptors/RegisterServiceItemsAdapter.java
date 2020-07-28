package com.cotrack.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.cotrack.R;
import com.cotrack.global.ServiceProviderDataHolder;
import com.cotrack.helpers.OnItemClick;
import com.cotrack.models.ProviderDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class RegisterServiceItemsAdapter extends RecyclerView.Adapter {
    List<ServiceProviderDataHolder> mProducts;
    Context mContext;
    public static final int LOADING_ITEM = 0;
    public static final int PRODUCT_ITEM = 1;
    int LoadingItemPos;
    public boolean loading = false;
    private OnItemClick itemClick;
    ServiceProviderDataHolder currentProduct;

    public OnItemClick getItemClick() {
        return itemClick;
    }

    public void setItemClick(OnItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public RegisterServiceItemsAdapter(Context mContext, List<ServiceProviderDataHolder> products) {
        this.mProducts = products;
        //mProducts = new ArrayList<>();
        this.mContext = mContext;
    }

    //method to add products as soon as they fetched
    public void addProducts(List<ServiceProviderDataHolder> products) {
        int lastPos = mProducts.size();
        this.mProducts.addAll(products);
        notifyItemRangeInserted(lastPos, mProducts.size());
    }


    @Override
    public int getItemViewType(int position) {
        currentProduct = mProducts.get(position);
        if (currentProduct.isLoading()) {
            return LOADING_ITEM;
        } else {
            return PRODUCT_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //Check which view has to be populated
        View row = inflater.inflate(R.layout.custom_row__registered_service, parent, false);
        return new ProductHolder(row);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //get current product
        currentProduct = mProducts.get(position);
        if (holder instanceof ProductHolder) {
            ProductHolder productHolder = (ProductHolder) holder;
            productHolder.position = position;
            //bind products information with view
            Picasso.with(mContext).load(currentProduct.getImageResource()).into(productHolder.imageViewProductThumb);
            String provider_name = currentProduct.getService_provider_name();
            if (provider_name == null) {
                provider_name = "To be deleted";
            }
            String serviceName = currentProduct.getType().toUpperCase();
            switch (serviceName.toUpperCase()) {
                case "AMBULANCE":
                    productHolder.imageViewProductThumb.setImageResource(R.drawable.ambulance_icon);
                    break;
                case "DOCTOR":
                    productHolder.imageViewProductThumb.setImageResource(R.drawable.doctor_icon);
                    break;
                case "PATHOLOGY CENTERS":
                    productHolder.imageViewProductThumb.setImageResource(R.drawable.pathology_icon);
                    break;
                case "DISINFECTANT":
                    productHolder.imageViewProductThumb.setImageResource(R.drawable.disinfect_icon);
                    break;
                case "MEDICINE":
                    productHolder.imageViewProductThumb.setImageResource(R.drawable.medicine_icon);
                    break;
                case "HOSPITAL":
                    productHolder.imageViewProductThumb.setImageResource(R.drawable.hospital_icon);
                    break;
                default:
                    productHolder.imageViewProductThumb.setImageResource(R.drawable.medical_icon);
            }
            productHolder.textViewProductName.setText(currentProduct.getService_provider_name());
            productHolder.textViewProductPrice.setText(currentProduct.getAddress_line() + "\n" + currentProduct.getCity() + "\n" + currentProduct.getState() + "\n" + currentProduct.getPostal_code());
            if (currentProduct.isNew())
                productHolder.textViewNew.setVisibility(View.VISIBLE);
            else
                productHolder.textViewNew.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (mProducts == null || !mProducts.isEmpty()) {
            return mProducts.size();
        } else {
            return 0;
        }
    }

    //Holds view of product with information
    private class ProductHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        ImageView imageViewProductThumb;
        TextView textViewProductName, textViewProductPrice, textViewNew;
        int position;


        public ProductHolder(View itemView) {
            super(itemView);
            imageViewProductThumb = itemView.findViewById(R.id.imageViewProductThumbService);
            textViewProductName = itemView.findViewById(R.id.textViewProductNameService);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPriceService);
            textViewNew = itemView.findViewById(R.id.textViewNewService);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClick != null) {
                itemClick.onItemClicked(position);
            }
        }
    }

    //holds view of loading item
    private class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }

    //method to show loading
    public void showLoading() {
        ServiceProviderDataHolder product = new ServiceProviderDataHolder();
        product.setLoading(true);
        mProducts.add(product);
        LoadingItemPos = mProducts.size();
        notifyItemInserted(mProducts.size());
        loading = true;
    }

    //method to hide loading
    public void hideLoading() {
        if (LoadingItemPos <= mProducts.size()) {
            mProducts.remove(LoadingItemPos - 1);
            notifyItemRemoved(LoadingItemPos);
            loading = false;
        }
    }

    //method to hide loading
    public void updateListLoading(List<ServiceProviderDataHolder> serviceProviderDataHolders) {
        mProducts = serviceProviderDataHolders;
        notifyDataSetChanged();//    notifyItemRemoved(LoadingItemPos);
        loading = false;
    }
}