package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import android.widget.TextView;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.AutoSplitTextView;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

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
public class CropActivity extends AppCompatActivity implements NetCallBack, View.OnClickListener {
    String CropId;
    ArrayList<CropData> arr_CropData = new ArrayList<>();
    ArrayList<CropData> arr_CropDataTmp = new ArrayList<>();
    AutoSplitTextView tv_category2_content, tv_varietyname_content, tv_authorizenumber_content, tv_isgen_content, tv_features_content, tv_production_content, tv_breedregion_content, tv_breedkill_content, tv_breedorganization;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_activity);
        Intent intent = getIntent();
        CropId = intent.getStringExtra("CropId");
        initView();
        getData();
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
        tv_breedorganization = (AutoSplitTextView) findViewById(R.id.tv_breedorganization);
    }

    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("CropId", CropId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCropById.php", opt_map, CropDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        CropDataTmp mCropDataTmp = ObjectUtil.cast(rspBaseBean);
        arr_CropDataTmp = mCropDataTmp.getDetail();
        updateView();
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CropData mCropData = new CropData();
            mCropData = arr_CropDataTmp.get(0);
            tv_category2_content.setText(mCropData.getCropCategory2());
            tv_varietyname_content.setText(mCropData.getVarietyName());
            tv_authorizenumber_content.setText(mCropData.getAuthorizeNumber());
            tv_isgen_content.setText(mCropData.getIsGen());
            tv_features_content.setText(mCropData.getFeatures());
            tv_production_content.setText(mCropData.getProduction());
            tv_breedregion_content.setText(mCropData.getBreedRegion());
            tv_breedkill_content.setText(mCropData.getBreedSkill());
            tv_breedorganization.setText(mCropData.getBreedOrganization());

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
}
