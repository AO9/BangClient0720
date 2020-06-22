package com.gto.bang.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseInputFragment;
import com.gto.bang.college.AcademySelectActivity;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenjialong on 16/6/9 19:53.
 * 首页－帮帮tab 备份  要该版20160823
 * 20160806
 */
public class HBangbangFragmentBak extends BaseInputFragment {

    EditText phoneET;
    EditText mailET;
    EditText schoolET;
    TextView academyTV;
    EditText cityET;
    EditText[] views;
    RelativeLayout rl;
    Button submit;

    String[] inputHints = new String[]{"请填写手机号！", "请填写邮箱！", "请填写学校！", "请填写城市！"};

    public HBangbangFragmentBak() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.bang_bang, container, false);
        academyTV = (TextView) rootView.findViewById(R.id.bang_bang_academy_et);
        rl = (RelativeLayout) rootView.findViewById(R.id.bang_bang_academy_lv);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AcademySelectActivity.class);
                startActivityForResult(intent, 2001);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 20001) {
            Bundle b = data.getExtras();
            String academy = b.getString(Constant.ACADEMY);
            Toast t = Toast.makeText(getActivity(), b.getString(Constant.ACADEMY), Toast.LENGTH_SHORT);
            t.show();
            this.academyTV.setText(academy);
        }

    }


    /**
     * 初始化View的监听事件
     */
    @Override
    public void initViews() {

        View rootView = getView();
        phoneET = (EditText) rootView.findViewById(R.id.bang_bang_phone_et);
        mailET = (EditText) rootView.findViewById(R.id.bang_bang_mail_et);
        schoolET = (EditText) rootView.findViewById(R.id.bang_bang_school_et);
        cityET = (EditText) rootView.findViewById(R.id.bang_bang_city_et);
        mailET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        views = new EditText[]{phoneET, mailET, schoolET, cityET};

        submit = (Button) rootView.findViewById(R.id.bang_submit_btn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sjl", "帮帮开始校验");
                if (check()) {
                    Log.i("sjl", "帮帮校验通过");
                    //校验通过后拼接请求参数并向服务器发送请求
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("phone", phoneET.getText().toString());
                    params.put("email", mailET.getText().toString());
                    params.put("school", schoolET.getText().toString());
                    params.put("academy", academyTV.getText().toString());
                    params.put("city", cityET.getText().toString());
                    params.put("username", getSharedPreferences().getString(Constant.USERNAME, Constant.EMPTY));
                    params.put("userid", getSharedPreferences().getString(Constant.ID, Constant.AUTHORID_DEFAULT));
                    publish(params);
                }
            }
        });
    }

    /**
     * 字段非空校验
     *
     * @return
     */
    private boolean check() {
        Log.i("sjl", "begin check");
        Toast t;
        if (StringUtils.isEmpty(academyTV.getText().toString())) {
            t = Toast.makeText(getActivity(), "请选择学院！", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }
        for (int i = 0; i < views.length; i++) {
            if (null == views[i].getText() || StringUtils.isEmpty(views[i].getText().toString())) {
                t = Toast.makeText(getActivity(), inputHints[i], Toast.LENGTH_SHORT);
                t.show();
                return false;
            }
        }
        return true;
    }

    /**
     * 创建 帮帮
     */
    public void publish(HashMap<String, String> params) {
        submit.setEnabled(false);
        submit.setText("正在提交...");
        Listener listener = new Listener();
        String url = Constant.URL_BASE + Constant.BANG_CREATE_AJAX;
        CustomRequest req = new CustomRequest(getActivity(), listener, listener, params, url, Request.Method.POST);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }

    @Override
    public String getRequestTag() {
        return HBangbangFragmentBak.class.getName();
    }

    @Override
    protected String[] getHints() {
        return new String[]{"请求成功，客服会第一时间联系您", "服务繁忙,请稍后重试"};
    }

    /**
     * 请求响应
     */
    public class Listener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {

        Toast t;

        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(getActivity(), getHints()[1], Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            Object status = res.get("status");
            if (null == status || !Constant.RES_SUCCESS.equals(status.toString())) {
                t = Toast.makeText(getActivity(), getHints()[1], Toast.LENGTH_SHORT);
                setFlag(true);
            } else {
                t = Toast.makeText(getActivity(), getHints()[0], Toast.LENGTH_SHORT);
            }
            submit.setEnabled(true);
            submit.setText("请求帮帮");
            t.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("主页－帮帮"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("主页－帮帮");
    }
}