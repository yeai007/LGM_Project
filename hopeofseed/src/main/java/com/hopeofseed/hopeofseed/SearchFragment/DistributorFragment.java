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

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Activitys.DistributorActivity;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Adapter.CropDataAdapter;
import com.hopeofseed.hopeofseed.Adapter.DistributorDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;

import com.hopeofseed.hopeofseed.JNXData.DistributorData;

import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorDataTmp;
import com.hopeofseed.hopeofseed.R;
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
     ArrayList<DistributorData> arr_DistributorData = new ArrayList<>();
     ArrayList<DistributorData> arr_DistributorDataTmp = new ArrayList<>();
    static String Str_search="";


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
        mDistributorDataAdapter = new DistributorDataAdapter(getActivity(), arr_DistributorData);
        lv_list.setAdapter(mDistributorDataAdapter);
        lv_list.setOnItemClickListener(myListener);
    }

    private  void getData(String Str_search) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("StrSearch", Str_search);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchDistributor.php", opt_map, DistributorDataTmp.class, netCallBack);
    }

     NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            DistributorDataTmp mDistributorDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_DistributorDataTmp = mDistributorDataTmp.getDetail();
            updateView();
        }

        @Override
        public void onError(String error) {

        }

        @Override
        public void onFail() {

        }
    };

    private  void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private  Handler updateViewHandle = new Handler() {
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

    public  void Search(String text) {
        Str_search = text;
        getData(Str_search);
    }
}