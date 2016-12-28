package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXDataTmp.pushFileResultTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/9 14:58
 * 修改人：whisper
 * 修改时间：2016/12/9 14:58
 * 修改备注：
 */
public class UpdateGroupDesc extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    String GroupID;
    pushFileResultTmp mCommResultTmp = new pushFileResultTmp();
    Handler handler = new Handler();
    EditText et_desc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        GroupID = intent.getStringExtra("GroupID");
        initView();

    }

    private void initView() {
        setContentView(R.layout.update_group_desc);

        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("更新群");
        Button btn_topriht = (Button) findViewById(R.id.btn_topright);
        btn_topriht.setText("更新");
        btn_topriht.setVisibility(View.VISIBLE);
        btn_topriht.setOnClickListener(this);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        et_desc = (EditText) findViewById(R.id.et_desc);
        et_desc.requestFocus();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                updateDesc();
                break;
        }
    }

    private void updateDesc() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("GroupID", String.valueOf(GroupID));
        opt_map.put("Desc", et_desc.getText().toString());
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "updateGroupDesc.php", opt_map, pushFileResultTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        mCommResultTmp = ObjectUtil.cast(rspBaseBean);
        handler.post(resultPushFile);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    /**
     * uploadimg
     */
    Runnable resultPushFile = new Runnable() {
        @Override
        public void run() {
            if (mCommResultTmp.getDetail().getContent().equals("上传成功")) {
                Toast.makeText(getApplicationContext(), mCommResultTmp.getDetail().getContent(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(802, intent);

                finish();
            } else {
                Toast.makeText(getApplicationContext(), mCommResultTmp.getDetail().getContent(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                setResult(802, intent);
                finish();
            }
        }
    };
}
