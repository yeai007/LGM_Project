package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.AutoTextDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.DataForHttp.AddNewYieldData;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.SortsData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommHttpResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.SortsDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.id.edit;
import static com.hopeofseed.hopeofseed.R.id.et_essay;
import static com.hopeofseed.hopeofseed.R.id.et_yield_sum;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/1 14:12
 * 修改人：whisper
 * 修改时间：2016/9/1 14:12
 * 修改备注：
 */
public class ShareYield extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "ShareYield";
    EditText et_yield_sum, et_planting_area, et_yield, et_essay;
    AutoCompleteTextView et_variety;
    private String[] items = {"玉米"};
    ArrayList<CropData> arr_CropData = new ArrayList<>();
    ArrayList<CropData> arr_CropDataTmp = new ArrayList<>();
    AutoTextDataAdapter mAutoTextDataAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_yield);
        initView();
        initData();
    }

    private void initView() {
        ((TextView) findViewById(R.id.apptitle)).setText("分享产量");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        (findViewById(R.id.btn_topright)).setOnClickListener(this);
        et_variety = (AutoCompleteTextView) findViewById(R.id.et_variety);
        mAutoTextDataAdapter = new AutoTextDataAdapter(getApplicationContext(), arr_CropData);
        et_variety.setAdapter(mAutoTextDataAdapter);
        et_yield_sum = (EditText) findViewById(R.id.et_yield_sum);
        et_planting_area = (EditText) findViewById(R.id.et_planting_area);
        et_yield = (EditText) findViewById(R.id.et_yield);
        et_essay = (EditText) findViewById(R.id.et_essay);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                AddNewYield();
                //  finish();
                break;
        }
    }

    private void AddNewYield() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("YieldVariety", et_variety.getText().toString());
        opt_map.put("YieldSum", et_yield_sum.getText().toString());
        opt_map.put("YieldArea", et_planting_area.getText().toString());
        opt_map.put("YieldYield", et_yield.getText().toString());
        opt_map.put("YieldEssay", et_essay.getText().toString());
        opt_map.put("YieldCreateUser", String.valueOf(Const.currentUser.user_id));
        opt_map.put("YieldImg", "img");
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "AddNewYieldData.php", opt_map, CommHttpResultTmp.class, this);




        /*new Thread(new Runnable() {
            @Override
            public void run() {
                AddNewYieldData addNewYieldData = new AddNewYieldData();
                addNewYieldData.YieldVariety = et_variety.getText().toString();
                addNewYieldData.YieldSum = et_yield_sum.getText().toString();
                addNewYieldData.YieldArea = et_planting_area.getText().toString();
                addNewYieldData.YieldYield = et_yield.getText().toString();
                addNewYieldData.YieldEssay = et_essay.getText().toString();
                Boolean bRet = addNewYieldData.RunData();
                Message msg = addNewYieldDataHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.sendToTarget();
            }
        }).start();*/
    }

    private Handler addNewYieldDataHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {

                case 0:

                    break;
                case 1:

                    break;
                case 2:

                    break;
            }
        }
    };

    private void initData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetVarietyAutoData.php", opt_map, CropDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("getVariety")) {
            CropDataTmp mCropDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_CropDataTmp = mCropDataTmp.getDetail();
            updateView();

        } else if (rspBaseBean.RequestSign.equals("AddNewYieldData")) {
            if (rspBaseBean.resultNote.equals("success")) {
                Log.e(TAG, "onSuccess: success");
                this.finish();
            } else {
                Log.e(TAG, "onSuccess: failed");
                this.finish();
            }
        }
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
            Log.e(TAG, "handleMessage: updateview");
            arr_CropData.clear();
            arr_CropData.addAll(arr_CropDataTmp);
            Log.e(TAG, "handleMessage: updateview" + arr_CropData.size());
            mAutoTextDataAdapter.notifyDataSetChanged();

        }
    };
}
