package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.JNXData.GroupData;
import com.hopeofseed.hopeofseed.JNXDataTmp.GroupDataTmp;
import com.hopeofseed.hopeofseed.Adapter.GroupsListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/1 15:38
 * 修改人：whisper
 * 修改时间：2016/9/1 15:38
 * 修改备注：
 */
public class SelectGroup extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    PullToRefreshListView lv_groups;
    GroupsListAdapter groupListAadpter;
    ArrayList<GroupData> arr_GroupData = new ArrayList<>();
    private static final String TAG = "SelectGroup";
    private static int CREATE_GROUP = 102;
    GroupDataTmp groupDataTmp = new GroupDataTmp();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discovery_group);
        initView();
        lv_groups = (PullToRefreshListView) findViewById(R.id.lv_groups);
        groupListAadpter = new GroupsListAdapter(getApplicationContext(), arr_GroupData);
        lv_groups.setAdapter(groupListAadpter);
        lv_groups.setMode(PullToRefreshBase.Mode.BOTH);
        initGroups();
        getGroups();
    }

    private void initGroups() {
        getGroups();
        lv_groups.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                String str = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // 下拉刷新 业务代码
                if (refreshView.isShownHeader()) {
                    lv_groups.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_groups.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    lv_groups.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    getGroups();
                }
                // 上拉加载更多 业务代码
                if (refreshView.isShownFooter()) {
                    lv_groups.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    lv_groups.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    lv_groups.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    getGroups();
                }
            }
        });
    }

    private void initView() {
        ((TextView) findViewById(R.id.apptitle)).setText("发现群");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        (findViewById(R.id.btn_topright)).setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_topleft:
                intent = new Intent(SelectGroup.this, HomePageActivity.class);
                intent.putExtra("page", 1);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_topright:
                intent = new Intent(this, CreateGroupActivity.class);
                startActivityForResult(intent, CREATE_GROUP);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == CREATE_GROUP && resultCode == RESULT_OK) {
            //添加成功
            getGroups();
        }

    }
    private void getGroups() {
        HashMap<String, String> opt_map = new HashMap<>();
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetDiscoveryGroup.php", opt_map, GroupDataTmp.class, this);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Intent intent = new Intent(SelectGroup.this, HomePageActivity.class);
            intent.putExtra("page", 1);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        groupDataTmp = ObjectUtil.cast(rspBaseBean);
        updateGroupView();
    }
    @Override
    public void onError(String error) {

    }
    @Override
    public void onFail() {

    }
    private void updateGroupView() {
        Message msg = groupViewHandle.obtainMessage();
        msg.arg1 = 1;
        msg.sendToTarget();
    }
    private Handler groupViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    arr_GroupData.clear();
                    arr_GroupData.addAll(groupDataTmp.getDetail());
                    groupListAadpter.notifyDataSetChanged();
                    lv_groups.onRefreshComplete();
                    break;
                case 2:

                    break;
            }
        }
    };
}
