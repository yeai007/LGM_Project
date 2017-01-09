package com.hopeofseed.hopeofseed.SearchFragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hopeofseed.hopeofseed.Adapter.SelectEnterpriseAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityDataNoUser;
import com.hopeofseed.hopeofseed.JNXData.EnterpriseCommodity;
import com.hopeofseed.hopeofseed.JNXData.EnterpriseCommodityArray;
import com.hopeofseed.hopeofseed.JNXDataTmp.EnterpriseCommodityTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * 企业
 */
public class EnterpriseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recy_list;
    SelectEnterpriseAdapter mEnterpriseAdapter;
    ArrayList<EnterpriseCommodity> arrEnterpriseCommodityTmp = new ArrayList<>();
    ArrayList<EnterpriseCommodityArray> mEnterpriseCommodityArray = new ArrayList<>();
    ArrayList<EnterpriseCommodityArray> mEnterpriseCommodityArrayTmp = new ArrayList<>();
    static String Str_search = "";
    boolean isLoading = false;

    private SwipeRefreshLayout mRefreshLayout;
    int PageNo = 0;
    Handler mHandler = new Handler();
    public static EnterpriseFragment newInstance( String strSearch) {
        EnterpriseFragment f = new EnterpriseFragment();
        Bundle b = new Bundle();
        b.putString("strSearch", strSearch);
        f.setArguments(b);
        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Str_search = getArguments().getString("strSearch");
        }    if(TextUtils.isEmpty(Str_search))
        {
            Str_search="";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.search_list_enterprise_fragment, null);
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
        mEnterpriseAdapter = new SelectEnterpriseAdapter(getContext(), mEnterpriseCommodityArray);
        recy_list.setAdapter(mEnterpriseAdapter);
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
        opt_map.put("PageNo", String.valueOf(PageNo));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchEnterpriseData.php", opt_map, EnterpriseCommodityTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            EnterpriseCommodityTmp enterpriseDataTmp = ObjectUtil.cast(rspBaseBean);
            arrEnterpriseCommodityTmp = enterpriseDataTmp.getDetail();

            mEnterpriseCommodityArrayTmp.clear();
            EnterpriseCommodity lastEnterpriseCommodity = new EnterpriseCommodity();
            int j = 0;
            for (int i = 0; i < arrEnterpriseCommodityTmp.size(); i++) {
                EnterpriseCommodity itemEnterpriseCommodity = new EnterpriseCommodity();
                itemEnterpriseCommodity = arrEnterpriseCommodityTmp.get(i);
                if (i == 0) {
                    EnterpriseCommodityArray EnterpriseCommodityArray = new EnterpriseCommodityArray();
                    EnterpriseCommodityArray.setEnterpriseId(itemEnterpriseCommodity.getEnterpriseId());
                    EnterpriseCommodityArray.setEnterpriseName(itemEnterpriseCommodity.getEnterpriseName());
                    EnterpriseCommodityArray.setEnterpriseBusScrope(itemEnterpriseCommodity.getEnterpriseBusScrope());
                    EnterpriseCommodityArray.setEnterpriseLevel(itemEnterpriseCommodity.getEnterpriseLevel());
                    EnterpriseCommodityArray.setEnterpriseTelephone(itemEnterpriseCommodity.getEnterpriseTelephone());
                    EnterpriseCommodityArray.setEnterpriseFlag(itemEnterpriseCommodity.getEnterpriseFlag());
                    EnterpriseCommodityArray.setEnterpriseIntroduce(itemEnterpriseCommodity.getEnterpriseIntroduce());
                    EnterpriseCommodityArray.setEnterpriseProvince(itemEnterpriseCommodity.getEnterpriseProvince());
                    EnterpriseCommodityArray.setEnterpriseCity(itemEnterpriseCommodity.getEnterpriseCity());
                    EnterpriseCommodityArray.setEnterpriseZone(itemEnterpriseCommodity.getEnterpriseZone());
                    EnterpriseCommodityArray.setEnterpriseAddressDetail(itemEnterpriseCommodity.getEnterpriseAddressDetail());
                    EnterpriseCommodityArray.setEnterpriseLat(itemEnterpriseCommodity.getEnterpriseLat());
                    EnterpriseCommodityArray.setEnterpriseLon(itemEnterpriseCommodity.getEnterpriseLon());
                    EnterpriseCommodityArray.setUser_id(itemEnterpriseCommodity.getUser_id());
                    CommodityDataNoUser itemCommodityData = new CommodityDataNoUser();
                    itemCommodityData.setCommodityId(itemEnterpriseCommodity.getCommodityId());
                    itemCommodityData.setCommodityTitle(itemEnterpriseCommodity.getCommodityTitle());
                    itemCommodityData.setCommodityName(itemEnterpriseCommodity.getCommodityName());
                    itemCommodityData.setCommodityPrice(itemEnterpriseCommodity.getCommodityPrice());
                    itemCommodityData.setCreateTime(itemEnterpriseCommodity.getCreateTime());
                    itemCommodityData.setCommodityFlag(itemEnterpriseCommodity.getCommodityFlag());
                    itemCommodityData.setCommodityFlagTime(itemEnterpriseCommodity.getCommodityFlagTime());
                    itemCommodityData.setCommodityDescribe(itemEnterpriseCommodity.getCommodityDescribe());
                    itemCommodityData.setOwner(itemEnterpriseCommodity.getOwner());
                    itemCommodityData.setNewId(itemEnterpriseCommodity.getNewId());
                    itemCommodityData.setCommodityImgs(itemEnterpriseCommodity.getCommodityImgs());
                    itemCommodityData.setCommodityVariety(itemEnterpriseCommodity.getCommodityVariety());
                    itemCommodityData.setCommodityVariety_1(itemEnterpriseCommodity.getCommodityVariety_1());
                    itemCommodityData.setCommodityVariety_2(itemEnterpriseCommodity.getCommodityVariety_2());
                    EnterpriseCommodityArray.getCommodityData().add(itemCommodityData);

                    lastEnterpriseCommodity = itemEnterpriseCommodity;
                    mEnterpriseCommodityArrayTmp.add(EnterpriseCommodityArray);
                    j = j + 1;
                } else {
                    if (itemEnterpriseCommodity.getEnterpriseId().equals(lastEnterpriseCommodity.getEnterpriseId())) {
                        CommodityDataNoUser itemCommodityData = new CommodityDataNoUser();
                        itemCommodityData.setCommodityId(itemEnterpriseCommodity.getCommodityId());
                        itemCommodityData.setCommodityTitle(itemEnterpriseCommodity.getCommodityTitle());
                        itemCommodityData.setCommodityName(itemEnterpriseCommodity.getCommodityName());
                        itemCommodityData.setCommodityPrice(itemEnterpriseCommodity.getCommodityPrice());
                        itemCommodityData.setCreateTime(itemEnterpriseCommodity.getCreateTime());
                        itemCommodityData.setCommodityFlag(itemEnterpriseCommodity.getCommodityFlag());
                        itemCommodityData.setCommodityFlagTime(itemEnterpriseCommodity.getCommodityFlagTime());
                        itemCommodityData.setCommodityDescribe(itemEnterpriseCommodity.getCommodityDescribe());
                        itemCommodityData.setOwner(itemEnterpriseCommodity.getOwner());
                        itemCommodityData.setNewId(itemEnterpriseCommodity.getNewId());
                        itemCommodityData.setCommodityImgs(itemEnterpriseCommodity.getCommodityImgs());
                        itemCommodityData.setCommodityVariety(itemEnterpriseCommodity.getCommodityVariety());
                        itemCommodityData.setCommodityVariety_1(itemEnterpriseCommodity.getCommodityVariety_1());
                        itemCommodityData.setCommodityVariety_2(itemEnterpriseCommodity.getCommodityVariety_2());
                        mEnterpriseCommodityArrayTmp.get(j - 1).getCommodityData().add(itemCommodityData);
                    } else {
                        EnterpriseCommodityArray EnterpriseCommodityArray = new EnterpriseCommodityArray();
                        EnterpriseCommodityArray.setEnterpriseId(itemEnterpriseCommodity.getEnterpriseId());
                        EnterpriseCommodityArray.setEnterpriseName(itemEnterpriseCommodity.getEnterpriseName());
                        EnterpriseCommodityArray.setEnterpriseBusScrope(itemEnterpriseCommodity.getEnterpriseBusScrope());
                        EnterpriseCommodityArray.setEnterpriseLevel(itemEnterpriseCommodity.getEnterpriseLevel());
                        EnterpriseCommodityArray.setEnterpriseTelephone(itemEnterpriseCommodity.getEnterpriseTelephone());
                        EnterpriseCommodityArray.setEnterpriseFlag(itemEnterpriseCommodity.getEnterpriseFlag());
                        EnterpriseCommodityArray.setEnterpriseIntroduce(itemEnterpriseCommodity.getEnterpriseIntroduce());
                        EnterpriseCommodityArray.setEnterpriseProvince(itemEnterpriseCommodity.getEnterpriseProvince());
                        EnterpriseCommodityArray.setEnterpriseCity(itemEnterpriseCommodity.getEnterpriseCity());
                        EnterpriseCommodityArray.setEnterpriseZone(itemEnterpriseCommodity.getEnterpriseZone());
                        EnterpriseCommodityArray.setEnterpriseAddressDetail(itemEnterpriseCommodity.getEnterpriseAddressDetail());
                        EnterpriseCommodityArray.setEnterpriseLat(itemEnterpriseCommodity.getEnterpriseLat());
                        EnterpriseCommodityArray.setEnterpriseLon(itemEnterpriseCommodity.getEnterpriseLon());
                        EnterpriseCommodityArray.setUser_id(itemEnterpriseCommodity.getUser_id());
                        CommodityDataNoUser itemCommodityData = new CommodityDataNoUser();
                        itemCommodityData.setCommodityId(itemEnterpriseCommodity.getCommodityId());
                        itemCommodityData.setCommodityTitle(itemEnterpriseCommodity.getCommodityTitle());
                        itemCommodityData.setCommodityName(itemEnterpriseCommodity.getCommodityName());
                        itemCommodityData.setCommodityPrice(itemEnterpriseCommodity.getCommodityPrice());
                        itemCommodityData.setCreateTime(itemEnterpriseCommodity.getCreateTime());
                        itemCommodityData.setCommodityFlag(itemEnterpriseCommodity.getCommodityFlag());
                        itemCommodityData.setCommodityFlagTime(itemEnterpriseCommodity.getCommodityFlagTime());
                        itemCommodityData.setCommodityDescribe(itemEnterpriseCommodity.getCommodityDescribe());
                        itemCommodityData.setOwner(itemEnterpriseCommodity.getOwner());
                        itemCommodityData.setNewId(itemEnterpriseCommodity.getNewId());
                        itemCommodityData.setCommodityImgs(itemEnterpriseCommodity.getCommodityImgs());
                        itemCommodityData.setCommodityVariety(itemEnterpriseCommodity.getCommodityVariety());
                        itemCommodityData.setCommodityVariety_1(itemEnterpriseCommodity.getCommodityVariety_1());
                        itemCommodityData.setCommodityVariety_2(itemEnterpriseCommodity.getCommodityVariety_2());
                        EnterpriseCommodityArray.getCommodityData().add(itemCommodityData);
                        lastEnterpriseCommodity = itemEnterpriseCommodity;
                        mEnterpriseCommodityArrayTmp.add(EnterpriseCommodityArray);
                        j = j + 1;
                    }
                }
            }
            mHandler.post(updateList);
        }

        @Override
        public void onError(String error) {

        }

        @Override
        public void onFail() {

        }
    };

    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            if (PageNo == 0) {
                mEnterpriseCommodityArray.clear();
            }
            mEnterpriseCommodityArray.addAll(mEnterpriseCommodityArrayTmp);
            mEnterpriseAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
            isLoading = false;
        }
    };
    public void Search(String text) {
        PageNo = 0;
        Str_search = text;
        getData(Str_search);
    }

    @Override
    public void onRefresh() {
        PageNo = 0;
        getData(Str_search);
    }
}