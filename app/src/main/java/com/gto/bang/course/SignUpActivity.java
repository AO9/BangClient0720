package com.gto.bang.course;

import android.content.Context;
import android.os.Bundle;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 课程报名
 * @20200705
 */
public class SignUpActivity extends BaseActivity {

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
