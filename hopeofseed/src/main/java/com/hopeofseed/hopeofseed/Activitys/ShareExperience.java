package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Adapter.Sp_TitleAdapter;
import com.hopeofseed.hopeofseed.Adapter.SpinnerSortsAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.DataForHttp.AddNesExperienceData;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommHttpResult;
import com.hopeofseed.hopeofseed.JNXData.SortsData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommHttpResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.SortsDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.hopeofseed.hopeofseed.R.id.btn_title;
import static com.hopeofseed.hopeofseed.R.id.sp_title;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：liguangming
 * 类描述：分享农技经验
 * 创建人：whisper
 * 创建时间：2016/9/1 14:11
 * 修改人：whisper
 * 修改时间：2016/9/1 14:11
 * 修改备注：
 */
public class ShareExperience extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "ShareExperience";
    EditText et_title, et_content;
    Spinner sp_class;
    SpinnerSortsAdapter mSpinnerSortsAdapter;
    ArrayList<SortsData> arr_SortsData = new ArrayList<>();
    ArrayList<SortsData> arr_SortsDataTmp = new ArrayList<>();
    String ClassName = "";
    String ClassId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_experience);
        initView();
        initData();
    }

    private void initData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSortsData.php", opt_map, SortsDataTmp.class, this);
    }

    private void initView() {
        ((TextView) findViewById(R.id.apptitle)).setText("分享农技经验");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        (findViewById(R.id.btn_topright)).setOnClickListener(this);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
        sp_class = (Spinner) findViewById(R.id.sp_class);
        mSpinnerSortsAdapter = new SpinnerSortsAdapter(getApplicationContext(), arr_SortsData);
        sp_class.setAdapter(mSpinnerSortsAdapter);
        sp_class.setOnItemSelectedListener(spTitleListtener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                SubmitExperienceData();
                break;
        }
    }

    private void SubmitExperienceData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("StrTitle", et_title.getText().toString());
        opt_map.put("StrContent", et_content.getText().toString());
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("ClassName", ClassName);
        opt_map.put("ClassId", ClassId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "AddNewsExperience.php", opt_map, CommHttpResultTmp.class, this);
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                AddNesExperienceData addNesExperienceData = new AddNesExperienceData();
                addNesExperienceData.StrTitle = et_title.getText().toString();
                addNesExperienceData.StrContent = et_content.getText().toString();
                Boolean bRet = addNesExperienceData.RunData();
                Message msg = getNewsHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.sendToTarget();
            }
        }).start();*/
    }
    AdapterView.OnItemSelectedListener spTitleListtener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            ClassName = arr_SortsData.get(i).getVarietyname();
            ClassId = arr_SortsData.get(i).getVarietyid();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("GetSortsData")) {
            SortsDataTmp mSortsDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_SortsDataTmp = mSortsDataTmp.getDetail();
            updateView();
        } else if (rspBaseBean.RequestSign.equals("AddNewsExperience")) {
            if (rspBaseBean.resultNote.equals("success")) {
                Log.e(TAG, "onSuccess: success" );
                this.finish();
            } else {
                Log.e(TAG, "onSuccess: failed" );
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
            arr_SortsData.clear();
            arr_SortsData.addAll(arr_SortsDataTmp);
            Log.e(TAG, "handleMessage: updateview" + arr_SortsData.size());
            mSpinnerSortsAdapter.notifyDataSetChanged();
        }
    };
}
