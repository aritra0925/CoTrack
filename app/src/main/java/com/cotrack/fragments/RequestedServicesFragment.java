package com.cotrack.fragments;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.cotrack.R;
import com.cotrack.adaptors.RequestedServicesAdapter;
import com.cotrack.global.OrderDataHolder;
import com.cotrack.helpers.OnItemClick;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class RequestedServicesFragment  extends Fragment implements OnItemClick {

    ListView listView;
    private static RequestedServicesFragment instance = null;
    View view;
    List<OrderDataHolder> orders;
    final String COOKIE_FILE_NAME = "Cookie.properties";
    final String USER_COOKIE = "UserCookie";
    final String USER_TYPE_COOKIE = "UserTypeCookie";
    ProgressBar progressBar;
    FrameLayout frameLayout;
    public RequestedServicesFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestedServicesFragment newInstance() {
        if (instance == null) {
            instance = new RequestedServicesFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_requested_services, container, false);
        frameLayout = view.findViewById(R.id.requestedServicesLayout);
        ImageView backButton = getActivity().findViewById(R.id.backButtonService);
        disableBackButton(backButton);
        listView=(ListView) view.findViewById(R.id.listViewRequestedServices);
        listView.setDivider(null);
        try {
            new DataLoadTask().execute("").get();
        } catch (ExecutionException e) {
            Log.e("Fatal Error", "Exception while retreiving data", e);
        } catch (InterruptedException e) {
            Log.e("Fatal Error", "Exception while retreiving data", e);
        }
        if( orders == null ||  orders.isEmpty()) {
            view = inflater.inflate(R.layout.layout_missing_data, container, false);
            return view;
        }
        RequestedServicesAdapter serviceAdapter = new RequestedServicesAdapter(view.getContext(), orders);
        listView.setAdapter(serviceAdapter);
        serviceAdapter.setItemClick(this);
        return view;
    }

    public void disableBackButton(ImageView backButton){
        if(backButton != null) {
            backButton.setBackgroundColor(getResources().getColor(R.color.primary));
            backButton.setColorFilter(getResources().getColor(R.color.primary));
            backButton.setEnabled(false);
        }
    }

    @Override
    public void onItemClicked(int position) {
    }

    public Properties getProperties(){
        Properties props = new Properties();
        try {
            FileInputStream fin= view.getContext().openFileInput(COOKIE_FILE_NAME);
            props.load(fin);
        } catch (FileNotFoundException e) {
            Log.e("File Error", "Error reading properties file", e);
        } catch (IOException e) {
            Log.e("File Error", "Error reading properties file", e);
        }
        return props;
    }

    @SuppressWarnings("deprecation")
    class DataLoadTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = new ProgressBar(view.getContext());
            progressBar.setTooltipText("Please wait. Fetching data...");
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary)));
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            frameLayout.addView(progressBar, params);
        }

        /**
         * @param objects
         * @deprecated
         */
        @Override
        public Boolean doInBackground(String... objects) {
            orders = OrderDataHolder.getAllServiceSpecificDetails().get(getProperties().getProperty(USER_COOKIE));
            System.out.println("User specific data" + orders);
            return true;
        }

        public void onPostExecute(Boolean objects) {
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
