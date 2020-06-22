package com.gto.bang.college;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
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

import java.util.Map;

public class NoticeActivity extends BaseActivity {

    TextView notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);
        initDatas();
        initViews();
    }

    public void initDatas() {
        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.NOTICE_URL + "userId=" + getUserId();
        CustomRequest req = new CustomRequest(this, listener, listener, null, url, Request.Method.GET);
        req.setTag("首页Banner");
        VolleyUtils.getRequestQueue(this).add(req);
    }


    public void initViews() {
        notice = (TextView) findViewById(R.id.notice);
    }

    @Override
    public Context getContext() {
        return NoticeActivity.this;
    }

    @Override
    public String getRequestTag() {
        return NoticeActivity.class.getName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("报名开始");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("报名结束");
        MobclickAgent.onPause(this);
    }


    public class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t;

        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(NoticeActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            if (null == res.get("status") || !Constant.RES_SUCCESS.equals(res.get("status").toString())) {
                String data = (null == res.get("data")) ? "null" : res.get("data").toString();
                t = Toast.makeText(NoticeActivity.this, data, Toast.LENGTH_SHORT);
                t.show();
            } else {
                Map<String, String> noticeInfo = (Map<String, String>) res.get(Constant.DATA);
                if (null != noticeInfo) {
                    notice.setText(noticeInfo.get("content"));
                }
            }

        }
    }

}
