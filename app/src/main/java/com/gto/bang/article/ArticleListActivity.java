package com.gto.bang.article;

import android.os.Bundle;
import android.view.MenuItem;

import com.gto.bang.R;
import com.gto.bang.base.BaseCreateActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 20200221
 * 夜，宝宝睡
 */
public class ArticleListActivity extends BaseCreateActivity {

    public static final String TAG = "问题列表";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_list);
    }

    @Override
    protected String getRequestTag() {
        return ArticleListActivity.class.getName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }


}
