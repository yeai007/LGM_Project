package com.hopeofseed.hopeofseed.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.AllCommodityActivity;
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

import static com.baidu.location.h.j.S;
import static com.hopeofseed.hopeofseed.R.id.recy_news;

/**
 * 品种
 */
public class CommdityFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "CommdityFragment";
    private static final String ARG_POSITION = "position";
    private int position;
    RecyclerView recy_list;
    CommodityRecycleListAdapter mCommodityRecycleListAdapter;
    ArrayList<CommodityData> arr_CommodityData = new ArrayList<>();
    ArrayList<CommodityData> arr_CommodityDataTmp = new ArrayList<>();
    static String Str_search = "";
    Button btn_show_all;
    int PageNo = 0;
    private SwipeRefreshLayout mRefreshLayout;
    String UserId = "";

    public CommdityFragment(String userId) {
        super();
        UserId = userId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.commdity_list_fragment, null);
        initView(v);
        getData();
        return v;
    }

    private void initView(View v) {

        btn_show_all = (Button) v.findViewById(R.id.btn_show_all);
        btn_show_all.setOnClickListener(this);
        recy_list = (RecyclerView) v.findViewById(R.id.recy_list);
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        recy_list.setLayoutManager(manager);
        mCommodityRecycleListAdapter = new CommodityRecycleListAdapter(getActivity(), arr_CommodityData);
        recy_list.setAdapter(mCommodityRecycleListAdapter);
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        if (UserId.equals(String.valueOf(Const.currentUser.user_id))) {
            opt_map.put("IsMe", "0");
        } else {
            opt_map.put("IsMe", "1");
        }
        opt_map.put("UserId", UserId);
        opt_map.put("Str_search", Str_search);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetMyCommodity.php", opt_map, CommodityDataTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            CommodityDataTmp mCommodityDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_CommodityDataTmp = mCommodityDataTmp.getDetail();
            updateView();
        }

        @Override
        public void onError(String error) {
        }

        @Override
        public void onFail() {
        }
    };

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (PageNo == 0) {
                arr_CommodityData.clear();
            }
            arr_CommodityData.addAll(arr_CommodityDataTmp);
            mCommodityRecycleListAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
        }
    };

    public void Search(String text) {
        Str_search = text;
        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_show_all:
                Intent intent = new Intent(getActivity(), AllCommodityActivity.class);
                intent.putExtra("UserId",UserId);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onRefresh() {
        getData();
    }
}