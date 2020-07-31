package com.cotrack.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cotrack.R;
import com.cotrack.adaptors.OrdersAdapter;
import com.cotrack.global.OrderDataHolder;
import com.cotrack.global.UserDataHolder;
import com.cotrack.helpers.OnItemClick;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings("deprecation")
public class OrdersFragment  extends Fragment implements OnItemClick {

    ListView listView;
    private static OrdersFragment instance = null;
    View view;
    List<OrderDataHolder> orders;
    final String COOKIE_FILE_NAME = "Cookie.properties";
    final String USER_COOKIE = "UserCookie";
    final String USER_TYPE_COOKIE = "UserTypeCookie";

    public OrdersFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance() {
        if (instance == null) {
            instance = new OrdersFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_orders, container, false);
        ImageView backButton = getActivity().findViewById(R.id.backButton);
        disableBackButton(backButton);
        listView=(ListView) view.findViewById(R.id.listViewOrders);
        listView.setDivider(null);
        Map<String, List<OrderDataHolder>> userSpecificDetails = OrderDataHolder.getAllUserSpecificDetails();
        if( userSpecificDetails == null ||  userSpecificDetails.isEmpty()){
            Log.d("No Data For User", "Null or empty");
            view = inflater.inflate(R.layout.layout_missing_data, container, false);
            return view;
        } else if(userSpecificDetails.get(getProperties().getProperty(USER_COOKIE)) == null
                || userSpecificDetails.get(getProperties().getProperty(USER_COOKIE)).isEmpty()){
            Log.d("No Data For User", "User ID: " + getProperties().getProperty(USER_COOKIE)  + " Data: " + userSpecificDetails);
            view = inflater.inflate(R.layout.layout_missing_data, container, false);
            return view;
        } else {
            Log.d("No Data For User", "Data present: " + getProperties().getProperty(USER_COOKIE) + " Data: " + userSpecificDetails);
            orders = userSpecificDetails.get(getProperties().getProperty(USER_COOKIE));
        }
        OrdersAdapter serviceAdapter = new OrdersAdapter(view.getContext(), orders);
        listView.setAdapter(serviceAdapter);
        serviceAdapter.setItemClick(this);
        return view;
    }

    public void disableBackButton(ImageView backButton){
        if(backButton != null) {
            backButton.setBackgroundColor(getResources().getColor(R.color.primary));
            backButton.setColorFilter(getResources().getColor(R.color.primary));
            backButton.setEnabled(false);
        }
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
