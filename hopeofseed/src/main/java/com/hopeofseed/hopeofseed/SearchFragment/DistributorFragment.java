package com.hopeofseed.hopeofseed.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Activitys.DistributorActivity;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Adapter.CropDataAdapter;
import com.hopeofseed.hopeofseed.Adapter.DistributorDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;

import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.JNXData.CommodityDataNoUser;
import com.hopeofseed.hopeofseed.JNXData.DistributorCommodity;

import com.hopeofseed.hopeofseed.JNXData.DistributorCommodityArray;
import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorCommodityTmp;

import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.util.NullStringToEmptyAdapterFactory;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;


/**
 * 经销商
 */
public class DistributorFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;
    PullToRefreshListView lv_list;
    DistributorDataAdapter mDistributorDataAdapter;
    ArrayList<DistributorCommodity> arr_DistributorData = new ArrayList<>();
    ArrayList<DistributorCommodity> arr_DistributorDataTmp = new ArrayList<>();
    static String Str_search = "";
    ArrayList<DistributorCommodityArray> mArrDistributorCommodityArray = new ArrayList<>();

/*    public static  DistributorFragment newInstance(int position, String search) {
        Str_search = search;

        DistributorFragment f = new DistributorFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.search_list_fragment, null);
        initView(v);
        getData(Str_search);
        return v;
    }

    private void initView(View v) {
        lv_list = (PullToRefreshListView) v.findViewById(R.id.lv_list);
        mDistributorDataAdapter = new DistributorDataAdapter(getActivity(), mArrDistributorCommodityArray);
        lv_list.setAdapter(mDistributorDataAdapter);
        lv_list.setOnItemClickListener(myListener);
    }

    private void getData(String Str_search) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("StrSearch", Str_search);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchDistributor.php", opt_map, DistributorCommodityTmp.class, netCallBack);
    }


    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            DistributorCommodityTmp mDistributorDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_DistributorDataTmp = mDistributorDataTmp.getDetail();
            DistributorCommodity lastDistributorCommodity = new DistributorCommodity();
            int j=0;
            for (int i = 0; i < arr_DistributorDataTmp.size(); i++) {
                DistributorCommodity itemDistributorCommodity = new DistributorCommodity();
                itemDistributorCommodity = arr_DistributorDataTmp.get(i);
                if (i == 0) {
                    DistributorCommodityArray distributorCommodityArray = new DistributorCommodityArray();
                    distributorCommodityArray.setDistributorId(itemDistributorCommodity.getDistributorId());
                    distributorCommodityArray.setDistributorName(itemDistributorCommodity.getCommodityName());
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
                    mArrDistributorCommodityArray.add(distributorCommodityArray);
                    j=j+1;
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
                        mArrDistributorCommodityArray.get(j - 1).getCommodityData().add(itemCommodityData);
                    } else {
                        DistributorCommodityArray distributorCommodityArray = new DistributorCommodityArray();
                        distributorCommodityArray.setDistributorId(itemDistributorCommodity.getDistributorId());
                        distributorCommodityArray.setDistributorName(itemDistributorCommodity.getCommodityName());
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
                        mArrDistributorCommodityArray.add(distributorCommodityArray);
                        j=j+1;
                    }
                }
            }


            Gson gson= new GsonBuilder()
                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();
            String aa=gson.toJson(mArrDistributorCommodityArray);
            Log.e(TAG, "onSuccess: "+aa);
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
            Log.e(TAG, "handleMessage: updateview");
            arr_DistributorData.clear();
            arr_DistributorData.addAll(arr_DistributorDataTmp);
            Log.e(TAG, "handleMessage: updateview" + arr_DistributorData.size());
            mDistributorDataAdapter.notifyDataSetChanged();
        }
    };
    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            intent.putExtra("userid", String.valueOf(arr_DistributorData.get(i - 1).getUser_id()));
            startActivity(intent);
        }
    };

    public void Search(String text) {
        Str_search = text;
        getData(Str_search);
    }
}