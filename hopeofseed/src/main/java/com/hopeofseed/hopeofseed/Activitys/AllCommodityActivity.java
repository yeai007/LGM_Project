package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hopeofseed.hopeofseed.Adapter.CommodityRecycleListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommodityDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/29 18:58
 * 修改人：whisper
 * 修改时间：2016/10/29 18:58
 * 修改备注：
 */
public class AllCommodityActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, NetCallBack {
    private static final String TAG = "AllCommodityActivity";
    RecyclerView recy_list;
    CommodityRecycleListAdapter mCommodityRecycleListAdapter;
    ArrayList<CommodityData> arr_CommodityData = new ArrayList<>();
    ArrayList<CommodityData> arr_CommodityDataTmp = new ArrayList<>();
    static String Str_search = "";
    private SwipeRefreshLayout mRefreshLayout;
    int PageNo = 0;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_commodity_activity);
        initView();
        getData();
    }

    private void initView() {
        recy_list = (RecyclerView) findViewById(R.id.recy_list);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        final GridLayoutManager manager = new GridLayoutManager(AllCommodityActivity.this, 2);
        recy_list.setLayoutManager(manager);
        mCommodityRecycleListAdapter = new CommodityRecycleListAdapter(AllCommodityActivity.this, arr_CommodityData);
        recy_list.setAdapter(mCommodityRecycleListAdapter);
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("Str_search", Str_search);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetMyCommodity.php", opt_map, CommodityDataTmp.class, this);
    }

    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            if (PageNo == 0) {
                arr_CommodityData.clear();
            }
            arr_CommodityData.addAll(arr_CommodityDataTmp);
            mCommodityRecycleListAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topright:
                finish();
                break;
        }
    }


    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        CommodityDataTmp mCommodityDataTmp = ObjectUtil.cast(rspBaseBean);
        arr_CommodityDataTmp = mCommodityDataTmp.getDetail();
        mHandler.post(updateList);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }
}
