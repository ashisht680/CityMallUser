package com.javinindia.citymalls.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.javinindia.citymalls.R;
import com.javinindia.citymalls.apiparsing.stateparsing.CityMasterParsing;
import com.javinindia.citymalls.apiparsing.stateparsing.CountryMasterApiParsing;
import com.javinindia.citymalls.apiparsing.userParsing.UesrParsingResponse;
import com.javinindia.citymalls.constant.Constants;
import com.javinindia.citymalls.font.FontAsapRegularSingleTonClass;
import com.javinindia.citymalls.preference.SharedPreferencesManager;
import com.javinindia.citymalls.utility.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ashish on 12-10-2016.
 */
public class EditProfileFragment extends BaseFragment implements View.OnClickListener {
    private RequestQueue requestQueue;
    private String day = "";
    private String month = "";
    private String year = "";
    private String hour = "";
    private String min = "";
    private String sec = "";
    public ArrayList<String> stateList = new ArrayList<>();
    String stateArray[] = null;
    public ArrayList<String> cityList = new ArrayList<>();
    String cityArray[] = null;
    ImageView imgProfilePic, imgProfilePicNotFound;
    AppCompatEditText etPersonName, etEmailAddress, etMobile, etGender, etDOB, etState, etCity, etAddress;
    RelativeLayout  rlBack;
    AppCompatTextView txtUpdate, txtEmailHd, txtMobileHd, txtGenderHd, txtDOBHd, txtStateHd, txtCityHd, txtAddressHd;

    private Uri mImageCaptureUri = null;
    // private ImageView mImageView;
    private android.app.AlertDialog dialog;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    Bitmap photo = null;
    int size = 0;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    String sPic = null;

    private File outPutFile = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private OnCallBackUpdateProfileListener onCallBackUpdateProfileListener;

    public interface OnCallBackUpdateProfileListener {
        void OnCallBackUpdateProfile();
    }

