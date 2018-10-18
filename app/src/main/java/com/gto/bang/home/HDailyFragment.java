package com.gto.bang.home;

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
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 每日一文
 * 20171013
 */
public class HDailyFragment extends Fragment {

    TextView content;
    String articleContent;
    View rootView;

    public HDailyFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.daily, container, false);
        content=(TextView) rootView.findViewById(R.id.content);
        this.rootView=rootView;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        initDatas();
    }

    public void initDatas() {
        ResponseListener listener = new ResponseListener();
        String url= Constant.URL_BASE+ Constant.ACTICLE_VIEW_URL;
        CustomRequest req = new CustomRequest(getActivity(),listener,listener,null,url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }

    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(getActivity(), Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            if(null==res.get("status")||!Constant.RES_SUCCESS.equals(res.get("status").toString())){
                String data=(null==res.get("data"))?"null":res.get("data").toString();
                t = Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT);
                t.show();
            }else{
                articleContent = (String)res.get("data");
                if(StringUtils.isNotBlank(articleContent)){
                    LinearLayout tips = (LinearLayout) rootView.findViewById(R.id.tips);
                    tips.setVisibility(View.GONE);
                    LinearLayout body = (LinearLayout) rootView.findViewById(R.id.body);
                    body.setVisibility(View.VISIBLE);
                    content.setText(articleContent);
                }
            }

        }
    }
    protected String getRequestTag(){
        return "HDailyFragment";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("每日一文");
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("每日一文");
    }

}
