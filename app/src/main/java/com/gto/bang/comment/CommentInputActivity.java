package com.gto.bang.comment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
 * Created by shenjialong on 16/6/10 12:54.
 * type 字段暂时写死为1 该字段暂时不用了，因为经验和问答已经放在一个数据库表里，所以key为id一个字段就够
 * type=3 吐槽时，里面无title字段  20161223
 */
public class CommentInputActivity extends BaseActivity{

    public static int SUCCESS=1;
    public static int FAILE=0;
    String artType=null;


    //按钮标志
    public boolean flag=false;

    public static String REQUEST_TAG="comment_tag";

    EditText comment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_input);
        initViews();
    }

    private void initViews(){
        this.artType=getIntent().getExtras().getString("artType");
        comment=(EditText)findViewById(R.id.info_input_et);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_ok:

                if(!flag){
                    flag=true;
                    submit();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 发表评论
     * @return
     */
    private boolean submit(){
        if(check()){
//            boolean check= CommonUtil.checkContent(comment.getText().toString());
//            if (check){
//                Toast t = Toast.makeText(CommentInputActivity.this, "发布内容涉及敏感词汇，请重新编辑", Toast.LENGTH_SHORT);
//                t.show();
//                return false;
//            }
            publish();
            return true;
        }else{
            Toast t = Toast.makeText(CommentInputActivity.this, "请填写评论内容", Toast.LENGTH_SHORT);
            t.show();
            flag=false;
            return false;
        }

    }

    public boolean check(){
        return !(null == comment || comment.getText() == null || StringUtils.isEmpty(comment.getText().toString()));
    }

    /**
     *
     */
    public void publish() {
        Toast t = Toast.makeText(CommentInputActivity.this, "已发送", Toast.LENGTH_SHORT);
        t.show();

        Object artId=getIntent().getExtras().getString(Constant.ID);
        Object artTitle=getIntent().getExtras().getString("artTitle");


        //校验通过后拼接请求参数并向服务器发送请求
        ResponseListener listener = new ResponseListener();
        HashMap<String, String> params=new HashMap<String, String>();
        params.put("content",comment.getText().toString());
        params.put("type",artType.toString());
        params.put("userId",getSharedPreferences().getString(Constant.ID,Constant.AUTHORID_DEFAULT));
        params.put("artId",artId.toString());
        params.put("username",getSharedPreferences().getString(Constant.USERNAME,Constant.EMPTY));
        params.put("artTitle",artTitle.toString());
        params.put("artType",artType.toString());

        String url=Constant.URL_BASE+ Constant.COMMENT_CREATE_AJAX;
        CustomRequest req = new CustomRequest(CommentInputActivity.this,listener,listener,params,url, Request.Method.POST);
        req.setTag(REQUEST_TAG);
        VolleyUtils.getRequestQueue(CommentInputActivity.this).add(req);
    }

    /**
     * 请求响应
     */
    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {

        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            flag=false;
            t = Toast.makeText(CommentInputActivity.this, "评论失败，请稍后重试", Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            Object status=res.get("status");
            if(null==status||!Constant.RES_SUCCESS.equals(status.toString())){
                t = Toast.makeText(CommentInputActivity.this, "评论失败，请稍后重试", Toast.LENGTH_SHORT);
                flag=false;
                t.show();
            }else{
                setResult(SUCCESS);
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(CommentInputActivity.this).cancelAll(REQUEST_TAG);
    }

    public void onResume() {
        super.onResume();
        if(Constant.TYPE_EXPERIENCE.equals(artType.toString())){
            MobclickAgent.onPageStart("经验－写评论页");
        }else{
            MobclickAgent.onPageStart("问答－写评论页");
        }
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        if(Constant.TYPE_EXPERIENCE.equals(artType.toString())){
            MobclickAgent.onPageEnd("经验－写评论页");
        }else{
            MobclickAgent.onPageEnd("问答－写评论页");
        }
        MobclickAgent.onPause(this);
    }
}
