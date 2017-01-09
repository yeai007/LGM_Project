package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.BreedOrganizationAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.CropUserData;
import com.hopeofseed.hopeofseed.JNXData.CustomData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropUserDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.AutoSplitTextView;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.hopeofseed.hopeofseed.R.id.view;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/24 17:24
 * 修改人：whisper
 * 修改时间：2016/10/24 17:24
 * 修改备注：
 */
public class CropActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    String CropId;
    ArrayList<CropData> arr_CropData = new ArrayList<>();
    ArrayList<CropData> arr_CropDataTmp = new ArrayList<>();
    AutoSplitTextView tv_category2_content, tv_varietyname_content, tv_authorizenumber_content, tv_isgen_content, tv_features_content, tv_production_content, tv_breedregion_content, tv_breedkill_content;
    RecyclerView tv_breedorganization;
    Handler mHandler = new Handler();
    BreedOrganizationAdapter breedOrganizationAdapter;

    ArrayList<CropUserData> arrCropUserData = new ArrayList<>();
    ArrayList<CropUserData> arrCropUserDataTmp = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_activity);
        Intent intent = getIntent();
        CropId = intent.getStringExtra("CropId");
        initView();
        getData();
        getBreedData();
    }


    private void initView() {
        TextView Apptitle = (TextView) findViewById(R.id.apptitle);
        Apptitle.setText("品种详情");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        tv_category2_content = (AutoSplitTextView) findViewById(R.id.tv_category2_content);
        tv_varietyname_content = (AutoSplitTextView) findViewById(R.id.tv_varietyname_content);
        tv_authorizenumber_content = (AutoSplitTextView) findViewById(R.id.tv_authorizenumber_content);
        tv_isgen_content = (AutoSplitTextView) findViewById(R.id.tv_isgen_content);
        tv_features_content = (AutoSplitTextView) findViewById(R.id.tv_features_content);
        tv_production_content = (AutoSplitTextView) findViewById(R.id.tv_production_content);
        tv_breedregion_content = (AutoSplitTextView) findViewById(R.id.tv_breedregion_content);
        tv_breedkill_content = (AutoSplitTextView) findViewById(R.id.tv_breedkill_content);
        tv_breedorganization = (RecyclerView) findViewById(R.id.tv_breedorganization);
        GridLayoutManager manager = new GridLayoutManager(CropActivity.this, 2);
        tv_breedorganization.setLayoutManager(manager);
        breedOrganizationAdapter = new BreedOrganizationAdapter(CropActivity.this, arrCropUserData);
        tv_breedorganization.setAdapter(breedOrganizationAdapter
        );
    }

    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("CropId", CropId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCropById.php", opt_map, CropDataTmp.class, this);
    }

    private void getBreedData() {
        Log.e(TAG, "getData: 育种企业列表");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("CropId", CropId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCropUserById.php", opt_map, CropUserDataTmp.class, this);
    }

    Runnable updateView = new Runnable() {
        @Override
        public void run() {
            CropData mCropData = new CropData();
            mCropData = arr_CropDataTmp.get(0);
            breedOrganizationAdapter.notifyDataSetChanged();
            tv_category2_content.setText(mCropData.getCropCategory2());
            tv_varietyname_content.setText(mCropData.getVarietyName());
            tv_authorizenumber_content.setText(mCropData.getAuthorizeNumber());
            tv_isgen_content.setText(mCropData.getIsGen());
            tv_features_content.setText(mCropData.getFeatures());
            tv_production_content.setText(mCropData.getProduction());
            tv_breedregion_content.setText(mCropData.getBreedRegion());
            tv_breedkill_content.setText(mCropData.getBreedSkill());
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_topleft:
                finish();
                break;
        }

    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("GetCropById")) {
            CropDataTmp mCropDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_CropDataTmp = mCropDataTmp.getDetail();
            mHandler.post(updateView);
        } else if (rspBaseBean.RequestSign.equals("GetCropUserById")) {
            arrCropUserDataTmp = ((CropUserDataTmp) rspBaseBean).getDetail();
            mHandler.post(updateBreed);
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updateBreed = new Runnable() {
        @Override
        public void run() {
            arrCropUserData.clear();
            arrCropUserData.addAll(arrCropUserDataTmp);
            breedOrganizationAdapter.notifyDataSetChanged();
        }
    };
}
