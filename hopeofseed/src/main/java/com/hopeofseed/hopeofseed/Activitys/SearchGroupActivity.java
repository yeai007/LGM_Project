package com.hopeofseed.hopeofseed.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.ConversationListAdapter;
import com.hopeofseed.hopeofseed.Adapter.UnReadConversationListAdapter;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;

import cn.jpush.im.android.api.model.Conversation;

import static cn.jpush.im.android.api.JMessageClient.getConversationList;
import static com.hopeofseed.hopeofseed.Activitys.MessageFragment.MESSAGE_UPDATE_LIST;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/6 10:53
 * 修改人：whisper
 * 修改时间：2016/12/6 10:53
 * 修改备注：
 */
public class SearchGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SearchGroupActivity";
    RecyclerView recycler_list;
    ConversationListAdapter mAdapter;
    ArrayList<Conversation> mList = new ArrayList<>();
    ArrayList<Conversation> mListTmp = new ArrayList<>();
    private UpdateBroadcastReceiver updateBroadcastReceiver;  //刷新列表广播
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_group_activity);
        initView();
        initReceiver();
    }

    private void initReceiver() {
        // 注册广播接收
        updateBroadcastReceiver = new UpdateBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MESSAGE_UPDATE_LIST);    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(updateBroadcastReceiver, filter);
    }

    private void getData() {

        if ((ArrayList<Conversation>) getConversationList() != null) {
            mListTmp = (ArrayList<Conversation>) getConversationList();
            mHandler.post(updatelist);
        } else {
            getData();
        }
    }

    Runnable updatelist = new Runnable() {
        @Override
        public void run() {
            if (mListTmp.size() > 0) {
                mList.clear();
                mList.addAll(mListTmp);
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("查找群");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
        recycler_list.setHasFixedSize(true);
        getData();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(SearchGroupActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);
        mAdapter = new ConversationListAdapter(SearchGroupActivity.this, mList);
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

    class UpdateBroadcastReceiver extends BroadcastReceiver {

        /* 覆写该方法，对广播事件执行响应的动作  */
        public void onReceive(Context context, Intent intent) {
            getData();
        }
    }
}
