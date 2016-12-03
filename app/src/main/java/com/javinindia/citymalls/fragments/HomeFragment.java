package com.javinindia.citymalls.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.recyclerview.ViewPagerAdapter;

/**
 * Created by Ashish on 08-09-2016.
 */
public class HomeFragment extends BaseFragment {
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

        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        return view;
    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tab, null);
        tabOne.setText("Favourate\nmalls");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.favourite_malls, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Offers");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.offers, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

     /*   TextView tabThree = (TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tab, null);
        tabThree.setText("Event");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.events, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);*/

        TextView tabFour = (TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tab, null);
        tabFour.setText("Search");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.offers, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabFour);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.tab_bar_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onResume() {
        Log.e("resume","res");
        activity.getSupportActionBar().show();
        super.onResume();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MallsFragmet(), "Favorite Malls");
        adapter.addFragment(new OffersFragment(), "Offers");
       // adapter.addFragment(new EventFragment(), "Event");
        adapter.addFragment(new SearchTabFragment(), "Search");
        viewPager.setAdapter(adapter);

    }

}
