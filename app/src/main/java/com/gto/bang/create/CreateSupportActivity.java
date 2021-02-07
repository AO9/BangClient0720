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
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * 2019年6月2日 红包求助
 */
public class CreateSupportActivity extends BaseCreateActivity {

    TextView price;
    TextView tips;
    TextView describe;
    TextView wordNumber;
    TextView[] textViews;
    String inputHints = new String("内容填写不全");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_support);
        initViews();
    }


    public void initTextView(TextView textView,String hint) {
        SpannableString ss = new SpannableString(hint);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setHint(new SpannedString(ss));
    }

    public void initViews() {

        describe = (TextView) findViewById(R.id.question_describe_et);
        price = (TextView) findViewById(R.id.question_price);
        wordNumber = (TextView) findViewById(R.id.wordNumber);

        initTextView(describe,"请简单描述您的论文需求.\n\n客服小帮会尽量24小时内微信联系您，请留意.\n\n关注论文帮官微 lwb20191202\n\nTips:请不要添加其他不明来源账号");
        initTextView(price,"70~120元/千字，最终以实际沟通为准");
        initTextView(wordNumber,"请填写字数");

        tips = (TextView) findViewById(R.id.tips);
        textViews = new TextView[]{price, describe, wordNumber};

        submit = (Button) findViewById(R.id.question_ok_btn);
        setSubmitText("发布");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    //校验通过后拼接请求参数并向服务器发送请求
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put(Constant.PRICE, price.getText().toString());
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
        // 20200626 预算
        String priceValue = price.getText().toString();
        if (Integer.valueOf(priceValue).intValue() == 0) {
            tips.setText("预算不能为0");
            return false;
        }

        // 20200626 预算
        String describeValue = describe.getText().toString();
        if (describeValue.length() < 0) {
            tips.setText("论文问题描述过于简单，至少10个字");
            return false;
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
