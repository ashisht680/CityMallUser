package com.javinindia.citymalls.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.apiparsing.CountryModel;
import com.javinindia.citymalls.recyclerview.OfferAdaptar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ashish on 08-09-2016.
 */
public class OffersFragment extends BaseFragment implements View.OnClickListener, OfferAdaptar.MyClickListener{

    private RecyclerView recyclerview;
    private List<CountryModel> mCountryModel;
    private OfferAdaptar adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity.getSupportActionBar().show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initialize(view);
        setRequest();
        return view;
    }

    private void setRequest() {
        String[] locales = Locale.getISOCountries();
        mCountryModel = new ArrayList<>();

        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            mCountryModel.add(new CountryModel(obj.getDisplayCountry(), obj.getISO3Country()));
        }

        adapter = new OfferAdaptar(mCountryModel);
        adapter.setMyClickListener(OffersFragment.this);
        recyclerview.setAdapter(adapter);
    }

    private void initialize(View view) {
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerviewOffer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
      /*  RelativeLayout rlFavourateMalls = (RelativeLayout)view.findViewById(R.id.rlFavourateMalls);
        RelativeLayout rlOffers = (RelativeLayout)view.findViewById(R.id.rlOffers);
        RelativeLayout rlEvents = (RelativeLayout)view.findViewById(R.id.rlEvents);
        RelativeLayout rlSearch = (RelativeLayout)view.findViewById(R.id.rlSearch);
        rlOffers.setBackgroundColor(Color.parseColor("#000000"));
        rlFavourateMalls.setOnClickListener(this);
        rlOffers.setOnClickListener(this);
        rlEvents.setOnClickListener(this);
        rlSearch.setOnClickListener(this);*/

    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.offer_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }



 /*   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.navigation_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(activity,"brands",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

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
          /*  case R.id.rlFavourateMalls:
                BaseFragment fragment = new MallsFragmet();
                callFragmentMethod(fragment,this.getClass().getSimpleName(),R.id.navigationContainer);
                break;
            case R.id.rlOffers:

                break;
            case R.id.rlEvents:

                break;
            case R.id.rlSearch:

                break;*/
        }
    }

    @Override
    public void onItemClick(int position, CountryModel model) {
        BaseFragment fragment = new OfferDetailFragment();
        callFragmentMethod(fragment,this.getClass().getSimpleName(),R.id.navigationContainer);
    }
}
