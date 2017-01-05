package com.hopeofseed.hopeofseed.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommHttpResult;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommHttpResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CustomPushDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.iosDialog;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2017/1/5 15:28
 * 修改人：whisper
 * 修改时间：2017/1/5 15:28
 * 修改备注：
 */
public class PushSMSToCustom extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "PushToCustom";
    EditText et_text;
    String PushId = "";
    Handler mHandler = new Handler();
    String StrError = "";
    CommHttpResultTmp commResultTmp = new CommHttpResultTmp();

    String StrProcess;
    ArrayList<CommHttpResult> detail = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_sms_to_custom);
        initView();
        Intent intent = getIntent();

        PushId = intent.getStringExtra("PushId");

    }

    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("通知");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("发送");
        btn_topright.setOnClickListener(this);
        btn_topright.setVisibility(View.VISIBLE);
        et_text = (EditText) findViewById(R.id.et_text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                pushToUsers();
                break;
        }
    }

    private void pushToUsers() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("PushId", PushId);
        opt_map.put("Content", et_text.getText().toString());
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "PushSmsToUsers.php", opt_map, CommHttpResultTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        commResultTmp = ObjectUtil.cast(rspBaseBean);
        mHandler.post(updateResult);
    }

    @Override
    public void onError(String error) {
        Log.e(TAG, "onError: " + error);
        StrError = error;
        mHandler.post(updateResult);
    }

    @Override
    public void onFail() {
    }

    Runnable updateResult = new Runnable() {
        @Override
        public void run() {
            detail = commResultTmp.getDetail();
            String show = "";
            if (detail.size() > 0) {
                for (int i = 0; i < detail.size(); i++) {
                    show =show+ detail.get(i).getResult() + "\n";
                }
            }
            iosDialog mIosDialog = new iosDialog.Builder(PushSMSToCustom.this)
                    .setMessage(show)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle("发送进度").create();
            mIosDialog.show();
        }

    };
}
