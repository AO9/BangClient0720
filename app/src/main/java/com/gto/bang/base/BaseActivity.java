package com.gto.bang.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.gto.bang.response.CommonResponseListener;
import com.gto.bang.util.Constant;
import com.gto.bang.util.RequestUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 16/5/5.
 * 20200622 增加writeToLocal方法，用于记录搜索过的关键词
 */
public abstract class BaseActivity extends ActionBarActivity {

    public abstract Context getContext();

    public abstract  String getRequestTag();

    public void log(String operateType) {
        Map<String, String> param = new HashMap<String, String>();
        param.put(Constant.USERID_V1, getUserId());
        param.put(Constant.OPERATETYPE, operateType);
        param.put(Constant.ANDROID_ID, getStrFromPreferences(Constant.ANDROID_ID));
        CommonResponseListener responseListener = new CommonResponseListener(Constant.EMPTY, getContext());
        RequestUtil.request(Constant.LOG_URL, param, responseListener, getRequestTag(), getContext());
    }

    public String getStrFromPreferences(String key){
        return getSharedPreferences().getString(key, Constant.EMPTY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

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
     * @param key
     * @return
     */
    public String readFromLocal(String key) {
        String value = getSharedPreferences().getString(key, Constant.EMPTY);
        return value;
    }

}
