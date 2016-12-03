package com.javinindia.citymalls.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.constant.Constants;
import com.javinindia.citymalls.preference.SharedPreferencesManager;
import com.javinindia.citymalls.recyclerview.ViewPagerAdapter;
import com.javinindia.citymalls.utility.Utility;

/**
 * Created by Ashish on 11-11-2016.
 */
public class FavoriteTabBarFragment extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
      //  viewPager.setCurrentItem(Constants.VIEW_PAGER_MALL_CURRENT_POSITION);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
    @Override
    protected int getFragmentLayout() {
        return R.layout.favorite_tab_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FavoriteMallFragment(), "Malls");
        adapter.addFragment(new FavoriteStoreFragment(), "Stores");
        adapter.addFragment(new FavoriteOfferFragment(), "Offers");
        viewPager.setAdapter(adapter);

    }
}
