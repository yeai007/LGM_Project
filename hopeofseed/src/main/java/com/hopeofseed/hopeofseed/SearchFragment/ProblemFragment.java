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
import com.hopeofseed.hopeofseed.Adapter.ProblemDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.ProblemData;
import com.hopeofseed.hopeofseed.JNXDataTmp.ProblemDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;
import java.util.ArrayList;
import java.util.HashMap;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 问题
 */
public class ProblemFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recy_list;
    ProblemDataAdapter mProblemDataAdapter;
    ArrayList<ProblemData> arr_ProblemData = new ArrayList<>();
    ArrayList<ProblemData> arr_ProblemDataTmp = new ArrayList<>();
    static String Str_search = "";
    String UserId = "";
    private SwipeRefreshLayout mRefreshLayout;
    int PageNo = 0;
    Handler mHandler = new Handler();
    boolean isLoading = false;
    boolean isSearch = false;
    public static ProblemFragment newInstance( String strSearch, String userId) {
        ProblemFragment f = new ProblemFragment();
        Bundle b = new Bundle();
        b.putString("strSearch", strSearch);
        b.putString("userId", userId);
        f.setArguments(b);
        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Str_search = getArguments().getString("strSearch");

            if (TextUtils.isEmpty(getArguments().getString("userId"))) {
                isSearch = true;
            } else {
                isSearch = false;
                UserId = getArguments().getString("userId");
            }

        }    if(TextUtils.isEmpty(Str_search))
        {
            Str_search="";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.search_list_problem_fragment, null);
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
        mProblemDataAdapter = new ProblemDataAdapter(getContext(), arr_ProblemData);
        recy_list.setAdapter(mProblemDataAdapter);
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
        Log.e(TAG, "getData: 获取问题数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", isSearch ? "0" : UserId);
        opt_map.put("PageNo", String.valueOf(PageNo));
        opt_map.put("StrSearch", TextUtils.isEmpty(Str_search) ? "" : Str_search);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchProblemResult.php", opt_map, ProblemDataTmp.class, netCallBack);
    }

    NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            ProblemDataTmp mProblemDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_ProblemDataTmp = mProblemDataTmp.getDetail();
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
                arr_ProblemData.clear();
            }
            arr_ProblemData.addAll(arr_ProblemDataTmp);
            mProblemDataAdapter.notifyDataSetChanged();
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