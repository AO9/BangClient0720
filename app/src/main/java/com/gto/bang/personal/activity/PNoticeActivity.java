package com.gto.bang.personal.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.util.List;
import java.util.Map;

/**
 * Created by shenjialong on 16/6/11 22:43.
 * 个人中心——通知页
 *
 * @0810 08点30 电子大厦
 * 增加消息自动更新为已读功能
 * TODO:
 * 1.我的消息功能增加同样的处理逻辑
 * 2.通知＋我的消息升级逻辑，只获取status=0未读的即可
 *
 */
public class PNoticeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_common_listview);
    }

    /**
     * 网络获取列表数据
     */
    public void initData() {
        ResponseListener listener = new ResponseListener();
        String url= Constant.URL_BASE+ Constant.SYSTEM_MESSAGE_AJAX+"startid=0"
                +"&userId="+getSharedPreferences(Constant.DB, Activity.MODE_MULTI_PROCESS).getString(Constant.ID,"");
        CustomRequest req = new CustomRequest(this,listener,listener,null,url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(this).add(req);
    }


    protected String getRequestTag(){
        return "PNoticeActivity_Request";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(this).cancelAll(getRequestTag());
        VolleyUtils.getRequestQueue(this).cancelAll("message_update_status");
    }

    /**
     * 针对已展示的消息回写成已读状态
     */
    public void updateStatus(List<Map<String, Object>> datas){
        EmptyListener listener = new EmptyListener();
        StringBuilder ids=new StringBuilder();
        for(int i=0;i<datas.size();i++){
            if(i==0){
                ids.append(datas.get(i).get(Constant.ID));
            }else{
                ids.append(Constant.COMMA).append(datas.get(i).get(Constant.ID));
            }
        }
        String url= Constant.URL_BASE+ Constant.MESSAGE_UPDATE_STATUS_URL+"messageIds="+ids.toString();
        CustomRequest req = new CustomRequest(this,listener,listener,null,url, Request.Method.GET);
        req.setTag("message_update_status");
        VolleyUtils.getRequestQueue(this).add(req);
    }


    public  class EmptyListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
        }
        @Override
        public void onResponse(Map<String, Object> res) {
        }
    }





    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(PNoticeActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            if(null==res.get(Constant.STATUS)||!Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())){
                String data=(null==res.get(Constant.DATA))?Constant.REQUEST_ERROR:res.get(Constant.DATA).toString();
                t = Toast.makeText(PNoticeActivity.this, data, Toast.LENGTH_SHORT);
                t.show();
            }else{
                final List<Map<String, Object>> datas = (List<Map<String, Object>>)res.get("data");
                ListView listView=(ListView)findViewById(R.id.bang_common_lv);
                SimpleAdapter adapter = new SimpleAdapter(PNoticeActivity.this, datas, R.layout.bang_pmynotice_item, new String[]{
                        "msgInfo"},
                        new int[]{R.id.bang_ncontent_tv});
                listView.setAdapter(adapter);
                updateStatus(datas);
            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
        MobclickAgent.onPageStart("通知");
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("通知");
        MobclickAgent.onPause(this);
    }

}
