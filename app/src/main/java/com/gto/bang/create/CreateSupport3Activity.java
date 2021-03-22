package com.gto.bang.create;


import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.gto.bang.R;
import com.gto.bang.base.BaseCreateActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * 2021年3月23日 降重
 */
public class CreateSupport3Activity extends BaseCreateActivity {

    TextView tips;
    TextView describe;
    TextView wordNumber;
    Button copy;
    TextView[] textViews;
    String inputHints = new String("内容填写不全");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_support3);
        initViews();
    }


    public void initTextView(TextView textView, String hint) {
        SpannableString ss = new SpannableString(hint);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setHint(new SpannedString(ss));
    }

    public void initViews() {


        copy = (Button) findViewById(R.id.copy);
        CommonUtil.copy(copy, this);

        describe = (TextView) findViewById(R.id.question_describe_et);
        wordNumber = (TextView) findViewById(R.id.wordNumber);

        initTextView(describe, "您的专业");
        initTextView(wordNumber, "请填写字数");

        tips = (TextView) findViewById(R.id.tips);
        textViews = new TextView[]{describe, wordNumber};

        submit = (Button) findViewById(R.id.question_ok_btn);
        setSubmitText("发布");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    //校验通过后拼接请求参数并向服务器发送请求
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put(Constant.PRICE, "1");
                    params.put(Constant.CONTENT, describe.getText().toString());
                    params.put(Constant.TYPE, Constant.TYPE_SUPPORT);
                    params.put("userId", getSharedPreferences().getString(Constant.ID, Constant.AUTHORID_DEFAULT));
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
        CommonUtil.localLog("降重|润色");
    }

    /**
     * 字段校验
     *
     * @return
     */
    private boolean check() {
        for (int i = 0; i < textViews.length; i++) {
            if (null == textViews[i].getText() || StringUtils.isEmpty(textViews[i].getText().toString())) {
                tips.setText(inputHints);
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
