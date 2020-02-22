package com.gto.bang.college;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

/**
 * 2019年9月8日 线下活动报名
 */
public class SalonActivity extends BaseActivity {

    TextView introduce;
    TextView location;
    TextView theme;
    TextView date;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salon_item);
        initDatas();
        initViews();
    }

    public void initDatas() {
        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.SALON_VIEW_URL + "userId=" + getUserId();
        CustomRequest req = new CustomRequest(this, listener, listener, null, url, Request.Method.GET);
        req.setTag("活动详情页");
        VolleyUtils.getRequestQueue(this).add(req);
    }


    public void initViews() {

        introduce = (TextView) findViewById(R.id.act_introduce);
        location = (TextView) findViewById(R.id.act_location);
        theme = (TextView) findViewById(R.id.act_theme);
        date = (TextView) findViewById(R.id.act_date);

        submit = (Button) findViewById(R.id.question_ok_btn);
//        setSubmitText("提交中");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(Constant.USERID, getSharedPreferences().getString(Constant.USERID, "0"));
                publish(params);
            }
        });
    }

    public void publish(HashMap<String, String> params) {
        ResponseListener1 listener = new ResponseListener1();
        String url = Constant.URL_BASE + Constant.SALON_CREATE_URL + "userId=" + getUserId();
        CustomRequest req = new CustomRequest(this, listener, listener, params, url, Request.Method.POST);
        req.setTag(getRequestTag());
        submit.setEnabled(false);
        submit.setText(Constant.SUBMMITING);
        submit.setTextColor(Color.GRAY);
        VolleyUtils.getRequestQueue(this).add(req);
    }


    public class ResponseListener1 implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t;

        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(SalonActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            if (null == res.get("status") || !Constant.RES_SUCCESS.equals(res.get("status").toString())) {
                String data = (null == res.get("data")) ? "null" : res.get("data").toString();
                t = Toast.makeText(SalonActivity.this, data, Toast.LENGTH_SHORT);
                t.show();
            } else {
                Map<String, String> datas = (Map<String, String>) res.get(Constant.DATA);
                Toast.makeText(SalonActivity.this, "报名成功，等待", Toast.LENGTH_SHORT).show();
                submit.setText(Constant.SUBMMITTED);
                submit.setTextColor(Color.WHITE);
            }

        }
    }


    protected String getRequestTag() {
        return SalonActivity.class.getName();
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
            t = Toast.makeText(SalonActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            if (null == res.get("status") || !Constant.RES_SUCCESS.equals(res.get("status").toString())) {
                String data = (null == res.get("data")) ? "null" : res.get("data").toString();
                t = Toast.makeText(SalonActivity.this, data, Toast.LENGTH_SHORT);
                t.show();
            } else {
                Map<String, String> datas = (Map<String, String>) res.get(Constant.DATA);
                Log.i("sjl", "salon data:" + datas.toString());
                if (null != datas) {
                    introduce.setText(datas.get(Constant.INTRODUCE));
                    location.setText(datas.get(Constant.LOCATION));
                    theme.setText(datas.get(Constant.THEME));
                    date.setText(datas.get(Constant.DATE));
                }
            }

        }
    }

}
