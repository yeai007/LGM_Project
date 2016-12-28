package com.hopeofseed.hopeofseed.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hopeofseed.hopeofseed.Activitys.CommentNew;
import com.hopeofseed.hopeofseed.Activitys.CommodityActivity;
import com.hopeofseed.hopeofseed.Activitys.ForwardNew;
import com.hopeofseed.hopeofseed.Activitys.HaveCommentNew;
import com.hopeofseed.hopeofseed.Activitys.NewsInfoNewActivity;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Adapter.RecyclerViewAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXData.UpdateZiabamResult;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.UpdateZiabamResultTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 实时
 */
public class NowFragment extends Fragment  implements NetCallBack, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NowFragment";
    private static final String ARG_POSITION = "position";
    ArrayList<NewsData> arr_NewsData = new ArrayList<>();
    ArrayList<NewsData> arr_NewsDataTmp = new ArrayList<>();
    Handler mHandler = new Handler();
    private int PageNo = 0;
    RecyclerView recy_news;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    boolean isLoading = false;
    private SwipeRefreshLayout mRefreshLayout;
    UpdateZiabamResult mUpdateZiabamResult = new UpdateZiabamResult();
    String updatePosition;
    private int position;

    String UserId = "";

    public NowFragment(String strSearch, String userId) {
        UserId = userId;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.now_list_fragment, null);
        initView(v);
        getNewsData();
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
        recy_news = (RecyclerView) v.findViewById(R.id.recy_news);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recy_news.setLayoutManager(manager);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(), arr_NewsData, true);
        recy_news.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, NewsData data) {
                Intent intent;
                switch (view.getId()) {
                    case R.id.rel_img_user_avatar://点击头像
                        intent = new Intent(getActivity(), UserActivity.class);
                        intent.putExtra("userid", String.valueOf(data.getUser_id()));
                        intent.putExtra("UserRole", Integer.parseInt(data.getUser_role()));
                        Log.e(TAG, "onItemClick: UserRole" + String.valueOf(data.getUser_role()));
                        startActivity(intent);
                        break;
                    case R.id.rel_forward://转发

                        intent = new Intent(getActivity(), ForwardNew.class);
                        intent.putExtra("NEWID", String.valueOf(data.getId()));
                        startActivityForResult(intent, 0);
                        break;
                    case R.id.rel_comment://评论

                        int comment_count = Integer.parseInt(data.getCommentCount());
                        if (comment_count > 0) {
                            intent = new Intent(getActivity(), NewsInfoNewActivity.class);
                            intent.putExtra("NEWID", String.valueOf(data.getId()));
                            intent.putExtra("NewClass", Integer.parseInt(data.getNewclass()));
                            startActivity(intent);
                        } else {
                            intent = new Intent(getActivity(), CommentNew.class);
                            intent.putExtra("NEWID", String.valueOf(data.getId()));
                            startActivityForResult(intent, 0);
                        }
                        break;
                    case R.id.rel_zambia:
                        updatePosition = String.valueOf(data.getId());
                        UpdateZambia(String.valueOf(data.getId()));
                        break;
                    case R.id.user_name:
                        intent = new Intent(getActivity(), UserActivity.class);
                        intent.putExtra("userid", data.getUser_id());
                        intent.putExtra("UserRole", Integer.parseInt(data.getUser_role()));
                        startActivity(intent);
                        break;
                    case R.id.tv_title:
                        Log.e(TAG, "onItemClick: tv_title");
                        intent = new Intent(getActivity(), NewsInfoNewActivity.class);
                        intent.putExtra("NEWID", String.valueOf(data.getId()));
                        intent.putExtra("NewClass", Integer.parseInt(data.getNewclass()));
                        startActivity(intent);
                        break;
                    case R.id.tv_content:
                        Log.e(TAG, "onItemClick: tv_content");
                        if (Integer.valueOf(data.getNewclass()) == 6) {
                            intent = new Intent(getActivity(), CommodityActivity.class);
                            intent.putExtra("CommodityId", data.getInfoid());
                            startActivity(intent);
                        } else {
                            Log.e(TAG, "onClick: " + data.getNewclass());
                            intent = new Intent(getActivity(), NewsInfoNewActivity.class);
                            intent.putExtra("NEWID", String.valueOf(data.getId()));
                            intent.putExtra("NewClass", Integer.parseInt(data.getNewclass()));
                            startActivity(intent);

                        }
                        break;
                    case R.id.rel_content:
                        Log.e(TAG, "onItemClick: rel_content");
                        if (Integer.valueOf(data.getNewclass()) == 6) {
                            intent = new Intent(getActivity(), CommodityActivity.class);
                            intent.putExtra("CommodityId", data.getInfoid());
                            startActivity(intent);
                        } else {
                            Log.e(TAG, "onClick: " + data.getNewclass());
                            intent = new Intent(getActivity(), NewsInfoNewActivity.class);
                            intent.putExtra("NEWID", String.valueOf(data.getId()));
                            intent.putExtra("NewClass", Integer.parseInt(data.getNewclass()));
                            startActivity(intent);

                        }
                        break;
                    case R.id.rel_share_new:
                        Log.e(TAG, "onItemClick: rel_share_new");
                        intent = new Intent(getActivity(), HaveCommentNew.class);
                        intent.putExtra("NEWID", String.valueOf(data.getFromid()));
                        startActivity(intent);

                        break;
                }
            }
        });

        //滚动监听，在滚动监听里面去实现加载更多的功能
        recy_news.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItem = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                int totalItemCount = manager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    if (!isLoading) {//一个布尔的变量，默认是false
                        Log.e(TAG, "onScrolled: loadingmaore");
                        isLoading = true;
                        PageNo = PageNo + 1;
                        refreshData();
                    } else if (arr_NewsDataTmp.size() < 20) {
                        //当没有更多的数据的时候去掉加载更多的布局
/*                        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recy_news.getAdapter();
                        adapter.setIsNeedMore(false);
                        adapter.notifyDataSetChanged();*/
                    }
                }
            }
        });
    }
    public void UpdateZambia(final String position) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("NewId", position);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "UpdateNewZambia.php", opt_map, UpdateZiabamResultTmp.class, this);
    }
    public void refreshData() {
        getNewsData();
    }
    public void getNewsData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", UserId);
        opt_map.put("PageNo", String.valueOf(PageNo));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getNowNewsByUserId.php", opt_map, NewsDataTmp.class, this);
    }

    @Override
    public void onRefresh() {
        getNewsData();
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("UpdateNewZambia")) {
            UpdateZiabamResultTmp mUpdateZiabamResultTmp = ObjectUtil.cast(rspBaseBean);

            mUpdateZiabamResult = mUpdateZiabamResultTmp.getDetail().get(0);
            mHandler.post(updateZiabam);
        } else {
            arr_NewsDataTmp = ((NewsDataTmp) rspBaseBean).getDetail();
            mHandler.post(updateList);
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }
    Runnable updateZiabam = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < arr_NewsData.size(); i++) {
                if (arr_NewsData.get(i).getId() == Integer.parseInt(updatePosition)) {
                    if (arr_NewsData.get(i).getZambiaCount() == 0) {
                        if (mUpdateZiabamResult.getFlag().equals("0")) {
                            arr_NewsData.get(i).setZambiaCount(1);
                        }

                    } else {
                        if (mUpdateZiabamResult.getFlag().equals("0")) {
                            arr_NewsData.get(i).setZambiaCount(arr_NewsData.get(i).getZambiaCount() + 1);
                        } else {
                            arr_NewsData.get(i).setZambiaCount(arr_NewsData.get(i).getZambiaCount() - 1);
                        }

                    }
                    mRecyclerViewAdapter.setList(arr_NewsData);
                    mRecyclerViewAdapter.notifyItemChanged(i);
                }
            }

        }
    };
    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            if (PageNo == 0) {
                arr_NewsData.clear();
            }
            arr_NewsData.addAll(arr_NewsDataTmp);
            mRecyclerViewAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
            isLoading = false;
        }

    };
}