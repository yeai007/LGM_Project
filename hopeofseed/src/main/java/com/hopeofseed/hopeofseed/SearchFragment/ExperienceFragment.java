package com.hopeofseed.hopeofseed.SearchFragment;


import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hopeofseed.hopeofseed.Adapter.ExperienceDataAdapter;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.ExperienceData;

import com.hopeofseed.hopeofseed.JNXDataTmp.ExperienceDataTmp;

import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 农技经验
 */
public class ExperienceFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ExperienceFragment";
    private static final String ARG_POSITION = "position";
    private static final String STR_SEARCH = "STR_SEARCH";

    private int position;
    RecyclerView recy_list;
    static ExperienceDataAdapter mExperienceDataAdapter;
    static ArrayList<ExperienceData> arr_ExperienceData = new ArrayList<>();
    static ArrayList<ExperienceData> arr_ExperienceDataTmp = new ArrayList<>();
    static String Str_search = "";
    String UserId = "";
    private SwipeRefreshLayout mRefreshLayout;
    int PageNo = 0;
    Handler mHandler = new Handler();
    boolean isLoading = false;
    boolean isSearch = false;

    public ExperienceFragment(String strSearch, String userId) {
        Str_search = strSearch;
        if (TextUtils.isEmpty(userId)) {
            isSearch = true;
        } else {
            isSearch = false;
            UserId = userId;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
        Str_search = getArguments().getString(STR_SEARCH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.search_list_experience_fragment, null);
        initList(v);
        getData(Str_search);
        return v;
    }

    private void initList(View v) {
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        recy_list = (RecyclerView) v.findViewById(R.id.recy_list);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recy_list.setLayoutManager(manager);
        mExperienceDataAdapter = new ExperienceDataAdapter(getContext(), arr_ExperienceData);
        recy_list.setAdapter(mExperienceDataAdapter);
        //滚动监听，在滚动监听里面去实现加载更多的功能
        recy_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                int totalItemCount = manager.getItemCount();
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
        Log.e(TAG, "getData: 获取农技经验数据");
        HashMap<String, String> opt_map = new HashMap<>();

        opt_map.put("UserId", isSearch ? "0" : UserId);
        opt_map.put("PageNo", String.valueOf(PageNo));
        opt_map.put("StrSearch", TextUtils.isEmpty(Str_search) ? "" : Str_search);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchExperienceResult.php", opt_map, ExperienceDataTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
            ExperienceDataTmp mExperienceDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_ExperienceDataTmp = mExperienceDataTmp.getDetail();
            mHandler.post(updateList);
        }

        @Override
        public void onError(String error) {

        }

        @Override
        public void onFail() {

        }
    };

    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            if (PageNo == 0) {
                arr_ExperienceData.clear();
            }
            arr_ExperienceData.addAll(arr_ExperienceDataTmp);
            mExperienceDataAdapter.notifyDataSetChanged();
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