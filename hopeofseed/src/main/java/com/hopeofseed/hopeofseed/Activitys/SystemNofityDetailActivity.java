package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.NotifyDataNorealm;
import com.hopeofseed.hopeofseed.R;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/9 7:38
 * 修改人：whisper
 * 修改时间：2016/12/9 7:38
 * 修改备注：
 */
public class SystemNofityDetailActivity extends AppCompatActivity implements View.OnClickListener {
    NotifyDataNorealm notifyDataNorealm = new NotifyDataNorealm();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sysytem_notify_detail_activity);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        notifyDataNorealm = bundle.getParcelable("data");
        initView();
    }

    private void initView() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        tv_title.setText(notifyDataNorealm.getNotifyTitle());
        tv_content.setText(notifyDataNorealm.getNotifyContent());
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
        }
    }
}
