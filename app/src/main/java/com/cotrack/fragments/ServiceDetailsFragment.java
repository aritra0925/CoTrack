package com.cotrack.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import com.cotrack.R;
import com.cotrack.global.AssetDataHolder;
import com.cotrack.global.ServiceProviderDataHolder;
import com.cotrack.models.Slots;

import java.util.ArrayList;
import java.util.List;


public class ServiceDetailsFragment extends Fragment {

    private static ServiceDetailsFragment instance = null;
    View view;
    TextView providerName;
    TextView providerAssetCount;
    TextView providerAssetDescription;
    TextView providerContact;
    TextView providerEmail;
    TextView quantityToBeAdded;
    String service_id;
    Button addToCart;
    Button addButton;
    LinearLayout buttonWrapperLayout;
    Button subtractButton;
    ImageView detailImage;
    int number = 0;


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
        providerName = view.findViewById(R.id.textView_ProviderName);
        providerAssetCount = view.findViewById(R.id.textView_ProviderDetails);
        providerAssetDescription = view.findViewById(R.id.textView_ProviderDescription);
        providerContact = view.findViewById(R.id.textView_ProviderContact);
        providerEmail = view.findViewById(R.id.textView_ProviderEmail);
        quantityToBeAdded = view.findViewById(R.id.quantity);
        number = Integer.parseInt(quantityToBeAdded.getText().toString());
        addButton = view.findViewById(R.id.addServiceDetails);
        subtractButton = view.findViewById(R.id.subtractServiceDetails);
        addToCart = view.findViewById(R.id.btn_add_to_cart);
        buttonWrapperLayout = view.findViewById(R.id.layio);
        detailImage = view.findViewById(R.id.serviceDetailsPic);
        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = number - 1;
                quantityToBeAdded.setText(Integer.toString(number));
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number = number + 1;
                quantityToBeAdded.setText(Integer.toString(number));
            }
        });
        service_id = getArguments().getString("service_id");
        loadData(view.getContext(), service_id);
        return view;
    }

    private void loadData(Context context, String service_id) {
        System.out.println("service_id : " + service_id);
        ServiceProviderDataHolder dataHolder = null;
        for (ServiceProviderDataHolder holder : ServiceProviderDataHolder.getAllInstances()) {

            if (holder.get_id().equalsIgnoreCase(service_id)) {
                dataHolder = holder;
                break;
            }
        }
        String description = dataHolder.getService_description();
        String pName = dataHolder.getService_provider_name();
        if(pName == null){
            pName = "To be deleted";
        }
        String contact = dataHolder.getContact();
        String email = dataHolder.getService_id();
        String serviceType = dataHolder.getType().toUpperCase();
        Log.d("Description", description);
        Log.d("PName", pName);
        Log.d("Contact", contact);
        Log.d("Email", email);
        Log.d("Service Type", serviceType);
        Log.d("_id", dataHolder.get_id());
        providerName.setText(pName);
        providerAssetDescription.setText(description);
        providerContact.setText("For any emergency please call us at : " + contact);
        providerEmail.setText("You can also reach out to us at : " + email);
        List<AssetDataHolder> assetDataHolderList = AssetDataHolder.getAllInstances();
        String primary_Count_Key = "";
        for (AssetDataHolder asset : assetDataHolderList) {
            if (asset.getAsset_type().equalsIgnoreCase(dataHolder.getType())) {
                primary_Count_Key = asset.getAsset_count_key();
                break;
            }
        }

        switch (serviceType) {
            case "PATHOLOGY CENTERS":
                AppCompatSpinner spinnerPath = new AppCompatSpinner(view.getContext());
                List<String> spinnerArrayTestTypes = dataHolder.getAvailable_tests();
                if(spinnerArrayTestTypes == null){
                    spinnerArrayTestTypes = new ArrayList<>();
                    spinnerArrayTestTypes.add("Data to be deleted");
                }
                ArrayAdapter<String> spinnerArrayAdapterPath = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_layout, spinnerArrayTestTypes);
                spinnerPath.setAdapter(spinnerArrayAdapterPath);
                String textVal = primary_Count_Key + " : ";
                for(String test: spinnerArrayTestTypes){
                    textVal = textVal + "\n" + test;
                }
                providerAssetCount.setText(textVal.toString());
                buttonWrapperLayout.addView(spinnerPath);
                addButton.setVisibility(View.GONE);
                quantityToBeAdded.setVisibility(View.GONE);
                subtractButton.setVisibility(View.GONE);
                detailImage.setImageResource(R.drawable.pathology_icon);
                break;
            case "HOSPITAL":
                detailImage.setImageResource(R.drawable.hospital_icon);
                providerAssetCount.setText(primary_Count_Key + " : " + dataHolder.getPrimary_quantity());
                addToCart.setText("Request Service");
                break;
            case "AMBULANCE":
                detailImage.setImageResource(R.drawable.ambulance_icon);
                providerAssetCount.setText(primary_Count_Key + " : " + dataHolder.getPrimary_quantity());
                addToCart.setText("Request Service");
                break;
            case "DOCTOR":
                detailImage.setImageResource(R.drawable.doctor_icon);
                addToCart.setText("Confirm Appointment");
                String text = primary_Count_Key + " : ";
                ArrayList<String> spinnerArray = new ArrayList<String>();
                String spinnerText = "";
                boolean flag = true;
                if (dataHolder.getSlots() == null || dataHolder.getSlots().isEmpty()) {
                    text = "No published schedule";
                    addToCart.setText("Request Callback");
                    addToCart.setGravity(Gravity.CENTER);
                    Log.e("WARNING", "No slots provided " + dataHolder.getSlots() + " for service id: " + dataHolder.get_id());
                    flag = false;
                    break;
                } else {
                    for (int count = 0; count < dataHolder.getSlots().size(); count++) {
                        Slots slot = dataHolder.getSlots().get(count);
                        spinnerText = slot.getDay() + " from " + slot.getStartTime() + " to " + slot.getEndTime();
                        text = text + "\n" + spinnerText;
                        spinnerArray.add(spinnerText);
                        System.out.println("Spinner Text " + count +": " + spinnerText);
                    }
                }
                providerAssetCount.setText(text);
                if (flag) {
                    AppCompatSpinner spinner = new AppCompatSpinner(view.getContext());
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_layout, spinnerArray);
                    spinner.setAdapter(spinnerArrayAdapter);
                    buttonWrapperLayout.addView(spinner);
                }
                addButton.setVisibility(View.GONE);
                quantityToBeAdded.setVisibility(View.GONE);
                subtractButton.setVisibility(View.GONE);

                break;
            case "MEDICINE":
                detailImage.setImageResource(R.drawable.medicine_icon);
                addToCart.setText("Request Service");
                addToCart.setGravity(Gravity.CENTER);
                addButton.setVisibility(View.GONE);
                quantityToBeAdded.setVisibility(View.GONE);
                subtractButton.setVisibility(View.GONE);
            case "DISINFECTANT":
                detailImage.setImageResource(R.drawable.disinfect_icon);
                addToCart.setText("Request Service");
                addToCart.setGravity(Gravity.CENTER);
                addButton.setVisibility(View.GONE);
                quantityToBeAdded.setVisibility(View.GONE);
                subtractButton.setVisibility(View.GONE);
                break;
            default:
                detailImage.setImageResource(R.drawable.medical_icon);
                providerAssetCount.setText(primary_Count_Key + " : " + dataHolder.getPrimary_quantity());
                addToCart.setText("Request Service");
                break;
        }
    }
}