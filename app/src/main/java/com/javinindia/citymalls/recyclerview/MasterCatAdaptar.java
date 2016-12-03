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

import com.javinindia.citymalls.R;
import com.javinindia.citymalls.apiparsing.mallListParsing.MallDetail;
import com.javinindia.citymalls.apiparsing.masterCategoryParsing.CategoryList;
import com.javinindia.citymalls.font.FontAsapBoldSingleTonClass;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
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
public class MasterCatAdaptar extends RecyclerView.Adapter<MasterCatAdaptar.ViewHolder> {
    List<CategoryList> list;
    Context context;
    MyClickListener myClickListener;
    ArrayList<CategoryList> shopCategoryListArrayList;

    public MasterCatAdaptar(Context context) {
        this.context = context;
    }

    public void setData(List<CategoryList> list) {
        this.list = list;
        this.shopCategoryListArrayList = new ArrayList<>();
        this.shopCategoryListArrayList.addAll(list);
    }

    public List<CategoryList> getData() {
        return list;
    }

    @Override
    public MasterCatAdaptar.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cat_item_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final CategoryList categoryList = (CategoryList) list.get(position);

        if (!TextUtils.isEmpty(categoryList.getCategory().trim())) {
            String catName = categoryList.getCategory().trim();
            viewHolder.txtCat.setText(Html.fromHtml(catName));
        }
        if (!TextUtils.isEmpty(categoryList.getCat_pic().trim())) {
            String pic = categoryList.getCat_pic().trim();
            Picasso.with(context).load(pic).transform(new CircleTransform()).into(viewHolder.imgCat);
        }else {
            Picasso.with(context).load(R.drawable.no_image_icon).transform(new CircleTransform()).into(viewHolder.imgCat);
        }

        viewHolder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(position,categoryList);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView txtCat;
        public CardView rlMain;
        private ImageView imgCat;
        public ProgressBar progressBar;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgCat = (ImageView) itemLayoutView.findViewById(R.id.imgCat);
            progressBar = (ProgressBar) itemLayoutView.findViewById(R.id.progress);
            txtCat = (AppCompatTextView) itemLayoutView.findViewById(R.id.txtCat);
            txtCat.setTypeface(FontAsapBoldSingleTonClass.getInstance(context).getTypeFace());
            rlMain = (CardView) itemLayoutView.findViewById(R.id.rlMain);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public interface MyClickListener {
        void onItemClick(int position, CategoryList model);

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
            for (CategoryList thankFulDetail : shopCategoryListArrayList) {
                if (thankFulDetail.getCategory().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    list.add(thankFulDetail);
                }
            }
        }
        notifyDataSetChanged();
    }
}