package com.gto.bang.navigation;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.util.CommonUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的钱包
 */
public class WalletActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet);

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的论文币");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的论文币");
        MobclickAgent.onPause(this);
    }


}
