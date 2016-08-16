package com.gto.bang.personal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;
import com.gto.bang.experience.EMainActivity;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Map;

/**
 * Created by shenjialong on 16/6/10 19:08.
 * 个人中心——消息列表页面
 * @惠民 16年0731日，尚未调试
 *
 * @六里桥东 16年0810日22:49 增加标记为已读的功能
 */
public class PMessageActivity extends BaseActivity {

    public  static  String MESSAGE="message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_message);

    }

    /**
     * 网络获取列表数据
     */
    public void initData() {
        ResponseListener listener = new ResponseListener();
        String url= Constant.URL_BASE+ Constant.COMMENT_MINE_AJAX+"startid=0"
                +"&authorid="+getSharedPreferences(Constant.DB, Activity.MODE_PRIVATE).getString(Constant.ID,"");
        CustomRequest req = new CustomRequest(this,listener,listener,null,url, Request.Method.GET);
        Log.i("sjl","正在获取我的未读消息列表 url:"+ url);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(this).add(req);
    }


    protected String getRequestTag(){
        return "PMessageActivity_request";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(this).cancelAll(getRequestTag());
        VolleyUtils.getRequestQueue(this).cancelAll("comment_update_status");

    }


    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(PMessageActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            Log.i("sjl","login res status:"+res.get(Constant.STATUS)+" data: "+res.get(Constant.DATA));

            if(null==res.get(Constant.STATUS)||!Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())){
                String data=(null==res.get(Constant.DATA))?Constant.REQUEST_ERROR:res.get(Constant.DATA).toString();
                t = Toast.makeText(PMessageActivity.this, data, Toast.LENGTH_SHORT);
                t.show();
            }else{
                final List<Map<String, Object>> datas = (List<Map<String, Object>>)res.get("data");

                for(int i=0;i<datas.size();i++){
                    if(datas.get(i).get("type").toString().equals(Constant.ARTICLE_TYPE_EXPERIENCE)){
                        datas.get(i).put("tag","经验");
                    }else if(datas.get(i).get("type").toString().equals(Constant.ARTICLE_TYPE_QUESTION)) {
                        datas.get(i).put("tag","问答");
                    }
                }

                ListView listView=(ListView)findViewById(R.id.bang_message_lv);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(PMessageActivity.this, EMainActivity.class);
                        Bundle bundle=new Bundle();
                        //actId文章的ID
                        bundle.putString("id",datas.get(position).get("actId").toString());
                        bundle.putString("artTitle",datas.get(position).get("artTitle").toString());
                        bundle.putString("artType",datas.get(position).get("type").toString());
                        bundle.putString("from",MESSAGE);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                SimpleAdapter adapter = new SimpleAdapter(PMessageActivity.this, datas, R.layout.bang_pmymessage_item, new String[]{
                        "artTitle","tag"},
                        new int[]{R.id.bang_mtitle_tv,R.id.bang_mtag_tv});
                listView.setAdapter(adapter);

                updateStatus(datas);
            }

        }
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
        String url= Constant.URL_BASE+ Constant.COMMENT_UPDATE_STATUS_URL+"commentIds="+ids.toString();
        CustomRequest req = new CustomRequest(this,listener,listener,null,url, Request.Method.GET);
        Log.i("sjl","更新评论状态 url:"+ url+" commentIds:"+ids.toString());
        req.setTag("comment_update_status");
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

    @Override
    public void onResume() {
        super.onResume();
        initData();
        MobclickAgent.onPageStart("我的－消息");
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的－消息");
        MobclickAgent.onPause(this);
    }

}
