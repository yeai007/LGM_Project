package com.hopeofseed.hopeofseed.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Activitys.AllCommodityActivity;
import com.hopeofseed.hopeofseed.Activitys.CommodityActivity;
import com.hopeofseed.hopeofseed.Activitys.CropActivity;
import com.hopeofseed.hopeofseed.Activitys.MyCommodity;
import com.hopeofseed.hopeofseed.Adapter.CommodityListAdapter;
import com.hopeofseed.hopeofseed.Adapter.CropDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommodityDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.FollowedFriendTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 品种
 */
public class CommdityFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_POSITION = "position";

    private int position;
    PullToRefreshListView lv_list;
    CommodityListAdapter mCommodityListAdapter;
    ArrayList<CommodityData> arr_CommodityData = new ArrayList<>();
    ArrayList<CommodityData> arr_CommodityDataTmp = new ArrayList<>();
    static String Str_search = "";
    Button btn_show_all;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.commdity_list_fragment, null);
        initView(v);
        getData(Str_search);
        return v;
    }

    private void initView(View v) {
        btn_show_all = (Button) v.findViewById(R.id.btn_show_all);
        btn_show_all.setOnClickListener(this);
        lv_list = (PullToRefreshListView) v.findViewById(R.id.lv_list);
        mCommodityListAdapter = new CommodityListAdapter(getActivity(), arr_CommodityData);
        lv_list.setAdapter(mCommodityListAdapter);
        lv_list.setOnItemClickListener(myListener);
    }

    private void getData(String Str_search) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
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
            Log.e(TAG, "handleMessage: updateview");
            arr_CommodityData.clear();
            arr_CommodityData.addAll(arr_CommodityDataTmp);
            Log.e(TAG, "handleMessage: updateview" + arr_CommodityData.size());
            mCommodityListAdapter.notifyDataSetChanged();
        }
    };
    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), CommodityActivity.class);
            intent.putExtra(" CommodityId", String.valueOf(arr_CommodityData.get(i - 1).getCommodityId()));
            startActivity(intent);
        }
    };

    public void Search(String text) {
        Str_search = text;
        getData(Str_search);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_show_all:
                Intent intent = new Intent(getActivity(), AllCommodityActivity.class);
                startActivity(intent);
                break;
        }
    }
}