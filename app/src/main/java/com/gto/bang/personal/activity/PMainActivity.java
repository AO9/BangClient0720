package com.gto.bang.personal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
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
 * Created by shenjialong on 16/6/11 07:20.
 * 个人中心主页面
 */
public class PMainActivity extends BaseActivity {

    private RelativeLayout noticeRL;
    private RelativeLayout messageRL;
    private RelativeLayout experienceRL;
    private RelativeLayout questionRL;
    RelativeLayout[] rls;

    //显示未读消息数
    private TextView messageNumTV;
    private TextView noticeNumTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_person_main);
        initViews();
    }

    private void initViews(){

        experienceRL=(RelativeLayout)findViewById(R.id.pexperience_rl);
        messageRL=(RelativeLayout)findViewById(R.id.pmessage_rl);
        questionRL=(RelativeLayout)findViewById(R.id.pquestion_rl);
        noticeRL=(RelativeLayout)findViewById(R.id.pnotice_rl);
        messageNumTV=(TextView)findViewById(R.id.pmessage_num_tv);
        noticeNumTV=(TextView)findViewById(R.id.pnotice_num_tv);
        rls=new RelativeLayout[]{noticeRL,messageRL,experienceRL,questionRL};
        for(int i=0;i<rls.length;i++){
            rls[i].setOnClickListener(listener);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.pmessage_rl:
                    intent =new Intent(PMainActivity.this,PMessageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.pnotice_rl:
                    intent =new Intent(PMainActivity.this,PNoticeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.pexperience_rl:
                    intent =new Intent(PMainActivity.this,PExperienceActivity.class);
                    startActivity(intent);
                    break;
                case R.id.pquestion_rl:
                    intent =new Intent(PMainActivity.this,PQuestionActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;

            }



        }
    };


    /**
     * 网络获取列表数据
     */
    public void initData() {
        ResponseListener listener = new ResponseListener();
        String url= Constant.URL_BASE+ Constant.COMMENT_UNREAD_NUM_AJAX+"startid=0"
                +"&authorid="+getSharedPreferences(Constant.DB, Activity.MODE_PRIVATE).getString(Constant.ID,"");
        CustomRequest req = new CustomRequest(this,listener,listener,null,url, Request.Method.GET);
        Log.i("sjl","正在查询未读消息数量 url:"+ url);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(this).add(req);
    }


    protected String getRequestTag(){
        return "PMainActivity_request";
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
            t = Toast.makeText(PMainActivity.this, Constant.MESSAGE_REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            Log.i("sjl","PMainActivity res status:"+res.get(Constant.STATUS)+" data: "+res.get(Constant.DATA));
            if(null==res.get(Constant.STATUS)||!Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())){
                t = Toast.makeText(PMainActivity.this, Constant.SERVER_ERROR, Toast.LENGTH_SHORT);
                t.show();
            }else{
                String num = res.get("data").toString();
                if(null!=num&&!"0".equals(num)){
                    messageNumTV.setText(num);
                }

            }

        }



    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
        MobclickAgent.onPageStart("我的");
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的");
        MobclickAgent.onPause(this);
    }

}
