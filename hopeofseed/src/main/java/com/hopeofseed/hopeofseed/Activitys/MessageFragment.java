package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.MessageListAdapter;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserMessageData;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserMessageDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.chatting.ChatActivity;

import java.util.ArrayList;
import java.util.HashMap;

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
    PullToRefreshListView lv_message;
    Button btn_topleft;
    ArrayList<UserMessageData> arrUserMessageData = new ArrayList<>();
    ArrayList<UserMessageData> arrUserMessageDataTmp = new ArrayList<>();
    Handler mHandle = new Handler();
    MessageListAdapter messageListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_message, null);
        initView(v);
        initData();
        return v;
    }

    private void initData() {
        HashMap<String, String> opt_map = new HashMap<>();
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetUserMessageData.php", opt_map, UserMessageDataTmp.class, this);
    }

    private void initView(View v) {
        TextView app_title = (TextView) v.findViewById(R.id.apptitle);
        app_title.setText("消息");
        (v.findViewById(R.id.btn_topright)).setOnClickListener(listener);
        (v.findViewById(R.id.btn_topleft)).setOnClickListener(listener);
        lv_message = (PullToRefreshListView) v.findViewById(R.id.lv_message);
        lv_message.setMode(PullToRefreshBase.Mode.BOTH);
        messageListAdapter = new MessageListAdapter(getActivity(), arrUserMessageData);
        lv_message.setAdapter(messageListAdapter);
        lv_message.setOnItemClickListener(list_listener);
        RelativeLayout rel_search = (RelativeLayout) v.findViewById(R.id.rel_search);
        rel_search.setOnClickListener(this);
        initList();
    }

    private void initList() {
        lv_message.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // 下拉刷新 业务代码
                if (refreshView.isShownHeader()) {
                    lv_message.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_message.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    lv_message.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    initData();
                }
                // 上拉加载更多 业务代码
                if (refreshView.isShownFooter()) {
                    lv_message.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    lv_message.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    lv_message.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    initData();
                }

            }
        });
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.btn_topright:
/*                    intent = new Intent(getActivity(), CreateChat.class);
                    startActivity(intent);*/
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
        arrUserMessageDataTmp = ((UserMessageDataTmp) rspBaseBean).getDetail();
        // mHandle.postDelayed(updateData,1000);
        lv_message.postDelayed(updateData, 1000);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updateData = new Runnable() {
        @Override
        public void run() {
            arrUserMessageData.clear();
            arrUserMessageData.addAll(arrUserMessageDataTmp);
            messageListAdapter.notifyDataSetChanged();
            lv_message.onRefreshComplete();
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
}
