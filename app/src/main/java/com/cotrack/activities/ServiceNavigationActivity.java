package com.cotrack.activities;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.cotrack.fragments.RegisteredServicesFragment;
import com.cotrack.fragments.ServiceFragment;
import com.cotrack.fragments.ServiceSpecificFragment;
import com.cotrack.global.AssetDataHolder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;

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
            return true;
        }

        public void onPostExecute(Boolean objects) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

}
