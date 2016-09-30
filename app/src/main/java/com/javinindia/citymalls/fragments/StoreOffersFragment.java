package com.javinindia.citymalls.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.apiparsing.CountryModel;
import com.javinindia.citymalls.recyclerview.OfferAdaptar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ashish on 29-09-2016.
 */
public class StoreOffersFragment extends BaseFragment implements View.OnClickListener, OfferAdaptar.MyClickListener{
    private RecyclerView recyclerview;
    private List<CountryModel> mCountryModel;
    private OfferAdaptar adapter;

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int selectedPosition = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initialize(view);
        setRequest();
        setCurrentItem(selectedPosition);
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
        adapter.setMyClickListener(StoreOffersFragment.this);
        recyclerview.setAdapter(adapter);
    }
    private void initialize(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerviewOffer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
       /* RelativeLayout rlFavourateMalls = (RelativeLayout)view.findViewById(R.id.rlFavourateMalls);
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
        return R.layout.store_offer_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onItemClick(int position, CountryModel model) {
        BaseFragment fragment = new StoreTabsFragment();
        callFragmentMethod(fragment, this.getClass().getSimpleName(),R.id.navigationContainer);
    }

    @Override
    public void onClick(View v) {

    }

    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }
    private void displayMetaInfo(int position) {
        //   txtShopeName.setText((position + 1) + " of " + images.size());

        //   PostImage image = images.get(position);
        //  lblTitle.setText(image.getName());
        //     txtOffer.setText(Utility.getDateFormatAmAndPm(image.getCreateDate()));
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    //	adapter
    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.image_preview);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
            //   PostImage image = images.get(position);

            //   Utility.imageLoadGlideLibrary(getActivity(), progressBar, imageViewPreview, image.getPostUrl());
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            //  return images.size();
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
