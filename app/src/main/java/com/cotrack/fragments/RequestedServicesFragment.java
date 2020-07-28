package com.cotrack.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cotrack.R;
import com.cotrack.adaptors.OrdersAdapter;
import com.cotrack.global.OrderDataHolder;
import com.cotrack.global.UserDataHolder;
import com.cotrack.helpers.OnItemClick;

import java.util.List;
import java.util.Map;

public class RequestedServicesFragment  extends Fragment implements OnItemClick {

    ListView listView;
    private static RequestedServicesFragment instance = null;
    View view;
    List<OrderDataHolder> orders;

    public RequestedServicesFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestedServicesFragment newInstance() {
        if (instance == null) {
            instance = new RequestedServicesFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_requested_services, container, false);
        listView=(ListView) view.findViewById(R.id.listView);
        listView.setDivider(null);
        Map<String, List<OrderDataHolder>> serviceSpecificDetails = OrderDataHolder.getAllServiceSpecificDetails();
        if( serviceSpecificDetails == null ||  serviceSpecificDetails.isEmpty()){
            view = inflater.inflate(R.layout.layout_missing_data, container, false);
            return view;
        } else if(serviceSpecificDetails.get(UserDataHolder.USER_ID) == null || serviceSpecificDetails.get(UserDataHolder.USER_ID).isEmpty()){
            view = inflater.inflate(R.layout.layout_missing_data, container, false);
            return view;
        } else {
            orders = serviceSpecificDetails.get(UserDataHolder.USER_ID);
        }
        OrdersAdapter serviceAdapter = new OrdersAdapter(this.getContext(), orders);
        listView.setAdapter(serviceAdapter);
        serviceAdapter.setItemClick(this);
        return view;
    }

    @Override
    public void onItemClicked(int position) {
        String orderStatus = orders.get(position).getOrder_status();
        ServiceSpecificFragment serviceDetailsFragment = ServiceSpecificFragment.newInstance();
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putString("order_status", orderStatus);
        serviceDetailsFragment.setArguments(args);
        fragmentTransaction.replace(R.id.container, this);
        fragmentTransaction.commit();
    }
}
