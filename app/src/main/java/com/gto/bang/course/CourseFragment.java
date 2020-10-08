package com.gto.bang.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.article.ArticleListActivity1;
import com.gto.bang.article.ArticleListActivity2;
import com.gto.bang.article.ArticleListActivity3;
import com.gto.bang.article.ArticleListActivity4;
import com.gto.bang.base.BaseFragment;
import com.gto.bang.college.NoticeActivity;
import com.gto.bang.create.CreateComplaintActivity;
import com.gto.bang.create.CreateExperienceActivity;
import com.gto.bang.create.CreateQuestionActivity;
import com.gto.bang.create.CreateSupportActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.RequestUtil;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 20200705
 * 课程页
 */
public class CourseFragment extends BaseFragment {

    private GridView offlineCourse;
    private GridView onlineCourse;
    private GridView soleArticle;

    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;

    public CourseFragment() {
    }

    @Override
    public String getRequestTag() {
        return CourseFragment.class.getName();
    }

//    public void initBanner() {
//        ResponseListener listener = new ResponseListener();
//        String url = Constant.URL_BASE + Constant.NOTICE_URL;
//        CustomRequest req = new CustomRequest(getActivity(), listener, listener, null, url, Request.Method.GET);
//        VolleyUtils.getRequestQueue(getActivity()).add(req);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.course, container, false);
        offlineCourse = (GridView) rootView.findViewById(R.id.offlineCourse);
        onlineCourse = (GridView) rootView.findViewById(R.id.onlineCourse);
        soleArticle = (GridView) rootView.findViewById(R.id.soleArticle);

        initClickEvents();
//        initBanner();
        return rootView;
    }

    private void initClickEvents() {

        String courseTitles[] = {"医学专业", "教育专业", "经济管理", "电子商务", "物流改革"};
        String courseDescribe1s[] = {"疫情下的各行各业发展等", "关于少儿教育理论等", "粤港澳大湾区经济规划等", "直播带货新电商形态等", "人工智能下的物流运输等"};
        String courseDescribe2s[] = {"已有3人报名，3/20", "已有6人报名，6/20", "已有9人报名，9/20", "已有14人报名，14/20", "已有18人报名，18/50"};

        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < courseTitles.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("courseTitle", courseTitles[i]);
            map.put("courseDescribe1", courseDescribe1s[i]);
            map.put("courseDescribe2", courseDescribe2s[i]);
            map.put("id", "1");
            dataList.add(map);
        }
        String[] from = {"courseTitle", "courseDescribe1", "courseDescribe2"};
        int[] to = {R.id.courseTitle, R.id.courseDescribe1, R.id.courseDescribe2};
        adapter = new SimpleAdapter(getActivity(), dataList, R.layout.course_item, from, to);
        offlineCourse.setAdapter(adapter);
        offlineCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent;
                Map<String, Object> map = dataList.get(arg2);
                log("立即报名"+arg2);
                intent = new Intent(getActivity(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("课程");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("课程");
    }


//    public class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
//        Toast t;
//
//        @Override
//        public void onErrorResponse(VolleyError arg0) {
//            t = Toast.makeText(getActivity(), Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
//            t.show();
//        }
//
//        @Override
//        public void onResponse(Map<String, Object> res) {
//
//            if (null == res.get("status") || !Constant.RES_SUCCESS.equals(res.get("status").toString())) {
//                String data = (null == res.get("data")) ? "null" : res.get("data").toString();
//                t = Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT);
//                t.show();
//            } else {
//                Map<String, String> noticeInfo = (Map<String, String>) res.get(Constant.DATA);
//                if (null != noticeInfo) {
//                    navigate.setText(noticeInfo.get(Constant.TITLE));
//                }
//            }
//
//        }
//    }

}
