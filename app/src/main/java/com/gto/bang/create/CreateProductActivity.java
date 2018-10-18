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
 * 新建
 * 20170203  @金三角
 */
public class CreateProductActivity extends BaseCreateActivity {

    TextView title;
    TextView content;
    TextView price;
    TextView wechat;
    TextView phone;
    Button publish;

    TextView[] views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_product);
        initViews();
    }

    public void initViews(){

        content=(TextView)findViewById(R.id.service_content);
        price=(TextView)findViewById(R.id.service_price);
        title=(TextView)findViewById(R.id.service_title);
        publish=(Button)findViewById(R.id.service_publish);
        wechat=(TextView)findViewById(R.id.service_wechat);
        phone=(TextView)findViewById(R.id.service_phone);

        submit = (Button)findViewById(R.id.service_publish);
        views=new TextView[]{title,content,price,wechat,phone};
        setSubmitText("发布服务");

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()){
                    //校验通过后拼接请求参数并向服务器发送请求
                    HashMap<String, String> params=new HashMap<String, String>();
                    params.put("title",title.getText().toString());
                    params.put("content",content.getText().toString());
                    params.put("price",price.getText().toString());
                    params.put("authorId",getSharedPreferences().getString(Constant.ID,Constant.AUTHORID_DEFAULT));
                    params.put("wechat",wechat.getText().toString());
                    params.put("phone",phone.getText().toString());

                    publish(params);
                }
            }
        });
    }

    private boolean check(){
        for(int i=0;i<views.length;i++){
            if(null==views[i].getText()||StringUtils.isEmpty(views[i].getText().toString())){
                Toast t = Toast.makeText(this, "内容不全，请填写完整！", Toast.LENGTH_SHORT);
                t.show();
                return false;
            }
        }
        return true;
    }

    public void publish(HashMap<String, String> params) {
        ResponseListener listener = new ResponseListener();
        String url=Constant.URL_BASE+ Constant.PRODUCT_CREATE_URL;
        CustomRequest req = new CustomRequest(this,listener,listener,params,url, Request.Method.POST);
        req.setTag(getRequestTag());
        submit.setText("正在发布中..");
        submit.setBackgroundColor(Color.GRAY);
        submit.setEnabled(false);
        VolleyUtils.getRequestQueue(this).add(req);
    }

    @Override
    protected String getRequestTag(){
        return "CreateProductActivity";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("新建帮助");
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("新建帮助");
        MobclickAgent.onPause(this);
    }


}
