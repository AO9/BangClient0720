package com.gto.bang.navigation;

import android.os.Bundle;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by shenjialong on 16/6/13 00:25.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_about);

    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("关于");
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关于");
        MobclickAgent.onPause(this);
    }


}
