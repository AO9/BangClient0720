package com.gto.bang.course;

import android.content.Context;
import android.os.Bundle;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 论文详情页
 * 20210311
 */
public class PaperDetailActivity extends BaseActivity {

    @Override
    public Context getContext() {
        return PaperDetailActivity.this;
    }

    @Override
    public String getRequestTag() {
        return PaperDetailActivity.class.getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paper_sign);

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("原创论文");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("原创论文");
        MobclickAgent.onPause(this);
    }


}
