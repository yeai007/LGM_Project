package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.CommodityDistributorAdapter;
import com.hopeofseed.hopeofseed.Adapter.SelectDistributorAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.AddRelationResult;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.AddRelationResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.hopeofseed.hopeofseed.R.id.lv_list;
import static com.hopeofseed.hopeofseed.R.id.search_et_input;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/2 9:56
 * 修改人：whisper
 * 修改时间：2016/11/2 9:56
 * 修改备注：
 */
public class SettingDistributorActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "SettingDistributorActiv";
    SelectDistributorAdapter mSelectDistributorAdapter;
    ArrayList<DistributorData> arr_DistributorData = new ArrayList<>();
    DistributorDataTmp mDistributorDataTmp = new DistributorDataTmp();
    Button btn_topright;
    String CommodityId;
    String DistributorIds;
    ArrayList<String> arrDistributorIds = new ArrayList<>();
    ArrayList<AddRelationResult> mAddRelationResult = new ArrayList<>();
    Button btn_search;
    boolean IsSearch = false;
    EditText search_et_input;
    RecyclerView recy_list;
    SwipeRefreshLayout mRefreshLayout;
    int PageNo = 0;
    Handler mHandler = new Handler();
    boolean isLoading = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                arrDistributorIds.clear();
                HashMap<Integer, Boolean> isSelected = mSelectDistributorAdapter.getIsSelected();
                Iterator iter = isSelected.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object key = entry.getKey();
                    Object val = entry.getValue();
                    Log.e(TAG, "onClick: " + arr_DistributorData.get(Integer.parseInt(String.valueOf(key))).getDistributorName() + String.valueOf(val));
                    if ((boolean) val) {
                        arrDistributorIds.add(arr_DistributorData.get(Integer.parseInt(String.valueOf(key))).getDistributorId());
                    }
                }
                Log.e(TAG, "onClick: " + arrDistributorIds.toString());
                 AddRelation();
                break;
            case R.id.btn_search:
                IsSearch = true;
                PageNo=0;
                SelectDistributorAdapter.setIsSelectedClean();
                getData();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_distributor_activity);
        Intent intent = getIntent();
        CommodityId = intent.getStringExtra("CommodityId");
        initView();
        initRecyle();
        getData();
    }

    private void initView() {
        TextView Apptitle = (TextView) findViewById(R.id.apptitle);
        Apptitle.setText("经销商维护");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("确定");
        btn_topright.setOnClickListener(this);
        btn_topright.setVisibility(View.VISIBLE);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        search_et_input = (EditText) findViewById(R.id.search_et_input);
    }

    private void initRecyle() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        recy_list = (RecyclerView) findViewById(R.id.recy_list);
        final LinearLayoutManager manager = new LinearLayoutManager(SettingDistributorActivity.this);
        recy_list.setLayoutManager(manager);
        mSelectDistributorAdapter = new SelectDistributorAdapter(SettingDistributorActivity.this, arr_DistributorData);
        recy_list.setAdapter(mSelectDistributorAdapter);
        //滚动监听，在滚动监听里面去实现加载更多的功能
        recy_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                int totalItemCount = manager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    if (!isLoading) {//一个布尔的变量，默认是false
                        isLoading = true;
                        PageNo = PageNo + 1;
                        getData();
                    }
                }
            }
        });
    }

    private void AddRelation() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("CommodityId", CommodityId);
        opt_map.put("arrDistributorIds", arrDistributorIds.toString().replace("[", "").replace("]", ""));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "AddRelationDistributor.php", opt_map, AddRelationResultTmp.class, netCallBack);
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("CommodityId", CommodityId);
        opt_map.put("PageNo", String.valueOf(PageNo));
        if (IsSearch) {
            opt_map.put("IsSearch", "0");
            if (TextUtils.isEmpty(search_et_input.getText().toString())) {
                opt_map.put("IsSearch", "1");
            } else {
                opt_map.put("StrSearch", search_et_input.getText().toString().trim());
            }

        } else {
            opt_map.put("IsSearch", "1");
        }
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetDistributorByAddRelation.php", opt_map, DistributorDataTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            if (rspBaseBean.RequestSign.equals("AddRelationDistributor")) {
                AddRelationResultTmp mAddRelationResultTmp = new AddRelationResultTmp();
                mAddRelationResultTmp = ObjectUtil.cast(rspBaseBean);
                mAddRelationResult = mAddRelationResultTmp.getDetail();
                updateResult();
            } else {
                mDistributorDataTmp = ObjectUtil.cast(rspBaseBean);
                updateView();
            }
        }

        @Override
        public void onError(String error) {

        }

        @Override
        public void onFail() {

        }
    };

    private void updateResult() {
        Message msg = updateResultHandle.obtainMessage();
        msg.sendToTarget();
    }

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private Handler updateResultHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int result = 0;
            for (int i = 0; i < mAddRelationResult.size(); i++) {
                if (mAddRelationResult.get(i).getResult().equals("已存在")) {
                    result = result + 1;
                } else if (Integer.parseInt(mAddRelationResult.get(i).getResult()) == 0) {
                } else {
                    result = result + 1;
                }
            }
            Toast.makeText(getApplicationContext(), "添加成功" + result, Toast.LENGTH_SHORT).show();
            finish();
        }
    };
    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (PageNo == 0) {
                arr_DistributorData.clear();
            }
            arr_DistributorData.addAll(mDistributorDataTmp.getDetail());
            Log.e(TAG, "handleMessage: updateview" + arr_DistributorData.size());
            mSelectDistributorAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
            isLoading = false;
        }
    };

    @Override
    public void onRefresh() {
        PageNo = 0;
        SelectDistributorAdapter.setIsSelectedClean();
        getData();
    }

}
