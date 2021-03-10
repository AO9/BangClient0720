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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 20210303
 * 商城
 */
public class MallFragment extends BaseFragment {

    //    private GridView titles;
    private GridView papers;
    private GridView course;
    LinearLayout tips;

    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;

    public MallFragment() {
    }

    @Override
    public String getRequestTag() {
        return MallFragment.class.getName();
    }

    public void initCourse() {
        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.COURSE_URL;
        CustomRequest req = new CustomRequest(getActivity(), listener, listener, null, url, Request.Method.GET);
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }

    public void initPapers() {

        String[] from = {"title", "describe"};
        int[] to = {R.id.title};
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

        String[] title = new String[]{"一带一路对京津冀地区经济协同发展的影响", "婺源县乡村旅游经济发展问题分析", "企业应收账款管理中的问题级对策研究--以联康集团为例", "南锡业股份有限公司成本管理存在问题及对策研究"};
        String[] describe = new String[]{"经济类", "经济类", "会计类", "会计类",};


        for (int i = 0; i < title.length; i++) {
            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("title", title[i]);
            map1.put("describe", describe[i]);
            datas.add(map1);
        }

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), datas, R.layout.paper_item, from, to);
        papers.setAdapter(adapter);
        papers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent;
                Map<String, Object> map = dataList.get(arg2);
                log("原创论文(独家合作老师提供，供范文参考)" + map.get("title"));
                intent = new Intent(getActivity(), PaperDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.mall, container, false);
//        titles = (GridView) rootView.findViewById(R.id.titles);
        papers = (GridView) rootView.findViewById(R.id.papers);
        course = (GridView) rootView.findViewById(R.id.course);
        tips = (LinearLayout) rootView.findViewById(R.id.tips);

        initCourse();
        initPapers();
//        initTitles();

        return rootView;
    }

    private void initClickEvents() {
        String[] from = {"courseTitle", "courseDescribe1", "courseDescribe2"};
        int[] to = {R.id.courseTitle, R.id.courseDescribe1, R.id.courseDescribe2};
        adapter = new SimpleAdapter(getActivity(), dataList, R.layout.course_item, from, to);
        course.setAdapter(adapter);
        course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
//                    tips.setVisibility(View.GONE);
                    initClickEvents();
                }
            }

        }
    }

}
