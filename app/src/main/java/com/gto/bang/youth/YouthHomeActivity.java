package com.gto.bang.youth;


import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.util.CommonUtil;

/**
 * 同学录首页
 */
public class YouthHomeActivity extends BaseActivity {

    LinearLayout myMemory;
    LinearLayout writeMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youth_main);
        initViews();
    }

    public void initViews() {

        myMemory = (LinearLayout) findViewById(R.id.myMemory);
        writeMemory = (LinearLayout) findViewById(R.id.writeMemory);

        myMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtil.showTips("暂未收到同学赠言", YouthHomeActivity.this);
                log("同学录|我的");
            }
        });

        writeMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtil.showTips("敬请期待!", YouthHomeActivity.this);
                log("同学录|写一封");
            }
        });
    }

    @Override
    public Context getContext() {
        return YouthHomeActivity.this;
    }

    @Override
    public String getRequestTag() {
        return YouthHomeActivity.class.getName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
