package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.R;

import java.util.HashMap;

import io.realm.Realm;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/3 9:07
 * 修改人：whisper
 * 修改时间：2016/9/3 9:07
 * 修改备注：
 */
public class CommentNew extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    String NEW_ID;
    NewsData newsData = new NewsData();
    Realm myRealm = Realm.getDefaultInstance();
    EditText et_comment;
    Button btn_at;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_new);
        Intent intent = getIntent();
        NEW_ID = intent.getStringExtra("NEWID");
        initView();
    }


    private void initView() {
        ((TextView) findViewById(R.id.apptitle)).setText("发评论");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        btn_topright.setText("发送");
        btn_topright.setVisibility(View.VISIBLE);
        et_comment = (EditText) findViewById(R.id.et_comment);
        btn_at = (Button) findViewById(R.id.btn_at);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                ComentThisNew();
                break;
            case R.id.btn_at:
                Intent intent = new Intent(CommentNew.this, SelectAtUser.class);
                startActivityForResult(intent, 119);
                break;
        }
    }

    private void ComentThisNew() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("Comment", et_comment.getText().toString().replace("\n","\\n"));
        opt_map.put("CommentFromNewId", NEW_ID);
        opt_map.put("CommentFromUser", "");
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "CommentNew.php", opt_map, CommResultTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        CommResultTmp commResultTmp = new CommResultTmp();
        commResultTmp = (CommResultTmp) rspBaseBean;
        if (Integer.parseInt(commResultTmp.getDetail()) > 0) {
/*            Intent intent = new Intent();  //Itent就是我们要发送的内容
            intent.setAction(NEWS_UPDATE_LIST);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
            sendBroadcast(intent);   //发送广播*/
            Intent intent1=new Intent();
            setResult(RESULT_OK, intent1); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "评论失败，清稍后再试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }
}
