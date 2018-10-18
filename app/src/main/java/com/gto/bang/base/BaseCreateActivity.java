package com.gto.bang.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;

import java.util.Map;

/**
 * 1105 新建文章的基类
 */
public class BaseCreateActivity extends ActionBarActivity{

    public String submitText;

    public String getSubmitText() {
        return submitText;
    }

    public void setSubmitText(String submitText) {
        this.submitText = submitText;
    }

    String [] hints=new String[]{"发布成功","服务繁忙,请稍后重试"};

    public String getPromptForSubmitSuccess(){
        return "发布成功";
    }

    public Button submit;

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

    public SharedPreferences.Editor getEditor(){
        SharedPreferences sp=getSharedPreferences(Constant.DB, Activity.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor=sp.edit();
        return editor;
    }

    public SharedPreferences getSharedPreferences(){
        SharedPreferences sp=getSharedPreferences(Constant.DB, Activity.MODE_MULTI_PROCESS);

        return sp;
    }

    /**
     * 初始化View
     */
    protected String[] getHints(){
        return hints;
    }


    /**
     * 请求的标签
     */
    protected String getRequestTag(){
        return null;
    }


    /**
     * 请求响应
     */
    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {

        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(BaseCreateActivity.this, getHints()[1], Toast.LENGTH_SHORT);
            t.show();
            submit.setEnabled(true);
            submit.setBackgroundColor(getResources().getColor(R.color.withe));
            submit.setText(getSubmitText());
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            Object status=res.get("status");
            if(null==status||!Constant.RES_SUCCESS.equals(status.toString())){
                // 发布失败，提示稍后重试
                t = Toast.makeText(BaseCreateActivity.this, res.get("data").toString(), Toast.LENGTH_SHORT);
                submit.setEnabled(true);
                submit.setText(getSubmitText());
            }else{
                // 发布成功，回到前一个页面
                t = Toast.makeText(BaseCreateActivity.this, getPromptForSubmitSuccess(), Toast.LENGTH_SHORT);
                finish();
            }
            t.show();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(this).cancelAll(getRequestTag());
    }


}
