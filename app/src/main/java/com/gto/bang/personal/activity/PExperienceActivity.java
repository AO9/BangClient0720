package com.gto.bang.personal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
 * @汇融大厦  23点27分 阿贝还没下班！
 */
public class PExperienceActivity extends BaseActivity {

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
        String url= Constant.URL_BASE+ Constant.ARTICLE_MYLIST_AJAX+"startid=0"
                +"&type=1&authorid="+getSharedPreferences(Constant.DB, Activity.MODE_PRIVATE).getString(Constant.ID,"");
        CustomRequest req = new CustomRequest(this,listener,listener,null,url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(this).add(req);
    }


    protected String getRequestTag(){
        return "PExperienceActivity_request";
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
            t = Toast.makeText(PExperienceActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            if(null==res.get(Constant.STATUS)||!Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())){
                String data=(null==res.get(Constant.DATA))?Constant.REQUEST_ERROR:res.get(Constant.DATA).toString();
                t = Toast.makeText(PExperienceActivity.this, data, Toast.LENGTH_SHORT);
                t.show();
            }else{
                final List<Map<String, Object>> datas = (List<Map<String, Object>>)res.get("data");
                if(datas!=null&&datas.size()==0){
                    findViewById(R.id.bang_common_tips).setVisibility(View.VISIBLE);
                }
                ListView listView=(ListView)findViewById(R.id.bang_common_lv);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(PExperienceActivity.this, EMainActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("id",datas.get(position).get("id").toString());
                        bundle.putString("artTitle",datas.get(position).get("title").toString());
                        bundle.putString("artType",Constant.TYPE_EXPERIENCE);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                SimpleAdapter adapter = new SimpleAdapter(PExperienceActivity.this, datas, R.layout.bang_pmyquestion_item, new String[]{
                "title","status"},
                new int[]{R.id.title,R.id.status});
                listView.setAdapter(adapter);
            }

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
        MobclickAgent.onPageStart("我的－经验");
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的－经验");
        MobclickAgent.onPause(this);
    }
}
