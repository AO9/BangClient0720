package com.gto.bang.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseFragment;
import com.gto.bang.util.Constant;
import com.gto.bang.util.RequestUtil;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.collections4.CollectionUtils;

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
    LinearLayout tips;

    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;

    public CourseFragment() {
    }

    @Override
    public String getRequestTag() {
        return CourseFragment.class.getName();
    }

    public void initBanner() {
        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.COURSE_URL;
        CustomRequest req = new CustomRequest(getActivity(), listener, listener, null, url, Request.Method.GET);
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.course, container, false);
        offlineCourse = (GridView) rootView.findViewById(R.id.offlineCourse);
        onlineCourse = (GridView) rootView.findViewById(R.id.onlineCourse);
        soleArticle = (GridView) rootView.findViewById(R.id.soleArticle);
        tips = (LinearLayout) rootView.findViewById(R.id.tips);

        initBanner();

        return rootView;
    }

    private void initClickEvents() {
        String[] from = {"courseTitle", "courseDescribe1", "courseDescribe2"};
        int[] to = {R.id.courseTitle, R.id.courseDescribe1, R.id.courseDescribe2};
        adapter = new SimpleAdapter(getActivity(), dataList, R.layout.course_item2, from, to);
        offlineCourse.setAdapter(adapter);
        offlineCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent;
                Map<String, Object> map = dataList.get(arg2);
                log("立即报名" + arg2);
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


    public class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t;

        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(getActivity(), Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            if (null == res.get("status") || !Constant.RES_SUCCESS.equals(res.get("status").toString())) {
                String data = (null == res.get("data")) ? "null" : res.get("data").toString();
                t = Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT);
                t.show();
            } else {
                Map<String, String> noticeInfo = (Map<String, String>) res.get(Constant.DATA);
                dataList = RequestUtil.parseResponseForDatas(res);
                if (CollectionUtils.isNotEmpty(dataList)) {
                    tips.setVisibility(View.GONE);
                    initClickEvents();
                }
            }

        }
    }

}
