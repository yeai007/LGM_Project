package com.hopeofseed.hopeofseed.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hopeofseed.hopeofseed.Adapter.UnReadConversationListAdapter;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.NotifyData;
import com.hopeofseed.hopeofseed.JNXData.UserMessageData;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserMessageDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.chatting.ChatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import cn.jpush.im.android.api.model.Conversation;
import io.realm.Realm;
import io.realm.RealmResults;

import static cn.jpush.im.android.api.JMessageClient.getConversationList;


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
    RelativeLayout rel_pinglun, rel_zan, rel_hangye, rel_xitong;
    RecyclerView recycler_list;
    UnReadConversationListAdapter mAdapter;
    ArrayList<Conversation> mList = new ArrayList<>();
    ArrayList<Conversation> mListTmp = new ArrayList<>();
    private UpdateBroadcastReceiver updateBroadcastReceiver;  //刷新列表广播
    Handler mHandler = new Handler();
    Realm myRealm = Realm.getDefaultInstance();
    ImageView pinglun_img, xitong_img, hangye_img;
    TextView pinglun_unread_count, xitong_unread_count, hangye_unread_count;

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
        if ((ArrayList<Conversation>) getConversationList() != null) {
            mListTmp = (ArrayList<Conversation>) getConversationList();
            mHandler.post(updatelist);
        }
    }

    private void getNotifyData() {
        //系统通知
        RealmResults<NotifyData> results1 =
                myRealm.where(NotifyData.class).equalTo("NotifyIsRead", "0").equalTo("NotifyType", "1").findAll();
        if (results1.size() > 0) {
            xitong_img.setImageResource(R.drawable.img_message_count);
            xitong_unread_count.setVisibility(View.VISIBLE);
            xitong_unread_count.setText(String.valueOf(results1.size()));
        } else {
            xitong_img.setImageResource(R.drawable.right_arrow);
            xitong_unread_count.setVisibility(View.INVISIBLE);
        }
        //行业通知
        RealmResults<NotifyData> results2 =
                myRealm.where(NotifyData.class).equalTo("NotifyIsRead", "0").equalTo("NotifyType", "2").findAll();
        if (results2.size() > 0) {
            hangye_img.setImageResource(R.drawable.img_message_count);
            hangye_unread_count.setVisibility(View.VISIBLE);
            hangye_unread_count.setText(String.valueOf(results2.size()));
        } else {
            hangye_img.setImageResource(R.drawable.right_arrow);
            hangye_unread_count.setVisibility(View.INVISIBLE);
        }
    }

    Runnable updatelist = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "getData: " + mListTmp);
            if (mListTmp.size() > 0) {
                mList.clear();
                for (int i = 0; i < mListTmp.size(); i++) {
                    if (mListTmp.get(i).getUnReadMsgCnt() > 0) {
                        mList.add(mListTmp.get(i));
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    };

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
        RelativeLayout rel_search = (RelativeLayout) v.findViewById(R.id.rel_search);
        rel_search.setOnClickListener(this);
        rel_pinglun = (RelativeLayout) v.findViewById(R.id.rel_pinglun);
        rel_zan = (RelativeLayout) v.findViewById(R.id.rel_zan);
        rel_hangye = (RelativeLayout) v.findViewById(R.id.rel_hangye);
        rel_xitong = (RelativeLayout) v.findViewById(R.id.rel_xitong);
        rel_pinglun.setOnClickListener(listener);
        rel_zan.setOnClickListener(listener);
        rel_hangye.setOnClickListener(listener);
        rel_xitong.setOnClickListener(listener);
        pinglun_img = (ImageView) v.findViewById(R.id.pinglun_img);
        pinglun_unread_count = (TextView) v.findViewById(R.id.pinglun_unread_count);
        xitong_img = (ImageView) v.findViewById(R.id.xitong_img);
        xitong_unread_count = (TextView) v.findViewById(R.id.xitong_unread_count);
        hangye_img = (ImageView) v.findViewById(R.id.hangye_img);
        hangye_unread_count = (TextView) v.findViewById(R.id.hangye_unread_count);
        getData();
        recycler_list = (RecyclerView) v.findViewById(R.id.recycler_list);
        recycler_list.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);
        mAdapter = new UnReadConversationListAdapter(getActivity(), mList);
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
                case R.id.rel_pinglun:
                    intent = new Intent(getActivity(), CommentAboutMe.class);
                    startActivity(intent);
                    break;
                case R.id.rel_zan:
                    intent = new Intent(getActivity(), CommentAboutMe.class);
                    startActivity(intent);
                    break;
                case R.id.rel_hangye:
                    intent = new Intent(getActivity(), SystemNofityActivity.class);
                    intent.putExtra("type","2");
                    startActivity(intent);
                    break;
                case R.id.rel_xitong:
                    intent = new Intent(getActivity(), SystemNofityActivity.class);
                    intent.putExtra("type","1");
                    startActivity(intent);
                    break;

            }
        }
    };

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        arrUserMessageDataTmp = ((UserMessageDataTmp) rspBaseBean).getDetail();

    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

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
            getNotifyData();
        }
    }
}
