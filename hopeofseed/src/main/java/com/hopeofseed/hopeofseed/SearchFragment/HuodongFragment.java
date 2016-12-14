package com.hopeofseed.hopeofseed.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Activitys.NewsInfoNewActivity;
import com.hopeofseed.hopeofseed.Adapter.HuodongListAdapter;
import com.hopeofseed.hopeofseed.Adapter.YieldDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.HuodongData;
import com.hopeofseed.hopeofseed.JNXData.YieldData;
import com.hopeofseed.hopeofseed.JNXDataTmp.HuodongDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.YieldDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.hopeofseed.hopeofseed.R.id.lv_list;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 产量
 */
public class HuodongFragment extends Fragment {
    private static final String ARG_POSITION = "position";    private static final String STR_SEARCH = "STR_SEARCH";
    private int position;
    RecyclerView recycler_list;
    HuodongListAdapter mAdapter;
    ArrayList<HuodongData> mList = new ArrayList<>();
    ArrayList<HuodongData> mListTmp = new ArrayList<>();
    static  String Str_search="";
    Handler mHandler = new Handler();
    public HuodongFragment(String strSearch) {
        Str_search=strSearch;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Str_search = getArguments().getString(STR_SEARCH);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_near_huodong, null);
        initView(v);
        getData(Str_search);
        return v;
    }

    private void initView(View v) {
        recycler_list = (RecyclerView) v.findViewById(R.id.recycler_list);
        recycler_list.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);
        mAdapter = new HuodongListAdapter(getActivity(), mList);
        recycler_list.setAdapter(mAdapter);
    }

    private void getData(String Str_search) {
        HashMap<String, String> opt_map = new HashMap<>();
        HttpUtils hu = new HttpUtils();
        opt_map.put("StrSearch", Str_search);
        hu.httpPost(Const.BASE_URL + "GetSearchHuoDongResult.php", opt_map, HuodongDataTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            HuodongDataTmp mHuodongDataTmp = ObjectUtil.cast(rspBaseBean);
            mListTmp = mHuodongDataTmp.getDetail();
            mHandler.post(updatelist);
        }

        @Override
        public void onError(String error) {

        }

        @Override
        public void onFail() {

        }
    };

    Runnable updatelist = new Runnable() {
        @Override
        public void run() {
            if (mListTmp.size() > 0) {
                mList.clear();
                mList.addAll(mListTmp);
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    public void Search(String text) {
        Str_search = text;
        getData(Str_search);
    }
}