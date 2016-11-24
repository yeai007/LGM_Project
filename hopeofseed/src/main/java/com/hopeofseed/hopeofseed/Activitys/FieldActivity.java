package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.FieldGroupGridViewAdapter;
import com.hopeofseed.hopeofseed.DataForHttp.GetVarietyData;
import com.hopeofseed.hopeofseed.JNXData.VarietyData;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：关注领域
 * 创建人：whisper
 * 创建时间：2016/9/28 14:28
 * 修改人：whisper
 * 修改时间：2016/9/28 14:28
 * 修改备注：
 */
public class FieldActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String SELECT_FIEDL = "SELECT_FIEDL";
    GridView gv_field_group;
    FieldGroupGridViewAdapter fieldGroupGridViewAdapter;
    ArrayList<VarietyData> arr_VarietyData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_activity);
        initView();
        getUserVarietyData();
    }

    private void initView() {
        TextView app_title = (TextView) findViewById(R.id.apptitle);
        app_title.setText("关注领域选择");
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        btn_topright.setText("确定");
        Button btn_topleft = (Button) findViewById(R.id.btn_topleft);
        btn_topleft.setOnClickListener(this);
        gv_field_group = (GridView) findViewById(R.id.gv_field_group);
        fieldGroupGridViewAdapter = new FieldGroupGridViewAdapter(getApplicationContext(), arr_VarietyData);
        gv_field_group.setAdapter(fieldGroupGridViewAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                String str_select = "";
                for (int i = 0; i < fieldGroupGridViewAdapter.getSelect().size(); i++) {
                    if (i == 0) {
                        str_select = arr_VarietyData.get(fieldGroupGridViewAdapter.getSelect().get(i)).getVarietyname();
                    } else {
                        str_select = str_select + "," + arr_VarietyData.get(fieldGroupGridViewAdapter.getSelect().get(i)).getVarietyname();
                    }
                }
                Intent intent = new Intent();
                intent.putExtra(SELECT_FIEDL, str_select);
                setResult(RESULT_OK, intent);
                finish();

                break;
        }
    }

    private void getUserVarietyData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetVarietyData getVarietyData = new GetVarietyData();
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
                    ArrayList<VarietyData> arrVarietyDataTmp = new ArrayList<>();
                    arrVarietyDataTmp = ObjectUtil.cast(msg.obj);
                    arr_VarietyData.clear();

                    arr_VarietyData.addAll(arrVarietyDataTmp);
                    fieldGroupGridViewAdapter.notifyDataSetChanged();

                    break;
                case 2:

                    break;
            }
        }
    };

}
