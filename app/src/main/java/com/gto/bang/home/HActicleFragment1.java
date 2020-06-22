package com.gto.bang.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseFragment;
import com.gto.bang.util.Constant;
import com.gto.bang.util.JsonUtil;
import com.gto.bang.util.RequestUtil;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 20191116
 * 推荐好文
 * 暂时使用经验的链接type=5
 */
public class HActicleFragment1 extends BaseFragment {

    ListView listView;
    List<Map<String, Object>> datas;
    SwipeRefreshLayout swipeRefreshLayout;
    View rootView;
    int pageNum = 1;
    boolean resultFlag = true;

    public HActicleFragment1() {
    }

    @Override
    public String getRequestTag() {
        return HActicleFragment1.class.getName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) rootView.findViewById(R.id.msgListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", datas.get(position).get("id").toString());
                bundle.putString("artTitle", datas.get(position).get("title").toString());
                bundle.putString("artType", Constant.TYPE_ARTICLE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        this.rootView = rootView;

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setEnabled(false);
                        initDatas(++pageNum, new ResponseListenerForRefresh());
                    }
                }).start();
            }
        });


        return rootView;
    }

    //handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    swipeRefreshLayout.setRefreshing(false);
                    Toast t;
                    if (resultFlag) {
                        ListView listView = (ListView) rootView.findViewById(R.id.msgListView);
                        for (int i = 0; i < datas.size(); i++) {
                            Map<String, Object> map = datas.get(i);
                            map.put("createTime", null == map.get("createTime") ? null : map.get("createTime").toString().substring(0, 10));
                        }
                        MyAdapter adapter = new MyAdapter(getActivity(), datas);
                        listView.setAdapter(adapter);
                        t = Toast.makeText(getActivity(), Constant.REFRESH_PROMPT + datas.size() + "条", Toast.LENGTH_SHORT);
                        t.show();
                    } else {
                        t = Toast.makeText(getActivity(), Constant.REFRESH_NOTHING, Toast.LENGTH_SHORT);
                        t.show();
                    }
                    swipeRefreshLayout.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initDatas(pageNum, new ResponseListener());
    }

    public void initDatas(int pageNum, ResponseListener responseListener) {
        String url = Constant.URL_BASE + Constant.ARTICLE_LIST_AJAX + "type=6" + "&" +
                Constant.PAGENUM + "=" + pageNum;
        Log.i("sjl", "initDatas url=" + url);
        CustomRequest req = new CustomRequest(getActivity(), responseListener, responseListener, null, url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getActivity()).add(req);
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
                datas = RequestUtil.parseResponseForDatas(res);
                if (CollectionUtils.isNotEmpty(datas)) {
                    LinearLayout tips = (LinearLayout) rootView.findViewById(R.id.comment_tips);
                    tips.setVisibility(View.GONE);
                    ListView listView = (ListView) rootView.findViewById(R.id.msgListView);
                    listView.setVisibility(View.VISIBLE);
                    for (int i = 0; i < datas.size(); i++) {
                        Map<String, Object> map = datas.get(i);
                        map.put("createTime", null == map.get("createTime") ? null : map.get("createTime").toString().substring(0, 10));
                    }
                    MyAdapter adapter = new MyAdapter(getActivity(), datas);
                    listView.setAdapter(adapter);
                }
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
        MobclickAgent.onPageStart("主页－经验"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("主页－经验");
    }


    private class MyAdapter extends BaseAdapter {

        List<Map<String, Object>> datas;

        private LayoutInflater mInflater;

        public MyAdapter(Context context, List<Map<String, Object>> datas) {
            this.datas = datas;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.hacticle_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.viewTimes = (TextView) convertView.findViewById(R.id.viewTimes);
                holder.commentNum = (TextView) convertView.findViewById(R.id.commentNum);
                holder.praise = (TextView) convertView.findViewById(R.id.praise);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.title.setText(datas.get(position).get(Constant.TITLE).toString());
            holder.content.setText(datas.get(position).get(Constant.CONTENT).toString());
            holder.viewTimes.setText(datas.get(position).get(Constant.VIEWTIMES).toString());

            return convertView;
        }

    }

    public final class ViewHolder {
        public TextView title;
        public TextView author;
        public TextView content;
        public TextView headIcon;
        public TextView viewTimes;
        public TextView commentNum;
        public TextView praise;
    }


    public class ResponseListenerForRefresh extends ResponseListener {
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
                List<Map<String, Object>> tem = (List<Map<String, Object>>) res.get("data");
                if (CollectionUtils.isNotEmpty(tem)) {
                    datas = tem;
                } else {
                    resultFlag = false;
                }
                mHandler.sendEmptyMessage(1);
            }


        }
    }


}
