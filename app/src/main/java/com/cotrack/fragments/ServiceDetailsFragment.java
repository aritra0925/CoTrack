package com.cotrack.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cotrack.R;
import com.cotrack.global.ServiceProviderDataHolder;

public class ServiceDetailsFragment  extends Fragment {

    private static ServiceDetailsFragment instance = null;
    View view;
    String service_id;

    public ServiceDetailsFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServiceFragment.
     */
    public static ServiceDetailsFragment newInstance() {
        if (instance == null) {
            instance = new ServiceDetailsFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_service_details, container, false);
        service_id = getArguments().getString("service_id");
        loadData(view.getContext(), service_id);
        return view;
    }

    private void loadData(Context context, String service_id) {
        for(ServiceProviderDataHolder holder : ServiceProviderDataHolder.getAllInstances()){
            String descr = holder.getService_description();
            
        }
    }
}
