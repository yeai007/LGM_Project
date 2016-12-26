package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hopeofseed.hopeofseed.Adapter.GroupListAdapter;
import com.hopeofseed.hopeofseed.Adapter.HuodongListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.GroupData;
import com.hopeofseed.hopeofseed.JNXData.HuodongData;
import com.hopeofseed.hopeofseed.JNXDataTmp.GroupDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.HuodongDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.AppUtil;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/13 10:21
 * 修改人：whisper
 * 修改时间：2016/12/13 10:21
 * 修改备注：
 */
public class HuoDongFragment extends Fragment implements NetCallBack {
    RecyclerView recycler_list;
    HuodongListAdapter mAdapter;
    ArrayList<HuodongData> mList = new ArrayList<>();
    ArrayList<HuodongData> mListTmp = new ArrayList<>();
    Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_near_huodong, null);
        initView(v);
        getData();
        return v;
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetHuoDongList.php", opt_map, HuodongDataTmp.class, this);
    }

    private void initView(View v) {
        recycler_list = (RecyclerView) v.findViewById(R.id.recycler_list);
        recycler_list.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);
        mAdapter = new HuodongListAdapter(getActivity(), mList);
        recycler_list.setAdapter(mAdapter);
    }

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

    Runnable updatelist = new Runnable() {
        @Override
        public void run() {
            if (mListTmp.size() > 0) {
                mList.clear();
                mList.addAll(mListTmp);
                mAdapter.notifyDataSetChanged();
            }
        }
    };
}
