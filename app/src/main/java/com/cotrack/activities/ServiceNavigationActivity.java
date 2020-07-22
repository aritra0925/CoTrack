package com.cotrack.activities;

import android.os.Bundle;
import android.view.MenuItem;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ServiceNavigationActivity extends AppCompatActivity {
    // Objects
    BottomNavigationView bottomNavigation;

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


}
