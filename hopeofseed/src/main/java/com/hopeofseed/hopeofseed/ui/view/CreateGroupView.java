package com.hopeofseed.hopeofseed.ui.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.R;

import static com.hopeofseed.hopeofseed.R.id.btn_topleft;
import static com.hopeofseed.hopeofseed.R.id.btn_topright;


public class CreateGroupView extends LinearLayout {

    private ImageButton mReturnBtn;
    private Button mCommitBtn;
    private EditText mGroupName;
    private Button btn_topleft, btn_topright;

    public CreateGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public void initModule() {
        mReturnBtn = (ImageButton) findViewById(R.id.creat_group_return_btn);
        mCommitBtn = (Button) findViewById(R.id.jmui_commit_btn);
        mGroupName = (EditText) findViewById(R.id.input_group_id);
        btn_topleft = (Button) findViewById(R.id.btn_topleft);
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("创建");
        btn_topright.setVisibility(View.VISIBLE);
    }

    public void setListeners(OnClickListener onClickListener) {
        mReturnBtn.setOnClickListener(onClickListener);
        mCommitBtn.setOnClickListener(onClickListener);
        btn_topleft.setOnClickListener(onClickListener);
        btn_topright.setOnClickListener(onClickListener);
    }

    public String getGroupName() {
        return mGroupName.getText().toString().trim();
    }

    public void groupNameError(Context context) {
        Toast.makeText(context, "群名不能为空", Toast.LENGTH_SHORT).show();
    }

    public void groupAddressError(Context context) {
        Toast.makeText(context, "群地址不能为空", Toast.LENGTH_SHORT).show();
    }
}
