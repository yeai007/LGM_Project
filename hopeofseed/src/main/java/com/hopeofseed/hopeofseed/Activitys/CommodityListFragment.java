package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.CommodityListAdapter;
import com.hopeofseed.hopeofseed.Adapter.GalleryAdapter;
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
import java.util.List;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/1 19:30
 * 修改人：whisper
 * 修改时间：2016/11/1 19:30
 * 修改备注：
 */
public class CommodityListFragment extends Fragment implements NetCallBack {
    PullToRefreshListView lv_groups;
    CommodityListAdapter groupListAadpter;
    public ArrayList<CommodityData> arr_CommodityData = new ArrayList<>();
    CommodityDataTmp commodityDataTmp = new CommodityDataTmp();
    private List<String> mDatas = new ArrayList<String>();
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.pager_list_commodity, null);
        initView(v);
        return v;
    }

    private void initData() {
    }

    private void initView(View v) {
        lv_groups = (PullToRefreshListView) v.findViewById(R.id.lv_groups);
        groupListAadpter = new CommodityListAdapter(getActivity(), arr_CommodityData);
        lv_groups.setAdapter(groupListAadpter);
        lv_groups.setMode(PullToRefreshBase.Mode.BOTH);
        initGroups();
        getGroups();
        initData();
        //搜索分类
        //Content
        //得到控件
        mRecyclerView = (RecyclerView) v.findViewById(R.id.id_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new GalleryAdapter(getActivity(), mDatas);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new GalleryAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {

                ArrayList<CommodityData> arr_CommodityDataTmp = new ArrayList<>();
                for (CommodityData itemCommodity : commodityDataTmp.getDetail()) {
                    if (itemCommodity.getCommodityVariety_1().equals(data)) {
                        arr_CommodityDataTmp.add(itemCommodity);
                    }
                }
                arr_CommodityData.clear();
                arr_CommodityData.addAll(arr_CommodityDataTmp);
                groupListAadpter.notifyDataSetChanged();
                lv_groups.onRefreshComplete();
            }
        });
    }

    private void initGroups() {
        getGroups();
        lv_groups.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
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

    private void getGroups() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetMyCommodity.php", opt_map, CommodityDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        commodityDataTmp = ObjectUtil.cast(rspBaseBean);
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
        msg.arg1 = 1;
        msg.sendToTarget();
    }

    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {

                case 0:
                    lv_groups.onRefreshComplete();
                    break;
                case 1:
                    arr_CommodityData.clear();
                    arr_CommodityData.addAll(commodityDataTmp.getDetail());
                    groupListAadpter.notifyDataSetChanged();
                    lv_groups.onRefreshComplete();

                    mDatas.clear();
                    for (int i = 0; i < arr_CommodityData.size(); i++) {
                        if (!mDatas.contains(arr_CommodityData.get(i).getCommodityVariety_1())) {
                            mDatas.add(arr_CommodityData.get(i).getCommodityVariety_1());
                        }

                    }

                    mAdapter.notifyDataSetChanged();
                    break;
                case 2:

                    break;
            }
        }
    };
}
