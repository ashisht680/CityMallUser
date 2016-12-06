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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.apiparsing.CountryModel;
import com.javinindia.citymalls.apiparsing.storeInMallParsing.ShopData;
import com.javinindia.citymalls.font.FontAsapBoldSingleTonClass;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ashish on 10-10-2016.
 */
public class MallStoreAdaptar extends RecyclerView.Adapter<MallStoreAdaptar.ViewHolder> {

    List<ShopData> list;
    Context context;
    MyClickListener myClickListener;
    ArrayList<ShopData> shopCategoryListArrayList;

    public MallStoreAdaptar(Context context) {
        this.context = context;
    }

    public void setData(List<ShopData> list) {
        this.list = list;
        this.shopCategoryListArrayList = new ArrayList<>();
        this.shopCategoryListArrayList.addAll(list);
    }

    public List<ShopData> getData() {
        return list;
    }

    @Override
    public MallStoreAdaptar.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final ShopData shopData = (ShopData) list.get(position);
        final ArrayList<String> data = new ArrayList<>();

        if (!TextUtils.isEmpty(shopData.getShopNo().trim())){
            String shopNumber = shopData.getShopNo().trim();
            data.add(shopNumber);
        }
        if (!TextUtils.isEmpty(shopData.getFloor().trim())){
            String shopFloor = shopData.getFloor().trim();
            data.add(shopFloor+" floor");
        }
        if (!TextUtils.isEmpty(shopData.getAddress().trim())){
            String shopAddress = shopData.getAddress().trim();
            data.add(shopAddress);
        }
        if (!TextUtils.isEmpty(shopData.getCity().trim())){
            String shopCity = shopData.getCity().trim();
            data.add(shopCity);
        }
        if (!TextUtils.isEmpty(shopData.getState().trim())){
            String shopState = shopData.getState().trim();
            data.add(shopState);
        }


        if(!TextUtils.isEmpty(shopData.getStoreName().trim())){
            String shopName = shopData.getStoreName().trim();
            viewHolder.txtShopName.setText(Html.fromHtml(shopName));
        }
        if(!TextUtils.isEmpty(shopData.getMallName().trim())){
            String mallName = shopData.getMallName().trim();
            viewHolder.txtMallName.setText(Html.fromHtml(mallName));
        }else {
            viewHolder.txtMallName.setText("Mall: Not found");
        }
        if (data.size()>0){
            String str = Arrays.toString(data.toArray());
            String test = str.replaceAll("[\\[\\](){}]", "");
            viewHolder.txtAddress.setText(Html.fromHtml(test));
        }else {
            viewHolder.txtAddress.setText("Address: Not found");
        }
        if(!TextUtils.isEmpty(shopData.getOpenTime().trim()) && !TextUtils.isEmpty(shopData.getCloseTime().trim())){
            String shopOpenTime =shopData.getOpenTime().trim();
            String shopCloseTime = shopData.getCloseTime().trim();
            viewHolder.txtTimingStore.setText(Html.fromHtml("Timings:" + "\t" + "<font color=#000000>" + shopOpenTime + "-" + shopCloseTime + "</font>"));
        }else {
            viewHolder.txtTimingStore.setText(Html.fromHtml("Timings:" + "\t" + "<font color=#000000>" + "Not found" + "</font>"));
        }
        if(shopData.getShopOfferCount()!=0){
            int totalOffer = shopData.getShopOfferCount();
            if (totalOffer==1){
                viewHolder.txtOfferAmount.setText(totalOffer+" Offer");
            }else {
                viewHolder.txtOfferAmount.setText(totalOffer+" Offers");
            }
        }else {
            viewHolder.txtOfferAmount.setText("No Offers");
        }
        if (!TextUtils.isEmpty(shopData.getProfilepic().trim())){
            String shopBanner = shopData.getProfilepic().trim();
            Utility.imageLoadGlideLibrary(context, viewHolder.progressBar, viewHolder.imgShopLogoStore, shopBanner);
        }else {
            viewHolder.imgShopLogoStore.setImageResource(R.drawable.no_image_icon);
        }

        viewHolder.txtViewAllOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(position,shopData);
            }
        });

        if (shopData.getFavStatus() == 1) {
            viewHolder.chkImageOffer.setChecked(true);
        } else {
            viewHolder.chkImageOffer.setChecked(false);
        }

        viewHolder.chkImageOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onShopFavClick(position ,shopData);
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtShopName,txtTimingStore,txtOfferAmount,txtViewAllOffers,txtMallName,txtAddress;
        public CardView rlMain;
        public CheckBox chkImageOffer;
        public ImageView imgShopLogoStore;
        public ProgressBar progressBar;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            rlMain = (CardView)itemLayoutView.findViewById(R.id.rlMain);
            progressBar = (ProgressBar) itemLayoutView.findViewById(R.id.progress);
            txtShopName = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtShopName);
            txtShopName.setTypeface(FontAsapBoldSingleTonClass.getInstance(context).getTypeFace());
            txtTimingStore = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtTimingStore);
            txtTimingStore.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            txtOfferAmount = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtOfferAmount);
            txtOfferAmount.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
           // txtOfferCategory = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtOfferCategory);
            txtViewAllOffers = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtViewAllOffers);
            txtViewAllOffers.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            chkImageOffer = (CheckBox) itemLayoutView.findViewById(R.id.chkImageOffer);
            imgShopLogoStore = (ImageView)itemLayoutView.findViewById(R.id.imgShopLogoStore);
            txtMallName = (AppCompatTextView)itemLayoutView.findViewById(R.id.txtMallName);
            txtMallName.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
            txtAddress = (AppCompatTextView)itemLayoutView.findViewById(R.id.txtAddress);
            txtAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(context).getTypeFace());
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onItemClick(int position, ShopData model);

        void onShopFavClick(int position ,ShopData model);
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
            for (ShopData thankFulDetail : shopCategoryListArrayList) {
                if (thankFulDetail.getStoreName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    list.add(thankFulDetail);
                }
            }
        }
        notifyDataSetChanged();
    }
}