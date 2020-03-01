package com.gto.bang.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.gto.bang.util.Constant;

/**
 * Created by user on 16/5/5.
 */
public class BaseActivity extends ActionBarActivity {

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

}
