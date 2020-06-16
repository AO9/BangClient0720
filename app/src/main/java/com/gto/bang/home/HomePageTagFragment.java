package com.gto.bang.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.gto.bang.college.NoticeActivity;
import com.gto.bang.create.CreateComplaintActivity;
import com.gto.bang.create.CreateQuestionActivity;
import com.gto.bang.create.CreateSupportActivity;
import com.gto.bang.navigation.FeedbackActivity;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
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
                if (null != noticeInfo) {
                    navigate.setText(noticeInfo.get(Constant.TITLE));
                }
            }

        }
    }


    public void initBanner() {
        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.NOTICE_URL;
        CustomRequest req = new CustomRequest(getActivity(), listener, listener, null, url, Request.Method.GET);
        req.setTag("首页Banner");
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.homepage, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        navigate = (TextView) rootView.findViewById(R.id.navigate);
        initClickEvents();
        initBanner();
//        initChildFragment();
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

        String name[] = {"求助","提问", "动态", "反馈", "优质文章","查重咨询","选题专区","硕士论文"};
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
//                    case 2:
//                        intent = new Intent(getActivity(), QuestionListActivity.class);
//                        startActivity(intent);
//                        break;
//                    case 3:
//                        intent = new Intent(getActivity(), CreateExperienceActivity.class);
//                        startActivity(intent);
//                        break;
                    case 2:
                        intent = new Intent(getActivity(), CreateComplaintActivity.class);
                        startActivity(intent);
                        break;
//                    case 5:
//                        intent = new Intent(getActivity(), ArticleListActivity.class);
//                        startActivity(intent);
//                        break;
                    case 3:
                        intent = new Intent(getActivity(), FeedbackActivity.class);
                        startActivity(intent);
                        break;
//                    case 7:
//                        intent = new Intent(getActivity(), UserListActivity.class);
//                        startActivity(intent);
//                        break;
                    case 4:
                        intent = new Intent(getActivity(), ArticleListActivity1.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(getActivity(), ArticleListActivity2.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(getActivity(), ArticleListActivity3.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(getActivity(), ArticleListActivity4.class);
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
        Log.d("--------onDestroy-","space destroy");
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
//        destroyFragment();
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


//    HActicleFragment articlesFragment ;
//    private void destroyFragment(){
//        if(articlesFragment != null){
//            Log.d("-------------------", "space no null");
//            FragmentManager fragmentManager = getFragmentManager();
//            if(fragmentManager != null && !fragmentManager.isDestroyed()){
//                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction ();
//                if(fragmentTransaction != null){
//                    fragmentTransaction.remove(articlesFragment).commit();
//                    fragmentTransaction.commitAllowingStateLoss();
//                    fragmentManager.executePendingTransactions();
//                    Log.d("------------------","  space destroy");
//                }
//            }
//        }
//    }


//    private void initChildFragment(){
//        Log.d("-------------------","init space ");
//        articlesFragment = (HActicleFragment)getFragmentManager().findFragmentById(R.id.articlesFragment);
//        if(articlesFragment != null){
//            Log.d("----------------","init space success and no null");
//        }
//    }

}
