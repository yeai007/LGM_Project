package com.hopeofseed.hopeofseed.SearchFragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hopeofseed.hopeofseed.Adapter.HuodongListAdapter;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.HuodongData;

import com.hopeofseed.hopeofseed.JNXDataTmp.HuodongDataTmp;

import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 产量
 */
public class HuodongFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private int position;
    RecyclerView recycler_list;
    HuodongListAdapter mAdapter;
    ArrayList<HuodongData> mList = new ArrayList<>();
    ArrayList<HuodongData> mListTmp = new ArrayList<>();
    static String Str_search = "";
    Handler mHandler = new Handler();
    int PageNo = 0;
    boolean isLoading = false;
    static boolean isSearch = false;
    static String UserId = "";
    private SwipeRefreshLayout mRefreshLayout;


    public static HuodongFragment newInstance(String strSearch, String userId) {
        if (TextUtils.isEmpty(userId)) {
            isSearch = true;
        } else {
            isSearch = false;
            UserId = userId;
        }
        HuodongFragment f = new HuodongFragment();
        Bundle b = new Bundle();
        b.putString("STR_SEARCH", strSearch);
        b.putString("userId", userId);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Str_search = getArguments().getString("STR_SEARCH");
            position = getArguments().getInt("userId");
        }    if(TextUtils.isEmpty(Str_search))
        {
            Str_search="";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_near_huodong, null);
        initView(v);
        getData(Str_search);
        return v;
    }

    private void initView(View v) {
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        recycler_list = (RecyclerView) v.findViewById(R.id.recycler_list);
        recycler_list.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);
        mAdapter = new HuodongListAdapter(getActivity(), mList);
        recycler_list.setAdapter(mAdapter);
        //滚动监听，在滚动监听里面去实现加载更多的功能
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    if (!isLoading) {//一个布尔的变量，默认是false
                        isLoading = true;
                        PageNo = PageNo + 1;
                        getData(Str_search);
                    } else {
                        //当没有更多的数据的时候去掉加载更多的布局
/*                        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recy_news.getAdapter();
                        adapter.setIsNeedMore(false);
                        adapter.notifyDataSetChanged();*/
                    }
                }
            }
        });
    }

    private void getData(String Str_search) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", isSearch ? "0" : UserId);
        opt_map.put("PageNo", String.valueOf(PageNo));
        opt_map.put("StrSearch", TextUtils.isEmpty(Str_search) ? "" : Str_search);
        HttpUtils hu = new HttpUtils();

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
            if (PageNo == 0) {
                mList.clear();
            }
            mList.addAll(mListTmp);
            mAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
            isLoading = false;

        }
    };

    public void Search(String text) {
        PageNo = 0;
        Str_search = text;
        getData(Str_search);
    }

    @Override
    public void onRefresh() {
        PageNo = 0;
        getData(Str_search);
    }
}