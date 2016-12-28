package com.hopeofseed.hopeofseed.SearchFragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hopeofseed.hopeofseed.Adapter.DistributorDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityDataNoUser;
import com.hopeofseed.hopeofseed.JNXData.DistributorCommodity;
import com.hopeofseed.hopeofseed.JNXData.DistributorCommodityArray;
import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorCommodityTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 经销商
 */
public class DistributorFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "DistributorFragment";
    private static final String ARG_POSITION = "position";
    private static final String STR_SEARCH = "STR_SEARCH";
    private int position;
    RecyclerView recy_list;
    DistributorDataAdapter mDistributorDataAdapter;
    ArrayList<DistributorCommodity> arr_DistributorDataTmp = new ArrayList<>();
    static String Str_search = "";
    ArrayList<DistributorCommodityArray> mArrDistributorCommodityArray = new ArrayList<>();
    ArrayList<DistributorCommodityArray> mArrDistributorCommodityArrayTmp = new ArrayList<>();
    boolean isLoading = false;

    private SwipeRefreshLayout mRefreshLayout;
    int PageNo = 0;
    Handler mHandler = new Handler();

    public DistributorFragment(String strSearch) {
        Str_search = strSearch;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Str_search = getArguments().getString(STR_SEARCH);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_list_distributor_fragment, null);
        initList(v);
        getData(Str_search);

        return v;
    }

    private void initList(View v) {
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        recy_list = (RecyclerView) v.findViewById(R.id.recy_list);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recy_list.setLayoutManager(manager);
        mDistributorDataAdapter = new DistributorDataAdapter(getContext(), mArrDistributorCommodityArray);
        recy_list.setAdapter(mDistributorDataAdapter);
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
                        getData(Str_search);
                    } else {
                        //当没有更多的数据的时候去掉加载更多的布局
/*                        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recy_news.getAdapter();
                        adapter.setIsNeedMore(false);
                        adapter.notifyDataSetChanged();*/
                    }
                }
            }
        });
    }


    private void getData(String Str_search) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("StrSearch", Str_search);
        opt_map.put("LocLat", String.valueOf(Const.LocLat));
        opt_map.put("LocLng", String.valueOf(Const.LocLng));
        opt_map.put("PageNo", String.valueOf(PageNo));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchDistributor.php", opt_map, DistributorCommodityTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            mArrDistributorCommodityArrayTmp.clear();
            DistributorCommodityTmp mDistributorDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_DistributorDataTmp = mDistributorDataTmp.getDetail();
            DistributorCommodity lastDistributorCommodity = new DistributorCommodity();
            int j = 0;
            for (int i = 0; i < arr_DistributorDataTmp.size(); i++) {
                DistributorCommodity itemDistributorCommodity = new DistributorCommodity();
                itemDistributorCommodity = arr_DistributorDataTmp.get(i);
                if (i == 0) {
                    DistributorCommodityArray distributorCommodityArray = new DistributorCommodityArray();
                    distributorCommodityArray.setDistributorId(itemDistributorCommodity.getDistributorId());
                    distributorCommodityArray.setDistributorName(itemDistributorCommodity.getDistributorName());
                    distributorCommodityArray.setDistributorTrademark(itemDistributorCommodity.getDistributorTrademark());
                    distributorCommodityArray.setDistributorLevel(itemDistributorCommodity.getDistributorLevel());
                    distributorCommodityArray.setDistributorTelephone(itemDistributorCommodity.getDistributorTelephone());
                    distributorCommodityArray.setDistributorFlag(itemDistributorCommodity.getDistributorFlag());
                    distributorCommodityArray.setDistributorIntroduce(itemDistributorCommodity.getDistributorIntroduce());
                    distributorCommodityArray.setDistributorProvince(itemDistributorCommodity.getDistributorProvince());
                    distributorCommodityArray.setDistributorCity(itemDistributorCommodity.getDistributorCity());
                    distributorCommodityArray.setDistributorZone(itemDistributorCommodity.getDistributorZone());
                    distributorCommodityArray.setDistributorAddressDetail(itemDistributorCommodity.getDistributorAddressDetail());
                    distributorCommodityArray.setDistributorLat(itemDistributorCommodity.getDistributorLat());
                    distributorCommodityArray.setDistributorLon(itemDistributorCommodity.getDistributorLon());
                    distributorCommodityArray.setUser_id(itemDistributorCommodity.getUser_id());
                    distributorCommodityArray.setDistance(itemDistributorCommodity.getDistance());
                    CommodityDataNoUser itemCommodityData = new CommodityDataNoUser();
                    itemCommodityData.setCommodityId(itemDistributorCommodity.getCommodityId());
                    itemCommodityData.setCommodityTitle(itemDistributorCommodity.getCommodityTitle());
                    itemCommodityData.setCommodityName(itemDistributorCommodity.getCommodityName());
                    itemCommodityData.setCommodityPrice(itemDistributorCommodity.getCommodityPrice());
                    itemCommodityData.setCreateTime(itemDistributorCommodity.getCreateTime());
                    itemCommodityData.setCommodityFlag(itemDistributorCommodity.getCommodityFlag());
                    itemCommodityData.setCommodityFlagTime(itemDistributorCommodity.getCommodityFlagTime());
                    itemCommodityData.setCommodityDescribe(itemDistributorCommodity.getCommodityDescribe());
                    itemCommodityData.setOwner(itemDistributorCommodity.getOwner());
                    itemCommodityData.setOwnerClass(itemDistributorCommodity.getOwnerClass());
                    itemCommodityData.setNewId(itemDistributorCommodity.getNewId());
                    itemCommodityData.setCommodityImgs(itemDistributorCommodity.getCommodityImgs());
                    itemCommodityData.setCommodityVariety(itemDistributorCommodity.getCommodityVariety());
                    itemCommodityData.setCommodityVariety_1(itemDistributorCommodity.getCommodityVariety_1());
                    itemCommodityData.setCommodityVariety_2(itemDistributorCommodity.getCommodityVariety_2());
                    distributorCommodityArray.getCommodityData().add(itemCommodityData);

                    lastDistributorCommodity = itemDistributorCommodity;
                    mArrDistributorCommodityArrayTmp.add(distributorCommodityArray);
                    j = j + 1;
                } else {
                    if (itemDistributorCommodity.getDistributorId().equals(lastDistributorCommodity.getDistributorId())) {
                        CommodityDataNoUser itemCommodityData = new CommodityDataNoUser();
                        itemCommodityData.setCommodityId(itemDistributorCommodity.getCommodityId());
                        itemCommodityData.setCommodityTitle(itemDistributorCommodity.getCommodityTitle());
                        itemCommodityData.setCommodityName(itemDistributorCommodity.getCommodityName());
                        itemCommodityData.setCommodityPrice(itemDistributorCommodity.getCommodityPrice());
                        itemCommodityData.setCreateTime(itemDistributorCommodity.getCreateTime());
                        itemCommodityData.setCommodityFlag(itemDistributorCommodity.getCommodityFlag());
                        itemCommodityData.setCommodityFlagTime(itemDistributorCommodity.getCommodityFlagTime());
                        itemCommodityData.setCommodityDescribe(itemDistributorCommodity.getCommodityDescribe());
                        itemCommodityData.setOwner(itemDistributorCommodity.getOwner());
                        itemCommodityData.setOwnerClass(itemDistributorCommodity.getOwnerClass());
                        itemCommodityData.setNewId(itemDistributorCommodity.getNewId());
                        itemCommodityData.setCommodityImgs(itemDistributorCommodity.getCommodityImgs());
                        itemCommodityData.setCommodityVariety(itemDistributorCommodity.getCommodityVariety());
                        itemCommodityData.setCommodityVariety_1(itemDistributorCommodity.getCommodityVariety_1());
                        itemCommodityData.setCommodityVariety_2(itemDistributorCommodity.getCommodityVariety_2());
                        mArrDistributorCommodityArrayTmp.get(j - 1).getCommodityData().add(itemCommodityData);
                    } else {
                        DistributorCommodityArray distributorCommodityArray = new DistributorCommodityArray();
                        distributorCommodityArray.setDistributorId(itemDistributorCommodity.getDistributorId());
                        distributorCommodityArray.setDistributorName(itemDistributorCommodity.getDistributorName());
                        distributorCommodityArray.setDistributorTrademark(itemDistributorCommodity.getDistributorTrademark());
                        distributorCommodityArray.setDistributorLevel(itemDistributorCommodity.getDistributorLevel());
                        distributorCommodityArray.setDistributorTelephone(itemDistributorCommodity.getDistributorTelephone());
                        distributorCommodityArray.setDistributorFlag(itemDistributorCommodity.getDistributorFlag());
                        distributorCommodityArray.setDistributorIntroduce(itemDistributorCommodity.getDistributorIntroduce());
                        distributorCommodityArray.setDistributorProvince(itemDistributorCommodity.getDistributorProvince());
                        distributorCommodityArray.setDistributorCity(itemDistributorCommodity.getDistributorCity());
                        distributorCommodityArray.setDistributorZone(itemDistributorCommodity.getDistributorZone());
                        distributorCommodityArray.setDistributorAddressDetail(itemDistributorCommodity.getDistributorAddressDetail());
                        distributorCommodityArray.setDistributorLat(itemDistributorCommodity.getDistributorLat());
                        distributorCommodityArray.setDistributorLon(itemDistributorCommodity.getDistributorLon());
                        distributorCommodityArray.setUser_id(itemDistributorCommodity.getUser_id());
                        distributorCommodityArray.setDistance(itemDistributorCommodity.getDistance());
                        CommodityDataNoUser itemCommodityData = new CommodityDataNoUser();
                        itemCommodityData.setCommodityId(itemDistributorCommodity.getCommodityId());
                        itemCommodityData.setCommodityTitle(itemDistributorCommodity.getCommodityTitle());
                        itemCommodityData.setCommodityName(itemDistributorCommodity.getCommodityName());
                        itemCommodityData.setCommodityPrice(itemDistributorCommodity.getCommodityPrice());
                        itemCommodityData.setCreateTime(itemDistributorCommodity.getCreateTime());
                        itemCommodityData.setCommodityFlag(itemDistributorCommodity.getCommodityFlag());
                        itemCommodityData.setCommodityFlagTime(itemDistributorCommodity.getCommodityFlagTime());
                        itemCommodityData.setCommodityDescribe(itemDistributorCommodity.getCommodityDescribe());
                        itemCommodityData.setOwner(itemDistributorCommodity.getOwner());
                        itemCommodityData.setOwnerClass(itemDistributorCommodity.getOwnerClass());
                        itemCommodityData.setNewId(itemDistributorCommodity.getNewId());
                        itemCommodityData.setCommodityImgs(itemDistributorCommodity.getCommodityImgs());
                        itemCommodityData.setCommodityVariety(itemDistributorCommodity.getCommodityVariety());
                        itemCommodityData.setCommodityVariety_1(itemDistributorCommodity.getCommodityVariety_1());
                        itemCommodityData.setCommodityVariety_2(itemDistributorCommodity.getCommodityVariety_2());
                        distributorCommodityArray.getCommodityData().add(itemCommodityData);
                        lastDistributorCommodity = itemDistributorCommodity;
                        mArrDistributorCommodityArrayTmp.add(distributorCommodityArray);
                        j = j + 1;
                    }
                }
            }
            mHandler.post(updateList);
        }

        @Override
        public void onError(String error) {
            mRefreshLayout.setRefreshing(false);
            isLoading = false;
        }

        @Override
        public void onFail() {
            mRefreshLayout.setRefreshing(false);
            isLoading = false;
        }
    };
    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            if (PageNo == 0) {
                mArrDistributorCommodityArray.clear();
            }
            mArrDistributorCommodityArray.addAll(mArrDistributorCommodityArrayTmp);
            mDistributorDataAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
            isLoading = false;
        }
    };
    public void Search(String text) {
        Log.e(TAG, "Search: 更新搜索经销商");
        Str_search = text;
        getData(Str_search);
    }

    @Override
    public void onRefresh() {
        PageNo = 0;
        getData(Str_search);
    }
}