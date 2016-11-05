package com.javinindia.citymalls.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;

/**
 * Created by Ashish on 09-09-2016.
 */
public class NavigationAboutFragment extends BaseFragment implements View.OnClickListener {
    ImageView imgProfilePic;
    AppCompatTextView personName,txtEmailHd,txtEmailAddress,txtMobileHd,txtMobile,txtGenderHd,txtGender,txtDOBHd,txtDOB,
            txtLocationHd,txtLocation,txtfavourateMall1Hd,txtfavourateMall1,txtfavourateMall2Hd,txtfavourateMall2,
            txtfavourateMall3Hd,txtfavourateMall3,txtfavourateMall4Hd,txtfavourateMall4,txtfavourateMall5Hd,txtfavourateMall5;
    RelativeLayout rlEdit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initialize(view);
        // setRequest();
        return view;
    }

    private void initialize(View view) {
        imgProfilePic = (ImageView)view.findViewById(R.id.imgProfilePic);
        rlEdit = (RelativeLayout)view.findViewById(R.id.rlEdit);
        personName = (AppCompatTextView)view.findViewById(R.id.personName);
        personName.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEmailHd = (AppCompatTextView)view.findViewById(R.id.txtEmailHd);
        txtEmailHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEmailAddress = (AppCompatTextView)view.findViewById(R.id.txtEmailAddress);
        txtEmailAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMobileHd = (AppCompatTextView)view.findViewById(R.id.txtMobileHd);
        txtMobileHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMobile = (AppCompatTextView)view.findViewById(R.id.txtMobile);
        txtMobile.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGenderHd = (AppCompatTextView)view.findViewById(R.id.txtGenderHd);
        txtGenderHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGender = (AppCompatTextView)view.findViewById(R.id.txtGender);
        txtGender.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtDOBHd = (AppCompatTextView)view.findViewById(R.id.txtDOBHd);
        txtDOBHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtDOB = (AppCompatTextView)view.findViewById(R.id.txtDOB);
        txtDOB.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtLocationHd = (AppCompatTextView)view.findViewById(R.id.txtLocationHd);
        txtLocationHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtLocation = (AppCompatTextView)view.findViewById(R.id.txtLocation);
        txtLocation.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall1Hd = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall1Hd);
        txtfavourateMall1Hd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall1 = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall1);
        txtfavourateMall1.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall2Hd = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall2Hd);
        txtfavourateMall2Hd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall2 = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall2);
        txtfavourateMall2.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall3Hd = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall3Hd);
        txtfavourateMall3Hd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall3 = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall3);
        txtfavourateMall3.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall4Hd = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall4Hd);
        txtfavourateMall4Hd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall4 = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall4);
        txtfavourateMall4.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall5Hd = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall5Hd);
        txtfavourateMall5Hd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall5 = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall5);
        txtfavourateMall5.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        rlEdit.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.profile_layout;
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
            case R.id.rlEdit:
                BaseFragment fragment1 = new EditProfileFragment();
                callFragmentMethod(fragment1, this.getClass().getSimpleName(),R.id.navigationContainer);
                break;
        }
    }
}
