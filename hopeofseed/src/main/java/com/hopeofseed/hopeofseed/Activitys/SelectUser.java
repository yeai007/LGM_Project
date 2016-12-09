package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.NotifyListAdapter;
import com.hopeofseed.hopeofseed.Adapter.UserListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserDataNoRealmTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/9 16:17
 * 修改人：whisper
 * 修改时间：2016/12/9 16:17
 * 修改备注：
 */
public class SelectUser extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "SelectUser";
    RecyclerView recycler_list;
    UserListAdapter mAdapter;
    ArrayList<UserDataNoRealm> arrUserDataNoRealm = new ArrayList<>();
    ArrayList<UserDataNoRealm> arrUserDataNoRealmTmp = new ArrayList<>();
    Handler mHandler = new Handler();
    ArrayList<String> usernameList = new ArrayList<>();
    public static HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();
    String GroupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        GroupId = intent.getStringExtra("GroupId");
        initView();
        getData();
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getSelectUser.php", opt_map, UserDataNoRealmTmp.class, this);
    }

    private void initView() {
        setContentView(R.layout.select_user_activity);
        TextView apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("添加成员");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_toprihgt = (Button) findViewById(R.id.btn_topright);
        btn_toprihgt.setText("添加");
        btn_toprihgt.setVisibility(View.VISIBLE);
        btn_toprihgt.setOnClickListener(this);
        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(SelectUser.this, LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);
        mAdapter = new UserListAdapter(SelectUser.this, arrUserDataNoRealm);
        recycler_list.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                addNewMember();
                break;
        }
    }

    private void addNewMember() {
        Log.e(TAG, "addNewMember: " + mAdapter.getIsSelected().toString());

        usernameList.clear();
        isSelected = mAdapter.getIsSelected();
        for (int i = 0; i < isSelected.size(); i++) {
            if (isSelected.get(i)) {
                usernameList.add(Const.JPUSH_PREFIX + arrUserDataNoRealm.get(i).getUser_id());
            }
        }
        Log.e(TAG, "addNewMember: " + usernameList.toString());
        JMessageClient.addGroupMembers(Long.parseLong(GroupId), usernameList, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.e(TAG, "gotResult: " + i + "---" + s);
                if (i == 0) {
                    Log.e(TAG, "gotResult: 添加成功");
                    Intent intent = new Intent();
                    setResult(803,intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        arrUserDataNoRealmTmp = ((UserDataNoRealmTmp) ObjectUtil.cast(rspBaseBean)).getDetail();
        mHandler.post(updateList);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "run: 更新");
            arrUserDataNoRealm.addAll(arrUserDataNoRealmTmp);
            mAdapter.notifyDataSetChanged();
        }
    };
}
