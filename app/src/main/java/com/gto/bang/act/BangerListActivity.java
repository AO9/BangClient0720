package com.gto.bang.act;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseCreateActivity;
import com.gto.bang.util.Constant;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帮手榜
 * 20190609 占位功能
 */
public class BangerListActivity extends BaseCreateActivity {

    public static final String TAG = "帮手排行";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bang_common_listview);
        initData();
    }

    @Override
    protected String getRequestTag() {
        return BangerListActivity.class.getName();
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

        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

        String[] bangerNames = new String[]{"迎着夕阳回家", "乐队的夏天", "我爱帮忙", "傲九在这里", "好帮手12"};
        String[] aNums = new String[]{"101", "78", "65", "45", "19"};
        String[] praiseNums = new String[]{"80", "60", "56", "34", "9"};
        for (int i = 0; i < bangerNames.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("bangerName", bangerNames[i]);
            map.put("aNum", aNums[i]);
            map.put("praiseNum", praiseNums[i]);
            datas.add(map);
        }

        findViewById(R.id.bang_common_tips).setVisibility(View.GONE);
        ListView listView = (ListView) findViewById(R.id.bang_common_lv);
        SimpleAdapter adapter = new SimpleAdapter(BangerListActivity.this, datas, R.layout.banger_item, new String[]{
                "bangerName", "aNum", "praiseNum"},
                new int[]{R.id.bangerName, R.id.aNum, R.id.praiseNum});
        listView.setAdapter(adapter);


//        ResponseListener listener = new ResponseListener();
//        String url= Constant.URL_BASE+ Constant.USER_LIST_URL;
//        CustomRequest req = new CustomRequest(this,listener,listener,null,url, Request.Method.GET);
//        req.setTag(getRequestTag());
//        VolleyUtils.getRequestQueue(this).add(req);
    }

    public class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t;

        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(BangerListActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            if (null == res.get(Constant.STATUS) || !Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())) {
                String data = (null == res.get(Constant.DATA)) ? Constant.REQUEST_ERROR : res.get(Constant.DATA).toString();
                t = Toast.makeText(BangerListActivity.this, data, Toast.LENGTH_SHORT);
                t.show();
            } else {
                final List<Map<String, Object>> datas = (List<Map<String, Object>>) res.get("data");
                if (datas != null && datas.size() == 0) {
                    findViewById(R.id.bang_common_tips).setVisibility(View.VISIBLE);
                }
                ListView listView = (ListView) findViewById(R.id.bang_common_lv);
                SimpleAdapter adapter = new SimpleAdapter(BangerListActivity.this, datas, R.layout.banger_item, new String[]{
                        "bangerName", "aNum", "praiseNum"},
                        new int[]{R.id.bangerName, R.id.aNum, R.id.praiseNum});
                listView.setAdapter(adapter);
            }

        }
    }


}
