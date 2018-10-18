package com.gto.bang.experience.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.experience.EMainActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import java.util.Map;

/**
 * Created by shenjialong on 16/6/9 15:08.
 * 经验详情页
 */
public class EDetailFragment extends Fragment {

    Map<String, Object> details;

    public EDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bang_experience_detail, container, false);
        return rootView;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initDatas();
    }

    public void initDatas() {
        ResponseListener listener = new ResponseListener();
        String id =((EMainActivity)getActivity()).getArticleId();
        String url= Constant.URL_BASE+ Constant.ARTICLE_DETAIL_AJAX+"id="+(int)Double.valueOf(id).doubleValue();
        CustomRequest req = new CustomRequest(getActivity(),listener,listener,null,url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }

    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(getActivity(), "网络请求失败，请重试", Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            if(null==res.get("status")||!Constant.RES_SUCCESS.equals(res.get("status").toString())){
                t = Toast.makeText(getActivity(), Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
                t.show();
            }else{

                LinearLayout tips = (LinearLayout) getView().findViewById(R.id.comment_tips);
                tips.setVisibility(View.GONE);
                LinearLayout ll = (LinearLayout) getView().findViewById(R.id.detail_ll);
                ll.setVisibility(View.VISIBLE);

                details = (Map<String, Object>)res.get("data");

                TextView content=(TextView)getView().findViewById(R.id.bang_econtent_tv);
                TextView author=(TextView)getView().findViewById(R.id.bang_eauthor_tv);
                TextView title=(TextView)getView().findViewById(R.id.bang_etitle_tv);
                TextView date=(TextView)getView().findViewById(R.id.bang_edate_tv);
                TextView headIcon=(TextView)getView().findViewById(R.id.experience_head_tv);

                String idStr=details.get("authorId").toString();
                Integer authorId=Integer.valueOf(idStr);
                author.setText(details.get("username").toString());
                title.setText(details.get("title").toString());
                date.setText(details.get("createTime").toString());
                content.setText(details.get("content").toString());

                CommonUtil.handlerHeadIcon(authorId,headIcon,details.get("username").toString());
                CommonUtil.setOnClickListenerForPHomePage(idStr,getActivity(),headIcon);
                CommonUtil.setOnClickListenerForPHomePage(idStr,getActivity(),author);

            }

        }
    }
    protected String getRequestTag(){
        return "ARTICLE_EDETAILS_REQUEST";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("详情－经验"); //统计页面
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("详情－经验");
    }

}
