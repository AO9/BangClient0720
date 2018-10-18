package com.gto.bang.personal.activity;

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
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人信息修改页面
 * 0916
 */
public class PInputActivity extends BaseActivity{

    public static int SUCCESS=1;
    public static int FAILE=0;
    int updateType;
    EditText content;

    public static String REQUEST_TAG="PInputActivity_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_input);
        initViews();
    }

    private void initViews(){
        this.updateType=getIntent().getExtras().getInt("udpateType");
        content=(EditText)findViewById(R.id.info_input_et);
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
                submit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 提交
     * @return
     */
    private boolean submit(){
        if(check()){
            publish();
            return true;
        }else{
            Toast t = Toast.makeText(PInputActivity.this, "请填写修改内容", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

    }

    public boolean check(){
        return !(null == content || content.getText() == null || StringUtils.isEmpty(content.getText().toString()));
    }

    /**
     *
     */
    public void publish() {
        //校验通过后拼接请求参数并向服务器发送请求
        ResponseListener listener = new ResponseListener();
        HashMap<String, String> params=new HashMap<String, String>();
        String value=content.getText().toString();
        boolean check = CommonUtil.checkUserName(value);
        if (check) {
            Toast t1 = Toast.makeText(PInputActivity.this, "昵称涉及敏感词汇，请重新填写!", Toast.LENGTH_SHORT);
            t1.show();
            return;
        }
        params.put("userId",getSharedPreferences().getString(Constant.ID,Constant.AUTHORID_DEFAULT));
        params.put("updateType",String.valueOf(updateType));
        params.put("content",content.getText().toString());

        String url=Constant.URL_BASE+ Constant.UPDATE_PERSION_INFO_AJAX;
        CustomRequest req = new CustomRequest(PInputActivity.this,listener,listener,params,url, Request.Method.POST);
        req.setTag(REQUEST_TAG);
        VolleyUtils.getRequestQueue(PInputActivity.this).add(req);
    }

    /**
     * 请求响应
     */
    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {

        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(PInputActivity.this, "修改失败，请重试", Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            Object status=res.get("status");
            if(null==status||!Constant.RES_SUCCESS.equals(status.toString())){
                t = Toast.makeText(PInputActivity.this, res.get("data").toString(), Toast.LENGTH_SHORT);
                t.show();
            }else{
                t = Toast.makeText(PInputActivity.this, "修改成功", Toast.LENGTH_SHORT);
                t.show();
                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("content",content.getText().toString());
                intent.putExtras(bundle);
                //代替成功请求的代码
                setResult(SUCCESS,intent);
                finish();
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(PInputActivity.this).cancelAll(REQUEST_TAG);
    }

    public void onResume() {
        super.onResume();

        switch(updateType){
            case Constant.PINFO_UPDATE_NAME:
                MobclickAgent.onPageStart("个人信息－昵称修改");
                break;
            case Constant.PINFO_UPDATE_REGINON:
                MobclickAgent.onPageStart("个人信息－城市修改");
                break;
            case Constant.PINFO_UPDATE_GENDER:
                MobclickAgent.onPageStart("个人信息－性别修改");
                break;
            case Constant.PINFO_UPDATE_SIGNATURE:
                MobclickAgent.onPageStart("个人信息－个性签名修改");
                break;
            case Constant.PINFO_UPDATE_ACADAMY:
                MobclickAgent.onPageStart("个人信息－学院修改");
                break;
            default:
                break;
        }

        MobclickAgent.onResume(this);
    }


    public void onPause() {
        super.onPause();
        switch(updateType){
            case Constant.PINFO_UPDATE_NAME:
                MobclickAgent.onPageEnd("个人信息－昵称修改");
                break;
            case Constant.PINFO_UPDATE_REGINON:
                MobclickAgent.onPageEnd("个人信息－城市修改");
                break;
            case Constant.PINFO_UPDATE_GENDER:
                MobclickAgent.onPageEnd("个人信息－性别修改");
                break;
            case Constant.PINFO_UPDATE_SIGNATURE:
                MobclickAgent.onPageEnd("个人信息－个性签名修改");
                break;
            case Constant.PINFO_UPDATE_ACADAMY:
                MobclickAgent.onPageEnd("个人信息－学院修改");
                break;
            default:
                break;
        }
        MobclickAgent.onPause(this);
    }
}
