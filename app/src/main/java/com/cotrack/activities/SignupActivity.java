package com.cotrack.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cotrack.R;
import com.cotrack.helpers.Session;
import com.cotrack.models.ProviderDetails;
import com.cotrack.models.UserDetails;
import com.cotrack.utils.CloudantProviderUtils;
import com.cotrack.utils.CommonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    final String COOKIE_FILE_NAME = "Cookie.properties";
    final String USER_COOKIE = "UserCookie";
    final String USER_TYPE_COOKIE = "UserTypeCookie";
    private static final String TAG = "SignupActivity";
    private String errorMessage;
    boolean isService = false;
    Session session;
    String email;
    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_address)
    EditText _addressText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_mobile)
    EditText _mobileText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;
    @BindView(R.id.state)
    AutoCompleteTextView _stateText;
    @BindView(R.id.postal_code)
    EditText _postalCode;
    @BindView(R.id.city)
    EditText _city;
    @BindView(R.id.login_user_type_registration)
    RadioGroup _radioGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, Arrays.asList(getResources().getStringArray(R.array.india_states)));
        adapter.setNotifyOnChange(true);
        _stateText.setAdapter(adapter);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String city = _city.getText().toString();
        String state = _stateText.getText().toString();
        String postalCode = _postalCode.getText().toString();
        String password = CommonUtils.encode(_passwordText.getText().toString());
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        RadioButton radioButton = (RadioButton) findViewById(_radioGroup.getCheckedRadioButtonId());
        if (radioButton.getText().toString().equalsIgnoreCase(getString(R.string.user_type_service))) {
            isService = true;
        }
        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        boolean flag = registerUser(isService, name, password, address, city, state, postalCode, mobile, email);
                        if (flag) {
                            onSignupSuccess();
                        } else {
                            onSignupFailed();
                        }
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        // Finish the registration screen and return to the Login activity
        Intent intent = null;
        if(isService) {
            intent = new Intent(this, ServiceNavigationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("service_user", email); //Your id
            intent.putExtras(bundle); //Put your id to your next Intent
            session = new Session(this);
            session.setUserName(email);
            session.setUserType("service");
            writeProperties(USER_COOKIE, email);
            writeProperties(USER_TYPE_COOKIE, "service");

        } else{
            intent = new Intent(this, NavigationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("regular_user", email); //Your id
            intent.putExtras(bundle); //Put your id to your next Intent
            session = new Session(this);
            session.setUserName(email);
            session.setUserType("regular");
            writeProperties(USER_COOKIE, email);
            writeProperties(USER_TYPE_COOKIE, "regular");
        }
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onSignupFailed() {
        _emailText.setError(errorMessage);
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String address = _addressText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String city = _city.getText().toString();
        String state = _stateText.getText().toString();
        String postalCode = _postalCode.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }

        if (city.isEmpty()) {
            _city.setError("Enter Valid City");
            valid = false;
        } else {
            _city.setError(null);
        }

        if (state.isEmpty()) {
            _stateText.setError("Enter Valid State");
            valid = false;
        } else {
            _stateText.setError(null);
        }

        if (postalCode.isEmpty()) {
            _postalCode.setError("Enter Valid Postal Code");
            valid = false;
        } else {
            _postalCode.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    class RegisterUser extends AsyncTask {

        private Exception exception;

        /**
         * @param objects
         * @deprecated
         */
        @Override
        protected String doInBackground(Object[] objects) {
            CloudantProviderUtils.insertDocument("Test test text");
            return "";
        }

        protected void onPostExecute(String feed) {
            //Toast.makeText(view.getContext(), feed, Toast.LENGTH_LONG).show();
        }
    }

    public boolean registerUser(boolean isService, String provider_name, String provider_password, String provider_address1, String provider_city, String provider_state, String provider_zip, String provider_contact, String provider_email) {
        boolean flag = true;
        int count = 1;
        int totalCount = 10;
        String id = CommonUtils.generateRandomDigits(8);
        if (isService) {
            if(CloudantProviderUtils.checkEntry("provider_signonid", provider_email)){
                errorMessage = "Email already exists";
                return false;
            }
            String _id = "provider:P" + id;
            while (CloudantProviderUtils.checkEntry(ProviderDetails.class, _id) && count <= totalCount) {
                _id = "provider:P" + CommonUtils.generateRandomDigits(8);
                count++;
            }
            if (count == totalCount) {
                errorMessage = "Please try again after some time";
                return false;
            }
            String type = "Other";
            String service_name = "";
            ProviderDetails providerDetails = new ProviderDetails(_id, type, provider_name, provider_email, provider_password, provider_address1, provider_city, provider_state, provider_zip, provider_contact, provider_email, service_name, _id, _id);
            CloudantProviderUtils.insertDocument(providerDetails);
            this.email = provider_email;
            flag = true;
        } else {
            if(CloudantProviderUtils.checkEntry("user_signonid", provider_email)){
                errorMessage = "Email already exists";
                return false;
            }
            String _id = "user:U" + id;
            while (CloudantProviderUtils.checkEntry(UserDetails.class, _id) && count <= totalCount) {
                _id = "user:U" + CommonUtils.generateRandomDigits(8);
                count++;
            }
            if (count == totalCount) {
                errorMessage = "Please try again after some time";
                return false;
            }
            String type = "Other";
            String service_name = "";
            UserDetails userDetails = new UserDetails(_id, type, provider_name, provider_email, provider_password, provider_address1, provider_city, provider_state, provider_zip, provider_contact, provider_email, provider_name, _id);
            CloudantProviderUtils.insertDocument(userDetails);
            this.email = provider_email;
            flag = true;
        }
        return flag;
    }

    public Properties writeProperties(String key, String value){
        Properties props = new Properties();
        try {
            File file = this.getFileStreamPath(COOKIE_FILE_NAME);
            if(!file.exists()){
                file.createNewFile();
            }
            FileInputStream fin= openFileInput(COOKIE_FILE_NAME);
            props.load(fin);
            props.put(key, value);
            FileOutputStream fOut = openFileOutput(COOKIE_FILE_NAME, Context.MODE_PRIVATE);
            props.store(fOut, "Cookie Data");
            System.out.println("Properties was written successfully");
        } catch (FileNotFoundException e) {
            Log.e("File Error", "Error reading properties file", e);
        } catch (IOException e) {
            Log.e("File Error", "Error reading properties file", e);
        }
        return props;
    }
}