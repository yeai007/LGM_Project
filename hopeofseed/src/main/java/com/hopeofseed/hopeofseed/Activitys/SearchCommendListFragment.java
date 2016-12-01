package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.CommendListAdapterNew;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommentDataNew;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommentDataNewTmp;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.PULL_FROM_END;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/7 20:30
 * 修改人：whisper
 * 修改时间：2016/10/7 20:30
 * 修改备注：
 */
public class SearchCommendListFragment extends Fragment implements NetCallBack {
    private static final String TAG = "DistributorListFragment";
    PullToRefreshListView lv_list;
    CommendListAdapterNew mCommendListAdapter;
    int classid = 1;
    ArrayList<CommentDataNew> arrCommentOrForward = new ArrayList<>();
    ArrayList<CommentDataNew> arrCommentOrForwardTmp = new ArrayList<>();
    Handler mHandle = new Handler();
    String InfoId, NewClass;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.pager_list_forwar, null);
        initView(v);
        getData();
        return v;
    }

    public SearchCommendListFragment(String infoId, String newClass) {
        super();
        InfoId = infoId;
        NewClass = newClass;
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("InfoId", InfoId);
        opt_map.put("NewClass",NewClass);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommentByInfoId.php", opt_map, CommentDataNewTmp.class, this);
    }

    private void initView(View v) {
        lv_list = (PullToRefreshListView) v.findViewById(R.id.lv_list);
        lv_list.setMode(PULL_FROM_END);
        mCommendListAdapter = new CommendListAdapterNew(getActivity(), arrCommentOrForward);
        lv_list.setAdapter(mCommendListAdapter);
        lv_list.setOnItemClickListener(myListener);
    }

    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           /* Intent intent = new Intent(getActivity(), DistributorActivity.class);
            intent.putExtra("ID", String.valueOf(arrCommentOrForward.get(i - 1).re()));
            startActivity(intent);*/
        }
    };

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        arrCommentOrForwardTmp = ((CommentDataNewTmp) rspBaseBean).getDetail();
        mHandle.post(updatelist);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }


    Runnable updatelist = new Runnable() {
        @Override
        public void run() {
            arrCommentOrForward.clear();
            arrCommentOrForward.addAll(arrCommentOrForwardTmp);
            mCommendListAdapter.notifyDataSetChanged();
            lv_list.onRefreshComplete();
        }
    };

    public void onrefreshList() {
        getData();
    }
}
