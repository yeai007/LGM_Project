package com.hopeofseed.hopeofseed.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.NewsListAdapter;
import com.hopeofseed.hopeofseed.Adapter.RecyclerViewAdapter;
import com.hopeofseed.hopeofseed.Adapter.Sp_TitleAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.DataForHttp.UpdateZambia;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXData.UpdateZiabamResult;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.UpdateZiabamResultTmp;
import com.hopeofseed.hopeofseed.curView.pulishDYNPopupWindow;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.data;
import static android.app.Activity.RESULT_OK;


/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/27 14:40
 * 修改人：whisper
 * 修改时间：2016/7/27 14:40
 * 修改备注：
 */
public class NewsFragment extends Fragment implements NetCallBack, SwipeRefreshLayout.OnRefreshListener {
    public static String NEWS_UPDATE_LIST = "NEWS_UPDATE_LIST";
    String TAG = "NewsFragment";
    pulishDYNPopupWindow menuWindow;
    Spinner sp_title;
    PullToRefreshListView lv_news;
    Button btn_title;
    TextView btn_topleft;
    ArrayList<String> arr_TopClass = new ArrayList<>();
    ArrayList<NewsData> arr_NewsData = new ArrayList<>();
    ArrayList<NewsData> arr_NewsDataTmp = new ArrayList<>();
    Sp_TitleAdapter sp_titleAdapter;
    NewsListAdapter newListAadpter;
    int classid = 0;
    RelativeLayout rel_search;
    Handler mHandler = new Handler();
    private int PageNo = 0;
    RecyclerView recy_news;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    boolean isLoading = false;
    private SwipeRefreshLayout mRefreshLayout;
    UpdateZiabamResult mUpdateZiabamResult = new UpdateZiabamResult();
    String updatePosition;
    private UpdateBroadcastReceiver updateBroadcastReceiver;  //刷新列表广播

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_news, null);
        initView(v);
        initSpTitle(v);
        initNews(v);
        initReceiver();
        return v;
    }

    private void initReceiver() {
        // 注册广播接收
        updateBroadcastReceiver = new UpdateBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NEWS_UPDATE_LIST);    //只有持有相同的action的接受者才能接收此广播
        getActivity().registerReceiver(updateBroadcastReceiver, filter);
    }

    private void initView(View v) {
        (v.findViewById(R.id.btn_topright)).setOnClickListener(listener);
        btn_topleft = (TextView) v.findViewById(R.id.btn_topleft);
        btn_topleft.setText(Const.UserLocation.replace("市", ""));
        btn_topleft.setOnClickListener(listener);
        lv_news = (PullToRefreshListView) v.findViewById(R.id.lv_news);
        newListAadpter = new NewsListAdapter(getActivity(), arr_NewsData);
        lv_news.setAdapter(newListAadpter);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        lv_news.setMode(PullToRefreshBase.Mode.BOTH);
        rel_search = (RelativeLayout) v.findViewById(R.id.rel_search);
        rel_search.setOnClickListener(listener);
        lv_news.setOnItemClickListener(pullItemListener);

        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        recy_news = (RecyclerView) v.findViewById(R.id.recy_news);
//        recy_news.setLayoutManager(new LinearLayoutManager(getActivity()));
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recy_news.setLayoutManager(manager);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity(), arr_NewsData, true);
        recy_news.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, NewsData data) {
                Intent intent;
                switch (view.getId()) {
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
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                Log.e(TAG, "onScrolled: " + lastVisibleItemPosition);
                if (lastVisibleItemPosition + 1 == recy_news.getAdapter().getItemCount()) {
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

    AdapterView.OnItemClickListener pullItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.e(TAG, "onItemClick: " + String.valueOf(l));
            // Toast.makeText(getActivity(), String.valueOf(l), Toast.LENGTH_SHORT).show();
        }
    };

    class UpdateBroadcastReceiver extends BroadcastReceiver {


        /* 覆写该方法，对广播事件执行响应的动作  */
        public void onReceive(Context context, Intent intent) {
            PageNo = 0;
            getNewsData();
        }
    }

    private void initNews(View v) {
        lv_news.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // 下拉刷新 业务代码
                if (refreshView.isShownHeader()) {
                    lv_news.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_news.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    lv_news.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    PageNo = 0;
                    getNewsData();
                }
                // 上拉加载更多 业务代码
                if (refreshView.isShownFooter()) {
                    lv_news.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    lv_news.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    lv_news.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    PageNo = PageNo + 1;
                    getNewsData();
                }

            }
        });
    }

    private void initSpTitle(View v) {
        setTitleData();
        final Spinner sp_title = (Spinner) v.findViewById(R.id.sp_title);
        sp_titleAdapter = new Sp_TitleAdapter(getActivity(), arr_TopClass);
        sp_title.setAdapter(sp_titleAdapter);
        sp_title.setOnItemSelectedListener(spTitleListtener);
        btn_title = (Button) v.findViewById(R.id.btn_title);
        ImageView img_title = (ImageView) v.findViewById(R.id.img_title);
        img_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp_title.performClick();
            }
        });
        btn_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp_title.performClick();
            }
        });
    }

    public void getNewsData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("classid", String.valueOf(classid));
        opt_map.put("UserLocation", String.valueOf(Const.UserLocation));
        opt_map.put("PageNo", String.valueOf(PageNo));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "get_News.php", opt_map, NewsDataTmp.class, this);
    }

    public void UpdateZambia(final String position) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("NewId", position);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "UpdateNewZambia.php", opt_map, UpdateZiabamResultTmp.class, this);
    }
    /*
    * @desc 获取顶部菜单数据
    * @author lgm
    * @time 2016/8/5 15:16
    * */

    private void setTitleData() {
        arr_TopClass.add("首页");
        arr_TopClass.add("我的关注");
        arr_TopClass.add("附近");
        arr_TopClass.add("农技经验");
        arr_TopClass.add("产量表现");
        arr_TopClass.add("问题");
        arr_TopClass.add("商品活动");
        arr_TopClass.add("专家分享");
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_topright:
                    menuWindow = new pulishDYNPopupWindow(getActivity(), itemsOnClick);
                    //显示窗口
                    menuWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                    break;
                case R.id.btn_topleft:
                    selectArea();
                    break;
                case R.id.rel_search:
                    Intent intent = new Intent(getActivity(), SearchAcitvity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void selectArea() {
        Intent intent = new Intent(getActivity(), SelectArea.class);
        startActivityForResult(intent, 115);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                if (!Const.UserLocation.equals("")) {
                    btn_topleft.setText(Const.UserLocation.replace("市", ""));
                }
                break;
            default:
                break;
        }
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            Intent intent;
            switch (v.getId()) {
                case R.id.btn_publish_photo://发布活动
                    intent = new Intent(getActivity(), PubishHuoDongActivity.class);
                    startActivity(intent);
                    menuWindow.dismiss();
                    break;
                case R.id.btn_publish_video://发布商品
                    intent = new Intent(getActivity(), MyCommodity.class);
                    intent.putExtra("commodityId", "0");
                    startActivity(intent);
                    menuWindow.dismiss();
                    break;
                case R.id.btn_publish_text://文字发布
                    menuWindow.dismiss();
                    intent = new Intent(getActivity(), PubishMainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_publish_share_experience://分享农技经验
                    intent = new Intent(getActivity(), ShareExperience.class);
                    startActivity(intent);
                    break;
                case R.id.btn_publish_share_yield://分享产量
                    intent = new Intent(getActivity(), ShareYield.class);
                    startActivity(intent);
                    break;
                case R.id.btn_publish_problem://发问
                    intent = new Intent(getActivity(), PublishProblem.class);
                    startActivity(intent);
                    break;
                case R.id.btn_cancel://取消操作
                    menuWindow.dismiss();
                    break;
                default:
                    break;
            }
        }

    };
    AdapterView.OnItemSelectedListener spTitleListtener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            RecyclerViewAdapter adapter = (RecyclerViewAdapter) recy_news.getAdapter();
            adapter.setIsNeedMore(true);
            PageNo = 0;
            classid = i;
            btn_title.setText(arr_TopClass.get(i));
            getNewsData();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };


    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("UpdateNewZambia")) {
            UpdateZiabamResultTmp mUpdateZiabamResultTmp = ObjectUtil.cast(rspBaseBean);

            mUpdateZiabamResult = mUpdateZiabamResultTmp.getDetail().get(0);
            mHandler.post(updateZiabam);
        } else {
            arr_NewsDataTmp = ((NewsDataTmp) rspBaseBean).getDetail();
            updateRealmData(rspBaseBean);
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
            newListAadpter.notifyDataSetChanged();
            lv_news.onRefreshComplete();
            mRecyclerViewAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
        }

    };

    private void updateRealmData(RspBaseBean rspBaseBean) {
      /*  NewsDataTmp newsDataTmp = new NewsDataTmp();
        newsDataTmp = ObjectUtil.cast(rspBaseBean);
        Realm insertRealm = Realm.getDefaultInstance();
        RealmResults<NewsData> results_del = insertRealm.where(NewsData.class).findAll();
        insertRealm.beginTransaction();
        results_del.deleteAllFromRealm();
        insertRealm.commitTransaction();
        for (NewsData o : newsDataTmp.getDetail()) {
            insertRealm.beginTransaction();
            NewsData newdata = insertRealm.copyToRealmOrUpdate(o);
            insertRealm.commitTransaction();
        }*/
    }

    public void refreshData() {
        getNewsData();
    }

    @Override
    public void onRefresh() {
        Log.e(TAG, "onRefresh: ");
        PageNo = 0;
        getNewsData();
        //数据重新加载完成后，提示数据发生改变，并且设置现在不在刷新

    }
}
