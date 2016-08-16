package com.gto.bang.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.experience.EMainActivity;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Map;

/**
 * Created by shenjialong on 16/6/9 19:53.
 * 首页－问答tab
 * 问答详情页和经验详情页统一用EMainActivity了
 */
public class HQuestionFragment extends Fragment {

    ListView listView;
    List<Map<String, Object>> datas;

    public HQuestionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView=(ListView) rootView.findViewById(R.id.msgListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EMainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",datas.get(position).get("id").toString());
                bundle.putString("artTitle",datas.get(position).get("title").toString());
                bundle.putString("artType",Constant.TYPE_QUESTION);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return rootView;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initDatas();
    }

    public void initDatas() {
        ResponseListener listener = new ResponseListener();
        String url= Constant.URL_BASE+ Constant.ARTICLE_LIST_AJAX+"type=2&startid=1";
        CustomRequest req = new CustomRequest(getActivity(),listener,listener,null,url, Request.Method.GET);
        Log.i("sjl","请求问题列表 url:"+ url);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }

    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(getActivity(), "网络请求失败，请重试", Toast.LENGTH_SHORT);
            t.show();
            Log.i("sjl",getRequestTag()+"response Error");
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            Log.i("sjl","HQuestionFragment status:"+res.get("status")+" data: "+res.get("data"));

            if(null==res.get("status")||!Constant.RES_SUCCESS.equals(res.get("status").toString())){
                String data=(null==res.get("data"))?"null":res.get("data").toString();
                t = Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT);
                t.show();
            }else{
                datas = (List<Map<String, Object>> )res.get("data");
                for(int i=0;i<datas.size();i++){
                    Map<String,Object> map=datas.get(i);
                    map.put("createTime",null==map.get("createTime")?null:map.get("createTime").toString().substring(0,19));
                }
                SimpleAdapter adapter = new SimpleAdapter(getActivity(), datas, R.layout.hquestion_item, new String[]{
                        "title", "username","createTime"},
                        new int[]{R.id.title_e_tv, R.id.author_e_tv,R.id.date_e_tv});
                listView.setAdapter(adapter);
            }

        }
    }
    protected String getRequestTag(){
        return "ARTICLE_QUESTION_LIST_REQUEST";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("主页－问答"); //统计页面
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("主页－问答");
    }
}