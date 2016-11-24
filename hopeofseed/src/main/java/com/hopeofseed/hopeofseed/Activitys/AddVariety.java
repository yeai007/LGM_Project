package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Adapter.Sp_VarietyAdapter;
import com.hopeofseed.hopeofseed.DataForHttp.GetVarietyData;
import com.hopeofseed.hopeofseed.JNXData.VarietyData;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.DataForHttp.AddUserVariety;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/23 18:30
 * 修改人：whisper
 * 修改时间：2016/8/23 18:30
 * 修改备注：
 */
public class AddVariety extends AppCompatActivity implements View.OnClickListener {
    String TAG = "AddVariety";
    Spinner sp_variety_1, sp_variety_2, sp_variety_3;
    Sp_VarietyAdapter sp_VarietyAdapter_1, sp_VarietyAdapter_2, sp_VarietyAdapter_3;
    ArrayList<VarietyData> arr_Variety_1 = new ArrayList<>();
    ArrayList<VarietyData> arr_Variety_2 = new ArrayList<>();
    ArrayList<VarietyData> arr_Variety_3 = new ArrayList<>();
    ArrayList<VarietyData> arr_Variety_Data_2 = new ArrayList<>();
    ArrayList<VarietyData> arr_Variety_Data_3 = new ArrayList<>();
    Button btn_add_variety;
    String varietyid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvariety);
        initView();
    }

    private void initView() {
        sp_variety_1 = (Spinner) findViewById(R.id.sp_variety_1);
        sp_variety_2 = (Spinner) findViewById(R.id.sp_variety_2);
        sp_variety_3 = (Spinner) findViewById(R.id.sp_variety_3);
        sp_VarietyAdapter_1 = new Sp_VarietyAdapter(getApplicationContext(), arr_Variety_1);
        sp_VarietyAdapter_2 = new Sp_VarietyAdapter(getApplicationContext(), arr_Variety_Data_2);
        sp_VarietyAdapter_3 = new Sp_VarietyAdapter(getApplicationContext(), arr_Variety_Data_3);
        sp_variety_1.setAdapter(sp_VarietyAdapter_1);
        sp_variety_2.setAdapter(sp_VarietyAdapter_2);
        sp_variety_3.setAdapter(sp_VarietyAdapter_3);
        sp_variety_1.setOnItemSelectedListener(spTitleListener_1);
        sp_variety_2.setOnItemSelectedListener(spTitleListener_2);
        sp_variety_3.setOnItemSelectedListener(spTitleListener_3);
        btn_add_variety = (Button) findViewById(R.id.btn_add_variety);
        btn_add_variety.setOnClickListener(this);
        getVarietyData();
    }

    AdapterView.OnItemSelectedListener spTitleListener_1 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            arr_Variety_Data_2.clear();
            for (int j = 0; j < arr_Variety_2.size(); j++) {
                VarietyData varietyData = new VarietyData();
                varietyData = arr_Variety_2.get(j);
                Log.e(TAG, "onItemSelected: " + Integer.parseInt(varietyData.getVarietyclassid()) + Integer.parseInt(arr_Variety_1.get(i).getVarietyid()));
                if (Integer.parseInt(varietyData.getVarietyclassid()) == Integer.parseInt(arr_Variety_1.get(i).getVarietyid())) {
                    arr_Variety_Data_2.add(varietyData);
                }
            }
            Log.e(TAG, "onItemSelected: " + arr_Variety_Data_2.size());
            sp_VarietyAdapter_2 = new Sp_VarietyAdapter(getApplicationContext(), arr_Variety_Data_2);
            sp_variety_2.setAdapter(sp_VarietyAdapter_2);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    AdapterView.OnItemSelectedListener spTitleListener_2 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            varietyid = arr_Variety_Data_2.get(i).getVarietyid();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    AdapterView.OnItemSelectedListener spTitleListener_3 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(getApplicationContext(), String.valueOf(l), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private void getVarietyData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetVarietyData getVarietyData = new GetVarietyData();
                getVarietyData.UserId = String.valueOf(Const.currentUser.user_id);
                Boolean bRet = getVarietyData.RunData();
                Message msg = getNewsHandle.obtainMessage();
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

    private Handler getNewsHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {

                case 0:

                    break;
                case 1:
                    ArrayList<VarietyData> arrVarietyDataTmp = new ArrayList<>();
                    arrVarietyDataTmp = ObjectUtil.cast(msg.obj);

                    ArrayList<VarietyData> arr_Variety_1_tmp = new ArrayList<>();
                    ArrayList<VarietyData> arr_Variety_2_tmp = new ArrayList<>();
                    ArrayList<VarietyData> arr_Variety_3_tmp = new ArrayList<>();
                    for (int i = 0; i < arrVarietyDataTmp.size(); i++) {
                        VarietyData variety = new VarietyData();
                        variety = arrVarietyDataTmp.get(i);
                        if (Integer.parseInt(variety.getVarietyclassid()) == 1) {
                            arr_Variety_1_tmp.add(variety);
                        }
                        if (Integer.parseInt(variety.getVarietyclassid()) > 1) {
                            arr_Variety_2_tmp.add(variety);
                        }
                        if (Integer.parseInt(variety.getVarietyclassid()) == 3) {
                            arr_Variety_3_tmp.add(variety);
                        }
                    }
                    arr_Variety_1.clear();
                    arr_Variety_1.addAll(arr_Variety_1_tmp);
                    arr_Variety_2.clear();
                    arr_Variety_2.addAll(arr_Variety_2_tmp);
                    arr_Variety_3.clear();
                    arr_Variety_3.addAll(arr_Variety_3_tmp);
                    Log.e("111111", "handleMessage: " + arr_Variety_1.size() + arr_Variety_1.get(0).getVarietyname());
                    sp_VarietyAdapter_1 = new Sp_VarietyAdapter(getApplicationContext(), arr_Variety_1);
                    sp_variety_1.setAdapter(sp_VarietyAdapter_1);
                    break;
                case 2:

                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_variety:
                addVariety();
                break;
        }
    }

    private void addVariety() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                AddUserVariety addUserVariety = new AddUserVariety();
                addUserVariety.UserId = String.valueOf(Const.currentUser.user_id);
                addUserVariety.VarietyId = varietyid;
                Boolean bRet = addUserVariety.RunData();
                Message msg =addVarietyHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.obj = addUserVariety.dataMessage.obj;
                msg.sendToTarget();
            }
        }).start();
    }
    private Handler addVarietyHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {

                case 0:

                    break;
                case 1:
                  String result=(String)msg.obj;
                    if(Integer.parseInt(result)>0)
                    {
                        finish();
                    }
                    break;
                case 2:

                    break;
            }
        }
    };
}
