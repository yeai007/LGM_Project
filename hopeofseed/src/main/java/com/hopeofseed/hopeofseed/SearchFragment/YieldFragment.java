package com.hopeofseed.hopeofseed.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.hopeofseed.hopeofseed.Activitys.ExpertActivity;
import com.hopeofseed.hopeofseed.Activitys.NewsInfoActivity;
import com.hopeofseed.hopeofseed.Activitys.NewsInfoNewActivity;
import com.hopeofseed.hopeofseed.Activitys.SearchInfoActivity;
import com.hopeofseed.hopeofseed.Activitys.YieldActivity;
import com.hopeofseed.hopeofseed.Adapter.CropDataAdapter;
import com.hopeofseed.hopeofseed.Adapter.ExperienceDataAdapter;
import com.hopeofseed.hopeofseed.Adapter.YieldDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.YieldData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.YieldDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.hopeofseed.hopeofseed.R.id.lv_list;
import static com.hopeofseed.hopeofseed.SearchFragment.ExperienceFragment.arr_ExperienceData;
import static com.hopeofseed.hopeofseed.SearchFragment.ExperienceFragment.arr_ExperienceDataTmp;
import static com.hopeofseed.hopeofseed.SearchFragment.ExperienceFragment.mExperienceDataAdapter;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 产量
 */
public class YieldFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_POSITION = "position";
    private static final String STR_SEARCH = "STR_SEARCH";
    private int position;
    RecyclerView recy_list;
    YieldDataAdapter mYieldDataAdapter;
    ArrayList<YieldData> arr_YieldData = new ArrayList<>();
    ArrayList<YieldData> arr_YieldDataTmp = new ArrayList<>();
    static String Str_search = "";
    String UserId = "";
    private SwipeRefreshLayout mRefreshLayout;
    int PageNo = 0;
    Handler mHandler = new Handler();
    boolean isLoading = false;
    boolean isSearch = false;

    public YieldFragment(String strSearch, String userId) {
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
        Str_search = getArguments().getString(STR_SEARCH);
        position = getArguments().getInt(ARG_POSITION);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.search_list_yield_fragment, null);
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
        mYieldDataAdapter = new YieldDataAdapter(getContext(), arr_YieldData);
        recy_list.setAdapter(mYieldDataAdapter);
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
        Log.e(TAG, "getData: 获取产量表现数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", isSearch ? "0" : UserId);
        opt_map.put("PageNo", String.valueOf(PageNo));
        opt_map.put("StrSearch", TextUtils.isEmpty(Str_search) ? "" : Str_search);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchYieldResult.php", opt_map, YieldDataTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
            YieldDataTmp mYieldDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_YieldDataTmp = mYieldDataTmp.getDetail();
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
                arr_YieldData.clear();
            }
            arr_YieldData.addAll(arr_YieldDataTmp);
            mYieldDataAdapter.notifyDataSetChanged();
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