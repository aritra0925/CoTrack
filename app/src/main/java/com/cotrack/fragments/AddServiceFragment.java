package com.cotrack.fragments;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cotrack.R;
import com.cotrack.activities.ServiceNavigationActivity;
import com.cotrack.global.AssetDataHolder;
import com.cotrack.global.ServiceProviderDataHolder;
import com.cotrack.global.UserDataHolder;
import com.cotrack.helpers.Session;
import com.cotrack.models.ProviderDetails;
import com.cotrack.models.ServiceDetails;
import com.cotrack.models.Slots;
import com.cotrack.utils.CloudantProviderUtils;
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

import butterknife.BindView;

@SuppressWarnings("deprecation")
public class AddServiceFragment extends Fragment {

    private static final String TAG = "ServiceAdditionActivity";
    final String COOKIE_FILE_NAME = "Cookie.properties";
    final String USER_COOKIE = "UserCookie";
    final String USER_TYPE_COOKIE = "UserTypeCookie";
    final String USER_NAME_COOKIE = "UserNameCookie";
    Session session;
    @BindView(R.id.city_service)
    EditText _cityText;
    @BindView(R.id.input_address_service)
    EditText _addressText;
    @BindView(R.id.input_mobile_service)
    EditText _mobileText;
    @BindView(R.id.postal_code_service)
    EditText _postalCode;
    @BindView(R.id.btn_add_service)
    Button _addServiceButton;
    @BindView(R.id.serviceTypeRegistration)
    AutoCompleteTextView _serviceTypeText;
    @BindView(R.id.decription_service)
    EditText _descriptionText;
    @BindView(R.id.serviceState)
    AutoCompleteTextView _stateText;
    @BindView(R.id.input_tags)
    EditText _tags;
    EditText ambulanceQuantity;
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
    private static AddServiceFragment instance = null;
    View view;
    private String primary_quantity;
    private List<String> available_tests = new ArrayList<>();
    private List<Slots> slots = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    public AddServiceFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServiceFragment.
     */
    public static AddServiceFragment newInstance() {
        if (instance == null) {
            instance = new AddServiceFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        view = inflater.inflate(R.layout.fragment_registered_service_home, container, false);
        session = new Session(getActivity());
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.select_dialog_item, Arrays.asList(getResources().getStringArray(R.array.india_states)));
        stateAdapter.setNotifyOnChange(true);
        _stateText = (AutoCompleteTextView) view.findViewById(R.id.serviceState);
        _stateText.setAdapter(stateAdapter);
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.select_dialog_item, Arrays.asList(getResources().getStringArray(R.array.service_types)));
        serviceAdapter.setNotifyOnChange(true);
        _serviceTypeText = (AutoCompleteTextView) view.findViewById(R.id.serviceTypeRegistration);
        _serviceTypeText.setAdapter(serviceAdapter);
        _addServiceButton = (Button) view.findViewById(R.id.btn_add_service);
        _cityText = (EditText) view.findViewById(R.id.city_service);
        _addressText = (EditText) view.findViewById(R.id.input_address_service);
        _mobileText = (EditText) view.findViewById(R.id.input_mobile_service);
        _postalCode = (EditText) view.findViewById(R.id.postal_code_service);
        _descriptionText = (EditText) view.findViewById(R.id.decription_service);
        _tags = (EditText) view.findViewById(R.id.input_tags);
        _addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    onServiceAdditionFailed(view.getContext());
                    return;
                }
                showServiceSpecificFields(view);
            }
        });
        return view;
    }

    public void addService(Context context) {
        Log.d(TAG, "Adding service");

        _addServiceButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding Service...");
        progressDialog.show();

        String serviceType = _serviceTypeText.getText().toString();
        String address = _addressText.getText().toString();
        String state = _stateText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String description = _descriptionText.getText().toString();
        String postalCode = _postalCode.getText().toString();
        String city = _cityText.getText().toString();
        String tagsText = _tags.getText().toString();
        if (tags.contains(",")) {
            tags = Arrays.asList(tagsText.split(","));
        } else {
            tags.add(tagsText);
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        if (addService(context, serviceType, description, address, mobile, state, city, postalCode)) {
                            onServiceAdditionSuccess(view.getContext());
                            ServiceProviderDataHolder.refreshAllUserSpecificDetails();
                        } else {
                            onServiceAdditionFailed(view.getContext());
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onServiceAdditionSuccess(Context context) {
        _addServiceButton.setEnabled(true);
        getActivity().setResult(ServiceNavigationActivity.RESULT_OK, null);
        Toast.makeText(context, "Service addition successful. Add another", Toast.LENGTH_LONG).show();
        //_stateText.getText().clear();
        _cityText.getText().clear();
        _stateText.getText().clear();
        _descriptionText.getText().clear();
        _postalCode.getText().clear();
        _addressText.getText().clear();
        _mobileText.getText().clear();
    }

    public void onServiceAdditionFailed(Context context) {
        Toast.makeText(context, "Service addition failed", Toast.LENGTH_LONG).show();

        _addServiceButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        List<String> serviceTypes = Arrays.asList(getResources().getStringArray(R.array.service_types));
        List<String> states = Arrays.asList(getResources().getStringArray(R.array.india_states));
        String serviceType = _serviceTypeText.getText().toString();
        String address = _addressText.getText().toString();
        String state = _stateText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String description = _descriptionText.getText().toString();
        String postalCode = _postalCode.getText().toString();
        String city = _cityText.getText().toString();

        if (serviceType.isEmpty() || !serviceTypes.contains(serviceType)) {
            _serviceTypeText.setError("Please select one of the valid options");
            valid = false;
        } else {
            _serviceTypeText.setError(null);
        }

        if (state.isEmpty() || !states.contains(state)) {
            _stateText.setError("Please select one of the valid options");
            valid = false;
        } else {
            _stateText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }

        if (description.isEmpty()) {
            _descriptionText.setError("At least 10 characters should be entered");
            valid = false;
        } else {
            _descriptionText.setError(null);
        }

        if (postalCode.isEmpty()) {
            _postalCode.setError("enter a valid email address");
            valid = false;
        } else {
            _postalCode.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (city.isEmpty()) {
            _cityText.setError("Enter City");
            valid = false;
        } else {
            _cityText.setError(null);
        }

        return valid;
    }

    public boolean addService(Context context, String type, String service_description, String address, String phone, String state, String city, String postalCode) {
        boolean flag = true;
        int count = 1;
        int totalCount = 10;
        String id = CommonUtils.generateRandomDigits(8);
        String _id = "service:S" + id;
        while (CloudantServiceUtils.checkEntry(_id) && count <= totalCount) {
            _id = "provider:P" + CommonUtils.generateRandomDigits(8);
            count++;
        }
        if (count == totalCount) {
            return false;
        }
        String asset_id = "";
        String service_name = "";

        for (AssetDataHolder assetDataHolder : AssetDataHolder.getAllInstances()) {
            System.out.println("Asset ID: " + asset_id);
            System.out.println("Asset Title: " + type);
            if (assetDataHolder.getAsset_type().equalsIgnoreCase(type)) {
                asset_id = assetDataHolder.getAsset_id();
                service_name = assetDataHolder.getAsset_title();
            }
        }

        ServiceDetails serviceDetails = new ServiceDetails(_id, type, getProperties(context).getProperty(USER_COOKIE),
                service_name, service_description, asset_id, _id, address, city, state, postalCode, phone, getProperties(view.getContext()).getProperty(USER_NAME_COOKIE));
        serviceDetails.setSlots(slots);
        serviceDetails.setPrimary_quantity(primary_quantity);
        serviceDetails.setAvailable_tests(available_tests);
        serviceDetails.setTags(tags);
        CloudantServiceUtils.insertDocument(serviceDetails);
        flag = true;
        return flag;
    }

    public Properties getProperties(Context context) {
        Properties props = new Properties();
        try {
            FileInputStream fin = context.openFileInput(COOKIE_FILE_NAME);
            props.load(fin);
        } catch (FileNotFoundException e) {
            Log.e("File Error", "Error reading properties file", e);
        } catch (IOException e) {
            Log.e("File Error", "Error reading properties file", e);
        }
        return props;
    }

    private void showServiceSpecificFields(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.AppTheme_Dark_Dialog);
        builder.setTitle("Additional Details");
        builder.setCancelable(false);
        String serviceType = _serviceTypeText.getText().toString();
        String assetPrimaryQuantityKey = "";
        for (AssetDataHolder holder : AssetDataHolder.getAllInstances()) {
            if (holder.getAsset_type().equalsIgnoreCase(serviceType)) {
                assetPrimaryQuantityKey = holder.getAsset_count_key();
                break;
            }
        }
        switch (serviceType.toUpperCase()) {
            case "AMBULANCE":
            case "HOSPITAL":
                // Set up the input quantity
                ambulanceQuantity = new EditText(view.getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                ambulanceQuantity.setPadding(2, 2, 2, 2);
                ambulanceQuantity.setInputType(InputType.TYPE_CLASS_TEXT);
                ambulanceQuantity.setHint(assetPrimaryQuantityKey);
                builder.setView(ambulanceQuantity);
                break;
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
        builder.setPositiveButton("Add Service", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //m_Text = input.getText().toString();
                // Finish the registration screen and return to the Login activity

                switch (serviceType.toUpperCase()) {
                    case "HOSPITAL":
                    case "AMBULANCE":
                        primary_quantity = ambulanceQuantity.getText().toString();
                        break;
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
                    case "DISINFECT":
                        break;
                    case "MEDICINE":
                        break;
                    default:
                }
                addService(view.getContext());
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
}
