package com.gto.bang.navigation;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.util.CommonUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by shenjialong on 16/6/13 00:25.
 */
public class AboutActivity extends BaseActivity {

    TextView statement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_about);
        statement=findViewById(R.id.statement);
        statement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statement=getStament();
                CommonUtil.showDialog(AboutActivity.this,statement
                        ,"用户协议与隐私权限","好的，我知道了");
            }
        });

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
