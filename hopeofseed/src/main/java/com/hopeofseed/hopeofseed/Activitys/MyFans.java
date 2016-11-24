package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.MyFollowedListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.FollowedFriend;
import com.hopeofseed.hopeofseed.JNXDataTmp.FollowedFriendTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/26 17:19
 * 修改人：whisper
 * 修改时间：2016/9/26 17:19
 * 修改备注：
 */
public class MyFans extends AppCompatActivity implements View.OnClickListener,NetCallBack{
    ListView lv_my_follow;
    MyFollowedListAdapter myFollowedListAdapter;
    ArrayList<FollowedFriend> arr_FollowedFriend = new ArrayList<>();
    ArrayList<FollowedFriend> arr_FollowedFriendTmp = new ArrayList<>();
    String UserId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_followed);
        Intent intent=getIntent();
        UserId=intent.getStringExtra("UserId");
        initView();
        getMyFollow();
    }

    private void getMyFollow() {

        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", UserId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getMyFans.php", opt_map, FollowedFriendTmp.class, this);
    }
    private void initView() {
        TextView apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("我的关注");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        lv_my_follow = (ListView) findViewById(R.id.lv_my_follow);
        myFollowedListAdapter = new MyFollowedListAdapter(getApplicationContext(), arr_FollowedFriend);
        lv_my_follow.setAdapter(myFollowedListAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
        }
    }


    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
        FollowedFriendTmp mFollowedFriendTmp = ObjectUtil.cast(rspBaseBean);
        arr_FollowedFriendTmp = mFollowedFriendTmp.getDetail();
        updateView();
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
            arr_FollowedFriend.clear();
            arr_FollowedFriend.addAll(arr_FollowedFriendTmp);
            Log.e(TAG, "handleMessage: updateview" + arr_FollowedFriend.size());
            myFollowedListAdapter.notifyDataSetChanged();
        }
    };
}
