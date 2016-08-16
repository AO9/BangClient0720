package com.gto.bang.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * 注册功能按钮还没有实现其功能 0727 22:56 休息去啦
 * 0728 22点39分  调整该功能，增加点击后的按钮状态控制，避免同时重复注册，将字段的初始化过程放到点击事件里，保证正确值
 * @汇融大厦 阿贝在加班，我和大白在这里等她，听男左女右，mark
 *
 * getSharedPreferences(Constant.DB, Activity.MODE_MULTI_PROCESS).edit()方法每次都是返回一个新的值
 * 0803 24:21分 阿贝很冷，睡梦中关上空调
 * */
public class RegisterActivity extends BaseActivity {

    Button register;

    EditText nicknameET;
    EditText passwordET;
    EditText password_2ET;
    EditText phoneET;

    String [] tips=new String[]{"昵称不能为空!","密码不能为空!","确认密码不能为空!","手机号不能为空!"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);
        initViews();
    }

    public void initViews(){

        nicknameET=(EditText)findViewById(R.id.register_nickname_et);
        phoneET=(EditText)findViewById(R.id.register_phone_et);
        passwordET=(EditText)findViewById(R.id.register_secret_et);
        password_2ET=(EditText)findViewById(R.id.register_secret_2_et);
        register=(Button)findViewById(R.id.f_register_ok_btn);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText [] array=new EditText[]{nicknameET,passwordET,password_2ET,phoneET};
                String password1=passwordET.getText().toString();
                String password2=password_2ET.getText().toString();
                String phone=phoneET.getText().toString();
                String nickname=nicknameET.getText().toString();

                Toast t;

                //非空校验
                for(int i=0;i<array.length;i++){
                    EditText editText=array[i];
                    if(StringUtils.isEmpty(editText.getText().toString())){
                        t = Toast.makeText(RegisterActivity.this, tips[i], Toast.LENGTH_SHORT);
                        t.show();
                        return;
                    }
                }

                //两次密码是否一致
                if(!password1.equals(password2)){
                    t = Toast.makeText(RegisterActivity.this, "请确保两次密码保持一致！", Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }
                register(nickname,password1,phone);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }




    public void register(String username,String password,String phone) {
        ResponseListener listener = new ResponseListener();
        String url= Constant.URL_BASE+ Constant.REGISTER_AJAX;
        HashMap<String, String> params=new HashMap<String, String>();
        params.put(Constant.USERNAME,username.replace("\n",""));
        params.put(Constant.PASSWORD,password.replace("\n",""));
        params.put(Constant.PHONE,phone);
        params.put("client","android");
        CustomRequest req = new CustomRequest(this,listener,listener,params,url, Request.Method.POST);
        Log.i("sjl","url:"+ url);
        req.setTag(getRequestTag());
        register.setText("正在注册,请等待...");
        register.setEnabled(false);
        VolleyUtils.getRequestQueue(this).add(req);
    }


    protected String getRequestTag(){
        return "ACCOUNT_REGISTER_REQUEST";
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
            register.setText("提交");
            register.setEnabled(true);
            t = Toast.makeText(RegisterActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            Log.i("sjl","register res status:"+res.get(Constant.STATUS)+" data: "+res.get(Constant.DATA));

            if(null==res.get(Constant.STATUS)||!Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())){
                String data=(null==res.get(Constant.DATA))?Constant.REGISTER_ERROR:res.get(Constant.DATA).toString();
                register.setText(Constant.SUBMIT);
                register.setEnabled(true);
                t = Toast.makeText(RegisterActivity.this, data, Toast.LENGTH_SHORT);
                t.show();
            }else{
                Map<String, Object> userinfo=(Map<String, Object>)res.get(Constant.DATA);
                SharedPreferences.Editor editor=getEditor();
                editor.putString(Constant.USERNAME,nicknameET.getText().toString());
                editor.putString(Constant.PASSWORD,passwordET.getText().toString());
                editor.putString(Constant.ID,userinfo.get(Constant.ID).toString());
                editor.commit();
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("result","success");
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();

            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("注册");
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("注册");
        MobclickAgent.onPause(this);
    }


}
