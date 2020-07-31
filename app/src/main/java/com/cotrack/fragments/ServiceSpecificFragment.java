package com.cotrack.fragments;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cotrack.R;
import com.cotrack.adaptors.ProviderAdapter;
import com.cotrack.global.ServiceProviderDataHolder;
import com.cotrack.global.UserDataHolder;
import com.cotrack.helpers.OnItemClick;
import com.cotrack.helpers.Space;
import com.cotrack.listeners.EndlessScrollListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("ALL")
public class ServiceSpecificFragment extends Fragment implements OnItemClick {

    String asset_id;
    ProviderAdapter providerAdapter;
    private static ServiceSpecificFragment instance = null;
    View view;
    List<ServiceProviderDataHolder> providerDetails;
    RecyclerView recyclerViewProducts;
    ProgressBar progressBar;
    FrameLayout frameLayout;
    final String COOKIE_FILE_NAME = "Cookie.properties";
    final String USER_COOKIE = "UserCookie";
    final String USER_TYPE_COOKIE = "UserTypeCookie";

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

    public void enableBackButton(ImageView backButton){
        backButton.setVisibility(View.VISIBLE);
        backButton.setColorFilter(getResources().getColor(R.color.accent));
        backButton.setEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        asset_id = getArguments().getString("asset_id");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_specific_service, container, false);
        ImageView backButton = getActivity().findViewById(R.id.backButton);
        enableBackButton(backButton);
        //Bind RecyclerView from layout to recyclerViewProducts object
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        frameLayout = view.findViewById(R.id.specificServiceFrameLayout);
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
        try {
            new DataLoadTask().execute("").get();
        } catch (ExecutionException e) {
            Log.e("Fatal Error", "Exception while retreiving data", e);
        } catch (InterruptedException e) {
            Log.e("Fatal Error", "Exception while retreiving data", e);
        }
        //Create new ProductsAdapter
        providerDetails = ServiceProviderDataHolder.getSpecificServiceDetails(asset_id);
        if( providerDetails == null ||  providerDetails.isEmpty()){
            Log.d("No Data For User", "Null or empty");
            view = inflater.inflate(R.layout.layout_missing_data, container, false);
            return view;
        }  else {
            Log.d("Data For User", "Data present: " + getProperties().getProperty(USER_COOKIE) + " Data: " + providerDetails.get(0).getType());
        }
        providerAdapter = new ProviderAdapter(view.getContext(), providerDetails);

        //Finally set the adapter
        recyclerViewProducts.setAdapter(providerAdapter);
        providerAdapter.setItemClick(this);
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
            this.providerDetails = ServiceProviderDataHolder.getSpecificServiceDetails(asset_id);
            recyclerViewProducts.post(new Runnable() {
                @Override
                public void run() {
                    //hide loading
                    providerAdapter.hideLoading();
                    //add products to recyclerview
                    providerAdapter.addProducts(ServiceProviderDataHolder.getSpecificServiceDetails(asset_id));
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

    @SuppressWarnings("deprecation")
    class DataLoadTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressBar(view.getContext());
            progressBar.setTooltipText("Please wait. Fetching data...");
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            frameLayout.addView(progressBar, params);
        }

        /**
         * @param objects
         * @deprecated
         */
        @Override
        public Boolean doInBackground(String... objects) {
            providerDetails = ServiceProviderDataHolder.getSpecificServiceDetails(asset_id);
            return true;
        }

        public void onPostExecute(Boolean objects) {
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void onItemClicked(int position) {
        providerAdapter.updateListLoading(providerDetails);
        String service_id = providerDetails.get(position).get_id();
        for (int count = 0; count < providerDetails.size(); count++) {
            Log.d("Position List", "Position: " + count);
            Log.d("Position List", "Service _id: " + service_id);
        }
        Log.d("Service Id CLick", "CLicked on service id: " + service_id + "\n Clicked onposition: " + position);
        ServiceDetailsFragment serviceDetailsFragment = ServiceDetailsFragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putString("service_id", service_id);
        serviceDetailsFragment.setArguments(args);
        fragmentTransaction.replace(R.id.container, serviceDetailsFragment).addToBackStack(null);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
    }

    public Properties getProperties(){
        Properties props = new Properties();
        try {
            FileInputStream fin= view.getContext().openFileInput(COOKIE_FILE_NAME);
            props.load(fin);
        } catch (FileNotFoundException e) {
            Log.e("File Error", "Error reading properties file", e);
        } catch (IOException e) {
            Log.e("File Error", "Error reading properties file", e);
        }
        return props;
    }
}
