package com.hopeofseed.hopeofseed.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Adapter.EnterpriseAdapter;
import com.hopeofseed.hopeofseed.Adapter.SelectEnterpriseAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityDataNoUser;
import com.hopeofseed.hopeofseed.JNXData.EnterpriseCommodity;
import com.hopeofseed.hopeofseed.JNXData.EnterpriseCommodityArray;
import com.hopeofseed.hopeofseed.JNXData.EnterpriseData;
import com.hopeofseed.hopeofseed.JNXDataTmp.EnterpriseCommodityTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 企业
 */
public class EnterpriseFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String STR_SEARCH = "STR_SEARCH";
    private int PageNo = 0;
    private int position;
    PullToRefreshListView lv_list;
    SelectEnterpriseAdapter mEnterpriseAdapter;
    ArrayList<EnterpriseData> arr_EnterpriseData = new ArrayList<>();
    ArrayList<EnterpriseData> arr_EnterpriseDataTmp = new ArrayList<>();
    ArrayList<EnterpriseCommodity> arrEnterpriseCommodityTmp = new ArrayList<>();
    ArrayList<EnterpriseCommodityArray> mEnterpriseCommodityArray = new ArrayList<>();
    ArrayList<EnterpriseCommodityArray> mEnterpriseCommodityArrayTmp = new ArrayList<>();
    static String Str_search = "";

    public EnterpriseFragment(String strSearch) {
        Str_search = strSearch;
    }
   /* public static EnterpriseFragment newInstance(int position, String search) {
        Str_search = search;
        EnterpriseFragment f = new EnterpriseFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
        Str_search = getArguments().getString(STR_SEARCH);
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
        mEnterpriseAdapter = new SelectEnterpriseAdapter(getActivity(), mEnterpriseCommodityArray);
        lv_list.setAdapter(mEnterpriseAdapter);
        lv_list.setOnItemClickListener(myListener);
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
                mEnterpriseCommodityArray.clear();
            }

            mEnterpriseCommodityArray.addAll(mEnterpriseCommodityArrayTmp);
            mEnterpriseAdapter.notifyDataSetChanged();
            lv_list.onRefreshComplete();

        }
    };
    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            intent.putExtra("userid", String.valueOf(arr_EnterpriseData.get(i - 1).getUser_id()));
            startActivity(intent);
        }
    };

    public void Search(String text) {
        Str_search = text;
        getData(Str_search);
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
}