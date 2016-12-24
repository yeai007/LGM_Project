package com.hopeofseed.hopeofseed.curView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.R;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/24 9:32
 * 修改人：whisper
 * 修改时间：2016/12/24 9:32
 * 修改备注：
 */
public abstract class CurrencyModifyActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_topright, btn_topleft;
    TextView appTitle;
    EditText et_text;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_modify_view);
        initCurrencyView();
    }

    private void initCurrencyView() {
        appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("修改");
        btn_topleft = (Button) findViewById(R.id.btn_topleft);
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topleft.setOnClickListener(this);
        btn_topright.setText("发送");
        btn_topright.setVisibility(View.VISIBLE);
        btn_topright.setOnClickListener(this);
        et_text=(EditText)findViewById(R.id.et_text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                onClickLeftButton(TextUtils.isEmpty(et_text.getText().toString()) ? "" : et_text.getText().toString());
                break;
            case R.id.btn_topright:
                onClickRightButton(TextUtils.isEmpty(et_text.getText().toString()) ? "" : et_text.getText().toString());
                break;
        }
    }

    public abstract void onClickRightButton(String et_text);

    public abstract void onClickLeftButton(String et_text);


    public  void setEditText(String text) {
        if (!TextUtils.isEmpty(text)) {
            et_text.setText(text);
        }

    }
}
