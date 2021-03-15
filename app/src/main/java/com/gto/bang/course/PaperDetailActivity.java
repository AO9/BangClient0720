package com.gto.bang.course;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.util.CommonUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 论文详情页
 * 20210311
 */
public class PaperDetailActivity extends BaseActivity {

    Button copy;

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
        copy = (Button) findViewById(R.id.copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cbm = (ClipboardManager) PaperDetailActivity.this
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cbm.setText("lwb20191202");
                CommonUtil.showTips("复制成功!lwb20191202", PaperDetailActivity.this);
            }
        });

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
