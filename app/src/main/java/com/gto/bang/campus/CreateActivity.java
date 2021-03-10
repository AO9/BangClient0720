package com.gto.bang.campus;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
 * 校内帮-新建
 */
public class CreateActivity extends BaseCreateActivity {


    RadioButton work;
    RadioButton life;
    RadioButton emotion;
    RadioButton study;

    EditText price;

    RadioGroup typeGroup;
    String type = null;

    TextView question_describe;
    TextView[] textViews;
    String[] inputHints = new String[]{"请填写问答内容"};

    private Context getContext() {
        return CreateActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campus_create);
        initViews();
    }

    public void initViews() {

        work = (RadioButton) findViewById(R.id.cp_work_rb);
        life = (RadioButton) findViewById(R.id.cp_life_rb);
        emotion = (RadioButton) findViewById(R.id.cp_emotion_rb);
        study = (RadioButton) findViewById(R.id.cp_study_rb);
        typeGroup = (RadioGroup) findViewById(R.id.type_list);

        price = (EditText) findViewById(R.id.price);

        typeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int buttonId) {
                switch (buttonId) {
                    case R.id.cp_work_rb:
                        if (work.isChecked()) {
                            CommonUtil.showTips("工作", getContext());
                            type = Constant.TYPE_CP_WORK;
                        }
                        break;
                    case R.id.cp_emotion_rb:
                        if (emotion.isChecked()) {
                            type = Constant.TYPE_CP_EMOTION;
                            CommonUtil.showTips("情感", getContext());
                        }
                        break;

                    case R.id.cp_study_rb:
                        if (study.isChecked()) {
                            type = Constant.TYPE_CP_STUDY;
                            CommonUtil.showTips("学习", getContext());
                        }
                        break;
                    case R.id.cp_life_rb:
                        if (life.isChecked()) {
                            type = Constant.TYPE_CP_LIFE;
                            CommonUtil.showTips("生活", getContext());
                        }
                        break;

                }
            }
        });


        question_describe = (TextView) findViewById(R.id.question_describe_et);
        textViews = new TextView[]{question_describe};

        submit = (Button) findViewById(R.id.question_ok_btn);
        setSubmitText("发布问题");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String describe = question_describe.getText().toString();
                if (describe.length() < 9) {
                    CommonUtil.showTips("请详细阐述你的问题，至少10个字", getContext());
                    return;
                }

                if (type == null) {
                    CommonUtil.showTips("请选择分类标签", getContext());
                    return;
                }

                if (price.getText().toString().trim().length() == 0) {
                    CommonUtil.showTips("请填写红包金额", getContext());
                    return;
                }

                if (check()) {
                    //校验通过后拼接请求参数并向服务器发送请求
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put(Constant.CONTENT, question_describe.getText().toString());
                    params.put(Constant.TYPE, type);
                    params.put(Constant.USERID_V1, getSharedPreferences().getString(Constant.ID, Constant.AUTHORID_DEFAULT));
                    params.put(Constant.PRICE, price.getText().toString());
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
