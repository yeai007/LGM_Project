package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class CommodityListFragment extends Fragment implements NetCallBack, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recy_list;
    CommodityListAdapter mCommodityListAdapter;
    public ArrayList<CommodityData> arr_CommodityData = new ArrayList<>();
    CommodityDataTmp commodityDataTmp = new CommodityDataTmp();
    private List<String> mDatas = new ArrayList<String>();
    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    boolean isLoading = false;

    private SwipeRefreshLayout mRefreshLayout;
    int PageNo = 0;
    Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.pager_list_commodity, null);
        initView(v);
        getData();
        return v;
    }

    private void initView(View v) {
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
                mCommodityListAdapter.notifyDataSetChanged();

            }
        });
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        recy_list=(RecyclerView)v.findViewById(R.id.recy_list);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recy_list.setLayoutManager(manager);
        mCommodityListAdapter = new CommodityListAdapter(getContext(), arr_CommodityData);
        recy_list.setAdapter(mCommodityListAdapter);
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
                    } else if (arr_CommodityData.size() < 20) {
                        //当没有更多的数据的时候去掉加载更多的布局
/*                        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recy_news.getAdapter();
                        adapter.setIsNeedMore(false);
                        adapter.notifyDataSetChanged();*/
                    }
                }
            }
        });
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetMyCommodity.php", opt_map, CommodityDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        commodityDataTmp = ObjectUtil.cast(rspBaseBean);
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
            if (PageNo == 0) {
                arr_CommodityData.clear();
            }
            arr_CommodityData.addAll(commodityDataTmp.getDetail());
            mCommodityListAdapter.notifyDataSetChanged();
            mDatas.clear();
            for (int i = 0; i < arr_CommodityData.size(); i++) {
                if (!mDatas.contains(arr_CommodityData.get(i).getCommodityVariety_1())) {
                    mDatas.add(arr_CommodityData.get(i).getCommodityVariety_1());
                }

            }
            mAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
            isLoading = false;
        }
    };

    @Override
    public void onRefresh() {
        PageNo = 0;
        getData();
    }
}
