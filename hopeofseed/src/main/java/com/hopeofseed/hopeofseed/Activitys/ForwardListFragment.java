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
import com.hopeofseed.hopeofseed.Adapter.ForwarListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommentOrForward;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommentOrForwardTmp;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/7 20:30
 * 修改人：whisper
 * 修改时间：2016/10/7 20:30
 * 修改备注：
 */
public class ForwardListFragment extends Fragment implements NetCallBack {
    private static final String TAG = "DistributorListFragment";
    PullToRefreshListView lv_list;
    ForwarListAdapter mForwarListAdapter;
    int classid = 0;
    ArrayList<CommentOrForward> arrCommentOrForward = new ArrayList<>();
    ArrayList<CommentOrForward> arrCommentOrForwardTmp = new ArrayList<>();
    Handler mHandle = new Handler();
    String NewId;

/*    public ForwardListFragment(String newId) {
        super();
        NewId = newId;
    }*/
    public static ForwardListFragment newInstance(String newId) {
        ForwardListFragment f = new ForwardListFragment();
        Bundle b = new Bundle();
        b.putString("newId", newId);
        f.setArguments(b);
        return f;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            NewId = getArguments().getString("newId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.pager_list_forwar, null);
        initView(v);
        getData();
        return v;
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("NewId", NewId);
        opt_map.put("ClassId", String.valueOf(classid));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommentOrForwardByNewId.php", opt_map, CommentOrForwardTmp.class, this);
    }

    private void initView(View v) {
        lv_list = (PullToRefreshListView) v.findViewById(R.id.lv_list);
        mForwarListAdapter = new ForwarListAdapter(getActivity(), arrCommentOrForward);
        lv_list.setAdapter(mForwarListAdapter);
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
        arrCommentOrForwardTmp = ((CommentOrForwardTmp) rspBaseBean).getDetail();
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
            mForwarListAdapter.notifyDataSetChanged();
            lv_list.onRefreshComplete();
        }
    };
}
