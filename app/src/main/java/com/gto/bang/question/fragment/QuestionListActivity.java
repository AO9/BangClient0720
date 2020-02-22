package com.gto.bang.question.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.gto.bang.R;
import com.gto.bang.base.BaseCreateActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 20190303
 *
 * @location 岛上
 * @describe 问题列表页面-帮TA
 */
public class QuestionListActivity extends BaseCreateActivity {

    public static final String TAG = "问题列表";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_list);
        Log.i("sjl", "test getName=" + getRequestTag());
    }

    @Override
    protected String getRequestTag() {
        return QuestionListActivity.class.getName();
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
