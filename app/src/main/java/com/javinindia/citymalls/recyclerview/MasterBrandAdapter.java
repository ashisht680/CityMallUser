package com.javinindia.citymalls.recyclerview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.apiparsing.masterCategoryParsing.CategoryList;
import com.javinindia.citymalls.apiparsing.materBrandParsing.BrandList;
import com.javinindia.citymalls.font.FontAsapBoldSingleTonClass;
import com.javinindia.citymalls.picasso.CircleTransform;
import com.javinindia.citymalls.preference.SharedPreferencesManager;
import com.javinindia.citymalls.utility.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ashish on 15-11-2016.
 */
public class MasterBrandAdapter extends RecyclerView.Adapter<MasterBrandAdapter.ViewHolder> {
    List<BrandList> list;
    Context context;
    MyClickListener myClickListener;
    ArrayList<BrandList> shopCategoryListArrayList;

    public MasterBrandAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<BrandList> list) {
        this.list = list;
        this.shopCategoryListArrayList = new ArrayList<>();
        this.shopCategoryListArrayList.addAll(list);
    }

    public List<BrandList> getData() {
        return list;
    }

    @Override
    public MasterBrandAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.brand_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final BrandList brandList = (BrandList) list.get(position);

        if (!TextUtils.isEmpty(brandList.getName().trim())) {
            String catName = brandList.getName().trim();
            viewHolder.txtBrand.setText(Html.fromHtml(catName));
        }
        if (!TextUtils.isEmpty(brandList.getLogo().trim())) {
            String pic = brandList.getLogo().trim();
             Utility.imageLoadGlideLibrary(context, viewHolder.progressBar, viewHolder.imgBrand, pic);
        }else {
            viewHolder.imgBrand.setImageResource(R.drawable.no_image_icon);
        }

        viewHolder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(position,brandList);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtBrand;
        public CardView rlMain;
        private ImageView imgBrand;
        public ProgressBar progressBar;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgBrand = (ImageView) itemLayoutView.findViewById(R.id.imgBrand);
            progressBar = (ProgressBar) itemLayoutView.findViewById(R.id.progress);
            txtBrand = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtBrand);
            txtBrand.setTypeface(FontAsapBoldSingleTonClass.getInstance(context).getTypeFace());
            rlMain = (CardView) itemLayoutView.findViewById(R.id.rlMain);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onItemClick(int position, BrandList model);

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
            for (BrandList thankFulDetail : shopCategoryListArrayList) {
                if (thankFulDetail.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    list.add(thankFulDetail);
                }
            }
        }
        notifyDataSetChanged();
    }
}
