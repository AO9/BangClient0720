package com.gto.bang.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 注册功能按钮还没有实现其功能 0727 22:56 休息去啦
 * 0728 22点39分  调整该功能，增加点击后的按钮状态控制，避免同时重复注册，将字段的初始化过程放到点击事件里，保证正确值
 *
 * @汇融大厦 阿贝在加班，我和大白在这里等她，听男左女右，mark
 * <p/>
 * getSharedPreferences(Constant.DB, Activity.MODE_MULTI_PROCESS).edit()方法每次都是返回一个新的值
 * 0803 24:21分 阿贝很冷，睡梦中关上空调
 * <p/>
 * 20190303 星期日 增加imei设备号
 * @location 绿岛 阿贝在黑山
 */
public class RegisterActivity extends BaseActivity {

    Button register;

    EditText userNameET;
    EditText passwordET;
    EditText phoneET;
    EditText schoolET;
    EditText wechatET;
    TextView tipsTV;
//    RelativeLayout rl;

    String[] tips = new String[]{"请填写昵称", "请设置账号密码", "请填写正确的手机号", "请填写学校信息", "微信不能为空"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initViews();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==20002){
//            Bundle b=data.getExtras();
//            String academy=b.getString(Constant.EDUCATION);
//            this.educationTV.setText(academy);
//        }

    }

    public void initViews() {

        userNameET = (EditText) findViewById(R.id.userName_et);
        phoneET = (EditText) findViewById(R.id.register_phone_et);
        passwordET = (EditText) findViewById(R.id.register_secret_et);
        register = (Button) findViewById(R.id.f_register_ok_btn);
        schoolET = (EditText) findViewById(R.id.register_school_et);
//        educationTV=(TextView) findViewById(R.id.bang_education_et);
        tipsTV = (TextView) findViewById(R.id.tips);
        wechatET = (EditText) findViewById(R.id.wehcat_et);


//        rl=(RelativeLayout)findViewById(R.id.bang_aducation_lv);
//        rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(RegisterActivity.this, EducationSelectActivity.class);
//                startActivityForResult(intent,2002);
//            }
//        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText[] array = new EditText[]{userNameET, passwordET, phoneET, schoolET, wechatET};
                String password1 = passwordET.getText().toString();
                String phone = phoneET.getText().toString();
                String userName = userNameET.getText().toString();
                String school = schoolET.getText().toString();
                String wechat = wechatET.getText().toString();

                //非空校验
                for (int i = 0; i < array.length; i++) {
                    EditText editText = array[i];
                    if (StringUtils.isEmpty(editText.getText().toString())) {
                        tipsTV.setText(tips[i]);
                        return;
                    }
                }

                register(userName, password1, phone, school, wechat);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    public void register(String userName, String password, String phone, String school, String wechat) {

        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.REGISTER_URL;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Constant.USERNAME_V1, userName.replace("\n", ""));
        params.put(Constant.PASSWORD, password.replace("\n", ""));
        params.put(Constant.PHONE, phone);
        params.put(Constant.SCHOOL, school);

        if (StringUtils.isNotBlank(wechat)) {
            params.put(Constant.WECHAT, wechat);
        }

        if (StringUtils.isNotBlank(CommonUtil.getIMEI(getBaseContext()))) {
            params.put(Constant.IMEI, CommonUtil.getIMEI(getBaseContext()));
        }

        if (StringUtils.isNotBlank(CommonUtil.getAndroidId(getBaseContext()))) {
            params.put(Constant.ANDROID_ID, CommonUtil.getAndroidId(getBaseContext()));
        }

        CommonUtil.localLog("register step 1");
        CustomRequest req = new CustomRequest(this, listener, listener, params, url, Request.Method.POST);
        req.setTag(getRequestTag());
        udpateButtonByStatus(Constant.BUTTON_STATUS_OFF, register);
        CommonUtil.localLog("register step 2");
        VolleyUtils.getRequestQueue(this).add(req);
    }


    @Override
    public Context getContext() {
        return RegisterActivity.this;
    }

    @Override
    public String getRequestTag() {
        return RegisterActivity.class.getName();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(this).cancelAll(getRequestTag());
    }

    @Override
    public android.support.v4.app.FragmentManager getSupportFragmentManager() {
        return null;
    }


    public class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError arg0) {
            udpateButtonByStatus(Constant.BUTTON_STATUS_ON, register);
            CommonUtil.showTips(Constant.REQUEST_ERROR, RegisterActivity.this);
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            CommonUtil.localLog("register step 4 res=" + res.get(Constant.DATA));

            if (null == res.get(Constant.STATUS) || !Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())) {

                String data = (null == res.get(Constant.DATA)) ? Constant.REGISTER_ERROR : res.get(Constant.DATA).toString();
                udpateButtonByStatus(Constant.BUTTON_STATUS_ON, register);
                CommonUtil.showTips(data, RegisterActivity.this);

            } else {
                Map<String, Object> userinfo = (Map<String, Object>) res.get(Constant.DATA);
                SharedPreferences.Editor editor = getEditor();
                editor.putString(Constant.USERNAME, userNameET.getText().toString());
                editor.putString(Constant.PASSWORD, passwordET.getText().toString());
                editor.putString(Constant.ID, userinfo.get(Constant.ID).toString());
                // 20200626 冗余这个userId字段，后续统一使用这个命名
                editor.putString(Constant.USERID_V1, userinfo.get(Constant.ID).toString());
                editor.commit();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("result", "success");
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();

            }

        }
    }

}
