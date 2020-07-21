package com.cotrack.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cotrack.R;
import com.cotrack.activities.ServiceNavigationActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class AddServiceFragment extends Fragment {

    private static final String TAG = "SignupActivity";

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
    @BindView(R.id.serviceType)
    AutoCompleteTextView _serviceTypeText;
    @BindView(R.id.decription_service)
    EditText _descriptionText;
    @BindView(R.id.serviceState)
    AutoCompleteTextView _stateText;

    private static AddServiceFragment instance = null;
    View view;

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
        view = inflater.inflate(R.layout.fragment_registered_service_home, container, false);
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.select_dialog_item, Arrays.asList(getResources().getStringArray(R.array.india_states)));
        stateAdapter.setNotifyOnChange(true);
        _stateText = (AutoCompleteTextView) view.findViewById(R.id.serviceState) ;
        _stateText.setAdapter(stateAdapter);
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.select_dialog_item, Arrays.asList(getResources().getStringArray(R.array.service_types)));
        serviceAdapter.setNotifyOnChange(true);
        _serviceTypeText = (AutoCompleteTextView) view.findViewById(R.id.serviceTypeRegistration) ;
        _serviceTypeText.setAdapter(serviceAdapter);
        _addServiceButton = (Button) view.findViewById(R.id.btn_add_service);
        _cityText = (EditText) view.findViewById(R.id.city_service);
        _addressText = (EditText) view.findViewById(R.id.input_address_service);
        _mobileText = (EditText) view.findViewById(R.id.input_mobile_service);
        _postalCode = (EditText) view.findViewById(R.id.postal_code_service);
        _descriptionText = (EditText) view.findViewById(R.id.decription_service);

        return view;
    }

    public void addService(Context context) {
        Log.d(TAG, "Adding service");

        if (!validate()) {
            onServiceAdditionFailed(context);
            return;
        }
        _addServiceButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String serviceType = _serviceTypeText.getText().toString();
        String address = _addressText.getText().toString();
        String state = _stateText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String description = _descriptionText.getText().toString();
        String postalCode = _postalCode.getText().toString();
        String city = _cityText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onServiceAdditionSuccess(view.getContext());
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onServiceAdditionSuccess(Context context) {
        _addServiceButton.setEnabled(true);
        getActivity().setResult(ServiceNavigationActivity.RESULT_OK, null);
        getActivity().finish();
    }

    public void onServiceAdditionFailed(Context context) {
        Toast.makeText(context, "Service Addition failed", Toast.LENGTH_LONG).show();

        _addServiceButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        List<String> serviceTypes = Arrays.asList(getResources().getStringArray(R.array.service_types));
        List<String> states = Arrays.asList(getResources().getStringArray(R.array.service_types));
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

        if (description.isEmpty() && description.length()<10) {
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
}