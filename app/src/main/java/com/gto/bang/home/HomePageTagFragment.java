package com.gto.bang.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.gto.bang.R;
import com.gto.bang.article.ArticleListActivity;
import com.gto.bang.college.NoticeActivity;
import com.gto.bang.create.CreateComplaintActivity;
import com.gto.bang.create.CreateExperienceActivity;
import com.gto.bang.create.CreateQuestionActivity;
import com.gto.bang.create.CreateSupportActivity;
import com.gto.bang.navigation.FeedbackActivity;
import com.gto.bang.question.fragment.QuestionListActivity;
import com.gto.bang.user.UserListActivity;
import com.gto.bang.util.VolleyUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页TAB
 */
public class HomePageTagFragment extends Fragment {

    TextView content;
    TextView navigate;
    View rootView;
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;

    public HomePageTagFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.homepage, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        navigate = (TextView) rootView.findViewById(R.id.navigate);
        initClickEvents();

        return rootView;
    }

    private void initClickEvents() {
        //活动报名
        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intent);
            }
        });

        String name[] = {"红包求助","提问", "帮TA", "分享", "吐槽一下", "论文资讯", "反馈", "活跃榜"};
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < name.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", name[i]);
            dataList.add(map);
        }
        String[] from = {"text"};
        int[] to = {R.id.text};
        adapter = new SimpleAdapter(getActivity(), dataList, R.layout.gridview_item, from, to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent;
                switch (arg2) {
                    case 0:
                        intent = new Intent(getActivity(), CreateSupportActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getActivity(), CreateQuestionActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), QuestionListActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), CreateExperienceActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), CreateComplaintActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(getActivity(), ArticleListActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(getActivity(), FeedbackActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(getActivity(), UserListActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }

    protected String getRequestTag() {
        return "HomePageTagFragment";
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
