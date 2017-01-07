package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommodityDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.GlideImageLoader;
import com.lgm.utils.ObjectUtil;
import com.youth.banner.Banner;

import java.util.Arrays;
import java.util.HashMap;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/29 10:34
 * 修改人：whisper
 * 修改时间：2016/10/29 10:34
 * 修改备注：
 */
public class CommodityActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    TextView tv_title, tv_variety, tv_name, tv_price;
    EditText et_discribe;
    String CommodityId;
    CommodityData mCommodityData = new CommodityData();
    Banner banner;
    TextView apptitle;
    /**
     * ViewPager当前显示页的下标
     */
    private int position = 0;
    String[] images;
    ImageView img_guaranteed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commodity_activity);
        Intent intent = getIntent();
        CommodityId = intent.getStringExtra("CommodityId");
        initView();
        getData();
    }

    private void initView() {
        apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("商品详情");
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_variety = (TextView) findViewById(R.id.tv_variety);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_price = (TextView) findViewById(R.id.tv_price);
        et_discribe = (EditText) findViewById(R.id.et_discribe);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        images = getResources().getStringArray(R.array.url2);
        img_guaranteed = (ImageView) findViewById(R.id.img_guaranteed);
        banner = (Banner) findViewById(R.id.banner);
        banner.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("CommodityId", CommodityId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommodityById.php", opt_map, CommodityDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        CommodityDataTmp mCommodityDataTmp = ObjectUtil.cast(rspBaseBean);
        mCommodityData = mCommodityDataTmp.getDetail().get(0);
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
            String[] arrImage = mCommodityData.getCommodityImgs().split(";");
            if (TextUtils.isEmpty(mCommodityData.getCommodityImgs())) {
                banner.setImages(Arrays.asList(R.drawable.no_have_img)).setImageLoader(new GlideImageLoader()).start();
            } else {
                for (int i = 0; i < arrImage.length; i++) {
                    if (!TextUtils.isEmpty(arrImage[i])) {
                        Log.e(TAG, "handleMessage: " + arrImage[i]);
                        arrImage[i] = Const.IMG_URL + arrImage[i];
                        Log.e(TAG, "handleMessage: " + arrImage[i]);
                    }
                }
                banner.setImages(Arrays.asList(arrImage)).setImageLoader(new GlideImageLoader()).start();
            }
            if (Integer.parseInt(mCommodityData.getOwnerClass()) == 2) {
                img_guaranteed.setVisibility(View.VISIBLE);
            } else {
                img_guaranteed.setVisibility(View.GONE);
            }
            apptitle.setText(mCommodityData.getCommodityTitle());
            tv_title.setText(mCommodityData.getCommodityTitle());
            tv_name.setText(mCommodityData.getCommodityName());
            tv_variety.setText("【" + mCommodityData.getCommodityVariety() + "】");
            et_discribe.setText(mCommodityData.getCommodityDescribe());
            tv_price.setText("￥ " + mCommodityData.getCommodityPrice());
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
