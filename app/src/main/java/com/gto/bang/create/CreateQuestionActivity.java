package com.gto.bang.create;


import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.gto.bang.R;
import com.gto.bang.base.BaseCreateActivity;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * 问答－新建功能页
 * 1105日  @金凤呈祥
 * 2019年6月2日
 */
public class CreateQuestionActivity extends BaseCreateActivity {

    TextView question_describe;
    TextView[] textViews;
    String[] inputHints = new String[]{"请填写问答内容"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_question);
        initViews();
    }

    public void initViews() {

        question_describe = (TextView) findViewById(R.id.question_describe_et);
        textViews = new TextView[]{question_describe};

        submit = (Button) findViewById(R.id.question_ok_btn);
        setSubmitText("发布问题");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String describe = question_describe.getText().toString();
                if (describe.length()<9){
                    Toast t = Toast.makeText(CreateQuestionActivity.this, "请详细阐述你的问题，至少10个字", Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }

                if (check()) {
                    //校验通过后拼接请求参数并向服务器发送请求
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put(Constant.CONTENT, question_describe.getText().toString());
                    params.put(Constant.TYPE, Constant.TYPE_QUESTION);
                    params.put("userId", getSharedPreferences().getString(Constant.ID, Constant.AUTHORID_DEFAULT));
//                    params.put("authorid", getSharedPreferences().getString(Constant.ID, Constant.AUTHORID_DEFAULT));
                    publish(params);
                }
            }
        });
    }

    /**
     * 发布
     */
    public void publish(HashMap<String, String> params) {
        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.ARTICLE_CREATE;
        CustomRequest req = new CustomRequest(this, listener, listener, params, url, Request.Method.POST);
        req.setTag(getRequestTag());
        submit.setEnabled(false);
        submit.setText("正在全力发布");
        submit.setTextColor(Color.GRAY);
        VolleyUtils.getRequestQueue(this).add(req);
    }

    /**
     * 字段非空校验
     *
     * @return
     */
    private boolean check() {
        for (int i = 0; i < textViews.length; i++) {
            if (null == textViews[i].getText() || StringUtils.isEmpty(textViews[i].getText().toString())) {
                Toast t = Toast.makeText(this, inputHints[i], Toast.LENGTH_SHORT);
                t.show();
                return false;
            }
        }
        return true;
    }

    protected String getRequestTag() {
        return "CreateQuestionActivity";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("新建问答");
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("新建问答");
        MobclickAgent.onPause(this);
    }

}
