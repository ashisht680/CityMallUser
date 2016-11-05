package com.javinindia.citymalls.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.javinindia.citymalls.R;

import java.util.Random;

/**
 * Created by Ashish on 12-10-2016.
 */
public class Test extends BaseFragment {
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
        LinearLayout lnrImages = (LinearLayout)view.findViewById(R.id.lnrImages);
        Random rnd = new Random();
        int prevTextViewId = 0;
        for(int i = 0; i < 20; i++)
        {
            final TextView textView = new TextView(activity);
            textView.setText("Text ashish "+i);
            textView.setTextColor(rnd.nextInt() | 0xff000000);
            textView.setBackgroundResource(R.drawable.button_border_black_fill_white);
            textView.setPadding(20,20,20,20);

            int curTextViewId = prevTextViewId + 1;
            textView.setId(curTextViewId);
            final LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(0, 0, 10, 0);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false);


           // params.addRule(RelativeLayout.RIGHT_OF, prevTextViewId);
            textView.setLayoutParams(params);

            prevTextViewId = curTextViewId;
            lnrImages.addView(textView, params);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.test_layout;
    }

    @Override
    public int getToolbarMenu() {
        return 0;
    }

    @Override
    public void onNetworkConnected() {

    }
}
