package com.cotrack.fragments;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cotrack.R;
import com.cotrack.adaptors.ServiceAdapter;
import com.cotrack.models.ServiceDetailsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.cotrack.fragments.ServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceFragment extends Fragment {

    ListView listView;
    private static ServiceFragment instance = null;
    View view;
    List<ServiceDetailsModel> serviceDetailsModels;

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

    /*@Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        serviceDetailsModels = getServiceDetails();
        inflater.inflate(R.menu., menu);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        serviceDetailsModels = getServiceDetails();
        view = inflater.inflate(R.layout.fragment_service, container, false);
        listView=(ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new ServiceAdapter(this.getContext(), serviceDetailsModels));

        return view;
    }

    private List<ServiceDetailsModel> getServiceDetails(){
        List<ServiceDetailsModel> serviceDetailsModels = new ArrayList<>();
        ServiceDetailsModel model1 = new ServiceDetailsModel("Hospital",5, 0);
        ServiceDetailsModel model2 = new ServiceDetailsModel("Pathology",0, 0);
        ServiceDetailsModel model3 = new ServiceDetailsModel("Ambulance",3, 0);
        ServiceDetailsModel model4 = new ServiceDetailsModel("Disinfectant",7, 0);
        ServiceDetailsModel model5 = new ServiceDetailsModel("Doctor",2, 0);
        ServiceDetailsModel model6 = new ServiceDetailsModel("Medicine",2, 0);
        ServiceDetailsModel model7 = new ServiceDetailsModel("Other",6, 0);
        serviceDetailsModels.add(model1);
        serviceDetailsModels.add(model2);
        serviceDetailsModels.add(model3);
        serviceDetailsModels.add(model4);
        serviceDetailsModels.add(model5);
        serviceDetailsModels.add(model6);
        serviceDetailsModels.add(model7);
        return serviceDetailsModels;
    }
}