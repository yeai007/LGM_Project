package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.DataForHttp.SearchFriends;
import com.hopeofseed.hopeofseed.JNXData.UserData;
import com.hopeofseed.hopeofseed.Adapter.NewFriendListAdapter;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/14 13:36
 * 修改人：whisper
 * 修改时间：2016/9/14 13:36
 * 修改备注：
 */
public class AddNewFriend extends AppCompatActivity implements View.OnClickListener {
    EditText et_serach;
    PullToRefreshListView lv_friends;
    ArrayList<UserData> arrUserData = new ArrayList<>();
    NewFriendListAdapter newFriendListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_friend);
        initView();
        initFriends();
        searchFriends();
    }

    private void initFriends() {
        lv_friends = (PullToRefreshListView) findViewById(R.id.lv_friends);
        newFriendListAdapter = new NewFriendListAdapter(getApplicationContext(), arrUserData);
        lv_friends.setAdapter(newFriendListAdapter);
        lv_friends.setMode(PullToRefreshBase.Mode.BOTH);
        lv_friends.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // 下拉刷新 业务代码
                if (refreshView.isShownHeader()) {
                    lv_friends.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_friends.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    lv_friends.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);

                }
                // 上拉加载更多 业务代码
                if (refreshView.isShownFooter()) {
                    lv_friends.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    lv_friends.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    lv_friends.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);

                }

            }
        });
    }


    private void initView() {
        et_serach = (EditText) findViewById(R.id.et_serach);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                break;
        }
    }

    private void searchFriends() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SearchFriends searchFriends = new SearchFriends();
                Boolean bRet = searchFriends.RunData();
                Message msg = searchFriendsHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.obj = searchFriends.retRows;
                msg.sendToTarget();
            }
        }).start();

    }

    private Handler searchFriendsHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {

                case 0:
                    lv_friends.onRefreshComplete();
                    break;
                case 1:
                    ArrayList<UserData> arr_UserData_tmp = new ArrayList<>();
                    arr_UserData_tmp = ObjectUtil.cast(msg.obj);
                    arrUserData.clear();
                    arrUserData.addAll(arr_UserData_tmp);
                    newFriendListAdapter.notifyDataSetChanged();
                    lv_friends.onRefreshComplete();
                    break;
                case 2:

                    break;
            }
        }
    };
}
