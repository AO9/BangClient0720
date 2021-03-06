package com.gto.bang.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.home.MainActivity;
import com.gto.bang.register.RegisterActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录功功能
 * 20160806 12点30 增加自动登录功能（上一次操作非注销）
 *
 * @六里桥 中午这么热阿贝还在玩奇迹暖暖，这名字听起来让人更想出汗
 * @20160827 更新：登录成功后，缓存服务器返回的有效用户信息
 *
 * 20210102 增加 找回密码
 */
public class LoginActivity extends BaseActivity {

    Button loginBtn;
    EditText nameEt;
    EditText passwordEt;
    TextView tipsTV;
    TextView forgetTV;

    Map<String, Object> userinfo;

    private static int AUTO_LOGIN = 1;
    private static int NUN_AUTO_LOGIN = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_STANDARD);
        setContentView(R.layout.login);
        initViews();
        //友盟SDK 禁止页面的默认统计方式  分ACTIVITY和Fragment统计
//        MobclickAgent.openActivityDurationTrack(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        autoLogin();
    }

    public void autoLogin() {
        String userName = getSharedPreferences().getString(Constant.USERNAME_V1, null);
        String password = getSharedPreferences().getString(Constant.PASSWORD, null);
        String id = getSharedPreferences().getString(Constant.ID, null);
        String lastAction = getSharedPreferences().getString(Constant.LASTACTION, null);
        //默认显示上一次登录的账号信息
        if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(id)) {
            nameEt.setText(userName);
            passwordEt.setText(password);
            //上一次用户操作不是［注销］则自动登录
            if (null == lastAction || !lastAction.equals("logout")) {
                login(userName, password);
            }
        }

    }


    public void initViews() {

        loginBtn = (Button) findViewById(R.id.home_login_btn);
        nameEt = (EditText) findViewById(R.id.home_nickname_et);
        passwordEt = (EditText) findViewById(R.id.home_password_et);
        tipsTV = (TextView) findViewById(R.id.tips);
        forgetTV = (TextView) findViewById(R.id.forget);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = nameEt.getText().toString();
                String password = passwordEt.getText().toString();
                if (StringUtils.isEmpty(username.trim()) || StringUtils.isEmpty(password.trim())) {
                    tipsTV.setText("用户名和密码不能为空！");
                } else {
                    username = username.replace("\n", "");
                    password = password.replace("\n", "");
                    login(username, password);
                }
            }
        });

        forgetTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonUtil.showDialog(getContext(), "请联系论文帮客服lwb20191202", "密码找回", "好的");
            }
        });


    }


    public void login(String userName, String password) {
        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.LOGIN_URL;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Constant.USERNAME_V1, userName);
        params.put(Constant.PASSWORD, password);
        params.put("client", "android");
        String imei = CommonUtil.getIMEI(getBaseContext());
        String androidId = CommonUtil.getAndroidId(getBaseContext());
        params.put(Constant.IMEI, imei);
        url = url + "?userName=" + userName + "&password=" + password + "&imei=" + imei + "&androidId=" + androidId;
        CustomRequest req = new CustomRequest(this, listener, listener, params, url, Request.Method.GET);
        req.setTag(getRequestTag());
        udpateButtonByStatus(Constant.BUTTON_STATUS_OFF, loginBtn);
        VolleyUtils.getRequestQueue(this).add(req);
    }


    @Override
    public Context getContext() {
        return LoginActivity.this;
    }

    @Override
    public String getRequestTag() {
        return "ACCOUNT_LOGIN_REQUEST";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(this).cancelAll(getRequestTag());
    }


    public class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError arg0) {
            udpateButtonByStatus(Constant.BUTTON_STATUS_ON, loginBtn);
            tipsTV.setText(Constant.REQUEST_ERROR);
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            if (null == res.get(Constant.STATUS) || !Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())) {
                String data = (null == res.get(Constant.DATA)) ? "登录失败" : res.get(Constant.DATA).toString();
                tipsTV.setText(data);
                udpateButtonByStatus(Constant.BUTTON_STATUS_ON, loginBtn);
            } else {
                userinfo = (Map<String, Object>) res.get("data");
                String[] feilds = new String[]{Constant.ID, Constant.USERNAME_V1, Constant.PASSWORD,
                        Constant.PHONE, Constant.SCHOOL, Constant.EDUCATION, Constant.EMAIL, Constant.VIP, Constant.PROMPT,
                        Constant.INFO, Constant.LEVEL_INSTRUCTION, Constant.USERNAME};
                // 缓存个人信息
                handleUserInfo(userinfo, feilds);
                Object usernameObj = userinfo.get(Constant.USERNAME);
                if (null != usernameObj) {
                    MobclickAgent.onProfileSignIn(usernameObj.toString());
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    /**
     * 前端缓存个人信息
     *
     * @param userinfo
     * @param feilds
     */
    public void handleUserInfo(Map<String, Object> userinfo, String[] feilds) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(Constant.INFO, "");
        editor.putString(Constant.VIP, "0");
        editor.putString(Constant.LEVEL_INSTRUCTION, "");
        for (int i = 0; i < feilds.length; i++) {
            if (null != userinfo.get(feilds[i])) {
                if (null != userinfo.get(feilds[i])) {
                    editor.putString(feilds[i], userinfo.get(feilds[i]).toString());
                }
            }
        }
        editor.putString(Constant.LASTACTION, null);
        editor.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_register:
                intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, 1002);
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                Bundle b = data.getExtras();
                String result = b.getString("result");
                //注册成功
                if ("success".equals(result)) {
                    Toast t = Toast.makeText(LoginActivity.this, "注册成功!正在登录", Toast.LENGTH_SHORT);
                    t.show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }

}
