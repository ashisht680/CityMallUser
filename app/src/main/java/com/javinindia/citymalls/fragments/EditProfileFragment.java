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
import android.widget.RelativeLayout;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;

/**
 * Created by Ashish on 12-10-2016.
 */
public class EditProfileFragment extends BaseFragment implements View.OnClickListener {
    ImageView imgProfilePic;
    AppCompatEditText etPersonName,etEmailAddress,etMobile,etGender,etDOB,etLocation,etfavourateMall1,etfavourateMall2,etfavourateMall3,
            etfavourateMall4,etfavourateMall5;
    RelativeLayout rlUpadteImg;
    AppCompatTextView txtUpdate,txtEmailHd,txtMobileHd,txtGenderHd,txtDOBHd,txtLocationHd,txtfavourateMall1Hd,
            txtfavourateMall2Hd,txtfavourateMall3Hd,txtfavourateMall4Hd,txtfavourateMall5Hd;

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
        rlUpadteImg = (RelativeLayout)view.findViewById(R.id.rlUpadteImg);
        etPersonName = (AppCompatEditText)view.findViewById(R.id.etPersonName);
        etPersonName.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEmailHd = (AppCompatTextView)view.findViewById(R.id.txtEmailHd);
        txtEmailHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etEmailAddress = (AppCompatEditText)view.findViewById(R.id.etEmailAddress);
        etEmailAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMobileHd = (AppCompatTextView)view.findViewById(R.id.txtMobileHd);
        txtMobileHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etMobile = (AppCompatEditText)view.findViewById(R.id.etMobile);
        etMobile.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGenderHd = (AppCompatTextView)view.findViewById(R.id.txtGenderHd);
        txtGenderHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etGender = (AppCompatEditText)view.findViewById(R.id.etGender);
        etGender.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtDOBHd = (AppCompatTextView)view.findViewById(R.id.txtDOBHd);
        txtDOBHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etDOB = (AppCompatEditText)view.findViewById(R.id.etDOB);
        etDOB.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtLocationHd = (AppCompatTextView)view.findViewById(R.id.txtLocationHd);
        txtLocationHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etLocation = (AppCompatEditText)view.findViewById(R.id.etLocation);
        etLocation.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall1Hd = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall1Hd);
        txtfavourateMall1Hd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etfavourateMall1 = (AppCompatEditText)view.findViewById(R.id.etfavourateMall1);
        etfavourateMall1.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall2Hd = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall2Hd);
        txtfavourateMall2Hd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etfavourateMall2 = (AppCompatEditText)view.findViewById(R.id.etfavourateMall2);
        etfavourateMall2.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall3Hd = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall3Hd);
        txtfavourateMall3Hd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etfavourateMall3 = (AppCompatEditText)view.findViewById(R.id.etfavourateMall3);
        etfavourateMall3.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall4Hd = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall4Hd);
        txtfavourateMall4Hd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etfavourateMall4 = (AppCompatEditText)view.findViewById(R.id.etfavourateMall4);
        etfavourateMall4.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtfavourateMall5Hd = (AppCompatTextView)view.findViewById(R.id.txtfavourateMall5Hd);
        txtfavourateMall5Hd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etfavourateMall5 = (AppCompatEditText)view.findViewById(R.id.etfavourateMall5);
        etfavourateMall5.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtUpdate = (AppCompatTextView) view.findViewById(R.id.txtUpdate);
        txtUpdate.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        txtUpdate.setOnClickListener(this);
        rlUpadteImg.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.edit_profile_layout;
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
            case R.id.txtUpdate:
               activity.onBackPressed();
                break;
            case R.id.rlUpadteImg:

                break;
        }
    }
}