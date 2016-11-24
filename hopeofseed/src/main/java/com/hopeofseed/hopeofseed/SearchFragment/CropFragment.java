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
import com.hopeofseed.hopeofseed.Activitys.CropActivity;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Adapter.CropDataAdapter;
import com.hopeofseed.hopeofseed.Adapter.DistributorAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.hopeofseed.hopeofseed.R.id.lv_distributor;
import static com.hopeofseed.hopeofseed.R.id.tv_name;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 品种
 */
public class CropFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String STR_SEARCH = "STR_SEARCH";
    private static final String STR_FAXIAN = "STR_FAXIAN";
    private int position;
    PullToRefreshListView lv_list;
    CropDataAdapter mCropDataAdapter;
    ArrayList<CropData> arr_CropData = new ArrayList<>();
    ArrayList<CropData> arr_CropDataTmp = new ArrayList<>();
    static String Str_search = "";
    private int Str_Faxian;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Str_search = getArguments().getString(STR_SEARCH);
        position = getArguments().getInt(ARG_POSITION);
        Str_Faxian = getArguments().getInt(STR_FAXIAN);
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
        mCropDataAdapter = new CropDataAdapter(getActivity(), arr_CropData);
        lv_list.setAdapter(mCropDataAdapter);
        lv_list.setOnItemClickListener(myListener);
    }

    private void getData(String Str_search) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("StrSearch", Str_search);
        opt_map.put("Str_Faxian", String.valueOf(Str_Faxian));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchCropResult.php", opt_map, CropDataTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            CropDataTmp mCropDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_CropDataTmp = mCropDataTmp.getDetail();
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
            arr_CropData.clear();
            arr_CropData.addAll(arr_CropDataTmp);
            mCropDataAdapter.notifyDataSetChanged();
        }
    };
       private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), CropActivity.class);
            intent.putExtra("CropId", String.valueOf(arr_CropDataTmp.get(i - 1).getCropId()));
            startActivity(intent);
        }
    };

    public void Search(String text) {
        Str_search = text;
        getData(Str_search);
    }
}