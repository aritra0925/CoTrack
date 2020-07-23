package com.cotrack.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cotrack.R;
import com.cotrack.adaptors.RegisterServiceItemsAdapter;
import com.cotrack.global.ServiceProviderDataHolder;
import com.cotrack.helpers.OnItemClick;
import com.cotrack.helpers.Space;
import com.cotrack.listeners.EndlessScrollListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class RegisteredServicesFragment extends Fragment implements OnItemClick {

    final String COOKIE_FILE_NAME = "Cookie.properties";
    final String USER_COOKIE = "UserCookie";
    final String USER_TYPE_COOKIE = "UserTypeCookie";
    private static RegisteredServicesFragment instance = null;
    RegisterServiceItemsAdapter registerServiceItemsAdapter;
    List<ServiceProviderDataHolder> providerDetails;
    RecyclerView recyclerViewProducts;
    ProgressBar progressBar;
    FrameLayout frameLayout;
    String user_id;

    View view;

    public RegisteredServicesFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServiceFragment.
     */
    public static RegisteredServicesFragment newInstance() {
        instance = new RegisteredServicesFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_registered_service, container, false);
        //Bind RecyclerView from layout to recyclerViewProducts object
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProductsService);
        frameLayout = (FrameLayout) view.findViewById(R.id.registeredServiceFrameLayout);
        if(fileExists(view.getContext(), COOKIE_FILE_NAME)){
            Properties properties = getProperties(view.getContext());
            if(properties.containsKey(USER_COOKIE) && (properties.getProperty(USER_COOKIE) != null) && !properties.getProperty(USER_COOKIE).isEmpty()){
                user_id = properties.getProperty(USER_COOKIE);
            }
        } else {
            System.out.println("Properties file is not present as of now");
        }

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
                if (!registerServiceItemsAdapter.loading) {
                    //feedData();
                }
            }

        };
        //to give loading item full single row
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (registerServiceItemsAdapter.getItemViewType(position)) {
                    case RegisterServiceItemsAdapter.PRODUCT_ITEM:
                        return 1;
                    case RegisterServiceItemsAdapter.LOADING_ITEM:
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
            Log.e("Fatal Error" , "Exception while retreiving data" ,e);
        } catch (InterruptedException e) {
            Log.e("Fatal Error" , "Exception while retreiving data" ,e);
        }
        //Create new ProductsAdapter
        registerServiceItemsAdapter = new RegisterServiceItemsAdapter(view.getContext(), ServiceProviderDataHolder.getUserSpecificServiceDetails(user_id));
        //Finally set the adapter
        recyclerViewProducts.setAdapter(registerServiceItemsAdapter);
        registerServiceItemsAdapter.setItemClick(this);
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
                registerServiceItemsAdapter.showLoading();
            }
        });

        if (providerDetails == null || providerDetails.isEmpty()) {
            this.providerDetails = ServiceProviderDataHolder.getUserSpecificServiceDetails(user_id);
            recyclerViewProducts.post(new Runnable() {
                @Override
                public void run() {
                    //hide loading
                    registerServiceItemsAdapter.hideLoading();
                    //add products to recyclerview
                    registerServiceItemsAdapter.addProducts(ServiceProviderDataHolder.getUserSpecificServiceDetails(user_id));
                }
            });
        } else {
            recyclerViewProducts.post(new Runnable() {
                @Override
                public void run() {
                    registerServiceItemsAdapter.hideLoading();
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
            ServiceProviderDataHolder.getUserSpecificServiceDetails(user_id);
            return true;
        }

        public void onPostExecute(Boolean objects) {
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void onItemClicked(int position) {
        registerServiceItemsAdapter.updateListLoading(ServiceProviderDataHolder.getUserSpecificServiceDetails(user_id));
        String service_id = ServiceProviderDataHolder.getUserSpecificServiceDetails(user_id).get(position - 1).getService_id();
        ServiceDetailsFragment serviceDetailsFragment = ServiceDetailsFragment.newInstance();
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putString("service_id", service_id);
        serviceDetailsFragment.setArguments(args);
        fragmentTransaction.replace(R.id.containerService, serviceDetailsFragment);
        fragmentTransaction.commit();
    }

    public Properties getProperties(Context context){
        Properties props = new Properties();
        try {
            FileInputStream fin= context.openFileInput(COOKIE_FILE_NAME);
            props.load(fin);
        } catch (FileNotFoundException e) {
            Log.e("File Error", "Error reading properties file", e);
        } catch (IOException e) {
            Log.e("File Error", "Error reading properties file", e);
        }
        return props;
    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }
}
