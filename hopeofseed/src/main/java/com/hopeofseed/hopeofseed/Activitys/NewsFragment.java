package com.hopeofseed.hopeofseed.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.hopeofseed.hopeofseed.Adapter.Sp_TitleAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsDataTmp;
import com.hopeofseed.hopeofseed.curView.pulishDYNPopupWindow;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.HashMap;

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
public class NewsFragment extends Fragment implements NetCallBack {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_news, null);
        Log.e(TAG, "NewsFragment: " + Thread.currentThread().getId());
        initView(v);
        initSpTitle(v);
        initNews(v);
        return v;
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
    }

    AdapterView.OnItemClickListener pullItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.e(TAG, "onItemClick: " + String.valueOf(l));
            // Toast.makeText(getActivity(), String.valueOf(l), Toast.LENGTH_SHORT).show();
        }
    };

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
        arr_NewsDataTmp = ((NewsDataTmp) rspBaseBean).getDetail();
        updateRealmData(rspBaseBean);
        mHandler.post(updateList);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            if (PageNo == 0) {
                arr_NewsData.clear();
            }
            arr_NewsData.addAll(arr_NewsDataTmp);
            newListAadpter.notifyDataSetChanged();
            lv_news.onRefreshComplete();
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

 /*   public void refreshData() {
        getNewsData();
    }*/
}
