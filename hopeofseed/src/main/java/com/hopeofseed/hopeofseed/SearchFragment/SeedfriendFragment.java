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
import com.hopeofseed.hopeofseed.Adapter.SeedfriendDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserDataNoRealmTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 种友
 */
public class SeedfriendFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String STR_SEARCH = "STR_SEARCH";
    private int position;
    PullToRefreshListView lv_list;
    SeedfriendDataAdapter mSeedfriendDataAdapter;
    ArrayList<UserDataNoRealm> arr_UserDataNoRealm = new ArrayList<>();
    ArrayList<UserDataNoRealm> arr_UserDataNoRealmTmp = new ArrayList<>();
    static String Str_search = "";
    int PageNo = 0;

    public SeedfriendFragment(String strSearch) {
        Str_search=strSearch;
    }
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
        lv_list.setMode(BOTH);
        mSeedfriendDataAdapter = new SeedfriendDataAdapter(getActivity(), arr_UserDataNoRealm);
        lv_list.setAdapter(mSeedfriendDataAdapter);
        lv_list.setOnItemClickListener(myListener);

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

    private void getData(String Str_search) {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("StrSearch", Str_search);
        opt_map.put("PageNo", String.valueOf(PageNo));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchSeedfriendData.php", opt_map, UserDataNoRealmTmp.class, netCallBack);
    }


    private NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
            UserDataNoRealmTmp mUserDataNoRealmTmp = ObjectUtil.cast(rspBaseBean);
            arr_UserDataNoRealmTmp = mUserDataNoRealmTmp.getDetail();
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
                arr_UserDataNoRealm.clear();
            }
            arr_UserDataNoRealm.addAll(arr_UserDataNoRealmTmp);
            mSeedfriendDataAdapter.notifyDataSetChanged();
            lv_list.onRefreshComplete();
        }
    };
    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            intent.putExtra("userid", String.valueOf(arr_UserDataNoRealm.get(i - 1).getUser_id()));
            startActivity(intent);
        }
    };

    public void Search(String text) {
        Str_search = text;
        getData(Str_search);
    }

}