package com.gto.bang.question.fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseCreateActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 2019年0608
 * 帮TA详情页面
 */
public class SupportDetailActivity extends BaseCreateActivity {

    public static final String TAG = "问题详情";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_detail);
        initDatas();
    }

    public void initDatas() {
        Bundle bundle = getIntent().getExtras();
//        Object title = bundle.getString("artTitle");
        String id = null != bundle.getString("id") ? bundle.getString("id").toString() : null;
        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.ARTICLE_DETAIL_AJAX + "id=" + (int) Double.valueOf(id).doubleValue();
        CustomRequest req = new CustomRequest(this, listener, listener, null, url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(this).add(req);
    }

    @Override
    protected String getRequestTag() {
        return SupportDetailActivity.class.getName();
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


    public class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t;

        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(SupportDetailActivity.this, "网络请求失败，请重试", Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            if (null == res.get("status") || !Constant.RES_SUCCESS.equals(res.get("status").toString())) {
                t = Toast.makeText(SupportDetailActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
                t.show();
            } else {
                LinearLayout tips = (LinearLayout) findViewById(R.id.comment_tips);
                tips.setVisibility(View.GONE);
                LinearLayout ll = (LinearLayout) findViewById(R.id.detail_ll);
                ll.setVisibility(View.VISIBLE);
                Map<String, Object> details = (Map<String, Object>) res.get("data");
                TextView author = (TextView) findViewById(R.id.question_detail_author_tv);
                TextView date = (TextView) findViewById(R.id.question_detail_date_tv);
                TextView describe = (TextView) findViewById(R.id.question_detail_describe_tv);
                TextView headIcon = (TextView) findViewById(R.id.question_head_tv);
                TextView price = (TextView) findViewById(R.id.price);
                Button contact = (Button) findViewById(R.id.contact);

                price.setText(details.get("price").toString());

                String idStr = details.get("authorId").toString();
                Integer authorId = Integer.valueOf(idStr);
                author.setText(details.get("username").toString());
                date.setText(details.get("createTime").toString());
                describe.setText(details.get("content").toString());
                contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String info = getSharedPreferences().getString(Constant.AUTHENTICATE_INFO, "");
                        String isAuthenticate = getSharedPreferences().getString(Constant.IS_AUTHENTICATE, "");
                        if (StringUtils.isBlank(info)) {
                            info = "想成为专业的帮手,需先进行认证。提交以下信息到管理员邮箱 lunwenbang2020@163.com.cn\n1、身份证反正面照片\n2、本人手持证件照\n3、本站登录账号";
                        }
                        if (!isAuthenticate.equals(Constant.AUTHENTICATED)) {
                            CommonUtil.showDialog(SupportDetailActivity.this, info,"帮手招募", "确认");
                        }
                    }
                });

                CommonUtil.handlerHeadIcon(authorId, headIcon, details.get("username").toString());
                CommonUtil.setOnClickListenerForPHomePage(idStr, SupportDetailActivity.this, headIcon);
                CommonUtil.setOnClickListenerForPHomePage(idStr, SupportDetailActivity.this, author);
            }

        }
    }

}
