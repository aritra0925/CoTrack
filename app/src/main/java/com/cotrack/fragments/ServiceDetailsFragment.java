package com.cotrack.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cotrack.R;
import com.cotrack.global.AssetDataHolder;
import com.cotrack.global.OrderDataHolder;
import com.cotrack.global.ServiceProviderDataHolder;
import com.cotrack.global.UserDataHolder;
import com.cotrack.models.OrderDetails;
import com.cotrack.models.ServiceDetails;
import com.cotrack.models.Slots;
import com.cotrack.models.Test;
import com.cotrack.utils.CloudantOrderUtils;
import com.cotrack.utils.CloudantServiceUtils;
import com.cotrack.utils.CommonUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;


public class ServiceDetailsFragment extends Fragment {

    final String COOKIE_FILE_NAME = "Cookie.properties";
    final String USER_COOKIE = "UserCookie";
    final String USER_TYPE_COOKIE = "UserTypeCookie";

    private static ServiceDetailsFragment instance = null;
    private static final String TAG = "Order Details Fragment";
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
    AppCompatSpinner spinner;
    int number = 0;
    String primaryQuantity = "";
    String scheduledAppointment = "";
    String contact;
    String email;
    String serviceType;
    String serviceId;
    ProgressBar progressBar;
    LinearLayout serviceDetailsLayout;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    TextView appointmentSchedule;
    String primary_Count_Key = "";
    ServiceProviderDataHolder dataHolder;
    EditText startTime;
    EditText endTime;
    EditText availableTests;
    CheckBox sunday;
    CheckBox monday;
    CheckBox tuesday;
    CheckBox wednesday;
    CheckBox thursday;
    CheckBox friday;
    CheckBox saturday;
    Test test;
    AppCompatSpinner spinnerPath;
    private List<String> available_tests = new ArrayList<>();
    private List<Slots> slots = new ArrayList<>();

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
        instance = new ServiceDetailsFragment();
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
        // Inflate the layout for this fragment
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        view = inflater.inflate(R.layout.fragment_service_details, container, false);
        ImageView backButton = null;
        if(getProperties().getProperty(USER_TYPE_COOKIE).equalsIgnoreCase("SERVICE")) {
            backButton = getActivity().findViewById(R.id.backButtonService);
        } else {
            backButton = getActivity().findViewById(R.id.backButton);
        }
        enableBackButton(backButton);
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
        serviceDetailsLayout = view.findViewById(R.id.serviceDetailsLayout);
        progressBarHolder = (FrameLayout) view.findViewById(R.id.progressBarHolder);
        appointmentSchedule = view.findViewById(R.id.appointmentView);
        appointmentSchedule.setVisibility(View.INVISIBLE);
        subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(quantityToBeAdded.getText().toString()) == 0) {
                    return;
                }
                number = Integer.parseInt(quantityToBeAdded.getText().toString()) - 1;
                quantityToBeAdded.setText(Integer.toString(number));
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!primaryQuantity.isEmpty() && Integer.parseInt(quantityToBeAdded.getText().toString()) == Integer.parseInt(primaryQuantity)) {
                    return;
                }
                number = Integer.parseInt(quantityToBeAdded.getText().toString()) + 1;
                quantityToBeAdded.setText(Integer.toString(number));
            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addService(view.getContext());
            }
        });
        service_id = getArguments().getString("service_id");
        switch (getProperties().getProperty(USER_TYPE_COOKIE).toUpperCase()) {
            case "SERVICE":
                loadServiceData(view.getContext(), service_id);
                break;
            default:
                loadData(view.getContext(), service_id);
                break;
        }
        return view;
    }

    private void loadData(Context context, String service_id) {
        for (ServiceProviderDataHolder holder : ServiceProviderDataHolder.getAllInstances()) {

            if (holder.get_id().equalsIgnoreCase(service_id)) {
                dataHolder = holder;
                break;
            }
        }
        String description = dataHolder.getService_description();
        String pName = dataHolder.getService_provider_name();
        if (pName == null) {
            pName = "To be deleted";
        }
        contact = dataHolder.getContact();
        email = dataHolder.getService_id();
        serviceType = dataHolder.getType().toUpperCase();
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
        for (AssetDataHolder asset : assetDataHolderList) {
            if (asset.getAsset_type().equalsIgnoreCase(dataHolder.getType())) {
                primary_Count_Key = asset.getAsset_count_key();
                break;
            }
        }

        switch (serviceType) {
            case "PATHOLOGY":
                spinnerPath = new AppCompatSpinner(view.getContext());
                List<String> spinnerArrayTestTypes = dataHolder.getAvailable_tests();
                if (spinnerArrayTestTypes == null) {
                    spinnerArrayTestTypes = new ArrayList<>();
                    spinnerArrayTestTypes.add("Data to be deleted");
                }
                ArrayAdapter<String> spinnerArrayAdapterPath = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_layout, spinnerArrayTestTypes);
                spinnerPath.setAdapter(spinnerArrayAdapterPath);
                String textVal = primary_Count_Key;
                String allSpinnerTextPath = "";
                for (String test : spinnerArrayTestTypes) {
                    allSpinnerTextPath = allSpinnerTextPath + "\n" + "\u2022  " + test;
                }
                providerAssetCount.setText(textVal.toString());
                buttonWrapperLayout.addView(spinnerPath);
                appointmentSchedule.setText(allSpinnerTextPath);
                appointmentSchedule.setVisibility(View.VISIBLE);
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
                String text = primary_Count_Key;
                ArrayList<String> spinnerArray = new ArrayList<String>();
                String spinnerText = "";
                String allSpinnerText = "";
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
                        allSpinnerText = allSpinnerText + "\n" + "\u2022  " + spinnerText;
                        spinnerArray.add(spinnerText);
                    }
                }
                providerAssetCount.setText(text);
                appointmentSchedule.setText(allSpinnerText);
                appointmentSchedule.setVisibility(View.VISIBLE);
                if (flag) {
                    spinner = new AppCompatSpinner(view.getContext());
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

    private void loadServiceData(Context context, String service_id) {
        System.out.println("service_id : " + service_id);
        for (ServiceProviderDataHolder holder : ServiceProviderDataHolder.getAllInstances()) {

            if (holder.get_id().equalsIgnoreCase(service_id)) {
                dataHolder = holder;
                break;
            }
        }
        String description = dataHolder.getService_description();
        String pName = dataHolder.getService_provider_name();
        if (pName == null) {
            pName = "To be deleted";
        }
        contact = dataHolder.getContact();
        email = dataHolder.getService_id();
        serviceType = dataHolder.getType().toUpperCase();
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
        for (AssetDataHolder asset : assetDataHolderList) {
            if (asset.getAsset_type().equalsIgnoreCase(dataHolder.getType())) {
                primary_Count_Key = asset.getAsset_count_key();
                break;
            }
        }

        Log.d(TAG, "Selected Service Type: " + serviceType);

        Button button = new Button(view.getContext());
        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rounded_rectangle_button,
                0,
                0,
                0);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showServiceSpecificFields(view);
            }
        });
        switch (serviceType) {
            case "PATHOLOGY":
                AppCompatSpinner spinnerPath = new AppCompatSpinner(view.getContext());
                List<String> spinnerArrayTestTypes = dataHolder.getAvailable_tests();
                String spinnerTextPath = "";
                String allSpinnerTextPath = "";
                if (spinnerArrayTestTypes == null) {
                    spinnerArrayTestTypes = new ArrayList<>();
                    spinnerArrayTestTypes.add("Data to be deleted");
                }
                String textVal = primary_Count_Key;
                for (String test : spinnerArrayTestTypes) {
                    allSpinnerTextPath = allSpinnerTextPath + "\n" + "\u2022  " + test;
                }
                appointmentSchedule.setText(allSpinnerTextPath);
                appointmentSchedule.setVisibility(View.VISIBLE);
                providerAssetCount.setText(textVal.toString());
                addButton.setVisibility(View.GONE);
                quantityToBeAdded.setVisibility(View.GONE);
                subtractButton.setVisibility(View.GONE);
                detailImage.setImageResource(R.drawable.pathology_icon);
                addToCart.setText("Update");
                button.setText("Additional Tests");
                buttonWrapperLayout.addView(button);
                break;
            case "HOSPITAL":
                detailImage.setImageResource(R.drawable.hospital_icon);
                providerAssetCount.setText(primary_Count_Key + " : " + dataHolder.getPrimary_quantity());
                quantityToBeAdded.setText(dataHolder.getPrimary_quantity());
                addToCart.setText("Update No of Beds");
                break;
            case "AMBULANCE":
                detailImage.setImageResource(R.drawable.ambulance_icon);
                providerAssetCount.setText(primary_Count_Key + " : " + dataHolder.getPrimary_quantity());
                quantityToBeAdded.setText(dataHolder.getPrimary_quantity());
                addToCart.setText("Update No of Ambulances");
                break;
            case "DOCTOR":
                detailImage.setImageResource(R.drawable.doctor_icon);
                button.setText("Add Schedule(s)");
                addToCart.setText("Update");
                String text = primary_Count_Key;
                String spinnerText = "";
                String allSpinnerText = "";
                boolean flag = true;
                if (dataHolder.getSlots() == null || dataHolder.getSlots().isEmpty()) {
                    allSpinnerText = "No published schedule";
                    addToCart.setGravity(Gravity.CENTER);
                    Log.e("WARNING", "No slots provided " + dataHolder.getSlots() + " for service id: " + dataHolder.get_id());
                    flag = false;
                    /*
                    break;*/
                } else {
                    for (int count = 0; count < dataHolder.getSlots().size(); count++) {
                        Slots slot = dataHolder.getSlots().get(count);
                        spinnerText = slot.getDay() + " from " + slot.getStartTime() + " to " + slot.getEndTime();
                        allSpinnerText = allSpinnerText + "\n" + "\u2022  " + spinnerText;
                    }
                }
                providerAssetCount.setText(text);
                appointmentSchedule.setText(allSpinnerText);
                appointmentSchedule.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.INVISIBLE);
                quantityToBeAdded.setVisibility(View.INVISIBLE);
                subtractButton.setVisibility(View.INVISIBLE);
                buttonWrapperLayout.addView(button);
                Log.d(TAG, "Added button view");
                break;
            default:
                detailImage.setImageResource(R.drawable.medical_icon);
                providerAssetCount.setText(primary_Count_Key + " : " + dataHolder.getPrimary_quantity());
                addButton.setVisibility(View.GONE);
                quantityToBeAdded.setVisibility(View.GONE);
                subtractButton.setVisibility(View.GONE);
                addToCart.setVisibility(View.GONE);
                break;
        }

    }

    public void addService(Context context) {
        String userTypeCookie = getProperties().getProperty(USER_TYPE_COOKIE);
        switch (userTypeCookie.toUpperCase()) {
            case "SERVICE":
                switch (serviceType.toUpperCase()) {

                    case "DOCTOR":
                    case "PATHOLOGY":

                        showServiceSpecificFields(view);
                        break;
                    default:
                        updateServiceDetails(context);
                        break;
                }
                break;
            default:
                raiseServiceRequest(view.getContext());
                break;
        }
    }

    public void raiseServiceRequest(Context context) {
        Log.d(TAG, "Adding service");

        addToCart.setEnabled(false);

        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);

        switch (serviceType) {
            case "AMBULANCE":
            case "HOSPITAL":
                primaryQuantity = quantityToBeAdded.getText().toString();
                break;
            case "DOCTOR":
                scheduledAppointment = spinner.getSelectedItem().toString();
                break;
            case "PATHOLOGY":
                String selectedItem = spinnerPath.getSelectedItem().toString();
                test = new Test();
                test.setTest_type(selectedItem);
                test.setTest_status("Requested");
                break;
            default:
                break;
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onS
                        // ignupSuccess or onSignupFailed
                        // depending on success
                        ServiceDetails serviceDetails = CloudantServiceUtils.getWithId(dataHolder.get_id());
                        if (requestService(context)) {
                            onServiceRequestSuccess(view.getContext());
                            ServiceProviderDataHolder.refreshAllUserSpecificDetails();
                            switch (serviceType) {
                                case "AMBULANCE":
                                case "HOSPITAL":
                                    String updatedQuant = Integer.toString(Integer.parseInt(dataHolder.getPrimary_quantity()) - Integer.parseInt(primaryQuantity));
                                    dataHolder.setPrimary_quantity(updatedQuant);
                                    CloudantServiceUtils.updateDocument(serviceDetails);
                                    ServiceProviderDataHolder.refreshAllServiceSpecificDetails();
                                    ServiceProviderDataHolder.refreshAllUserSpecificDetails();
                                    break;
                                default:
                                    break;
                            }

                        } else {
                            onServiceRequestFailure(view.getContext());
                        }
                        outAnimation = new AlphaAnimation(1f, 0f);
                        outAnimation.setDuration(200);
                        progressBarHolder.setAnimation(outAnimation);
                        progressBarHolder.setVisibility(View.GONE);
                        addToCart.setEnabled(true);
                    }
                }, 3000);
    }

    public void updateServiceDetails(Context context) {
        Log.d(TAG, "Adding service");

        addToCart.setEnabled(false);

        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);

        switch (serviceType) {
            case "AMBULANCE":
            case "HOSPITAL":
                primaryQuantity = quantityToBeAdded.getText().toString();
                break;
            case "DOCTOR":
                scheduledAppointment = spinner.getSelectedItem().toString();
                break;
            default:
                break;
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onS
                        // ignupSuccess or onSignupFailed
                        // depending on success

                        if (updateService(context)) {
                            onServiceUpdateSuccess(view.getContext());
                        } else {
                            onServiceUpdateFailure(view.getContext());
                        }
                        outAnimation = new AlphaAnimation(1f, 0f);
                        outAnimation.setDuration(200);
                        progressBarHolder.setAnimation(outAnimation);
                        progressBarHolder.setVisibility(View.GONE);
                        addToCart.setEnabled(true);
                    }
                }, 3000);
    }

    public void onServiceRequestSuccess(Context context) {
        OrderDataHolder.refreshAllUserSpecificDetails();
        switch (serviceType) {
            case "AMBULANCE":
            case "HOSPITAL":
                String updatedQuant = Integer.toString(Integer.parseInt(dataHolder.getPrimary_quantity()) - Integer.parseInt(primaryQuantity));
                providerAssetCount.setText(primary_Count_Key + " : " + updatedQuant);
                primaryQuantity = quantityToBeAdded.getText().toString();
                break;
            default:
        }
        Toast.makeText(view.getContext(), "Service requested successfully. Request another", Toast.LENGTH_LONG).show();
    }

    public void onServiceRequestFailure(Context context) {
        Toast.makeText(view.getContext(), "There was some error requesting service. Please try again after some time", Toast.LENGTH_LONG).show();

    }

    public void onServiceUpdateSuccess(Context context) {
        OrderDataHolder.refreshAllUserSpecificDetails();
        for (ServiceProviderDataHolder holder : ServiceProviderDataHolder.getAllInstances()) {
            if (holder.get_id().equalsIgnoreCase(service_id)) {
                dataHolder = holder;
                break;
            }
        }
        switch (serviceType) {
            case "AMBULANCE":
            case "HOSPITAL":
                String updatedQuant = Integer.toString(Integer.parseInt(dataHolder.getPrimary_quantity()) + Integer.parseInt(primaryQuantity));
                providerAssetCount.setText(primary_Count_Key + " : " + updatedQuant);
                primaryQuantity = quantityToBeAdded.getText().toString();
                break;
            case "DOCTOR":
                String spinnerText = "";
                for (int count = 0; count < dataHolder.getSlots().size(); count++) {
                    Slots slot = dataHolder.getSlots().get(count);
                    spinnerText = spinnerText + "\n" + "\u2022  " + slot.getDay() + " from " + slot.getStartTime() + " to " + slot.getEndTime();
                }
                appointmentSchedule.setText(spinnerText);
                break;
            case "PATHOLOGY":
                String pathSpinnerText = "";
                for (String test : dataHolder.getAvailable_tests()) {
                    pathSpinnerText = pathSpinnerText + "\n" + "\u2022  " + test;
                }
                appointmentSchedule.setText(pathSpinnerText);
                break;
            default:
        }
        Toast.makeText(view.getContext(), "Service updated successfully", Toast.LENGTH_LONG).show();
    }

    public void onServiceUpdateFailure(Context context) {
        Toast.makeText(view.getContext(), "There was some error updating service. Please try again after some time", Toast.LENGTH_LONG).show();

    }

    public boolean requestService(Context context) {
        boolean flag = false;
        try {
            String id = CommonUtils.generateRandomDigits(8);
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.set_id("order:ORD" + id);
            orderDetails.setUser_id(getProperties().getProperty(USER_COOKIE));
            orderDetails.setService_type(serviceType);
            orderDetails.setService_id(email);
            orderDetails.setOrder_status("Created");
            orderDetails.setPrimary_quantity(primaryQuantity);
            orderDetails.setScheduled_appointment(scheduledAppointment);
            orderDetails.setTests(test);
            CloudantOrderUtils.insertDocument(orderDetails);
            Log.d(TAG, "Order Created. ID: " + id);
            flag = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return flag;
    }

    private void showServiceSpecificFields(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.AppTheme_Dark_Dialog);
        builder.setTitle("Additional Details");
        builder.setCancelable(false);
        String assetPrimaryQuantityKey = "";
        for (AssetDataHolder holder : AssetDataHolder.getAllInstances()) {
            if (holder.getAsset_type().equalsIgnoreCase(serviceType)) {
                assetPrimaryQuantityKey = holder.getAsset_count_key();
                break;
            }
        }
        switch (serviceType.toUpperCase()) {
            case "DOCTOR":
                // get prompts.xml view
                LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                View promptView = layoutInflater.inflate(R.layout.layout_doctor_additional_details, null);

                startTime = (EditText) promptView.findViewById(R.id.startTime_doctor);
                endTime = (EditText) promptView.findViewById(R.id.endTime_doctor);
                sunday = (CheckBox) promptView.findViewById(R.id.sunday);
                monday = (CheckBox) promptView.findViewById(R.id.monday);
                tuesday = (CheckBox) promptView.findViewById(R.id.tuesday);
                wednesday = (CheckBox) promptView.findViewById(R.id.wednesday);
                thursday = (CheckBox) promptView.findViewById(R.id.thursday);
                friday = (CheckBox) promptView.findViewById(R.id.friday);
                saturday = (CheckBox) promptView.findViewById(R.id.saturday);
                startTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour,
                                                  int selectedMinute) {
                                startTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("Start Time");
                        mTimePicker.show();
                    }
                });

                endTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour,
                                                  int selectedMinute) {
                                endTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        }, hour, minute, true);//Yes 24 hour time
                        mTimePicker.setTitle("End Time");
                        mTimePicker.show();
                    }
                });
                builder.setView(promptView);
                break;
            case "PATHOLOGY":
                // Set up the input quantity
                availableTests = new EditText(view.getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                availableTests.setPadding(2, 2, 2, 2);
                availableTests.setInputType(InputType.TYPE_CLASS_TEXT);
                availableTests.setHint(assetPrimaryQuantityKey + " (Comma Separated)");
                builder.setView(availableTests);
                break;
            case "DISINFECT":
            case "MEDICINE":
            default:
        }


        // Set up the buttons
        builder.setPositiveButton("Update Service", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //m_Text = input.getText().toString();
                // Finish the registration screen and return to the Login activity

                switch (serviceType.toUpperCase()) {
                    case "DOCTOR":
                        String startTimeText = startTime.getText().toString();
                        String endTimeText = endTime.getText().toString();
                        if (sunday.isChecked()) {
                            Slots slot = new Slots();
                            slot.setStartTime(startTimeText);
                            slot.setEndTime(endTimeText);
                            slot.setDay(sunday.getText().toString());
                            slots.add(slot);
                        }
                        if (monday.isChecked()) {
                            Slots slot = new Slots();
                            slot.setStartTime(startTimeText);
                            slot.setEndTime(endTimeText);
                            slot.setDay(monday.getText().toString());
                            slots.add(slot);
                        }
                        if (tuesday.isChecked()) {
                            Slots slot = new Slots();
                            slot.setStartTime(startTimeText);
                            slot.setEndTime(endTimeText);
                            slot.setDay(tuesday.getText().toString());
                            slots.add(slot);
                        }
                        if (wednesday.isChecked()) {
                            Slots slot = new Slots();
                            slot.setStartTime(startTimeText);
                            slot.setEndTime(endTimeText);
                            slot.setDay(wednesday.getText().toString());
                            slots.add(slot);
                        }
                        if (thursday.isChecked()) {
                            Slots slot = new Slots();
                            slot.setStartTime(startTimeText);
                            slot.setEndTime(endTimeText);
                            slot.setDay(thursday.getText().toString());
                            slots.add(slot);
                        }
                        if (friday.isChecked()) {
                            Slots slot = new Slots();
                            slot.setStartTime(startTimeText);
                            slot.setEndTime(endTimeText);
                            slot.setDay(friday.getText().toString());
                            slots.add(slot);
                        }
                        if (saturday.isChecked()) {
                            Slots slot = new Slots();
                            slot.setStartTime(startTimeText);
                            slot.setEndTime(endTimeText);
                            slot.setDay(saturday.getText().toString());
                            slots.add(slot);
                        }
                        break;
                    case "PATHOLOGY":
                        String tests = availableTests.getText().toString();
                        if (tests.contains(",")) {
                            available_tests = Arrays.asList(tests.split(","));
                        } else {
                            available_tests.add(tests);
                        }
                        break;
                    default:
                }

                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                progressBarHolder.setAnimation(inAnimation);
                progressBarHolder.setVisibility(View.VISIBLE);

                //updateServiceDetails(view.getContext());
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                if (updateService(view.getContext())) {
                                    onServiceUpdateSuccess(view.getContext());
                                } else {
                                    onServiceUpdateFailure(view.getContext());
                                }

                                outAnimation = new AlphaAnimation(1f, 0f);
                                outAnimation.setDuration(200);
                                progressBarHolder.setAnimation(outAnimation);
                                progressBarHolder.setVisibility(View.GONE);
                                addToCart.setEnabled(true);

                            }
                        }, 3000);


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public boolean updateService(Context context) {
        boolean flag = false;
        try {
            ServiceProviderDataHolder.refreshAllUserSpecificDetails();
            ServiceDetails serviceDetails = CloudantServiceUtils.getWithId(dataHolder.get_id());
            switch (serviceType) {
                case "AMBULANCE":
                case "HOSPITAL":
                    serviceDetails.setPrimary_quantity(primaryQuantity);
                    CloudantServiceUtils.updateDocument(serviceDetails);
                    ServiceProviderDataHolder.refreshAllServiceSpecificDetails();
                    ServiceProviderDataHolder.refreshAllUserSpecificDetails();
                    break;
                case "PATHOLOGY":
                    serviceDetails.setAvailable_tests(available_tests);
                    CloudantServiceUtils.updateDocument(serviceDetails);
                    ServiceProviderDataHolder.refreshAllServiceSpecificDetails();
                    ServiceProviderDataHolder.refreshAllUserSpecificDetails();
                    break;
                case "DOCTOR":
                    serviceDetails.setSlots(slots);
                    CloudantServiceUtils.updateDocument(serviceDetails);
                    ServiceProviderDataHolder.refreshAllServiceSpecificDetails();
                    ServiceProviderDataHolder.refreshAllUserSpecificDetails();
                    break;
                default:
                    break;
            }
            flag = true;
        } catch (Exception e) {
            Log.e("Updating Service", "Error thrown", e);
        }
        return flag;
    }

    public Properties getProperties() {
        Properties props = new Properties();
        try {
            FileInputStream fin = view.getContext().openFileInput(COOKIE_FILE_NAME);
            props.load(fin);
        } catch (FileNotFoundException e) {
            Log.e("File Error", "Error reading properties file", e);
        } catch (IOException e) {
            Log.e("File Error", "Error reading properties file", e);
        }
        return props;
    }
}