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
import com.cotrack.models.ProviderDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RegisterServiceItemsAdapter extends RecyclerView.Adapter {
    List<ProviderDetails> mProducts;
    Context mContext;
    public static final int LOADING_ITEM = 0;
    public static final int PRODUCT_ITEM = 1;
    int LoadingItemPos;
    public boolean loading = false;

    public RegisterServiceItemsAdapter(Context mContext) {
        mProducts = new ArrayList<>();
        this.mContext = mContext;
    }
    //method to add products as soon as they fetched
    public void addProducts(List<ProviderDetails> products) {
        int lastPos = mProducts.size();
        this.mProducts.addAll(products);
        notifyItemRangeInserted(lastPos, mProducts.size());
    }


    @Override
    public int getItemViewType(int position) {
        ProviderDetails currentProduct = mProducts.get(position);
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
        if (viewType == LOADING_ITEM) {
            View row = inflater.inflate(R.layout.custom_row_registered_loading, parent, false);
            return new LoadingHolder(row);
        } else if (viewType == PRODUCT_ITEM) {
            View row = inflater.inflate(R.layout.custom_row__registered_service, parent, false);
            return new ProductHolder(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //get current product
        final ProviderDetails currentProduct = mProducts.get(position);
        if (holder instanceof ProductHolder) {
            ProductHolder productHolder = (ProductHolder) holder;
            //bind products information with view
            Picasso.with(mContext).load(currentProduct.getImageResourceId()).into(productHolder.imageViewProductThumb);
            productHolder.textViewProductName.setText(currentProduct.getProvider_name());
            productHolder.textViewProductPrice.setText(currentProduct.getProvider_address1());
            if (currentProduct.isNew())
                productHolder.textViewNew.setVisibility(View.VISIBLE);
            else
                productHolder.textViewNew.setVisibility(View.GONE);

            productHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // user selected product now you can show details of that product
                    Toast.makeText(mContext, "Selected "+currentProduct.getProvider_name(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    //Holds view of product with information
    private class ProductHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProductThumb;
        TextView textViewProductName, textViewProductPrice, textViewNew;


        public ProductHolder(View itemView) {
            super(itemView);
            imageViewProductThumb = itemView.findViewById(R.id.imageViewProductThumbService);
            textViewProductName = itemView.findViewById(R.id.textViewProductNameService);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPriceService);
            textViewNew = itemView.findViewById(R.id.textViewNewService);

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
        ProviderDetails product = new ProviderDetails();
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
}