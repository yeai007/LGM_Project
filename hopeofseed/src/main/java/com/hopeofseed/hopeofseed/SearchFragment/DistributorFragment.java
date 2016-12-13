package com.hopeofseed.hopeofseed.SearchFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
public class DistributorFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String STR_SEARCH = "STR_SEARCH";
    private int position;
    PullToRefreshListView lv_list;
    DistributorDataAdapter mDistributorDataAdapter;

    ArrayList<DistributorCommodity> arr_DistributorDataTmp = new ArrayList<>();
    static String Str_search = "";
    ArrayList<DistributorCommodityArray> mArrDistributorCommodityArray = new ArrayList<>();
    ArrayList<DistributorCommodityArray> mArrDistributorCommodityArrayTmp = new ArrayList<>();
    /*    public static  DistributorFragment newInstance(int position, String search) {
            Str_search = search;

            DistributorFragment f = new DistributorFragment();
            Bundle b = new Bundle();
            b.putInt(ARG_POSITION, position);
            f.setArguments(b);
            return f;
        }*/
    private int PageNo = 0;

    public DistributorFragment(String strSearch) {Str_search=strSearch;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Str_search = getArguments().getString(STR_SEARCH);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.search_list_fragment, null);
        initView(v);
        initDatas(v);
        getData(Str_search);

        return v;
    }

    private void initView(View v) {
        lv_list = (PullToRefreshListView) v.findViewById(R.id.lv_list);
        lv_list.setMode(PullToRefreshBase.Mode.BOTH);
        mDistributorDataAdapter = new DistributorDataAdapter(getActivity(), mArrDistributorCommodityArray);
        lv_list.setAdapter(mDistributorDataAdapter);
/*        lv_list.setOnItemClickListener(myListener);*/
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

    private void initDatas(View v) {
        lv_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // 下拉刷新 业务代码
                if (refreshView.isShownHeader()) {
                    lv_list.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_list.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    lv_list.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    PageNo = 0;
                    getData(Str_search);
                }
                // 上拉加载更多 业务代码
                if (refreshView.isShownFooter()) {
                    lv_list.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    lv_list.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    lv_list.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    PageNo = PageNo + 1;
                    getData(Str_search);
                }

            }
        });
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
                mArrDistributorCommodityArray.clear();
            }
            mArrDistributorCommodityArray.addAll(mArrDistributorCommodityArrayTmp);
            mDistributorDataAdapter.notifyDataSetChanged();
            lv_list.onRefreshComplete();
        }
    };
    public void Search(String text) {
        Str_search = text;
        getData(Str_search);
    }
}