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
 * 问答－新建功能页
 * 1105日  @金凤呈祥
 */
public class CreateExperienceActivity extends BaseCreateActivity {

    TextView content;
    TextView title;
    TextView[] views;
    String [] inputHints=new String[]{"请填写标题","请填写经验内容"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_experience);
        initViews();
    }

    @Override
    public String getPromptForSubmitSuccess(){
        return Constant.Toast_Experience;
    }

    public void initViews(){

        content=(TextView)findViewById(R.id.experience_content_et);
        title=(TextView)findViewById(R.id.experience_title_et);

        views=new TextView[]{title,content};
        setSubmitText("发布");
        submit = (Button)findViewById(R.id.experience_save_btn);

        //发布经验按钮监听事件
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                boolean check=CommonUtil.checkContent(title.getText().toString());
//                check=check || CommonUtil.checkContent(content.getText().toString());
//                if (check){
//                    Toast t = Toast.makeText(CreateExperienceActivity.this, "发布内容涉及敏感词汇，请重新编辑", Toast.LENGTH_SHORT);
//                    t.show();
//                    return;
//                }

                if(check()){
                    //校验通过后拼接请求参数并向服务器发送请求
                    HashMap<String, String> params=new HashMap<String, String>();
                    params.put("title",title.getText().toString());
                    params.put("content",content.getText().toString());
                    params.put("type",Constant.TYPE_EXPERIENCE);
                    params.put("authorid",getSharedPreferences().getString(Constant.ID,Constant.AUTHORID_DEFAULT));
                    publish(params);
                }
            }
        });
    }

    /**
     * 字段非空校验
     * @return
     */
    private boolean check(){
        for(int i=0;i<views.length;i++){
            if(null==views[i].getText()||StringUtils.isEmpty(views[i].getText().toString())){
                Toast t = Toast.makeText(this, inputHints[i], Toast.LENGTH_SHORT);
                t.show();
                return false;
            }
        }
        return true;
    }

    /**
     * 发布经验
     */
    public void publish(HashMap<String, String> params) {
        ResponseListener listener = new ResponseListener();
        String url=Constant.URL_BASE+ Constant.ARTICLE_CREATE_AJAX;
        CustomRequest req = new CustomRequest(this,listener,listener,params,url, Request.Method.POST);
        req.setTag(getRequestTag());
        submit.setEnabled(false);
        submit.setText("正在全力发布");
        submit.setBackgroundColor(Color.GRAY);
        VolleyUtils.getRequestQueue(this).add(req);
    }

    @Override
    protected String getRequestTag(){
        return "CreateExperienceActivity";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("新建经验");
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("新建经验");
        MobclickAgent.onPause(this);
    }


}
