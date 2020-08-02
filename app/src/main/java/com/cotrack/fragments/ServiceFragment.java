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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cotrack.R;
import com.cotrack.adaptors.ServiceAdapter;
import com.cotrack.global.AssetDataHolder;
import com.cotrack.helpers.OnItemClick;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.cotrack.fragments.ServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("ALL")
public class ServiceFragment extends Fragment implements OnItemClick {
    ProgressBar progressBar;
    FrameLayout frameLayout;
    //SendMessage sendMessage;
    ListView listView;
    private static ServiceFragment instance = null;
    View view;
    List<AssetDataHolder> assets;

    public ServiceFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceFragment newInstance() {
        if (instance == null) {
            instance = new ServiceFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_service, container, false);
        frameLayout = (FrameLayout) view.findViewById(R.id.assetDetailsLayout);
        listView=(ListView) view.findViewById(R.id.listView);
        listView.setDivider(null);
        try {
            new DataLoadTask().execute("").get();
        } catch (ExecutionException e) {
            Log.e("Fatal Error" , "Exception while retreiving data" ,e);
        } catch (InterruptedException e) {
            Log.e("Fatal Error" , "Exception while retreiving data" ,e);
        }
        ServiceAdapter serviceAdapter = new ServiceAdapter(this.getContext(), assets);
        listView.setAdapter(serviceAdapter);
        serviceAdapter.setItemClick(this);
        return view;
    }

    @Override
    public void onItemClicked(int position) {
        String asset_id = assets.get(position).getAsset_id();
        ServiceSpecificFragment serviceDetailsFragment = ServiceSpecificFragment.newInstance();
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putString("asset_id", asset_id);
        serviceDetailsFragment.setArguments(args);
        fragmentTransaction.replace(R.id.container, serviceDetailsFragment).addToBackStack(null);
        fragmentTransaction.commit();
        fragmentTransaction.addToBackStack(null);
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
            assets = AssetDataHolder.refreshAllInstances();
            return true;
        }

        public void onPostExecute(Boolean objects) {
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}