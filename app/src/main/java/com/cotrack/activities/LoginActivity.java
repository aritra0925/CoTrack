package com.cotrack.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cotrack.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginActivity extends AccountAuthenticatorActivity {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        RadioButton radioButton = (RadioButton) findViewById(_radioGroup.getCheckedRadioButtonId());
        boolean isService = false;
        if(radioButton.getText().toString().equalsIgnoreCase(getString(R.string.user_type_service))){
            isService = true;
        }
        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
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

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

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
}
