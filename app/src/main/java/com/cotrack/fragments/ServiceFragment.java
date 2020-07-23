package com.cotrack.fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cotrack.R;
import com.cotrack.adaptors.ServiceAdapter;
import com.cotrack.global.AssetDataHolder;
import com.cotrack.helpers.OnItemClick;
import com.cotrack.models.ServiceDetailsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.cotrack.fragments.ServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("ALL")
public class ServiceFragment extends Fragment implements OnItemClick {

    //SendMessage sendMessage;
    ListView listView;
    private static ServiceFragment instance = null;
    View view;
    List<AssetDataHolder> assets;

    public ServiceFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceFragment newInstance() {
        if (instance == null) {
            instance = new ServiceFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_service, container, false);
        listView=(ListView) view.findViewById(R.id.listView);
        listView.setDivider(null);
        assets = AssetDataHolder.getAllInstances();
        ServiceAdapter serviceAdapter = new ServiceAdapter(this.getContext(), assets);
        listView.setAdapter(serviceAdapter);
        serviceAdapter.setItemClick(this);
        return view;
    }

    @Override
    public void onItemClicked(int position) {
        String asset_id = assets.get(position).getAsset_id();
        ServiceSpecificFragment serviceDetailsFragment = ServiceSpecificFragment.newInstance();
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putString("asset_id", asset_id);
        serviceDetailsFragment.setArguments(args);
        fragmentTransaction.replace(R.id.container, serviceDetailsFragment);
        fragmentTransaction.commit();
    }
}