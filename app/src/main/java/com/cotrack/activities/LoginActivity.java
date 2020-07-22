package com.cotrack.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.cloudant.client.api.query.Selector;
import com.cotrack.R;
import com.cotrack.helpers.Session;
import com.cotrack.receivers.Restarter;
import com.cotrack.services.LoginService;
import com.cotrack.utils.APIUtils;
import com.cotrack.utils.CommonUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cloudant.client.api.query.Expression.eq;
import static com.cloudant.client.api.query.Operation.and;


public class LoginActivity extends AccountAuthenticatorActivity {
    final String COOKIE_FILE_NAME = "Cookie.properties";
    final String USER_COOKIE = "UserCookie";
    final String USER_TYPE_COOKIE = "UserTypeCookie";
    private String errorMessage;
    boolean isService = false;
    Session session;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public String loginNameVal;
    public String loginPswdVal;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    @BindView(R.id.login_user_type)
    RadioGroup _radioGroup;
    boolean flag = false;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};
    int PERMISSION_ALL = 1;
    String email;
    LoginService mLoginService;
    Intent mServiceIntent;
    public String userCookie;
    public String userTypeCookie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        session = new Session(this);
        setContentView(R.layout.activity_login);
        mLoginService = new LoginService();
        mServiceIntent = new Intent(this, mLoginService.getClass());
        if (!isMyServiceRunning(mLoginService.getClass())) {
            System.out.println("Service is already running");
            startService(mServiceIntent);
            System.out.println("Service started");
        } else {
            System.out.println("Service is already running");
        }
        ButterKnife.bind(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            System.out.println("Checking permission request");
        }
        if(session.getusername() != null && !session.getusername().isEmpty()
                && session.getUserType() != null && !session.getUserType().isEmpty()) {
            email = session.getusername();
            onSessionActive();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        String userCookie =  savedInstanceState.getString(USER_COOKIE);
        if(!TextUtils.isEmpty(userCookie)) {
            this.userCookie = userCookie;
        }
        String userTypeCookie =  savedInstanceState.getString(USER_TYPE_COOKIE);
        if(!TextUtils.isEmpty(userTypeCookie)) {
            this.userTypeCookie = userTypeCookie;
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the critical data
        outState.putString(USER_COOKIE, userCookie);
        outState.putString(USER_TYPE_COOKIE, userTypeCookie);
        super.onSaveInstanceState(outState);
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        RadioButton radioButton = (RadioButton) findViewById(_radioGroup.getCheckedRadioButtonId());
        if (radioButton.getText().toString().equalsIgnoreCase(getString(R.string.user_type_service))) {
            isService = true;
        }
        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        if (userLogin(isService, email, password)) {
                            onLoginSuccess();
                        } else {
                            onLoginFailed();
                        }

                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        setResult(RESULT_OK, null);
        // Finish the registration screen and return to the Login activity
        Intent intent = null;
        if (isService) {
            intent = new Intent(this, ServiceNavigationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("service_user", email); //Your id
            intent.putExtras(bundle); //Put your id to your next Intent
            writeProperties(USER_COOKIE, email);
            writeProperties(USER_TYPE_COOKIE, "service");

        } else {
            intent = new Intent(this, NavigationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("regular_user", email); //Your id
            intent.putExtras(bundle); //Put your id to your next Intent
            writeProperties(USER_COOKIE, email);
            writeProperties(USER_TYPE_COOKIE, "regular");
        }
        startActivity(intent);
        finish();
    }

    public void onSessionActive() {
        _loginButton.setEnabled(true);
        setResult(RESULT_OK, null);
        // Finish the registration screen and return to the Login activity
        Intent intent = null;
        if (isService) {
            intent = new Intent(this, ServiceNavigationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("service_user", email); //Your id
            intent.putExtras(bundle); //Put your id to your next Intent
            session = new Session(this);
            session.setUserName(email);
            session.setUserType("service");
            userCookie = email;
            userTypeCookie = "service";
            writeProperties(USER_COOKIE, email);
            writeProperties(USER_TYPE_COOKIE, "service");

        } else {
            intent = new Intent(this, NavigationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("regular_user", email); //Your id
            intent.putExtras(bundle); //Put your id to your next Intent
            session = new Session(this);
            session.setUserName(email);
            session.setUserType("regular");
            userCookie = email;
            userTypeCookie = "regular";
            writeProperties(USER_COOKIE, email);
            writeProperties(USER_TYPE_COOKIE, "regular");
        }
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        _emailText.setError("Login Failed");
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private boolean userLogin(boolean isService, String userName, String password) {
        flag = false;
        if (!isService) {
            Selector selector = and(eq("user_signonid", userName), eq("user_password", CommonUtils.encode(password)));
            flag = APIUtils.validateEntry(selector);
        } else {
            Selector selector = and(eq("provider_signonid", userName), eq("provider_password", CommonUtils.encode(password)));
            flag = APIUtils.validateEntry(selector);
        }
        return flag;
    }

    // method to add account..
    private void addAccount(String username, String password) {
        AccountManager accnt_manager = AccountManager
                .get(getApplicationContext());

        Account[] accounts = accnt_manager
                .getAccountsByType(getString(R.string.account_type)); // account name identifier.

        if (accounts.length > 0) {
            return;
        }

        final Account account = new Account(username,
                getString(R.string.account_type));

        accnt_manager.addAccountExplicitly(account, password, null);

        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
        intent.putExtra(AccountManager.KEY_PASSWORD, password);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE,
                getString(R.string.account_type));
        // intent.putExtra(AccountManager.KEY_AUTH_TOKEN_LABEL,
        // PARAM_AUTHTOKEN_TYPE);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, "token");
        this.setAccountAuthenticatorResult(intent.getExtras());
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

    // method to retrieve account.
    private boolean validateAccount() {
        AccountManagerCallback<Bundle> callback = new AccountManagerCallback<Bundle>() {

            @Override
            public void run(AccountManagerFuture<Bundle> arg0) {
                Log.e("calback", "msg");

                try {
                    Bundle b = arg0.getResult();
                    if (b.getBoolean(AccountManager.KEY_ACCOUNT_MANAGER_RESPONSE)) {
                        //User account exists!!..
                    }
                } catch (OperationCanceledException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (AuthenticatorException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        AccountManager accnt_manager = AccountManager
                .get(getApplicationContext());

        Account[] accounts = accnt_manager
                .getAccountsByType(getString(R.string.account_type));

        if (accounts.length <= 0) {
            return false;
        } else {
            loginNameVal = accounts[0].name;
            loginPswdVal = accnt_manager.getPassword(accounts[0]);
            return true;
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                int selfPermission = ActivityCompat.checkSelfPermission(context, permission);
                int permissionGrantedCode = PackageManager.PERMISSION_GRANTED;
                System.out.println("Permission status: " + selfPermission);
                System.out.println("Permission Granted: " + permissionGrantedCode);
                if (selfPermission != permissionGrantedCode) {
                    System.out.println("App permmission not working");
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        System.out.println("Validating permission reuqest");
        if (requestCode == PERMISSION_ALL) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    showExplanation("Permission Required", "This app will not work without the permissions", permission, PERMISSION_ALL);
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                    }
                }

            }
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                PERMISSIONS, permissionRequestCode);
    }

    public Properties writeProperties(String key, String value){
        Properties props = new Properties();
        try {
            File file = this.getFileStreamPath(COOKIE_FILE_NAME);
            if(!file.exists()){
                file.createNewFile();
            }
            FileInputStream  fin= openFileInput(COOKIE_FILE_NAME);
            props.load(fin);
            props.put(key, value);
            FileOutputStream fOut = openFileOutput(COOKIE_FILE_NAME,Context.MODE_PRIVATE);
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
