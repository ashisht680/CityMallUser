package com.javinindia.citymalls.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.apiparsing.CountryModel;
import com.javinindia.citymalls.preference.SharedPreferencesManager;
import com.javinindia.citymalls.recyclerview.RVAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ashish on 08-09-2016.
 */
public class BrandsFragment extends BaseFragment implements View.OnClickListener{

    private RecyclerView recyclerview;
    private List<CountryModel> mCountryModel;
    private RVAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        initialize(view);
        String loc = SharedPreferencesManager.getLocation(activity);
        if(!TextUtils.isEmpty(loc)){
            Log.e("BrandsFragment loc",loc);
        }
        return view;
    }

    private void initialize(View view) {
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerviewMall);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] locales = Locale.getISOCountries();
        mCountryModel = new ArrayList<>();

        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            mCountryModel.add(new CountryModel(obj.getDisplayCountry(), obj.getISO3Country()));
        }

        adapter = new RVAdapter(mCountryModel);
        recyclerview.setAdapter(adapter);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.mall_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    private List<CountryModel> filter(List<CountryModel> models, String query) {
        query = query.toLowerCase();

        final List<CountryModel> filteredModelList = new ArrayList<>();
        for (CountryModel model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }
}
