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
import com.hopeofseed.hopeofseed.Activitys.EnterpriseActivity;
import com.hopeofseed.hopeofseed.Adapter.EnterpriseAdapter;
import com.hopeofseed.hopeofseed.Adapter.ProblemDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.EnterpriseData;
import com.hopeofseed.hopeofseed.JNXData.ProblemData;
import com.hopeofseed.hopeofseed.JNXDataTmp.EnterpriseDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.ProblemDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.id;
import static com.hopeofseed.hopeofseed.R.id.lv_distributor;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 企业
 */
public class EnterpriseFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;
    PullToRefreshListView lv_list;
     EnterpriseAdapter mEnterpriseAdapter;
     ArrayList<EnterpriseData> arr_EnterpriseData = new ArrayList<>();
     ArrayList<EnterpriseData> arr_EnterpriseDataTmp = new ArrayList<>();

    static String Str_search="";
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
        mEnterpriseAdapter = new EnterpriseAdapter(getActivity(), arr_EnterpriseData);
        lv_list.setAdapter(mEnterpriseAdapter);
        lv_list.setOnItemClickListener(myListener);
    }

    private  void getData(String Str_search) {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("StrSearch", Str_search);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchEnterpriseData.php", opt_map, EnterpriseDataTmp.class, netCallBack);
    }

     NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            EnterpriseDataTmp enterpriseDataTmp = ObjectUtil.cast(rspBaseBean);
            Log.e(TAG, "onSuccess: " + enterpriseDataTmp.toString());
            arr_EnterpriseDataTmp = enterpriseDataTmp.getDetail();
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
            arr_EnterpriseData.clear();
            arr_EnterpriseData.addAll(arr_EnterpriseDataTmp);
            mEnterpriseAdapter.notifyDataSetChanged();
        }
    };
    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), EnterpriseActivity.class);
            intent.putExtra("EnterpriseId", String.valueOf(arr_EnterpriseData.get(i - 1).getEnterpriseId()));
            startActivity(intent);
        }
    };

    public  void Search(String text) {
        Str_search = text;
        getData(Str_search);
    }
}