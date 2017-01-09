package com.hopeofseed.hopeofseed.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Adapter.AuthorDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.AuthorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.AuthorDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 机构
 */
public class AuthorFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String STR_SEARCH = "STR_SEARCH";
    private int position;
    PullToRefreshListView lv_list;
    AuthorDataAdapter mAuthorDataAdapter;
    ArrayList<AuthorData> arr_AuthorData = new ArrayList<>();
    ArrayList<AuthorData> arr_AuthorDataTmp = new ArrayList<>();
    static String Str_search = "";
    int PageNo = 0;

    public static AuthorFragment newInstance(String strSearch) {
        AuthorFragment f = new AuthorFragment();
        Bundle b = new Bundle();
        b.putString("strSearch", strSearch);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Str_search = getArguments().getString("strSearch");
        }
        if (TextUtils.isEmpty(Str_search)) {
            Str_search = "";
        }
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
        mAuthorDataAdapter = new AuthorDataAdapter(getActivity(), arr_AuthorData);
        lv_list.setAdapter(mAuthorDataAdapter);
        lv_list.setOnItemClickListener(myListener);
    }

    private void getData(String Str_search) {
        Log.e(TAG, "getData: 获取机构数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("StrSearch", Str_search);
        opt_map.put("PageNo", String.valueOf(PageNo));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchAuthorData.php", opt_map, AuthorDataTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
            AuthorDataTmp mAuthorDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_AuthorDataTmp = mAuthorDataTmp.getDetail();
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
                arr_AuthorData.clear();
            }
            arr_AuthorData.addAll(arr_AuthorDataTmp);
            mAuthorDataAdapter.notifyDataSetChanged();
            lv_list.onRefreshComplete();
        }
    };
    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            intent.putExtra("userid", String.valueOf(arr_AuthorData.get(i - 1).getUser_id()));
            intent.putExtra("UserRole", Integer.parseInt(arr_AuthorData.get(i - 1).getUser_role()));
            startActivity(intent);
            Toast.makeText(getActivity(), arr_AuthorData.get(i - 1).getAuthorName(), Toast.LENGTH_SHORT).show();
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