    public void setMyCallUpdateProfileListener(OnCallBackUpdateProfileListener callback) {
        this.onCallBackUpdateProfileListener = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayout(), container, false);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initToolbar(view);
        initialize(view);
        captureImageInitialization();
        methodForHitView();
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        return view;
    }

    private void initToolbar(View view) {
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
        final ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(null);
        AppCompatTextView textView = (AppCompatTextView) view.findViewById(R.id.tittle);
        textView.setText("");
        textView.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
    }

    private void methodForHitView() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.USER_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        String sID = null, msg = null, gender = null, dob = null;
                        String sName, sEmail, sMobileNum, sState, sCity, sAddress;
                        int status = 0;
                        loading.dismiss();
                        UesrParsingResponse shopViewResponse = new UesrParsingResponse();
                        shopViewResponse.responseParseMethod(response);
                        status = shopViewResponse.getStatus();
                        msg = shopViewResponse.getMsg().trim();
                        if (status == 1) {
                            sID = shopViewResponse.getUserid().trim();
                            sPic = shopViewResponse.getProf_pic().trim();
                            sName = shopViewResponse.getName().trim();
                            sEmail = shopViewResponse.getEmail().trim();
                            sMobileNum = shopViewResponse.getPhone().trim();
                            sState = shopViewResponse.getState().trim();
                            sCity = shopViewResponse.getCity().trim();
                            sAddress = shopViewResponse.getAddress().trim();
                            gender = shopViewResponse.getGender().trim();
                            dob = shopViewResponse.getDob().trim();
                            methodSetDate(sPic, sName, sEmail, sMobileNum, sState, sCity, sAddress, gender, dob);
                        } else {
                            if (!TextUtils.isEmpty(msg)) {
                                showDialogMethod(msg);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", SharedPreferencesManager.getUserID(activity));
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void methodSetDate(String sPic, String sName, String sEmail, String sMobileNum, String sState, String sCity, String sAddress, String gender, String dob) {
        if (!TextUtils.isEmpty(sName) && !sName.equals("null")) {
            etPersonName.setText(Utility.fromHtml(sName));
        } else {
            //   etPersonName.setText(" ");
        }
        if (!TextUtils.isEmpty(sEmail) && !sEmail.equals("null")) {
            etEmailAddress.setText(Utility.fromHtml(sEmail));
        } else {
            //   etEmailAddress.setText(" ");
        }
        if (!TextUtils.isEmpty(sMobileNum) && !sMobileNum.equals("null")) {
            etMobile.setText(Utility.fromHtml(sMobileNum));
        } else {
            // etMobile.setText(" ");
        }
        if (!TextUtils.isEmpty(sState) && !sState.equals("null")) {
            etState.setText(Utility.fromHtml(sState));
        } else {
            // etState.setText(" ");
        }
        if (!TextUtils.isEmpty(sCity) && !sCity.equals("null")) {
            etCity.setText(Utility.fromHtml(sCity));
        } else {
            //  etCity.setText(" ");
        }
        if (!TextUtils.isEmpty(sAddress) && !sAddress.equals("null")) {
            etAddress.setText(Utility.fromHtml(sAddress));
        } else {
            //  etAddress.setText(" ");
        }
        if (!TextUtils.isEmpty(gender) && !gender.equals("null")) {
            etGender.setText(Utility.fromHtml(gender));
        } else {
            //  etGender.setText(" ");
        }
        if (!TextUtils.isEmpty(dob) && !dob.equals("null")) {
            etDOB.setText(Utility.fromHtml(dob));
        } else {
            //  etDOB.setText(" ");
        }
        if (!TextUtils.isEmpty(sPic)) {
            Utility.imageLoadGlideLibraryPic(activity, imgProfilePicNotFound, imgProfilePic, sPic);
        }
    }

    private void initialize(View view) {
        rlBack = (RelativeLayout) view.findViewById(R.id.rlBack);
        imgProfilePicNotFound = (ImageView) view.findViewById(R.id.imgProfilePicNotFound);
        imgProfilePic = (ImageView) view.findViewById(R.id.imgProfilePic);
        //rlUpadteImg = (RelativeLayout) view.findViewById(R.id.rlUpadteImg);
        etPersonName = (AppCompatEditText) view.findViewById(R.id.etPersonName);
        etPersonName.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtEmailHd = (AppCompatTextView) view.findViewById(R.id.txtEmailHd);
        txtEmailHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etEmailAddress = (AppCompatEditText) view.findViewById(R.id.etEmailAddress);
        etEmailAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtMobileHd = (AppCompatTextView) view.findViewById(R.id.txtMobileHd);
        txtMobileHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etMobile = (AppCompatEditText) view.findViewById(R.id.etMobile);
        etMobile.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtGenderHd = (AppCompatTextView) view.findViewById(R.id.txtGenderHd);
        txtGenderHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etGender = (AppCompatEditText) view.findViewById(R.id.etGender);
        etGender.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtDOBHd = (AppCompatTextView) view.findViewById(R.id.txtDOBHd);
        txtDOBHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etDOB = (AppCompatEditText) view.findViewById(R.id.etDOB);
        etDOB.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtStateHd = (AppCompatTextView) view.findViewById(R.id.txtStateHd);
        txtStateHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etState = (AppCompatEditText) view.findViewById(R.id.etState);
        etState.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtCityHd = (AppCompatTextView) view.findViewById(R.id.txtCityHd);
        txtCityHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etCity = (AppCompatEditText) view.findViewById(R.id.etCity);
        etCity.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtAddressHd = (AppCompatTextView) view.findViewById(R.id.txtAddressHd);
        txtAddressHd.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        etAddress = (AppCompatEditText) view.findViewById(R.id.etAddress);
        etAddress.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());
        txtUpdate = (AppCompatTextView) view.findViewById(R.id.txtUpdate);
        txtUpdate.setTypeface(FontAsapRegularSingleTonClass.getInstance(activity).getTypeFace());

        etDOB.setOnClickListener(this);
        etGender.setOnClickListener(this);
        etState.setOnClickListener(this);
        etCity.setOnClickListener(this);
        txtUpdate.setOnClickListener(this);
        //rlUpadteImg.setOnClickListener(this);
        rlBack.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disableTouchOfBackFragment(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != activity.RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:

                doCrop();

                break;

            case PICK_FROM_FILE:

                // After selecting image from files, save the selected path
                mImageCaptureUri = data.getData();
                doCrop();
                break;

            case CROP_FROM_CAMERA:
                try {
                    if (outPutFile.exists()) {
                        photo = decodeFile(outPutFile.getAbsolutePath());

                        imgProfilePicNotFound.setImageBitmap(photo);
                        imgProfilePic.setImageBitmap(photo);
                    } else {
                        Toast.makeText(activity, "Error while save image", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    public Bitmap decodeFile(String filePath) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);
        final int REQUIRED_SIZE = 1024;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, o2);
        return bitmap;
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
                methodUpadetProfile();
                break;
            case R.id.etGender:
                genderMethod();
                break;
            case R.id.etDOB:
                methodOpenDatePicker();
                break;
            case R.id.etState:
                methodState();
                break;
            case R.id.etCity:
                if (!TextUtils.isEmpty(etState.getText())) {
                    methodCity();
                } else {
                    Toast.makeText(activity, "Select State first", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.rlBack:
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                }else {
                    dialog.show();
                }
                break;
        }
    }

    private void methodUpadetProfile() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_USER_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String sID = null, msg = null, sPic = null, gender = null, dob = null;
                        String sName, sEmail, sMobileNum, sState, sCity, sAddress;
                        int status = 0;
                        loading.dismiss();
                        UesrParsingResponse shopViewResponse = new UesrParsingResponse();
                        shopViewResponse.responseParseMethod(response);
                        status = shopViewResponse.getStatus();
                        msg = shopViewResponse.getMsg().trim();
                        if (status == 1) {
                            onCallBackUpdateProfileListener.OnCallBackUpdateProfile();
                            activity.onBackPressed();

                        } else {
                            if (!TextUtils.isEmpty(msg)) {
                                showDialogMethod(msg);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                String sID = null, msg = null, gender = null, dob = null;
                String sName, sEmail, sMobileNum, sState, sCity, sAddress;
                sName = etPersonName.getText().toString().trim();
                sEmail = etEmailAddress.getText().toString().trim();
                sMobileNum = etMobile.getText().toString().trim();
                gender = etGender.getText().toString().trim();
                dob = etDOB.getText().toString().trim();
                sAddress = etAddress.getText().toString().trim();
                sState = etState.getText().toString().trim();
                sCity = etCity.getText().toString().trim();
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", SharedPreferencesManager.getUserID(activity));
                params.put("name", sName);
                params.put("email", sEmail);
                params.put("phone", sMobileNum);
                params.put("gender", gender);
                params.put("dob", dob);
                params.put("address", sAddress);
                params.put("country", "India");
                params.put("state", sState);
                params.put("city", sCity);
                params.put("device_token", "device_token");
                if (photo != null) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] data = bos.toByteArray();
                    String encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
                    params.put("pic", encodedImage + "image/jpeg");
                    params.put("action", "new");
                } else {
                    params.put("pic", sPic);
                    params.put("action", "old");
                }
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void genderMethod() {
        final String percentArray[] = {"MALE", "FEMALE"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle("Select gender");
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(percentArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                etGender.setText(percentArray[item]);
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void methodOpenDatePicker() {
        int mYear, mMonth, mDay, mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        etDOB.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        day = Integer.toString(dayOfMonth);
                        month = Integer.toString(monthOfYear + 1);
                        EditProfileFragment.this.year = Integer.toString(year);
                    }
                }, mYear, mMonth, mDay);
        long now = System.currentTimeMillis() - 1000;
        // datePickerDialog.getDatePicker().setMinDate(now);
        // c.add(c.DAY_OF_MONTH, 31);
        //  datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    private void methodState() {
        stateList.removeAll(stateList);
        stateArray = null;
        sendRequestOnState();
    }

    private void sendRequestOnState() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.STATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        hideLoader();
                        CountryMasterApiParsing countryMasterApiParsing = new CountryMasterApiParsing();
                        countryMasterApiParsing.responseParseMethod(response);
                        if (countryMasterApiParsing.getCountryDetails().getStateDetailsArrayList().size() > 0) {
                            for (int i = 0; i < countryMasterApiParsing.getCountryDetails().getStateDetailsArrayList().size(); i++) {
                                stateList.add(countryMasterApiParsing.getCountryDetails().getStateDetailsArrayList().get(i).getState().trim());
                            }
                            if (stateList.size() > 0) {
                                stateArray = new String[stateList.size()];
                                stateList.toArray(stateArray);

                                if (stateList != null && stateList.size() > 0) {
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                                    builder.setTitle("Select State");
                                    builder.setNegativeButton(android.R.string.cancel, null);
                                    builder.setItems(stateArray, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int item) {
                                            // Do something with the selection
                                            etState.setText(stateArray[item]);
                                            etCity.setText("");
                                        }
                                    });
                                    builder.create();
                                    builder.show();
                                }
                            }
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("country", "india");
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void methodCity() {
        cityList.removeAll(cityList);
        cityArray = null;

        sendRequestOnCity();
    }

    private void sendRequestOnCity() {
        final ProgressDialog loading = ProgressDialog.show(activity, "Loading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.STATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        hideLoader();
                        CityMasterParsing cityMasterParsing = new CityMasterParsing();
                        cityMasterParsing.responseParseMethod(response);
                        for (int i = 0; i < cityMasterParsing.getCountryDetails().getCityDetails().size(); i++) {
                            cityList.add(cityMasterParsing.getCountryDetails().getCityDetails().get(i).getCity().trim());
                        }
                        if (cityList.size() > 0) {
                            cityArray = new String[cityList.size()];
                            cityList.toArray(cityArray);

                            if (cityList != null && cityList.size() > 0) {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                                builder.setTitle("Select City");
                                builder.setNegativeButton(android.R.string.cancel, null);
                                builder.setItems(cityArray, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        // Do something with the selection
                                        etCity.setText(cityArray[item]);
                                    }
                                });
                                builder.create();
                                builder.show();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        volleyErrorHandle(error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("country", "india");
                params.put("state", etState.getText().toString());
                return params;
            }

        };
        stringRequest.setTag(this.getClass().getSimpleName());
        volleyDefaultTimeIncreaseMethod(stringRequest);
        requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void captureImageInitialization() {
        final String[] items = new String[]{"Take from camera",
                "Select from gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.select_dialog_item, items);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);

        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { // pick from
                // camera
                if (item == 0) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp1.jpg");
                    mImageCaptureUri = Uri.fromFile(f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, PICK_FROM_CAMERA);

                } else {
                    // pick from file
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, PICK_FROM_FILE);
                }
            }
        });

        dialog = builder.create();
    }

    public class CropOptionAdapter extends ArrayAdapter<CropOption> {
        private ArrayList<CropOption> mOptions;
        private LayoutInflater mInflater;

        public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
            super(context, R.layout.crop_selector, options);

            mOptions = options;

            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            if (convertView == null)
                convertView = mInflater.inflate(R.layout.crop_selector, null);

            CropOption item = mOptions.get(position);

            if (item != null) {
                ((ImageView) convertView.findViewById(R.id.iv_icon))
                        .setImageDrawable(item.icon);
                ((TextView) convertView.findViewById(R.id.tv_name))
                        .setText(item.title);

                return convertView;
            }

            return null;
        }
    }

    public class CropOption {
        public CharSequence title;
        public Drawable icon;
        public Intent appIntent;
    }


    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(
                intent, 0);

        int size = list.size();
        if (size == 0) {
            Toast.makeText(activity, "Can not find image crop app",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = activity.getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = activity.getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        activity, cropOptions);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
                builder.setTitle("Choose Crop App");
                builder.setCancelable(false);
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(
                                        cropOptions.get(item).appIntent,
                                        CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (mImageCaptureUri != null) {
                            activity.getContentResolver().delete(mImageCaptureUri, null,
                                    null);
                            mImageCaptureUri = null;
                        }
                    }
                });

                android.support.v7.app.AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    dialog.show();
                    //return;
                }else {
                    Toast.makeText(activity, "You Denied for camera permission so you cant't update image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}