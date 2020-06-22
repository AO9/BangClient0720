package com.gto.bang.personal.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
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

import java.util.Map;

/**
 * 1213终于早下班两天，可以弄弄论文帮
 * 阿伯貌似在玩阴阳师
 */
public class PHomePageActivity extends BaseActivity{

    public static String REQUEST_TAG="PHomePageActivity_tag";
    Map<String, Object> userinfo;

    @Override
    public Context getContext() {
        return PHomePageActivity.this;
    }

    @Override
    public String getRequestTag() {
        return PHomePageActivity.class.getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_homepage);
    }

    /**
     * 根据用户id查看个人信息
     */
    public void initData() {
        ResponseListener listener = new ResponseListener();

        Bundle bundle=getIntent().getExtras();
        String authorId=bundle.getString("authorId");

        String url= Constant.URL_BASE+ Constant.USER_INFO_AJAX+"authorid="+authorId;
        CustomRequest req = new CustomRequest(this,listener,listener,null,url, Request.Method.GET);
        req.setTag(REQUEST_TAG);
        VolleyUtils.getRequestQueue(this).add(req);
    }

    /**
     * 请求响应
     */
    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(PHomePageActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            if(null==res.get(Constant.STATUS)||!Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())){
                String data=(null==res.get(Constant.DATA))?Constant.REQUEST_ERROR:res.get(Constant.DATA).toString();
                t = Toast.makeText(PHomePageActivity.this, "数据获取失败", Toast.LENGTH_SHORT);
                t.show();
            }else{
                userinfo = (Map<String, Object>)res.get("data");
                initFeilds();
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(PHomePageActivity.this).cancelAll(REQUEST_TAG);
    }

    public void onResume() {
        super.onResume();
        initData();
        MobclickAgent.onPageStart("个人首页查看");
        MobclickAgent.onResume(this);
    }


    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("个人首页查看");
        MobclickAgent.onPause(this);
    }


    private void initFeilds(){
        if(null==userinfo){
            return;
        }
        TextView name=(TextView)findViewById(R.id.homepage_name_tv);
        TextView region=(TextView)findViewById(R.id.homepage_region_tv);
        TextView gender=(TextView) findViewById(R.id.homepage_gender_tv);
        TextView signature=(TextView) findViewById(R.id.homepage_signature_tv);
        TextView school=(TextView) findViewById(R.id.homepage_school_tv);
        TextView level=(TextView) findViewById(R.id.homepage_level_tv);
        TextView phone=(TextView) findViewById(R.id.homepage_phone_tv);
//        TextView qq=(TextView) findViewById(R.id.homepage_qq_tv);

        String [] feilds=new String[]{Constant.USERNAME,Constant.CITY,Constant.GENDER,Constant.SIGNATURE
                ,Constant.SCHOOL,Constant.LEVEL,Constant.PHONE};
        TextView [] textViews=new TextView[]{name,region,gender,signature,school,level,phone};
        for(int i=0;i<feilds.length;i++){
            if(null!=userinfo.get(feilds[i])){
                textViews[i].setText(userinfo.get(feilds[i]).toString());
            }
        }

        TextView icon=(TextView) findViewById(R.id.homepage_icon_tv);
        Integer authorId=Integer.valueOf(userinfo.get(Constant.ID).toString());
        switch (authorId%4){
            case 0:
                icon.setBackgroundResource(R.drawable.corner);
                break;
            case 1:
                icon.setBackgroundResource(R.drawable.corner_green);
                break;
            case 2:
                icon.setBackgroundResource(R.drawable.corner_blue);
                break;
            case 3:
                icon.setBackgroundResource(R.drawable.corner_pink);
                break;
            default:
                icon.setBackgroundResource(R.drawable.corner);
                break;
        }
    }
}
