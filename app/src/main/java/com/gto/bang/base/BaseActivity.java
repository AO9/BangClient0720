package com.gto.bang.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.Button;

import com.gto.bang.R;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * Created by user on 16/5/5.
 * 20200622 增加writeToLocal方法，用于记录搜索过的关键词
 * 20201108 增加log方法
 */
public abstract class BaseActivity extends ActionBarActivity {
    public void log(String operation) {
        CommonUtil.log(operation, getContext(), getRequestTag(), getUserId());
    }

    public abstract Context getContext();

    public abstract String getRequestTag();

    /**
     * 更新按钮操作状态
     * 20200626 端午节
     *
     * @param status
     * @param button
     */
    public void udpateButtonByStatus(int status, Button button) {
        if (status == Constant.BUTTON_STATUS_OFF) {
            button.setEnabled(false);
            button.setBackgroundResource(R.color.info_backgroud);
            button.setTextColor(Color.GRAY);
        } else {
            button.setEnabled(true);
            button.setBackgroundResource(R.color.color_theme);
            button.setTextColor(Color.WHITE);
        }
    }

//    public void log(String operateType) {
//        Map<String, String> param = new HashMap<String, String>();
//        param.put(Constant.USERID_V1, getUserId());
//        param.put(Constant.OPERATETYPE, operateType);
//        param.put(Constant.ANDROID_ID, CommonUtil.getAndroidId(BaseActivity.this));
//        CommonResponseListener responseListener = new CommonResponseListener(Constant.EMPTY, getContext());
//        RequestUtil.request(Constant.LOG_URL, param, responseListener, getRequestTag(), getContext());
//    }

    public String getStrFromPreferences(String key) {
        return getSharedPreferences().getString(key, Constant.EMPTY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        // 该方法是【友盟+】Push后台进行日活统计及多维度推送的必调用方法
        // add by shenjialong 20200623
        PushAgent.getInstance(getContext()).onAppStart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //图标点击事件
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public SharedPreferences.Editor getEditor() {

        SharedPreferences sp = getSharedPreferences(Constant.DB, Activity.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();
        return editor;
    }

    public SharedPreferences getSharedPreferences() {

        SharedPreferences sp = getSharedPreferences(Constant.DB, Activity.MODE_MULTI_PROCESS);
        return sp;
    }

    public String getUserId() {
        return getSharedPreferences().getString(Constant.ID, Constant.AUTHORID_DEFAULT);
    }

    public String getStament() {

        return getSharedPreferences().getString(Constant.INFO, "");
    }

    /**
     * 本地DB中写数据
     *
     * @param key
     * @param value
     */
    public void writeToLocal(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 本地DB中读数据
     *
     * @param key
     * @return
     */
    public String readFromLocal(String key) {
        String value = getSharedPreferences().getString(key, Constant.EMPTY);
        return value;
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getRequestTag());
        MobclickAgent.onResume(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getRequestTag());
        MobclickAgent.onPause(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(this).cancelAll(getRequestTag());
    }
}
