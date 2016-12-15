package com.hopeofseed.hopeofseed.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.hopeofseed.hopeofseed.util.AddNotify;
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
    EditText search_et;
    String StrSearch = "";
    ImageButton btn_search;
    boolean isSearch = false;
    int PageNo = 0;
    boolean isLoading = false;

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
        opt_map.put("StrSearch", StrSearch);
        opt_map.put("PageNo", String.valueOf(PageNo));
        if (isSearch) {
            if (StrSearch.equals("")) {
                opt_map.put("IsSearch", "0");
            } else {
                opt_map.put("IsSearch", "1");
            }

        } else {
            opt_map.put("IsSearch", "0");
        }
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getSelectUser.php", opt_map, UserDataNoRealmTmp.class, this);
    }

    private void initView() {
        setContentView(R.layout.select_user_activity);
        TextView apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("添加成员");
        search_et = (EditText) findViewById(R.id.search_et);
        btn_search = (ImageButton) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
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

        //滚动监听，在滚动监听里面去实现加载更多的功能
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount && dy > 0) {
                    if (!isLoading) {//一个布尔的变量，默认是false
                        Log.e(TAG, "onScrolled: loadingmaore");
                        isLoading = true;
                        PageNo = PageNo + 1;
                        getData();
                    } else if (arrUserDataNoRealmTmp.size() < 20) {
                        //当没有更多的数据的时候去掉加载更多的布局
/*                        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recy_news.getAdapter();
                        adapter.setIsNeedMore(false);
                        adapter.notifyDataSetChanged();*/
                    }
                }
            }
        });
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
            case R.id.btn_search:
                isSearch = true;
                PageNo = 0;
                StrSearch = search_et.getText().toString().trim();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
                getData();
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
                    setResult(803, intent);
                    AddNotify addNotify = new AddNotify();
                    addNotify.AddNewGroupMember(GroupId, usernameList);
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
            if (PageNo == 0) {
                arrUserDataNoRealm.clear();
            }
            arrUserDataNoRealm.addAll(arrUserDataNoRealmTmp);
            mAdapter.notifyDataSetChanged();
            isLoading = false;
        }
    };
}
