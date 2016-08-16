package com.gto.bang.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.home.HMainActivity;
import com.gto.bang.register.RegisterActivity;
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
 * @六里桥 中午这么热阿贝还在玩奇迹暖暖，这名字听起来让人更想出汗
 */
public class LoginActivity extends BaseActivity {

    Button loginBtn;
    EditText nameEt;
    EditText passwordEt;

    Map<String, Object> userinfo;

    private  static  int AUTO_LOGIN=1;
    private  static  int NUN_AUTO_LOGIN=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_STANDARD);
        setContentView(R.layout.bang_login);
        initViews();
        //友盟SDK 禁止页面的默认统计方式  分ACTIVITY和Fragment统计
        MobclickAgent.openActivityDurationTrack(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoLogin();
    }

    public void autoLogin(){
        String username=getSharedPreferences().getString(Constant.USERNAME,null);
        String password=getSharedPreferences().getString(Constant.PASSWORD,null);
        String id=getSharedPreferences().getString(Constant.ID,null);
        String lastAction=getSharedPreferences().getString(Constant.LASTACTION,null);
        //默认显示上一次登录的账号信息
        if(StringUtils.isNotEmpty(username)&&StringUtils.isNotEmpty(password)&&StringUtils.isNotEmpty(id)){
            nameEt.setText(username);
            passwordEt.setText(password);
            //上一次用户操作不是［注销］则自动登录
            if(null==lastAction||!lastAction.equals("logout")){
                login(username,password);
            }
        }

    }


    public void initViews(){

        loginBtn=(Button)findViewById(R.id.home_login_btn);
        nameEt=(EditText)findViewById(R.id.home_nickname_et);
        passwordEt=(EditText)findViewById(R.id.home_password_et);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username=nameEt.getText().toString();
                String password=passwordEt.getText().toString();
                Log.i("sjl","username:"+username);
                Log.i("sjl","password:"+password);
                if(StringUtils.isEmpty(username.trim())|| StringUtils.isEmpty(password.trim())){
                    Toast t = Toast.makeText(LoginActivity.this, "用户名和密码不能为空！", Toast.LENGTH_SHORT);
                    t.show();
                }else{
                    username=username.replace("\n","");
                    password=password.replace("\n","");
                    login(username,password);
                }
            }
        });
    }


    public void login(String username,String password) {
        ResponseListener listener = new ResponseListener();
        String url= Constant.URL_BASE+ Constant.LOGIN_AJAX;

        HashMap<String, String> params=new HashMap<String, String>();
        params.put(Constant.USERNAME,username);
        params.put(Constant.PASSWORD,password);
        params.put("client","android");

        CustomRequest req = new CustomRequest(this,listener,listener,params,url, Request.Method.POST);
        Log.i("sjl","正在登录 url:"+ url);
        req.setTag(getRequestTag());
        loginBtn.setEnabled(false);
        loginBtn.setText("正在登录...");
        VolleyUtils.getRequestQueue(this).add(req);
    }


    protected String getRequestTag(){
        return "ACCOUNT_LOGIN_REQUEST";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(this).cancelAll(getRequestTag());
    }


    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            //自动请求标志归零
            loginBtn.setEnabled(true);
            loginBtn.setText("登录");
            t = Toast.makeText(LoginActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            Log.i("sjl","login res status:"+res.get(Constant.STATUS)+" data: "+res.get(Constant.DATA));
            if(null==res.get(Constant.STATUS)||!Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())){
                String data=(null==res.get(Constant.DATA))?"登录失败":res.get(Constant.DATA).toString();
                t = Toast.makeText(LoginActivity.this, data, Toast.LENGTH_SHORT);
                t.show();
                loginBtn.setEnabled(true);
                loginBtn.setText("登录");
            }else{
                userinfo = (Map<String, Object>)res.get("data");

                Object idObj=userinfo.get(Constant.ID);
                Object usernameObj=userinfo.get(Constant.USERNAME);
                Object passwordObj=userinfo.get(Constant.PASSWORD);
                Object phoneObj=userinfo.get(Constant.PHONE);
                if(null!=usernameObj && null!=passwordObj && null!=idObj && null!=phoneObj){
                    SharedPreferences.Editor editor=getEditor();
                    editor.putString(Constant.USERNAME,usernameObj.toString());
                    editor.putString(Constant.PASSWORD,passwordObj.toString());
                    editor.putString(Constant.ID,idObj.toString());
                    editor.putString(Constant.LASTACTION,null);
                    editor.putString(Constant.PHONE,phoneObj.toString());

                    Object emailObj=userinfo.get(Constant.EMAIL);
                    if(null!=phoneObj && null!=emailObj){
                        editor.putString(Constant.EMAIL,emailObj.toString());
                    }
                    Log.i("sjl","username:"+usernameObj.toString()+" is logining in");
                    editor.commit();
                    MobclickAgent.onProfileSignIn(usernameObj.toString());
                }

                Intent intent=new Intent(LoginActivity.this, HMainActivity.class);
                startActivity(intent);
                finish();
            }
        }
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
                startActivityForResult(intent,1002);
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                Bundle b=data.getExtras();
                String result=b.getString("result");
                //注册成功
                if("success".equals(result)){
                    Toast t = Toast.makeText(LoginActivity.this, "注册成功!正在登录", Toast.LENGTH_SHORT);
                    t.show();
                    Intent intent=new Intent(LoginActivity.this, HMainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }

}
