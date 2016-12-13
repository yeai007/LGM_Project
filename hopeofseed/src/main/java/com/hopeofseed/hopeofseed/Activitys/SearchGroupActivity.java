package com.hopeofseed.hopeofseed.Activitys;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.ConversationListAdapter;
import com.hopeofseed.hopeofseed.Adapter.GroupListAdapter;
import com.hopeofseed.hopeofseed.Adapter.UnReadConversationListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.GroupData;
import com.hopeofseed.hopeofseed.JNXDataTmp.GroupDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserMessageDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
public class SearchGroupActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "SearchGroupActivity";
    RecyclerView recycler_list;
    GroupListAdapter mAdapter;
    ArrayList<GroupData> mList = new ArrayList<>();
    ArrayList<GroupData> mListTmp = new ArrayList<>();
    Handler mHandler = new Handler();
    EditText search_et_input;
    Button btn_search;
    String StrSearch = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_group_activity);
        initView();
        getData();
    }


    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        HttpUtils hu = new HttpUtils();
        opt_map.put("StrSearch", StrSearch);
        hu.httpPost(Const.BASE_URL + "GetGroupList.php", opt_map, GroupDataTmp.class, this);
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
        mAdapter = new GroupListAdapter(SearchGroupActivity.this, mList);
        recycler_list.setAdapter(mAdapter);
        search_et_input = (EditText) findViewById(R.id.search_et_input);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_search:
                StrSearch = search_et_input.getText().toString().trim();
                getData();
                break;
        }
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        GroupDataTmp groupDataTmp = ObjectUtil.cast(rspBaseBean);
        mListTmp = groupDataTmp.getDetail();
        mHandler.post(updatelist);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

}
