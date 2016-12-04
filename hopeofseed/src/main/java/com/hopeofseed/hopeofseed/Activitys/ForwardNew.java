package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsDataTmp;
import com.hopeofseed.hopeofseed.R;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;

import static com.hopeofseed.hopeofseed.Activitys.NewsFragment.NEWS_UPDATE_LIST;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/8/31 16:10
 * 修改人：whisper
 * 修改时间：2016/8/31 16:10
 * 修改备注：
 */
public class ForwardNew extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    String NEW_ID;
    private static final String TAG = "ForwardNew";
    NewsData newsData = new NewsData();
    ImageView img_share_new;
    TextView tv_share_new_title, tv_share_new_content, AppTitle;
    EditText et_share_mood;
    Handler mHandle = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        NEW_ID = intent.getStringExtra("NEWID");
        setContentView(R.layout.forward_new);
        Log.e(TAG, "onCreate: " + NEW_ID);
        initView();
        initData();
    }

    private void initData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("NewId", NEW_ID);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getNewByID.php", opt_map, NewsDataTmp.class, this);
    }

    private void initView() {
        AppTitle = (TextView) findViewById(R.id.apptitle);
        AppTitle.setText("转发");
        TextView appTitleUser = (TextView) findViewById(R.id.app_title_user);
        appTitleUser.setText(Const.currentUser.nickname);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        btn_topright.setText("发送");
        btn_topright.setVisibility(View.VISIBLE);
        img_share_new = (ImageView) findViewById(R.id.img_share_new);
        tv_share_new_title = (TextView) findViewById(R.id.tv_share_new_title);
        tv_share_new_content = (TextView) findViewById(R.id.tv_share_new_content);
        et_share_mood = (EditText) findViewById(R.id.et_share_mood);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                forwardTheNew();
                break;
        }
    }

    private void forwardTheNew() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        if (et_share_mood.getText().toString().equals("")) {
            opt_map.put("ForwardComment", "转发");
        } else {
            opt_map.put("ForwardComment", et_share_mood.getText().toString().replace("\n","\\n"));
        }

        if (Integer.parseInt(newsData.getFromid()) == 0) {
            opt_map.put("ForwardFromNewId", String.valueOf(newsData.getId()));
        } else {
            opt_map.put("ForwardFromNewId", newsData.getFromid());
        }
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "forwardNew.php", opt_map, CommResultTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("getNewById")) {
            //获取信息数据结果
            Log.e(TAG, "onSuccess: " + newsData);
            newsData = ((NewsDataTmp) rspBaseBean).getDetail().get(0);
            mHandle.post(updateTheNewData);
        } else {
            //转发结果
            CommResultTmp commResultTmp = new CommResultTmp();
            commResultTmp = (CommResultTmp) rspBaseBean;
            Log.e(TAG, "onSuccess: " + commResultTmp.getDetail());
            if (Integer.parseInt(commResultTmp.getDetail()) > 0) {
/*                Intent intent = new Intent();  //Itent就是我们要发送的内容
                intent.setAction(NEWS_UPDATE_LIST);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                sendBroadcast(intent);   //发送广播*/
                Intent intent1=new Intent();
                setResult(RESULT_OK, intent1); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "转发失败，清稍后再试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updateTheNewData = new Runnable() {
        @Override
        public void run() {
            String[] arrImage = newsData.getAssimgurl().split(";");
            List<String> images = java.util.Arrays.asList(arrImage);
            if (images.size() > 0) {
                Glide.with(getApplicationContext())
                        .load(Const.IMG_URL + images.get(0))
                        .centerCrop()
                        .into(img_share_new);
            } else {
            }
            tv_share_new_title.setText(newsData.getTitle());
            tv_share_new_content.setText(newsData.getContent().replace("\\n", "\n"));
            tv_share_new_content.setSingleLine(false);
            tv_share_new_content.setMaxLines(2);
            tv_share_new_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        }
    };
}
