package com.gto.bang.question.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseFragment;
import com.gto.bang.comment.CommentInputActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * 20200619 问答详情页上面部分
 * 20200620 UI调整，实现点赞、关注、回答等操作
 */
public class QuestionDetailFragment extends BaseFragment {

    Map<String, Object> details;

    public QuestionDetailFragment() {
    }

    @Override
    public String getRequestTag() {
        return QuestionDetailFragment.class.getName();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.question_article_detail, container, false);
        LinearLayout answer = (LinearLayout) rootView.findViewById(R.id.anwser);
        LinearLayout praise = (LinearLayout) rootView.findViewById(R.id.praise);
        LinearLayout colletion = (LinearLayout) rootView.findViewById(R.id.collection);

        ImageView answerImage=rootView.findViewById(R.id.answerImage);
        final ImageView collectionImage=rootView.findViewById(R.id.collectionImage);
        final ImageView praiseImage=rootView.findViewById(R.id.praiseImage);

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CommentInputActivity.class);
                intent.putExtras(((QuestionDetailActivity) getActivity()).getBundle());
                getActivity().startActivityForResult(intent, Constant.CALL_COMMENT_ACTIVITY);
            }
        });

        praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> param = new HashMap<String, String>();
                CommonResponseListener responseListener = new CommonResponseListener("赞+1");
                request(Constant.QUESTION_PRAISE_URL, genearteMapParam(), responseListener);
                praiseImage.setBackgroundResource(R.drawable.praiseselected);

            }
        });
        colletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonResponseListener responseListener = new CommonResponseListener("已关注");
                request(Constant.QUESTION_COLLECTION_URL, genearteMapParam(), responseListener);
                collectionImage.setBackgroundResource(R.drawable.collectionselected);

            }
        });

        return rootView;
    }

    public Map<String, String> genearteMapParam() {
        Map<String, String> param = new HashMap<String, String>();
        param.put(Constant.ARTICLEID, ((QuestionDetailActivity) getActivity()).getArticleId());
        param.put(Constant.USERID, getUserId());
        param.put(Constant.USERID, getAndroidId());
        return param;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initDatas();
    }

    public void initDatas() {
        ResponseListener listener = new ResponseListener();
        String id = ((QuestionDetailActivity) getActivity()).getArticleId();
        String url = Constant.URL_BASE + Constant.ARTICLE_DETAIL_AJAX + "id=" + (int) Double.valueOf(id).doubleValue();
        CustomRequest req = new CustomRequest(getActivity(), listener, listener, null, url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }

    public class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t;

        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(getActivity(), "网络请求失败，请重试", Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            if (null == res.get(Constant.STATUS) || !Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())) {
                t = Toast.makeText(getActivity(), Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
                t.show();
            } else {

                LinearLayout tips = (LinearLayout) getView().findViewById(R.id.comment_tips);
                tips.setVisibility(View.GONE);
                LinearLayout ll = (LinearLayout) getView().findViewById(R.id.detail_ll);
                ll.setVisibility(View.VISIBLE);

                details = (Map<String, Object>) res.get(Constant.DATA);

                TextView content = (TextView) getView().findViewById(R.id.content);
                TextView date = (TextView) getView().findViewById(R.id.date);
                TextView userName = (TextView) getView().findViewById(R.id.userName);

                String idStr = details.get("authorId").toString();
                userName.setText(details.get(Constant.USERNAME).toString());
                date.setText(details.get(Constant.CREATETIME).toString());
                content.setText(details.get(Constant.CONTENT).toString());
                CommonUtil.setOnClickListenerForPHomePage(idStr, getActivity(), userName);

            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("问答-2"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("问答-2");
    }

}
