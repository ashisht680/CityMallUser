package com.javinindia.citymalls.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.activity.LoginActivity;
import com.javinindia.citymalls.activity.NavigationActivity;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.picasso.CircleTransform;
import com.javinindia.citymalls.preference.SharedPreferencesManager;
import com.javinindia.citymalls.recyclerview.CustomSpinnerAdater;
import com.javinindia.citymalls.recyclerview.ViewPagerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ashish on 08-09-2016.
 */
public class HomeFragment extends BaseFragment implements NavigationAboutFragment.OnCallBackRefreshListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Spinner spinner_nav;
    int iCurrentSelection = 0;
    TextView txtCityName;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        setupDrawerLayout(view);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        return view;
    }

    private void initToolbar(View view) {
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        final ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(null);

        txtCityName = (TextView) view.findViewById(R.id.txtCityName);
        txtCityName.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        if (!TextUtils.isEmpty(SharedPreferencesManager.getCity(activity)) && !SharedPreferencesManager.getCity(activity).equals(null)) {
            txtCityName.setText(SharedPreferencesManager.getCity(activity));
        }
        txtCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCityName.setVisibility(View.GONE);
                spinner_nav.setVisibility(View.VISIBLE);
                spinner_nav.performClick();
                addItemsToSpinner();
            }
        });
        spinner_nav = (Spinner) view.findViewById(R.id.spinner_nav);
    }

    // add items into spinner dynamically
    public void addItemsToSpinner() {

        ArrayList<String> list = new ArrayList<String>();
        list.add("City");
        list.add("Delhi NCR");
        list.add("Mumbai");
        list.add("Kolkata");
        list.add("Bengaluru");
        list.add("Chennai");
        list.add("Hyderabad");

        // Custom ArrayAdapter with spinner item layout to set popup background

        CustomSpinnerAdater spinAdapter = new CustomSpinnerAdater(
                activity, list);


        spinner_nav.setAdapter(spinAdapter);
        // iCurrentSelection = spinner_nav.getSelectedItemPosition();
        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {

                if (position != 0) {
                    // On selecting a spinner item
                    String item = adapter.getItemAtPosition(position).toString();
                    if (!item.equals(SharedPreferencesManager.getCity(activity))) {
                        // Showing selected spinner item
                        Toast.makeText(activity, "Selected  : " + item,
                                Toast.LENGTH_LONG).show();
                        SharedPreferencesManager.setCity(activity, item);
                        Intent refresh = new Intent(activity, NavigationActivity.class);
                        startActivity(refresh);//Start the same Activity
                        activity.finish();
                        txtCityName.setText(item);
                        spinner_nav.setVisibility(View.GONE);
                        txtCityName.setVisibility(View.VISIBLE);
                    } else {
                        spinner_nav.setVisibility(View.GONE);
                        txtCityName.setVisibility(View.VISIBLE);
                    }
                } else {
                    //  spinner_nav.setVisibility(View.GONE);
                    //  txtCityName.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });

        //  spinner_nav.setSelection(1);

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

    private void setupDrawerLayout(View view) {
        drawerLayout = (DrawerLayout) view.findViewById(R.id.DrawerLayout);
        navigationView = (NavigationView) view.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                displayView(menuItem.getTitle());
                drawerLayout.closeDrawers();
                return true;
            }
        });

        final ImageView avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);
        final TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        final TextView txtOwnerName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtOwnerName);
        if (!TextUtils.isEmpty(SharedPreferencesManager.getProfileImage(activity))) {
            Picasso.with(activity).load(SharedPreferencesManager.getProfileImage(activity)).transform(new CircleTransform()).into(avatar);
        } else {
            Picasso.with(activity).load(R.drawable.no_image_icon).transform(new CircleTransform()).into(avatar);
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUsername(activity))) {
            txtOwnerName.setText(SharedPreferencesManager.getUsername(activity));
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getEmail(activity))) {
            email.setText(SharedPreferencesManager.getEmail(activity));
        }
    }

    private void displayView(CharSequence title) {
        if (title.equals("Home")) {
            drawerLayout.closeDrawers();
            Intent refresh = new Intent(activity, NavigationActivity.class);
            startActivity(refresh);//Start the same Activity
            activity.finish();
        } else if (title.equals("Profile")) {
            drawerLayout.closeDrawers();
            NavigationAboutFragment fragment = new NavigationAboutFragment();
            fragment.setMyCallUpdateProfileListener(this);
            callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.navigationContainer);
        } else if (title.equals("Favorite")) {
            drawerLayout.closeDrawers();
            BaseFragment fragment = new FavoriteTabBarFragment();
            callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.navigationContainer);
        } else if (title.equals("More")) {
            drawerLayout.closeDrawers();
            Toast.makeText(activity, title, Toast.LENGTH_LONG).show();

        } else if (title.equals("About App")) {
            drawerLayout.closeDrawers();
            BaseFragment fragment = new AboutAppFragments();
            callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.navigationContainer);
        } else if (title.equals("Settings")) {
            drawerLayout.closeDrawers();
            Toast.makeText(activity, title, Toast.LENGTH_LONG).show();

        } else if (title.equals("Logout")) {
            drawerLayout.closeDrawers();
            Toast.makeText(activity, title, Toast.LENGTH_LONG).show();
            dialogBox();
        }

    }

    public void dialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setMessage("Thanks for visiting City mall! Be back soon!");
        alertDialogBuilder.setPositiveButton("Ok!",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(activity, "Logout", Toast.LENGTH_LONG).show();
                        SharedPreferencesManager.setUserID(activity, null);
                        SharedPreferencesManager.setUsername(activity, null);
                        SharedPreferencesManager.setPassword(activity, null);
                        SharedPreferencesManager.setEmail(activity, null);
                        SharedPreferencesManager.setLocation(activity, null);
                        SharedPreferencesManager.setProfileImage(activity, null);
                        Intent refresh = new Intent(activity, LoginActivity.class);
                        startActivity(refresh);//Start the same Activity
                        activity.finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("Take me back",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(Gravity.LEFT);
                return true;
            case R.id.action_change_password:
                ChangePasswordFragment fragment = new ChangePasswordFragment();
                callFragmentMethod(fragment, this.getClass().getSimpleName(), R.id.navigationContainer);
                drawerLayout.closeDrawers();
                return true;
            case R.id.action_feedBack:
                FeedbackFragment fragment1 = new FeedbackFragment();
                callFragmentMethod(fragment1, this.getClass().getSimpleName(), R.id.navigationContainer);
                drawerLayout.closeDrawers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        activity.getMenuInflater().inflate(R.menu.navigation_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        //Favorite Malls
        adapter.addFragment(new MallsFragmet(), "Favorite Malls");
        adapter.addFragment(new OffersFragment(), "Offers");
        // adapter.addFragment(new EventFragment(), "Event");
        adapter.addFragment(new SearchTabFragment(), "Search");
        viewPager.setAdapter(adapter);

    }

    @Override
    public void OnCallBackRefresh(String name, String email, String pic) {
        final ImageView avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);
        final TextView txtEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        final TextView txtOwnerName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txtOwnerName);
        if (!TextUtils.isEmpty(SharedPreferencesManager.getProfileImage(activity))) {
            Picasso.with(activity).load(SharedPreferencesManager.getProfileImage(activity)).transform(new CircleTransform()).into(avatar);
        } else {
            // AVATAR_URL = "http://lorempixel.com/200/200/people/1/";
            Picasso.with(activity).load(R.drawable.no_image_icon).transform(new CircleTransform()).into(avatar);
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getUsername(activity))) {
            txtOwnerName.setText(SharedPreferencesManager.getUsername(activity));
        }
        if (!TextUtils.isEmpty(SharedPreferencesManager.getEmail(activity))) {
            txtEmail.setText(SharedPreferencesManager.getEmail(activity));
        }
    }

    @Override
    public void onBackPressButton(Activity activity) {
        super.onBackPressButton(activity);
        drawerLayout.closeDrawers();
    }
}
