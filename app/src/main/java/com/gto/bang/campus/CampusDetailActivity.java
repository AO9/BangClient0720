package com.gto.bang.campus;

import android.content.Context;
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
import com.gto.bang.base.BaseActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 20201108 校内帮 详情页
 */
public class CampusDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campus_detail);
        initDatas();
    }

    public void initDatas() {
        Bundle bundle = getIntent().getExtras();
        String id = null != bundle.getString("id") ? bundle.getString("id").toString() : null;
        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.ARTICLE_DETAIL_AJAX + "id=" + (int) Double.valueOf(id).doubleValue();
        CustomRequest req = new CustomRequest(this, listener, listener, null, url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(this).add(req);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public String getRequestTag() {
        return CampusDetailActivity.class.getName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t;

        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(CampusDetailActivity.this, "网络请求失败，请重试", Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            if (null == res.get("status") || !Constant.RES_SUCCESS.equals(res.get("status").toString())) {
                t = Toast.makeText(CampusDetailActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
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
                        CommonUtil.log("校内帮-举手",getContext(),getRequestTag(),getUserId());
                        String info = getSharedPreferences().getString(Constant.AUTHENTICATE_INFO, "");
                        String isAuthenticate = getSharedPreferences().getString(Constant.IS_AUTHENTICATE, "");
                        if (StringUtils.isBlank(info)) {
                            info = "目前有多位同学举手要帮助题主\n\n1、题主将在24小时内选定帮助者\n\n2、被确定后，您会收到题注的微信好友申请";
                        }
                        if (!isAuthenticate.equals(Constant.AUTHENTICATED)) {
                            CommonUtil.showDialog(CampusDetailActivity.this, info, "您的爱心已收到!", "好的，我知道了");
                        }
                    }
                });

                CommonUtil.handlerHeadIcon(authorId, headIcon, details.get("username").toString());
                CommonUtil.setOnClickListenerForPHomePage(idStr, CampusDetailActivity.this, headIcon);
                CommonUtil.setOnClickListenerForPHomePage(idStr, CampusDetailActivity.this, author);
            }

        }
    }

}
