package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.hopeofseed.hopeofseed.Adapter.NotifyListAdapter;
import com.hopeofseed.hopeofseed.JNXData.NotifyData;
import com.hopeofseed.hopeofseed.JNXData.NotifyDataNorealm;
import com.hopeofseed.hopeofseed.R;
import java.util.ArrayList;
import cn.jpush.im.android.api.model.Conversation;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/7 15:50
 * 修改人：whisper
 * 修改时间：2016/12/7 15:50
 * 修改备注：
 */
public class SystemNofityActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recycler_list;
    NotifyListAdapter mAdapter;
    ArrayList<NotifyDataNorealm> mList = new ArrayList<>();
    ArrayList<Conversation> mListTmp = new ArrayList<>();
    Handler mHandler = new Handler();
    Realm myRealm = Realm.getDefaultInstance();
    String type="1";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_notify_activity);
        Intent intent=getIntent();
        type=intent.getStringExtra("type");
        initView();
        getData();
    }

    private void getData() {
        RealmResults<NotifyData> results1 =
                myRealm.where(NotifyData.class).equalTo("NotifyIsRead",  "0").equalTo("NotifyType", type).findAll();
        for (NotifyData item : results1) {
            NotifyDataNorealm mNotifyDataNorealm = new NotifyDataNorealm();
            mNotifyDataNorealm.setNotifyId(item.getNotifyId());
            mNotifyDataNorealm.setNotifyType(item.getNotifyType());
            mNotifyDataNorealm.setNotifyTitle(item.getNotifyTitle());
            mNotifyDataNorealm.setNotifyContent(item.getNotifyContent());
            mNotifyDataNorealm.setNotifyURL(item.getNotifyURL());
            mNotifyDataNorealm.setNotifyImage(item.getNotifyImage());
            mNotifyDataNorealm.setNotifyToClass(item.getNotifyToClass());
            mNotifyDataNorealm.setNotifyIsRead(item.getNotifyIsRead());
            mNotifyDataNorealm.setNotifyShowTitle(item.getNotifyShowTitle());
            mNotifyDataNorealm.setNotifyShowContent(item.getNotifyShowContent());
            mNotifyDataNorealm.setNotifyCreateTime(item.getNotifyCreateTime());
            mList.add(mNotifyDataNorealm);
        }

    }

    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("系统通知");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(SystemNofityActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);
        mAdapter = new NotifyListAdapter(SystemNofityActivity.this, mList);
        recycler_list.setAdapter(mAdapter);

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
