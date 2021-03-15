package com.gto.bang.course;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.home.MainActivity;
import com.gto.bang.util.CommonUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 课程报名
 * @20200705
 */
public class SignUpActivity extends BaseActivity {

    Button copy;

    @Override
    public Context getContext() {
        return SignUpActivity.this;
    }

    @Override
    public String getRequestTag() {
        return SignUpActivity.class.getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        copy=(Button) findViewById(R.id.copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cbm= (ClipboardManager) SignUpActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cbm.setText("lwb20191202");
                CommonUtil.showTips("复制成功! lwb20191202",SignUpActivity.this);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("课程报名");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("课程报名");
        MobclickAgent.onPause(this);
    }


}
