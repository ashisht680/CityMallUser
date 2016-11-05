package com.javinindia.citymalls.recyclerview;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
public class OfferAdaptar extends RecyclerView.Adapter<OfferAdaptar.ViewHolder> {
    List<CountryModel> list;
    Context context;
    MyClickListener myClickListener;
    ArrayList<CountryModel> countryModelArrayList;


    public OfferAdaptar(List<CountryModel> mCountryModel) {
        this.list = mCountryModel;
        this.countryModelArrayList = new ArrayList<>();
        this.countryModelArrayList.addAll(mCountryModel);
    }


    @Override
    public OfferAdaptar.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final CountryModel countryModel = (CountryModel) list.get(position);
        String icoCode = countryModel.getisoCode();
        final String name = countryModel.getName();


     //   viewHolder.ratingBar.setRating(Float.parseFloat("2.0"));

  /*      viewHolder.chkImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(viewHolder.chkImage.isChecked()){
                    viewHolder.chkImage.setChecked(true);
                }else {
                    viewHolder.chkImage.setChecked(false);
                }
            }
        });*/


       /* viewHolder.txtShopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(position, countryModel);
            }
        });*/
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


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtShopName,txtAddress,txtTimingOffer,txtOfferAmount,txtOfferCategory;
       // public RatingBar ratingBar;
        public RelativeLayout rlMain;
        public CheckBox chkImageOffer;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            rlMain = (RelativeLayout)itemLayoutView.findViewById(R.id.rlMain);
            txtShopName = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtShopName);
         //   txtShopName.setTypeface(FontAsapBoldSingleTonClass.getInstance(context).getTypeFace());
            txtAddress = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtAddress);
         //   txtAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            txtTimingOffer = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtTimingOffer);
         //   txtTimingOffer.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            txtOfferAmount = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtOfferAmount);
         //   txtOfferAmount.setTypeface(FontAsapBoldSingleTonClass.getInstance(context).getTypeFace());
            txtOfferCategory = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtOfferCategory);
         //   txtOfferCategory.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
          //  ratingBar = (RatingBar) itemLayoutView.findViewById(R.id.ratingBar);
            rlMain = (RelativeLayout)itemLayoutView.findViewById(R.id.rlMain);
            chkImageOffer = (CheckBox) itemLayoutView.findViewById(R.id.chkImageOffer);
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