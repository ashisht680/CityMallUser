package com.javinindia.citymalls.recyclerview;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.apiparsing.CountryModel;
import com.javinindia.citymalls.font.FontAsapBoldSingleTonClass;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ashish on 14-09-2016.
 */
public class MallAdapter extends RecyclerView.Adapter<MallAdapter.ViewHolder> {
    List<CountryModel> list;
    Context context;
    MyClickListener myClickListener;
    ArrayList<CountryModel> countryModelArrayList;


    public MallAdapter(List<CountryModel> mCountryModel,Context context) {
        this.list = mCountryModel;
        this.countryModelArrayList = new ArrayList<>();
        this.countryModelArrayList.addAll(mCountryModel);
        this.context = context;
    }


    @Override
    public MallAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mall_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final CountryModel countryModel = (CountryModel) list.get(position);
        String icoCode = countryModel.getisoCode();
        final String name = countryModel.getName();

        viewHolder.txtTiming.setText(Html.fromHtml("Timing"+"\t"+"<font color=#000000>"+"10:00 AM - 11:00 PM"+"</font>"));
        viewHolder.txtDistance.setText(Html.fromHtml("Distance"+"\t"+"<font color=#000000>"+"5.5 km"+"</font>"));
        viewHolder.ratingBar.setRating(Float.parseFloat("2.0"));

        viewHolder.chkImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(viewHolder.chkImage.isChecked()){
                    viewHolder.chkImage.setChecked(true);
                }else {
                    viewHolder.chkImage.setChecked(false);
                }
            }
        });


        viewHolder.txtMallName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(position, countryModel);
            }
        });
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(countryModelArrayList);
        } else {
            for (CountryModel model : countryModelArrayList) {
                if (model.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    list.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtMallName,txtRating,txtAddress,txtTiming,txtDistance,txtOffers;
        public RatingBar ratingBar;
        public RelativeLayout rlMain;
        public CheckBox chkImage;
        public AppCompatButton btnDirection,btnViewOffers;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtMallName = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtMallName);
            txtMallName.setTypeface(FontAsapBoldSingleTonClass.getInstance(context).getTypeFace());
            txtRating = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtRating);
            txtRating.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            txtAddress = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtAddress);
            txtAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            txtTiming = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtTiming);
            txtTiming.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            txtDistance = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtDistance);
            txtDistance.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            txtOffers = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtOffers);
            txtOffers.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            ratingBar = (RatingBar) itemLayoutView.findViewById(R.id.ratingBar);
            rlMain = (RelativeLayout)itemLayoutView.findViewById(R.id.rlMain);
            chkImage = (CheckBox) itemLayoutView.findViewById(R.id.chkImage);
            btnDirection = (AppCompatButton)itemLayoutView.findViewById(R.id.btnDirection);
            btnDirection.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            btnViewOffers = (AppCompatButton)itemLayoutView.findViewById(R.id.btnViewOffers);
            btnViewOffers.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());

        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onItemClick(int position, CountryModel model);
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
}