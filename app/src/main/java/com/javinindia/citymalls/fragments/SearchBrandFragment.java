package com.javinindia.citymalls.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.citymalls.R;
import com.javinindia.citymalls.apiparsing.materBrandParsing.BrandList;
import com.javinindia.citymalls.apiparsing.materBrandParsing.MasterBrandListResponse;
import com.javinindia.citymalls.constant.Constants;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.recyclerview.MasterBrandAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ashish on 15-11-2016.
 */
public class SearchBrandFragment extends BaseFragment implements View.OnClickListener, MasterBrandAdapter.MyClickListener {
    RecyclerView brandRecyclerView;
    private RequestQueue requestQueue;
    private MasterBrandAdapter adapter;
    private int startLimit = 0;
    private int countLimit = 20;
    private boolean loading = true;
    ArrayList<BrandList> arrayList = new ArrayList<BrandList>();
    AppCompatEditText etSearch;
    String brand_edit = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initialize(view);
        sendRequestOnCatListFeed(0, 20);
        return view;
    }

    private void sendRequestOnCatListFeed(final int startLimit, final int countLimit) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MASTER_BRAND_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MasterBrandListResponse responseparsing = new MasterBrandListResponse();
                        responseparsing.responseParseMethod(response);
                        Log.e("res brand", startLimit + "\t" + countLimit + "\t" + response);
                        if (responseparsing.getStatus() == 1) {
                            if (responseparsing.getBrandListArrayList().size() > 0) {
                                arrayList = responseparsing.getBrandListArrayList();
                                if (arrayList.size() > 0) {
                                    if (adapter.getData() != null && adapter.getData().size() > 0) {
                                        adapter.getData().addAll(arrayList);
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        adapter.setData(arrayList);
                                        adapter.notifyDataSetChanged();

                                    }
                                }
                            }

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //startlimit=0&countlimit=5
                Map<String, String> params = new HashMap<String, String>();
                params.put("startlimit", String.valueOf(startLimit));
                params.put("countlimit", String.valueOf(countLimit));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);


    }

    private void initialize(View view) {
        brandRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewBrand);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 3);
        brandRecyclerView.setLayoutManager(gridLayoutManager);
        brandRecyclerView.addOnScrollListener(new brandScrollListener());
        adapter = new MasterBrandAdapter(activity);
        brandRecyclerView.setAdapter(adapter);
        adapter.setMyClickListener(SearchBrandFragment.this);

        LinearLayout llSearch = (LinearLayout) view.findViewById(R.id.llSearch);
        etSearch = (AppCompatEditText) view.findViewById(R.id.etSearch);
        etSearch.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        ImageView imgSearch = (ImageView) view.findViewById(R.id.imgSearch);
        AppCompatTextView txtTitle = (AppCompatTextView) view.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        imgSearch.setOnClickListener(this);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.search_brand_layout;
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
            case R.id.imgSearch:
                brand_edit = etSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(brand_edit)) {
                    BaseFragment fragment3 = new SearchBrandResultFragment();
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("brand", brand_edit);
                    fragment3.setArguments(bundle3);
                    callFragmentMethod(fragment3, this.getClass().getSimpleName(), R.id.navigationContainer);
                } else {
                    Toast.makeText(activity, "Error! You have not entered any text", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onItemClick(int position, BrandList model) {
        String brand = model.getName().trim();
        BaseFragment fragment1 = new SearchBrandResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("brand", brand);
        fragment1.setArguments(bundle);
        callFragmentMethod(fragment1, this.getClass().getSimpleName(), R.id.navigationContainer);
    }

    public class brandScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            LinearLayoutManager recyclerLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = recyclerLayoutManager.getItemCount();

            int visibleThreshold = ((totalItemCount / 2) < 20) ? totalItemCount / 2 : 20;
            int firstVisibleItem = recyclerLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > startLimit) {
                    loading = false;
                    startLimit = totalItemCount;
                }
            } else {
                int nonVisibleItemCounts = totalItemCount - visibleItemCount;
                int effectiveVisibleThreshold = firstVisibleItem + visibleThreshold;

                if (nonVisibleItemCounts <= effectiveVisibleThreshold) {
                    startLimit = startLimit + 1;
                    countLimit = 20;

                    showLoader();

                    sendRequestOnCatListFeed(startLimit, countLimit);
                    loading = true;
                }
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }
}