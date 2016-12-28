package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXDataTmp.UpdateResultDataTmp;
import com.hopeofseed.hopeofseed.curView.CurrencyModifyActivity;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/24 10:45
 * 修改人：whisper
 * 修改时间：2016/12/24 10:45
 * 修改备注：
 */
public class ModifyIntroduceActivity extends CurrencyModifyActivity implements NetCallBack {
    private static final String TAG = "ModifyIntroduceActivity";
    String UserId;
    String UserRoleId;
    String Introduce;
    String NewIntroduce = "";
    Handler mHandler = new Handler();
    UpdateResultDataTmp updateResultDataTmp = new UpdateResultDataTmp();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        UserId = intent.getStringExtra("UserId");
        UserRoleId = intent.getStringExtra("UserRoleId");
        Introduce = intent.getStringExtra("Introduce");
        setEditText(Introduce);
    }

    @Override
    public void onClickRightButton(String et_text) {
        Log.e(TAG, "onClickRightButton: " + et_text);
        NewIntroduce = et_text;
        updateIntroduce();
    }

    private void updateIntroduce() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", UserId);
        opt_map.put("UserRoleId", UserRoleId);
        opt_map.put("Introduce", NewIntroduce);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "ModifyIntroduce.php", opt_map, UpdateResultDataTmp.class, this);
    }

    @Override
    public void onClickLeftButton(String et_text) {
        finish();
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        updateResultDataTmp = ObjectUtil.cast(rspBaseBean);
        Log.e(TAG, "onSuccess: " + updateResultDataTmp.toString());
        mHandler.post(updateResult);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updateResult = new Runnable() {
        @Override
        public void run() {
          //  Toast.makeText(getApplicationContext(), updateResultDataTmp.getResult(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    };
}
