package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.UserVarietyListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.DataForHttp.GetUserVarietyData;
import com.hopeofseed.hopeofseed.JNXData.UserVarietyData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/19 17:02
 * 修改人：whisper
 * 修改时间：2016/8/19 17:02
 * 修改备注：
 */
public class SeedVariety extends AppCompatActivity implements View.OnClickListener {
    ListView lv_variety;
    UserVarietyListAdapter userVarietyListAdapter;
    ArrayList<UserVarietyData> arrVarietyData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seedvariety);
        initView();
    }

    private void initView() {
        TextView app_title = (TextView) findViewById(R.id.apptitle);
        app_title.setText("品种维护");
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        Button btn_topleft= (Button) findViewById(R.id.btn_topleft);
        btn_topleft.setOnClickListener(this);
        lv_variety = (ListView) findViewById(R.id.lv_variety);
        userVarietyListAdapter = new UserVarietyListAdapter(getApplicationContext(), arrVarietyData);
        lv_variety.setAdapter(userVarietyListAdapter);
        getUserVarietyData();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_topright:
                intent = new Intent(this, AddVariety.class);
                startActivity(intent);
                break;
            case  R.id.btn_topleft:
                finish();
                break;
        }
    }

    private void getUserVarietyData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetUserVarietyData getVarietyData = new GetUserVarietyData();
                getVarietyData.UserId = String.valueOf(Const.currentUser.user_id);
                Boolean bRet = getVarietyData.RunData();
                Message msg = getUserVarietyDataHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.obj = getVarietyData.retRows;
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler getUserVarietyDataHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    ArrayList<UserVarietyData> arrVarietyDataTmp = new ArrayList<>();
                    arrVarietyDataTmp = ObjectUtil.cast(msg.obj);
                    arrVarietyData.clear();
                    arrVarietyData.addAll(arrVarietyDataTmp);
                    userVarietyListAdapter.notifyDataSetChanged();
                    break;
                case 2:

                    break;
            }
        }
    };
}
