package com.gto.bang.create;

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
 * @六级山东
 * 1105日  新建－吐槽
 */
public class CreateComplaintActivity extends BaseCreateActivity {

    TextView content;
    TextView[] views;
    String [] inputHints=new String[]{"吐槽内容为空"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_complaint);
        initViews();
    }

    public void initViews(){

        content=(TextView)findViewById(R.id.complaint_content);

        views=new TextView[]{content};

        submit = (Button)findViewById(R.id.complaint_save_btn);
        setSubmitText("确认吐槽");

        //发布经验按钮监听事件
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()){
                    //校验通过后拼接请求参数并向服务器发送请求
                    HashMap<String, String> params=new HashMap<String, String>();
                    params.put("content",content.getText().toString());
                    params.put("type",Constant.TYPE_COMPLAINTS);
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
     * 提交吐槽
     */
    public void publish(HashMap<String, String> params) {
        Toast t = Toast.makeText(this, "正在吐槽...", Toast.LENGTH_SHORT);
        t.show();
        ResponseListener listener = new ResponseListener();
        String url=Constant.URL_BASE+ Constant.ARTICLE_CREATE_AJAX;
        CustomRequest req = new CustomRequest(this,listener,listener,params,url, Request.Method.POST);
        req.setTag(getRequestTag());
        submit.setEnabled(false);
        submit.setText("正在提交...");
        VolleyUtils.getRequestQueue(this).add(req);
    }

    @Override
    protected String getRequestTag(){
        return "CreateComplaintActivity";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("新建吐槽");
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("新建吐槽");
        MobclickAgent.onPause(this);
    }


}
