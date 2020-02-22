package com.gto.bang.user;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseCreateActivity;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Map;

/**
 * 20190309
 * 当日活跃用户列表
 */
public class UserListActivity extends BaseCreateActivity {

    public static final String TAG = "当日活跃用户列表";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_common_listview);
        initData();
    }

    @Override
    protected String getRequestTag() {
        return UserListActivity.class.getName();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    /**
     * 网络获取列表数据
     */
    public void initData() {
        ResponseListener listener = new ResponseListener();
        String url= Constant.URL_BASE+ Constant.USER_LIST_URL;
        CustomRequest req = new CustomRequest(this,listener,listener,null,url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(this).add(req);
    }
    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(UserListActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            if(null==res.get(Constant.STATUS)||!Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())){
                String data=(null==res.get(Constant.DATA))?Constant.REQUEST_ERROR:res.get(Constant.DATA).toString();
                t = Toast.makeText(UserListActivity.this, data, Toast.LENGTH_SHORT);
                t.show();
            }else{
                final List<Map<String, Object>> datas = (List<Map<String, Object>>)res.get("data");
                if(datas!=null&&datas.size()==0){
                    findViewById(R.id.bang_common_tips).setVisibility(View.VISIBLE);
                }
                ListView listView=(ListView)findViewById(R.id.bang_common_lv);
                SimpleAdapter adapter = new SimpleAdapter(UserListActivity.this, datas, R.layout.user_item, new String[]{
                        "username","school","createtime"},
                        new int[]{R.id.userName,R.id.school,R.id.createTime});
                listView.setAdapter(adapter);
            }

        }
    }



}
