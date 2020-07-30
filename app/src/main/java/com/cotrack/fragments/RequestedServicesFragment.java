package com.cotrack.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cotrack.R;
import com.cotrack.adaptors.OrdersAdapter;
import com.cotrack.adaptors.RequestedServicesAdapter;
import com.cotrack.global.OrderDataHolder;
import com.cotrack.global.UserDataHolder;
import com.cotrack.helpers.OnItemClick;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class RequestedServicesFragment  extends Fragment implements OnItemClick {

    ListView listView;
    private static RequestedServicesFragment instance = null;
    View view;
    List<OrderDataHolder> orders;
    final String COOKIE_FILE_NAME = "Cookie.properties";
    final String USER_COOKIE = "UserCookie";
    final String USER_TYPE_COOKIE = "UserTypeCookie";

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
        ImageView backButton = getActivity().findViewById(R.id.backButtonService);
        disableBackButton(backButton);
        listView=(ListView) view.findViewById(R.id.listViewRequestedServices);
        listView.setDivider(null);

        Map<String, List<OrderDataHolder>> serviceSpecificDetails = OrderDataHolder.getAllServiceSpecificDetails();
        if( serviceSpecificDetails == null ||  serviceSpecificDetails.isEmpty()){
            view = inflater.inflate(R.layout.layout_missing_data, container, false);
            return view;
        } else if(serviceSpecificDetails.get(getProperties().getProperty(USER_COOKIE)) == null
                || serviceSpecificDetails.get(getProperties().getProperty(USER_COOKIE)).isEmpty()){
            view = inflater.inflate(R.layout.layout_missing_data, container, false);
            return view;
        } else {
            orders = serviceSpecificDetails.get(getProperties().getProperty(USER_COOKIE));
        }
        RequestedServicesAdapter serviceAdapter = new RequestedServicesAdapter(this.getContext(), orders);
        listView.setAdapter(serviceAdapter);
        serviceAdapter.setItemClick(this);
        return view;
    }

    public void disableBackButton(ImageView backButton){
        backButton.setBackgroundColor(getResources().getColor(R.color.primary));
        backButton.setColorFilter(getResources().getColor(R.color.primary));
        backButton.setEnabled(false);
    }

    @Override
    public void onItemClicked(int position) {
        /*String orderStatus = orders.get(position).getOrder_status();
        ServiceSpecificFragment serviceDetailsFragment = ServiceSpecificFragment.newInstance();
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putString("order_status", orderStatus);
        serviceDetailsFragment.setArguments(args);
        fragmentTransaction.replace(R.id.containerService, this);
        fragmentTransaction.commit();*/
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
