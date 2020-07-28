package com.cotrack.activities;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cotrack.R;
import com.cotrack.fragments.AddServiceFragment;
import com.cotrack.fragments.ChatFragment;
import com.cotrack.fragments.HomeFragment;
import com.cotrack.fragments.OrdersFragment;
import com.cotrack.fragments.RegisteredServicesFragment;
import com.cotrack.fragments.RequestedServicesFragment;
import com.cotrack.fragments.ServiceFragment;
import com.cotrack.fragments.ServiceSpecificFragment;
import com.cotrack.global.AssetDataHolder;
import com.cotrack.global.OrderDataHolder;
import com.cotrack.global.UserDataHolder;
import com.cotrack.helpers.Session;
import com.cotrack.models.ProviderDetails;
import com.cotrack.utils.CloudantProviderUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.internal.LinkedTreeMap;

import javax.persistence.spi.ProviderUtil;

import butterknife.BindView;

import static com.cloudant.client.api.query.Expression.eq;

public class ServiceNavigationActivity extends AppCompatActivity {
    // Objects
    BottomNavigationView bottomNavigation;
    @BindView(R.id.serviceNavigationLayout)
    RelativeLayout serviceNavigationLayout;
    ProgressBar progressBar;

    // Listeners
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home_service:
                            openFragment(AddServiceFragment.newInstance());
                            return true;
                        case R.id.navigation_services_service:
                            openFragment(RegisteredServicesFragment.newInstance());
                            return true;
                        case R.id.navigation_orders_services:
                            openFragment(RequestedServicesFragment.newInstance());
                            return true;
                    }
                    return false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_services);
        serviceNavigationLayout = (RelativeLayout) findViewById(R.id.serviceNavigationLayout);
        new DataLoadTask().execute("");
        bottomNavigation = findViewById(R.id.bottom_navigationService);
        if(savedInstanceState == null){
            bottomNavigation.setSelectedItemId(R.id.navigation_services_service);
            openFragment(RegisteredServicesFragment.newInstance());
        }
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerService, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @SuppressWarnings("deprecation")
    class DataLoadTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressBar(ServiceNavigationActivity.this);
            progressBar.setTooltipText("Please wait. Fetching data...");
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            serviceNavigationLayout.addView(progressBar, params);
        }

        /**
         * @param objects
         * @deprecated
         */
        @Override
        public Boolean doInBackground(String... objects) {
            AssetDataHolder.getAllInstances();
            OrderDataHolder.getAllUserSpecificDetails();
            LinkedTreeMap treeMap = (LinkedTreeMap) CloudantProviderUtils.queryData(eq("provider_email", UserDataHolder.USER_ID)).getDocs().get(0);
            UserDataHolder.USER_NAME = treeMap.get("provider_name").toString();
            return true;
        }

        public void onPostExecute(Boolean objects) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

}
