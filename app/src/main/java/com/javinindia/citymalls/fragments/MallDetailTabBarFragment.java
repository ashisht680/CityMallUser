package com.javinindia.citymalls.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.recyclerview.ViewPagerAdapter;

/**
 * Created by Ashish on 30-09-2016.
 */
public class MallDetailTabBarFragment extends BaseFragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity.getSupportActionBar().show();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        //   viewPager.setCurrentItem(Constants.VIEW_PAGER_CURRENT_POSITION);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
    @Override
    protected int getFragmentLayout() {
        return R.layout.mall_detail_tab_layout;
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
        adapter.addFragment(new StoreOffersFragment(), "Stores");
        adapter.addFragment(new StoreOffersFragment(), "Offers");
        adapter.addFragment(new StoreEventsFragment(), "Events");
        viewPager.setAdapter(adapter);

    }
}
