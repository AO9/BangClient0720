package com.gto.bang.experience.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * 评论列表页：经验和问答暂时共同用这个，逻辑完全一致
 * Created by shenjialong on 16/6/9 15:07.
 * udpate by sjl 16/7/21 22:00
 */
public class ECommentFragment extends Fragment {

    ListView listview;
    List<Map<String, Object>> datas;

    String artType;

    public ECommentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.bang_experience_comment, container, false);
        listview=(ListView) rootView.findViewById(R.id.bang_c_listview);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initDatas();
    }

    public void initDatas() {

        String articleId=null;
        if(getActivity() instanceof EMainActivity){
            articleId=((EMainActivity)getActivity()).getArticleId();
        }
        Log.i("sjl","评论列表 articleId:"+ articleId);
        ResponseListener listener = new ResponseListener();

        this.artType=((EMainActivity)getActivity()).getArtType();
        String type="";
        if(Constant.TYPE_EXPERIENCE.equals(artType)){
            type="1";
        }else if(Constant.TYPE_QUESTION.equals(artType)){
            type="2";
        }

        String url= Constant.URL_BASE+ Constant.COMMENT_LIST_AJAX+"type="+type+"&startId=1&artId="+articleId;
        CustomRequest req = new CustomRequest(getActivity(),listener,listener,null,url, Request.Method.GET);
        Log.i("sjl","评论列表请求 url:"+ url);
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

            if(null==res.get("status")||!Constant.RES_SUCCESS.equals(res.get("status").toString())){
                String data=(null==res.get("data"))?"null":res.get("data").toString();
                t = Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT);
                t.show();
            }else{
                datas = (List<Map<String, Object>> )res.get("data");
                SimpleAdapter adapter = new SimpleAdapter(getActivity(), datas, R.layout.bang_ecomment_item, new String[]{
                        "username", "content", "createtime"},
                        new int[]{R.id.bang_ecomment_item_author_tv, R.id.bang_ecomment_item_content_tv, R.id.bang_ecomment_item_date_tv});
                listview.setAdapter(adapter);


            }

        }
    }
    protected String getRequestTag(){
        return "COMMENT_LIST_REQUEST";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
//        VolleyUtils.getRequestQueue(getActivity()).cancelAll("comment_update_status");
    }
    @Override
    public void onResume() {
        super.onResume();

        if(Constant.TYPE_EXPERIENCE.equals(artType)){
            MobclickAgent.onPageStart("评论－经验"); //统计页面
        }else if(Constant.TYPE_QUESTION.equals(artType)){
            MobclickAgent.onPageStart("评论－问答");
        }

    }
    @Override
    public void onPause() {
        super.onPause();

        if(Constant.TYPE_EXPERIENCE.equals(artType)){
            MobclickAgent.onPageEnd("评论－经验"); //统计页面
        }else if(Constant.TYPE_QUESTION.equals(artType)){
            MobclickAgent.onPageEnd("评论－问答");
        }
    }




}
