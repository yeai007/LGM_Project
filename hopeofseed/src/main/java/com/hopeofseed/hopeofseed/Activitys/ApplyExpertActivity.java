package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Adapter.Sp_TitleAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXDataTmp.pushFileResultTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.util.GetImagePath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import citypickerview.widget.CityPicker;
import me.shaohui.advancedluban.Luban;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/14 15:46
 * 修改人：whisper
 * 修改时间：2016/11/14 15:46
 * 修改备注：
 */
public class ApplyExpertActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "ApplyExpertActivity";
    EditText et_actualname, et_work_place, et_jianjie;
    Spinner sp_sex;
    ImageView img_certificate;
    Button btn_certificate, go;
    CityPicker cityPicker;
    private String StrProvince = "", StrCity = "", StrZone = "";
    Sp_TitleAdapter spSexAdapter;
    ArrayList<String> sexs = new ArrayList<>();
    String StrSex = "男";
    //上传文件
    ArrayList<File> arrFile = new ArrayList<>();
    long start;
    pushFileResultTmp mCommResultTmp;
    Handler mHandler = new Handler();
    private ArrayList<String> images = new ArrayList<>();
    String StrError = "";
    public static final int TO_SELECT_PHOTO = 147;
    private String mPicturePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_expert_activity);
        initView();
    }


    private void initView() {
        TextView apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("申请成为专家");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("申请");
        btn_topright.setOnClickListener(this);
        btn_topright.setVisibility(View.VISIBLE);
        et_actualname = (EditText) findViewById(R.id.et_actualname);
        et_work_place = (EditText) findViewById(R.id.et_work_place);
        et_jianjie = (EditText) findViewById(R.id.et_jianjie);
        sp_sex = (Spinner) findViewById(R.id.sp_sex);
        sp_sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    StrSex = "男";
                } else {
                    StrSex = "女";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        initSex();
        img_certificate = (ImageView) findViewById(R.id.img_certificate);
        btn_certificate = (Button) findViewById(R.id.btn_certificate);
        btn_certificate.setOnClickListener(this);
        go = (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cityPicker = new CityPicker.Builder(ApplyExpertActivity.this).textSize(20)
                        .title("地址选择")
                        .titleBackgroundColor("#5F9EA0")
                        .onlyShowProvinceAndCity(false)
                        .confirTextColor("#000000")
                        .cancelTextColor("#000000")
                        .province(Const.Province)
                        .city(Const.City)
                        .district(Const.Zone)
                        .textColor(Color.parseColor("#000000"))
                        .provinceCyclic(false)
                        .cityCyclic(false)
                        .districtCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .build();

                cityPicker.show();
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        go.setText("" + citySelected[0] + "  " + citySelected[1] + "  "
                                + citySelected[2]);
                        StrProvince = citySelected[0];
                        StrCity = citySelected[1];
                        StrZone = citySelected[2];
                    }
                });
            }
        });
    }

    private void initSex() {
        sexs.add("男");
        sexs.add("女");
        spSexAdapter = new Sp_TitleAdapter(getApplicationContext(), sexs);
        sp_sex.setAdapter(spSexAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                if (images.size() == 0) {
                    submitApplyFile(null);
                } else {
                    try {
                        submitApply();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_certificate:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("return-data", true);
                startActivityForResult(intent, TO_SELECT_PHOTO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TO_SELECT_PHOTO && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            mPicturePath = GetImagePath.getImageAbsolutePath(this, selectedImage);
            if (images.size() < 3) {
                images.add(mPicturePath);
            }
            Log.e(TAG, "onActivityResult: " + images.size());
            Glide.with(ApplyExpertActivity.this)
                    .load(new File(mPicturePath))
                    .centerCrop()
                    .into(img_certificate);
        }
    }

    private void submitApply() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < images.size(); i++) {
                    Log.e(TAG, "run: " + images.get(i));
                    if (images.get(i).equals("add")) {
                    } else if (i == 0) {
                        arrFile.add(new File(images.get(i)));
                    }
                }
                Log.e(TAG, "images: " + images);
                Luban.get(getApplicationContext()).setMaxSize(128)
                        .putGear(Luban.CUSTOM_GEAR)
                        .load(arrFile)                     // load all images
                        .asListObservable().doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        start = System.currentTimeMillis();
                        Log.e(TAG, "call: start" + System.currentTimeMillis());
                    }
                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<File>>() {
                            @Override
                            public void call(final List<File> fileList) {
                                for (int i = 0; i < fileList.size(); i++) {
                                    Log.e(TAG, "call: " + fileList.get(i).getName());
                                }
                                Log.e(TAG, "call: " + fileList);
                                submitApplyFile(fileList);
                            }
                        });
            }
        }).start();
    }

    private void submitApplyFile(List<File> fileList) {
        if (isChecked()) {
            HashMap<String, String> opt_map = new HashMap<>();
            opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
            opt_map.put("TrueName", et_actualname.getText().toString().trim());
            opt_map.put("Sex", StrSex);
            opt_map.put("StrProvince", StrProvince);
            opt_map.put("StrCity", StrCity);
            opt_map.put("StrZone", StrZone);
            opt_map.put("WorkPlace", et_work_place.getText().toString().trim());
            opt_map.put("Intruduce", et_jianjie.getText().toString());
            HttpUtils hu = new HttpUtils();
            hu.httpPostFiles(Const.BASE_URL + "ApplyExpert.php", opt_map, fileList, pushFileResultTmp.class, this);
        }
    }

    private boolean isChecked() {
        boolean ischeck = true;
        if (TextUtils.isEmpty(et_actualname.getText().toString())) {
            ischeck = false;
            Toast.makeText(getApplicationContext(), "真实姓名必须填写", Toast.LENGTH_SHORT).show();
        }
/*        if(TextUtils.isEmpty(et_work_place.getText().toString()))
        {
            ischeck=false;
            Toast.makeText(getApplicationContext(),"工作单位",Toast.LENGTH_SHORT).show();
        }*/
        if (TextUtils.isEmpty(et_jianjie.getText().toString())) {
            ischeck = false;
            Toast.makeText(getApplicationContext(), "简介必须填写", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(StrProvince)) {
            ischeck = false;
            Toast.makeText(getApplicationContext(), "地区必须填写", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(StrCity)) {
            ischeck = false;
            Toast.makeText(getApplicationContext(), "地区必须填写", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(StrZone)) {
            ischeck = false;
            Toast.makeText(getApplicationContext(), "地区必须填写", Toast.LENGTH_SHORT).show();
        }
        return ischeck;

    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("ApplyExpert")) {
            mHandler.post(updateResult);
        }
    }

    @Override
    public void onError(String error) {
        Log.e(TAG, "onError: " + error);
        StrError = error;
        mHandler.post(toastError);
    }

    @Override
    public void onFail() {

    }

    Runnable toastError = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), StrError, Toast.LENGTH_SHORT).show();
        }
    };
    Runnable updateResult = new Runnable() {
        @Override
        public void run() {
            if (mCommResultTmp.getDetail().getContent().equals("上传成功")) {
                Toast.makeText(getApplicationContext(), mCommResultTmp.getDetail().getContent(), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), mCommResultTmp.getDetail().getContent(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };
}
