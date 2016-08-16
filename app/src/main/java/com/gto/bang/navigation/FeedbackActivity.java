package com.gto.bang.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenjialong on 16/8/11 00:23.
 */
public class FeedbackActivity extends BaseActivity {

    EditText content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_reback_input);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.feedback, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_write_feedback:
                push();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 网络获取列表数据
     */
    public void push() {
        content = (EditText) this.findViewById(R.id.feedback_content_et);
        HashMap<String, String> params=new HashMap<String, String>();
        params.put("content",content.getText().toString());
        params.put("userid",getSharedPreferences().getString(Constant.ID,Constant.AUTHORID_DEFAULT));
        ResponseListener listener = new ResponseListener();

        String url= Constant.URL_BASE+ Constant.FEEDBACK_CREATE_AJAX;
        CustomRequest req = new CustomRequest(this,listener,listener,params,url, Request.Method.POST);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(this).add(req);
    }


    protected String getRequestTag(){
        return "FeedbackActivity_request";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(this).cancelAll(getRequestTag());

    }


    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(FeedbackActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            Log.i("sjl","login res status:"+res.get(Constant.STATUS)+" data: "+res.get(Constant.DATA));

            if(null==res.get(Constant.STATUS)||!Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())){
                String data=(null==res.get(Constant.DATA))?Constant.REQUEST_ERROR:res.get(Constant.DATA).toString();
                t = Toast.makeText(FeedbackActivity.this, data, Toast.LENGTH_SHORT);
                t.show();
            }else{
                t = Toast.makeText(FeedbackActivity.this,"发送成功", Toast.LENGTH_SHORT);
                t.show();
                finish();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("反馈");
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("反馈");
        MobclickAgent.onPause(this);
    }
}
