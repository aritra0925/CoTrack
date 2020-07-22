package com.cotrack.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cotrack.helpers.Space;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cotrack.R;
import com.cotrack.adaptors.MessageListAdapter;
import com.cotrack.adaptors.ProviderAdapter;
import com.cotrack.listeners.EndlessScrollListener;
import com.cotrack.models.Message;
import com.cotrack.models.ProviderDetails;

import java.util.ArrayList;
import java.util.List;

public class ServiceSpecificFragment extends Fragment {

    ProviderAdapter providerAdapter;
    private static ServiceSpecificFragment instance = null;
    View view;
    List<ProviderDetails> providerDetails;
    RecyclerView recyclerViewProducts;

    public ServiceSpecificFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServiceFragment.
     */
    public static ServiceSpecificFragment newInstance() {
        instance = new ServiceSpecificFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_specific_service, container, false);
        //Bind RecyclerView from layout to recyclerViewProducts object
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);

        //Create new ProductsAdapter
        providerAdapter = new ProviderAdapter(view.getContext());
        //Create new GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(),
                2,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager
        recyclerViewProducts.setLayoutManager(gridLayoutManager);

        //Crete new EndlessScrollListener fo endless recyclerview loading
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!providerAdapter.loading) {
                    feedData();
                }
            }

        };
        //to give loading item full single row
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (providerAdapter.getItemViewType(position)) {
                    case ProviderAdapter.PRODUCT_ITEM:
                        return 1;
                    case ProviderAdapter.LOADING_ITEM:
                        return 2; //number of columns of the grid
                    default:
                        return -1;
                }
            }
        });
        //add on on Scroll listener
        recyclerViewProducts.addOnScrollListener(endlessScrollListener);
        //add space between cards
        recyclerViewProducts.addItemDecoration(new Space(2, 20, true, 0));
        //Finally set the adapter
        recyclerViewProducts.setAdapter(providerAdapter);
        //load first page of recyclerview
        endlessScrollListener.onLoadMore(0, 0);
        return view;
    }
    
    //Load Data from your server here
    // loading data from server will make it very large
    // that's why i created data locally
    private void feedData() {
        //show loading in recyclerview
        recyclerViewProducts.post(new Runnable() {
            @Override
            public void run() {
                providerAdapter.showLoading();
            }
        });

        if (providerDetails == null || providerDetails.isEmpty()) {
            final List<ProviderDetails> products = new ArrayList<>();
            int[] imageUrls = {R.drawable.hospital_icon, R.drawable.ambulance_icon, R.drawable.disinfect_icon, R.drawable.doctor_icon, R.drawable.doctor_icon, R.drawable.doctor_icon};
            String[] ProductName = {"Ruby Hospital", "Peerless", "Amri", "Beleghat Ide", "xyz", "ijk"};
            String[] ProductPrice = {"₹594", "₹5000", "₹200", "₹1999", "$300", "$500"};
            boolean[] isNew = {true, false, false, true, true, false};
            for (int i = 0; i < imageUrls.length; i++) {
                ProviderDetails product = new ProviderDetails();
                product.setImageResourceId(imageUrls[i]);
                product.setProvider_name(ProductName[i]);
                product.setNew(isNew[i]);
                products.add(product);
            }
            this.providerDetails = products;
            recyclerViewProducts.post(new Runnable() {
                @Override
                public void run() {
                    //hide loading
                    providerAdapter.hideLoading();
                    //add products to recyclerview
                    providerAdapter.addProducts(products);
                }
            });
        } else {
            recyclerViewProducts.post(new Runnable() {
                @Override
                public void run() {
                    providerAdapter.hideLoading();
                }
            });
        }
    }
}
