package com.javinindia.citymalls.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.javinindia.citymalls.R;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;

/**
 * Created by Ashish on 10-10-2016.
 */
public class SearchStoreFragment extends BaseFragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initialize(view);
        // setRequest();
        return view;
    }

  /*  @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }*/

    private void initialize(View view) {
        LinearLayout llSearch = (LinearLayout)view.findViewById(R.id.llSearch);
        AppCompatEditText etSearch = (AppCompatEditText)view.findViewById(R.id.etSearch);
        ImageView imgSearch  =(ImageView)view.findViewById(R.id.imgSearch);
        AppCompatTextView txtTitle = (AppCompatTextView)view.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtCloths = (AppCompatTextView)view.findViewById(R.id.txtCloths);
        txtCloths.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtHome = (AppCompatTextView)view.findViewById(R.id.txtHome);
        txtHome.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtKitchen = (AppCompatTextView)view.findViewById(R.id.txtKitchen);
        txtKitchen.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtLifeStyle = (AppCompatTextView)view.findViewById(R.id.txtLifeStyle);
        txtLifeStyle.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtFashion = (AppCompatTextView)view.findViewById(R.id.txtFashion);
        txtFashion.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        AppCompatTextView txtFood = (AppCompatTextView)view.findViewById(R.id.txtFood);
        txtFood.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        llSearch.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.search_store_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSearch:
                BaseFragment fragment1 = new UneversalSearchFragment();
                callFragmentMethod(fragment1, this.getClass().getSimpleName(),R.id.navigationContainer);
                break;
            case R.id.etSearch:
                BaseFragment fragment2 = new UneversalSearchFragment();
                callFragmentMethod(fragment2, this.getClass().getSimpleName(),R.id.navigationContainer);
                break;
            case R.id.imgSearch:
                BaseFragment fragment3 = new UneversalSearchFragment();
                callFragmentMethod(fragment3, this.getClass().getSimpleName(),R.id.navigationContainer);
                break;
        }
    }
}
