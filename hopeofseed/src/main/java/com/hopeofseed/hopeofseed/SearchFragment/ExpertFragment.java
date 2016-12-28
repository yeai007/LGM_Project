package com.hopeofseed.hopeofseed.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Adapter.SelectExpertDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.ExpertEnterperiseData;
import com.hopeofseed.hopeofseed.JNXDataTmp.ExpertEnterperiseDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 专家
 */
public class ExpertFragment extends Fragment {
    private static final String TAG = "ExpertFragment";
    private static final String ARG_POSITION = "position";
    private static final String STR_SEARCH = "STR_SEARCH";
    int PageNo = 0;
    private int position;
    PullToRefreshListView lv_list;
    SelectExpertDataAdapter mExpertDataAdapter;
    ArrayList<ExpertEnterperiseData> arr_ExpertData = new ArrayList<>();
    ArrayList<ExpertEnterperiseData> arr_ExpertDataTmp = new ArrayList<>();
    static String Str_search = "";

    public ExpertFragment(String strSearch) {
        Str_search = strSearch;
    }


  /*  public static ExpertFragment newInstance(int position, String search) {
        Str_search = search;
        ExpertFragment f = new ExpertFragment();
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
        mExpertDataAdapter = new SelectExpertDataAdapter(getActivity(), arr_ExpertData);
        lv_list.setAdapter(mExpertDataAdapter);
        // lv_list.setOnItemClickListener(myListener);
    }

    private void getData(String Str_search) {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("PageNo", String.valueOf(PageNo));
        opt_map.put("StrSearch", Str_search);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchExpertData.php", opt_map, ExpertEnterperiseDataTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
            ExpertEnterperiseDataTmp mExpertDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_ExpertDataTmp = mExpertDataTmp.getDetail();
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
            if (PageNo == 0) {
                arr_ExpertData.clear();
            }

            arr_ExpertData.addAll(arr_ExpertDataTmp);
            Log.e(TAG, "handleMessage: updateview" + arr_ExpertData.size());
            mExpertDataAdapter.notifyDataSetChanged();
            lv_list.onRefreshComplete();
        }
    };
    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            intent.putExtra("userid", String.valueOf(arr_ExpertData.get(i - 1).getUser_id()));
            intent.putExtra("UserRole", Integer.parseInt(arr_ExpertData.get(i - 1).getUser_role()));
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