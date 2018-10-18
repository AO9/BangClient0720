package com.gto.bang.experience.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 产品详情
 */
public class PDetailFragment extends Fragment {

    Map<String, Object> details;

    public PDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.provider_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initDatas();
    }

    public void initDatas() {
        ResponseListener listener = new ResponseListener();
        String id =((EMainActivity)getActivity()).getArticleId();

        String url= Constant.URL_BASE+ Constant.PRODUCT_VIEW_URL+"?id="+(int)Double.valueOf(id).doubleValue();
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
                TextView author = (TextView) getView().findViewById(R.id.provider_detail_author);
                TextView date = (TextView) getView().findViewById(R.id.provider_detail_date);
                TextView content = (TextView) getView().findViewById(R.id.provider_detail_content);
                TextView headIcon = (TextView) getView().findViewById(R.id.provider_detail_head);
                TextView price = (TextView) getView().findViewById(R.id.provider_detail_price);
                TextView title = (TextView) getView().findViewById(R.id.provider_detail_title);
                TextView phone = (TextView) getView().findViewById(R.id.provider_detail_phone);
                TextView wechat = (TextView) getView().findViewById(R.id.provider_detail_wechat);
                final Button button = (Button) getView().findViewById(R.id.product_detail_praise_btn);

                title.setText(details.get("title").toString());
                author.setText(details.get("username").toString());
                date.setText(details.get("createTime").toString());
                content.setText(details.get("content").toString());
                price.setText(details.get("price").toString());
                String idStr=details.get("authorId").toString();
                phone.setText(details.get("phone").toString());
                wechat.setText(details.get("wechat").toString());

                Integer authorId=Integer.valueOf(idStr);
                String currentUserId=getSharedPreferences().getString(Constant.ID,"");
                if (StringUtils.isNotBlank(currentUserId)){
                    if(!currentUserId.equals(idStr)){
                        button.setVisibility(View.VISIBLE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                praise();
                                button.setEnabled(false);
                                button.setText("感谢您的好评");
                                button.setBackgroundColor(Color.GRAY);
                            }
                        });
                    }
                }

                String userName=details.get("username").toString();

                CommonUtil.handlerHeadIcon(authorId,headIcon,userName);
                CommonUtil.setOnClickListenerForPHomePage(idStr,getActivity(),headIcon);
                CommonUtil.setOnClickListenerForPHomePage(idStr,getActivity(),author);

            }

        }
    }


    public void praise() {

        ResponseListener1 listener = new ResponseListener1();
        String id =((EMainActivity)getActivity()).getArticleId();

        String url= Constant.URL_BASE+ Constant.PRODUCT_PRAISE_URL+"?id="+Integer.valueOf(id).intValue();
        CustomRequest req = new CustomRequest(getActivity(),listener,listener,null,url, Request.Method.GET);
        req.setTag(TAG);
        VolleyUtils.getRequestQueue(getActivity()).add(req);

    }

    public SharedPreferences getSharedPreferences(){
        SharedPreferences sp=getActivity().getSharedPreferences(Constant.DB, Activity.MODE_MULTI_PROCESS);
        return sp;
    }

    public static final String TAG="PDetailFragment_request_praise";
    protected String getRequestTag(){
        return "PDetailFragment_request";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("详情－产品"); //统计页面
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("详情－产品");
    }

    public  class ResponseListener1 implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
        }

        @Override
        public void onResponse(Map<String, Object> res) {
        }
    }

}
