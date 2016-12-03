package com.javinindia.citymalls.recyclerview;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.apiparsing.CountryModel;
import com.javinindia.citymalls.apiparsing.mallListParsing.MallDetail;
import com.javinindia.citymalls.apiparsing.offerListparsing.DetailsList;
import com.javinindia.citymalls.font.FontAsapBoldSingleTonClass;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.fragments.MallsFragmet;
import com.javinindia.citymalls.utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ashish on 14-09-2016.
 */
public class MallAdapter extends RecyclerView.Adapter<MallAdapter.ViewHolder> {
    List<MallDetail> list;
    Context context;
    MyClickListener myClickListener;
    ArrayList<MallDetail> shopCategoryListArrayList;

    public MallAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MallDetail> list) {
        this.list = list;
        this.shopCategoryListArrayList = new ArrayList<>();
        this.shopCategoryListArrayList.addAll(list);
    }

    public List<MallDetail> getData() {
        return list;
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
        final MallDetail mallDetail = (MallDetail) list.get(position);

        final ArrayList<String> data = new ArrayList<>();
        if (!TextUtils.isEmpty(mallDetail.getMallAddress().trim())){
            String mallAddress = mallDetail.getMallAddress().trim();
            data.add(mallAddress);
        }
        if (!TextUtils.isEmpty(mallDetail.getMallLandmark().trim())){
            String mallLandmark = mallDetail.getMallLandmark().trim();
            data.add(mallLandmark);
        }
        if (!TextUtils.isEmpty(mallDetail.getCity().trim())){
            String city = mallDetail.getCity().trim();
            data.add(city);
        }
        if (!TextUtils.isEmpty(mallDetail.getState().trim())){
            String state = mallDetail.getState().trim();
            data.add(state);
        }
        if (!TextUtils.isEmpty(mallDetail.getPinCode().trim())){
            String pinCode = mallDetail.getPinCode().trim();
            data.add(pinCode);
        }

        if (!TextUtils.isEmpty(mallDetail.getMallName().trim())) {
            String mallName = mallDetail.getMallName().trim();
            viewHolder.txtMallName.setText(Html.fromHtml(mallName));
        }
        if (data.size()>0){
            String str = Arrays.toString(data.toArray());
            String test = str.replaceAll("[\\[\\](){}]", "");
            viewHolder.txtAddress.setText(Html.fromHtml(test));
        }else {
            viewHolder.txtAddress.setText("Address: Not found");
        }

        if (!TextUtils.isEmpty(mallDetail.getOpenTime().trim()) && !TextUtils.isEmpty(mallDetail.getCloseTime().trim())) {
            String openTime = mallDetail.getOpenTime().trim();
            String closeTime = mallDetail.getCloseTime().trim();
            viewHolder.txtTiming.setText(Html.fromHtml("Timing:" + "\t" + "<font color=#000000>" + openTime + "-" + closeTime + "</font>"));
        }else {
            viewHolder.txtTiming.setText(Html.fromHtml("Timing:" + "\t" + "<font color=#000000>" + "Not found" + "</font>"));
        }
        if (mallDetail.getDistance() != 0.0) {
            double distance = mallDetail.getDistance();
            viewHolder.txtDistance.setText(Html.fromHtml("Distance:" + "\t" + "<font color=#000000>" + distance + " km" + "</font>"));
        } else {
            viewHolder.txtDistance.setText(Html.fromHtml("Distance:" + "\t" + "<font color=#000000>" + "Enable your Location" + "</font>"));
        }
        if (!TextUtils.isEmpty(mallDetail.getRating().trim()) && !mallDetail.getRating().trim().equals("0")) {
            String rating = mallDetail.getRating().trim();
            viewHolder.ratingBar.setRating(Float.parseFloat(rating));
            viewHolder.txtRating.setText("Rating: " + rating + "/5");
        }else {
            viewHolder.ratingBar.setRating(Float.parseFloat("0"));
            viewHolder.txtRating.setText("Rating: 0/5");
        }

        if (!TextUtils.isEmpty(mallDetail.getMallPic().trim())) {
            String mallPic = mallDetail.getMallPic().trim();
            Utility.imageLoadGlideLibrary(context, viewHolder.progressBar, viewHolder.imgLogo, mallPic);
        }
        if (mallDetail.getFavStatus() == 1) {
            viewHolder.chkImage.setChecked(true);
        } else {
            viewHolder.chkImage.setChecked(false);
        }
        if (mallDetail.getOfferCount() != 0) {
            int offerCount = mallDetail.getOfferCount();
            if (offerCount==1){
                viewHolder.txtOffers.setText(offerCount + " offer");
            }else {
                viewHolder.txtOffers.setText(offerCount + " offers");
            }
        } else {
            viewHolder.txtOffers.setText("No offer");
        }


        viewHolder.chkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onFavourite(position, mallDetail);
            }
        });


        viewHolder.txtMallName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onMallNameClick(position, mallDetail);
            }
        });

        viewHolder.btnViewOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(position, mallDetail);
            }
        });

        viewHolder.btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onDirectionClick(position,mallDetail);
            }
        });

        viewHolder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onAddressClick(position,mallDetail);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtMallName, txtRating, txtAddress, txtTiming, txtDistance, txtOffers;
        public RatingBar ratingBar;
        public CardView rlMain;
        public CheckBox chkImage;
        public AppCompatButton btnDirection, btnViewOffers;
        private ImageView imgLogo;
        public ProgressBar progressBar;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgLogo = (ImageView) itemLayoutView.findViewById(R.id.imgLogo);
            progressBar = (ProgressBar) itemLayoutView.findViewById(R.id.progress);
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
            rlMain = (CardView) itemLayoutView.findViewById(R.id.rlMain);
            chkImage = (CheckBox) itemLayoutView.findViewById(R.id.chkImageMall);
            btnDirection = (AppCompatButton) itemLayoutView.findViewById(R.id.btnDirection);
            btnDirection.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            btnViewOffers = (AppCompatButton) itemLayoutView.findViewById(R.id.btnViewOffers);
            btnViewOffers.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());

        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onItemClick(int position, MallDetail model);

        void onFavourite(int position, MallDetail modal);

        void onMallNameClick(int position, MallDetail modal);

        void onDirectionClick(int position, MallDetail modal);

        void onAddressClick(int position, MallDetail modal);
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(shopCategoryListArrayList);
        } else {
            for (MallDetail thankFulDetail : shopCategoryListArrayList) {
                if (thankFulDetail.getMallName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    list.add(thankFulDetail);
                }
            }
        }
        notifyDataSetChanged();
    }
}