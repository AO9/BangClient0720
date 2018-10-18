package com.gto.bang.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.college.AcademySelectActivity;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenjialong on 16/8/23 06:57.
 * 新帮写页面，从fragment独立出activity
 */
public class HBangWriteActivity extends BaseActivity {

    EditText phoneET;
    EditText mailET;
    EditText qqET;
    EditText schoolET;
    TextView academyTV;
    EditText cityET;
    EditText [] views;
    RelativeLayout rl;
    Button submit;

    String [] inputHints=new String[]{"请填写手机号！","请填写邮箱！","请填写QQ号！","请填写学校！","请填写城市！"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_bang);
        initViews();
    }

    /**
     * 初始化View的监听事件
     */
    public void initViews(){

        academyTV=(TextView)findViewById(R.id.bang_bang_academy_et);
        rl=(RelativeLayout)findViewById(R.id.bang_bang_academy_lv);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HBangWriteActivity.this, AcademySelectActivity.class);
                startActivityForResult(intent,2001);
            }
        });

        phoneET=(EditText)findViewById(R.id.bang_bang_phone_et);
        mailET=(EditText)findViewById(R.id.bang_bang_mail_et);
        qqET=(EditText)findViewById(R.id.bang_bang_qq_et);
        schoolET=(EditText)findViewById(R.id.bang_bang_school_et);
        cityET=(EditText)findViewById(R.id.bang_bang_city_et);
        mailET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        // 自动填充用户信息
        String [] feilds=new String[]{Constant.PHONE,Constant.SCHOOL};
        EditText [] editTexts=new EditText[]{phoneET,schoolET};
        for(int i=0;i<feilds.length;i++){
            String value=this.getSharedPreferences().getString(feilds[i],null);
            if(StringUtils.isNotBlank(value)){
                editTexts[i].setText(value);
            }
        }
        views=new EditText[]{phoneET,mailET,schoolET,cityET};
        submit = (Button)findViewById(R.id.bang_submit_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()){
                    //校验通过后拼接请求参数并向服务器发送请求
                    HashMap<String, String> params=new HashMap<String, String>();
                    params.put(Constant.PHONE,phoneET.getText().toString());
                    params.put(Constant.EMAIL,mailET.getText().toString());
                    params.put(Constant.SCHOOL,schoolET.getText().toString());
                    params.put(Constant.ACADEMY,academyTV.getText().toString());
                    params.put(Constant.CITY,cityET.getText().toString());
                    params.put(Constant.QQ,qqET.getText().toString());

                    params.put(Constant.USERNAME,getSharedPreferences().getString(Constant.USERNAME,Constant.EMPTY));
                    params.put(Constant.USERID,getSharedPreferences().getString(Constant.ID,Constant.AUTHORID_DEFAULT));
                    publish(params);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==20001){
            Bundle b=data.getExtras();
            String academy=b.getString(Constant.ACADEMY);
            this.academyTV.setText(academy);
        }

    }


    /**
     * 字段非空校验
     * @return
     */
    private boolean check(){
        Toast t;
        if(StringUtils.isEmpty(academyTV.getText().toString())){
            t= Toast.makeText(HBangWriteActivity.this, "请选择学院！", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }
        for(int i=0;i<views.length;i++){
            if(null==views[i].getText()|| StringUtils.isEmpty(views[i].getText().toString())){
                t= Toast.makeText(HBangWriteActivity.this, inputHints[i], Toast.LENGTH_SHORT);
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
        Listener listener = new Listener();
        String url=Constant.URL_BASE+ Constant.BANG_CREATE_AJAX;
        CustomRequest req = new CustomRequest(HBangWriteActivity.this,listener,listener,params,url, Request.Method.POST);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(HBangWriteActivity.this).add(req);
    }

    protected String getRequestTag(){
        return "HBangWriteActivity_request";
    }

    protected String[] getHints(){
        return new String[]{"请求成功，客服会第一时间联系您","服务繁忙,请稍后重试"};
    }

    /**
     * 请求响应
     */
    public  class Listener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {

        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(HBangWriteActivity.this, getHints()[1], Toast.LENGTH_SHORT);
            t.show();
            submit.setEnabled(true);
            submit.setText("请求帮帮");
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            Object status=res.get("status");
            if(null==status||!Constant.RES_SUCCESS.equals(status.toString())){
                t = Toast.makeText(HBangWriteActivity.this, getHints()[1], Toast.LENGTH_SHORT);
            }else{
                t = Toast.makeText(HBangWriteActivity.this, getHints()[0], Toast.LENGTH_SHORT);
            }
            t.show();
            finish();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("帮写");
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("帮写");
        MobclickAgent.onPause(this);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(this).cancelAll(getRequestTag());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
