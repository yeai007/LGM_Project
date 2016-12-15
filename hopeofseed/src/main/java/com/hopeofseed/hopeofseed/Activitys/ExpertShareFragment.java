package com.hopeofseed.hopeofseed.Activitys;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hopeofseed.hopeofseed.Adapter.ExperienceAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.ExperienceData;
import com.hopeofseed.hopeofseed.JNXData.ExpertEnterperiseData;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXDataTmp.ExperienceDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.ExpertEnterperiseDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsDataTmp;
import com.hopeofseed.hopeofseed.R;
import java.util.ArrayList;
import java.util.HashMap;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/9 19:39
 * 修改人：whisper
 * 修改时间：2016/10/9 19:39
 * 修改备注：
 */
public class ExpertShareFragment extends Fragment implements NetCallBack {
    RecyclerView recycler_list;
    android.os.Handler mHandler = new android.os.Handler();
    private String StrProvince, StrCity, StrZone, StrPolitic;
    private int PageNo = 0;
    ExperienceAdapter mExperienceAdapter;
    ArrayList<ExpertEnterperiseData> arrExperienceDataData = new ArrayList<>();
    ArrayList<ExpertEnterperiseData> arrExperienceDataTmp = new ArrayList<>();
    boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_expert_share, null);
        initView(v);
        getData();
        return v;
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("StrProvince", StrProvince);
        opt_map.put("StrCity", StrCity);
        opt_map.put("StrZone", StrZone);
        opt_map.put("PageNo", String.valueOf(PageNo));
        opt_map.put("StrPolitic", StrPolitic);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetExpertShare.php", opt_map, ExpertEnterperiseDataTmp.class, this);
    }

    private void initView(View v) {
        recycler_list = (RecyclerView) v.findViewById(R.id.recycler_list);
        recycler_list.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);
        mExperienceAdapter = new ExperienceAdapter(getActivity(), arrExperienceDataData);
        recycler_list.setAdapter(mExperienceAdapter);
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
                        Log.e(TAG, "onScrolled: loadingmaore");
                        isLoading = true;
                        PageNo = PageNo + 1;
                        getData();
                    } else if (arrExperienceDataTmp.size() < 20) {
                        //当没有更多的数据的时候去掉加载更多的布局
/*                        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recy_news.getAdapter();
                        adapter.setIsNeedMore(false);
                        adapter.notifyDataSetChanged();*/
                    }
                }
            }
        });
    }
    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
        arrExperienceDataTmp = ((ExpertEnterperiseDataTmp) rspBaseBean).getDetail();
        mHandler.post(runnableNotifyList);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable runnableNotifyList = new Runnable() {
        @Override
        public void run() {
            arrExperienceDataData.clear();
            arrExperienceDataData.addAll(arrExperienceDataTmp);
            mExperienceAdapter.notifyDataSetChanged();
        }
    };

    public void setRefreshData(String... citySelected) {
        StrProvince = citySelected[0];
        StrCity = citySelected[1];
        StrZone = citySelected[2];
        getData();
    }
}
