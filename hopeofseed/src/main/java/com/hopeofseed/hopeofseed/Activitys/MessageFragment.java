package com.hopeofseed.hopeofseed.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hopeofseed.hopeofseed.Adapter.UnReadConversationListAdapter;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.ConvAndGroupData;
import com.hopeofseed.hopeofseed.JNXData.GroupData;
import com.hopeofseed.hopeofseed.JNXData.UserMessageData;
import com.hopeofseed.hopeofseed.JNXData.pushFileResult;
import com.hopeofseed.hopeofseed.JNXDataTmp.GroupDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserMessageDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.pushFileResultTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.chatting.ChatActivity;
import com.hopeofseed.hopeofseed.util.NullStringToEmptyAdapterFactory;
import com.hopeofseed.hopeofseed.util.SortComparator;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupIDListCallback;
import cn.jpush.im.android.api.model.Conversation;

import static cn.jpush.im.android.api.enums.ConversationType.single;


/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/27 14:40
 * 修改人：whisper
 * 修改时间：2016/7/27 14:40
 * 修改备注：
 */
public class MessageFragment extends Fragment implements NetCallBack, View.OnClickListener {
    private static final String TAG = "MessageFragment";
    public static String MESSAGE_UPDATE_LIST = "MESSAGE_UPDATE_LIST";
    Button btn_topleft;
    ArrayList<UserMessageData> arrUserMessageData = new ArrayList<>();
    ArrayList<UserMessageData> arrUserMessageDataTmp = new ArrayList<>();
    Handler mHandle = new Handler();
    RecyclerView recycler_list;
    UnReadConversationListAdapter mAdapter;
    ArrayList<GroupData> arrGroupListTmp = new ArrayList<>();
    ArrayList<ConvAndGroupData> arrConvAndGroupDataTmpUnRead = new ArrayList<>();
    ArrayList<ConvAndGroupData> arrConvAndGroupDataTmp = new ArrayList<>();
    ArrayList<ConvAndGroupData> arrConvAndGroupData = new ArrayList<>();
    private UpdateBroadcastReceiver updateBroadcastReceiver;  //刷新列表广播
    Handler mHandler = new Handler();
    int unReadCount = 0;
    private SwipeRefreshLayout mRefreshLayout;
    ArrayList<Conversation> arrConversation = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_message, null);
        initView(v);
        initReceiver();
        initData();
        getNotifyData();
        return v;
    }


    private void initReceiver() {

        // 注册广播接收
        updateBroadcastReceiver = new UpdateBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MESSAGE_UPDATE_LIST);    //只有持有相同的action的接受者才能接收此广播
        getActivity().registerReceiver(updateBroadcastReceiver, filter);
    }

    private void getData() {
        JMessageClient.getGroupIDList(new GetGroupIDListCallback() {
            @Override
            public void gotResult(int i, String s, List<Long> list) {
                if (i == 0) {
                    getGroupHttp(list);
                }
            }
        });
    }

    private void getNotifyData() {
        getUnReadComment();
    }

    Runnable updateCommend = new Runnable() {
        @Override
        public void run() {
            mAdapter.setPinglun(unReadCount);
            mAdapter.notifyItemChanged(0);
        }
    };
    Runnable updatelist = new Runnable() {
        @Override
        public void run() {
            mRefreshLayout.setRefreshing(false);
            mAdapter.setStatus();
            mAdapter.notifyDataSetChanged();
        }
    };

    private void getUnReadComment() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetUnReadCommentCount.php", opt_map, pushFileResultTmp.class, this);
    }

    private void initData() {
        HashMap<String, String> opt_map = new HashMap<>();
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetUserMessageData.php", opt_map, UserMessageDataTmp.class, this);
    }

    private void getGroupHttp(List<Long> list) {
        Gson mGson = new GsonBuilder()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("AllGroupIds", mGson.toJson(list));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetGroupHttp.php", opt_map, GroupDataTmp.class, this);
    }

    private void initView(View v) {
        TextView app_title = (TextView) v.findViewById(R.id.apptitle);
        app_title.setText("消息");


        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG, "onReceive: 收到广播");
                getData();
                getNotifyData();
            }
        });

        (v.findViewById(R.id.btn_topright)).setOnClickListener(listener);
        (v.findViewById(R.id.btn_topleft)).setOnClickListener(listener);
        RelativeLayout rel_search = (RelativeLayout) v.findViewById(R.id.rel_search);
        rel_search.setOnClickListener(this);
        getData();
        recycler_list = (RecyclerView) v.findViewById(R.id.recycler_list);
        recycler_list.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);
        mAdapter = new UnReadConversationListAdapter(getActivity(), arrConvAndGroupData);
        recycler_list.setAdapter(mAdapter);
    }


    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.btn_topright:
                    intent = new Intent(getActivity(), SearchGroupActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_topleft:
                    intent = new Intent(getActivity(), CreateGroupActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("GetUnReadCommentCount")) {
            pushFileResult mPushFileResult = ((pushFileResultTmp) rspBaseBean).getDetail();
            unReadCount = mPushFileResult.getNew_id();
            mHandle.post(updateCommend);
        } else if (rspBaseBean.RequestSign.equals("GetGroupHttp")) {
            GroupDataTmp mGroupDataTmp = ObjectUtil.cast(rspBaseBean);
            arrGroupListTmp = mGroupDataTmp.getDetail();
            arrConvAndGroupDataTmpUnRead.clear();
            arrConvAndGroupDataTmp.clear();
            for (int i = 0; i < arrGroupListTmp.size(); i++) {
                ConvAndGroupData mConvAndGroupData = new ConvAndGroupData();
                mConvAndGroupData.setConverType(0);
                mConvAndGroupData.setConversation(JMessageClient.getGroupConversation(Long.parseLong(arrGroupListTmp.get(i).getAppJpushGroupId())));
                mConvAndGroupData.setGroupData(arrGroupListTmp.get(i));
                if (mConvAndGroupData.getConversation() == null) {
                    //arrConvAndGroupDataTmp.add(mConvAndGroupData);
                } else {
                    if (mConvAndGroupData.getConversation().getUnReadMsgCnt() > 0) {
                        Log.e(TAG, "onSuccess: getUnReadMsgCnt" + mConvAndGroupData.getConversation().getUnReadMsgCnt() + mConvAndGroupData.getGroupData().getAppGroupName());
                        arrConvAndGroupDataTmpUnRead.add(mConvAndGroupData);
                    } else {
                        arrConvAndGroupDataTmp.add(mConvAndGroupData);
                        Log.e(TAG, "onSuccess: getUnReadMsgCnt" + mConvAndGroupData.getConversation().getUnReadMsgCnt() + mConvAndGroupData.getGroupData().getAppGroupName());
                    }
                }
            }
            arrConversation.clear();
            List<Conversation> arrConversationTmp = JMessageClient.getConversationList();
            if (arrConversationTmp != null) {
                if (arrConversationTmp.size() > 0) {
                    arrConversation.addAll(arrConversationTmp);
                    for (int i = 0; i < arrConversation.size(); i++) {
                        if (arrConversation.get(i).getType() == single) {
                            ConvAndGroupData iConvAndGroupData = new ConvAndGroupData();
                            iConvAndGroupData.setConverType(1);
                            iConvAndGroupData.setConversation(arrConversation.get(i));
                            if (iConvAndGroupData.getConversation().getUnReadMsgCnt() > 0) {
                                arrConvAndGroupDataTmpUnRead.add(iConvAndGroupData);
                            } else {
                                arrConvAndGroupDataTmp.add(iConvAndGroupData);
                            }
                        }
                    }
                }
            }
            arrConvAndGroupData.clear();
            arrConvAndGroupData.addAll(arrConvAndGroupDataTmpUnRead);
            arrConvAndGroupData.addAll(arrConvAndGroupDataTmp);

            mHandle.postDelayed(sortlist, 1000);

        } else {
            arrUserMessageDataTmp = ((UserMessageDataTmp) rspBaseBean).getDetail();
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }
Runnable sortlist=new Runnable() {
    @Override
    public void run() {
        Comparator comp = new SortComparator();
        Collections.sort(arrConvAndGroupData,comp);
        mHandle.postDelayed(updatelist, 1000);
    }
};
    AdapterView.OnItemClickListener list_listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent;
            switch (Integer.parseInt(arrUserMessageData.get(i - 1).getMessageTypeId())) {
                case 1://评论
                    Log.e(TAG, "onItemClick:评论");
                    intent = new Intent(getActivity(), CommentAboutMe.class);
                    startActivity(intent);
                    break;
                case 2://赞
                    Log.e(TAG, "onItemClick:赞");
                    intent = new Intent(getActivity(), CommentAboutMe.class);
                    startActivity(intent);
                    break;

                case 6://赞
                    intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("fromGroup", false);
                    intent.putExtra(Application.GROUP_ID, Long.parseLong(arrUserMessageData.get(i - 1).getMessageFrom()));
                    intent.putExtra(Application.GROUP_NAME, arrUserMessageData.get(i - 1).getMessageTitle());
                    Log.e(TAG, "onItemClick: 发送参数" + arrUserMessageData.get(i - 1).getMessageFrom() + arrUserMessageData.get(i - 1).getMessageTitle());
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_search:
                Intent intent = new Intent(getActivity(), SearchAcitvity.class);
                startActivity(intent);
                break;
        }
    }


    class UpdateBroadcastReceiver extends BroadcastReceiver {
        /* 覆写该方法，对广播事件执行响应的动作  */
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: 收到广播");
            getData();
            //  getNotifyData();
        }
    }


}
