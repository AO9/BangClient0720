package com.gto.bang.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import com.gto.bang.question.fragment.SupportDetailActivity;
import com.gto.bang.util.Constant;
import com.gto.bang.util.JsonUtil;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 2019年06月02日
 * 增加论文帮页面-红包问答
 */
public class HSupportFragment extends Fragment {

    ListView listView;
    List<Map<String, Object>> datas;
    View rootView;
    SwipeRefreshLayout swipeRefreshLayout;
    int pageNum = 1;

    public static final String TAG = "HSupportFragment";

    public HSupportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) rootView.findViewById(R.id.msgListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SupportDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", datas.get(position).get("id").toString());
                bundle.putString("artTitle", datas.get(position).get("title").toString());
                bundle.putString("artType", Constant.TYPE_QUESTION);
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
                        Log.i("sjl", "run before ");
                        initDatas(++pageNum,new ResponseListenerForRefresh());

                        Log.i("sjl", "sendEmptyMessage after ");
                    }
                }).start();
            }
        });

        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initDatas(pageNum,new ResponseListener());
    }

    public void initDatas(int pageNum, ResponseListener responseListener) {
        String url = Constant.URL_BASE + Constant.ARTICLE_LIST_AJAX + "pageNum="+pageNum+"&type=" +Constant.TYPE_SUPPORT;
        CustomRequest req = new CustomRequest(getActivity(), responseListener, responseListener, null, url, Request.Method.GET);
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
            if (null == res.get("status") || !Constant.RES_SUCCESS.equals(res.get("status").toString())) {
                String data = (null == res.get("data")) ? "null" : res.get("data").toString();
                t = Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT);
                t.show();
            } else {
                datas = (List<Map<String, Object>>) res.get("data");
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

    protected String getRequestTag() {
        return TAG;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("主页－问答"); //统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("主页－问答");
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
                if (position % 2 == 0) {
                    convertView = mInflater.inflate(R.layout.hsupport_item, null);
                } else {
                    convertView = mInflater.inflate(R.layout.hsupport_item_v2, null);
                }

                holder = new ViewHolder();
                holder.price = (TextView) convertView.findViewById(R.id.price_tv);
                holder.author = (TextView) convertView.findViewById(R.id.author_e_tv);
                holder.date = (TextView) convertView.findViewById(R.id.date_e_tv);
                holder.content = (TextView) convertView.findViewById(R.id.content_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.price.setText(datas.get(position).get(Constant.PRICE).toString());
            holder.author.setText(datas.get(position).get(Constant.USERNAME).toString());
            holder.date.setText(datas.get(position).get(Constant.CREATETIME).toString());
            holder.content.setText(datas.get(position).get(Constant.CONTENT).toString());

            return convertView;
        }

    }

    public final class ViewHolder {
        public TextView price;
        public TextView content;
        public TextView author;
        public TextView date;

    }


    public class ResponseListenerForRefresh extends  ResponseListener{
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
                datas = (List<Map<String, Object>>) res.get("data");
                try {
                    Log.i("sjl", "onResponse datas={}" + JsonUtil.obj2Str(datas));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mHandler.sendEmptyMessage(1);

        }
    }

    //handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.i("sjl", "what=1");
                    swipeRefreshLayout.setRefreshing(false);
                    if (CollectionUtils.isNotEmpty(datas)) {
                        ListView listView = (ListView) rootView.findViewById(R.id.msgListView);
                        for (int i = 0; i < datas.size(); i++) {
                            Map<String, Object> map = datas.get(i);
                            map.put("createTime", null == map.get("createTime") ? null : map.get("createTime").toString().substring(0, 10));
                        }
                        Log.i("sjl", "what=12");
                        MyAdapter adapter = new MyAdapter(getActivity(), datas);
                        listView.setAdapter(adapter);
                        Log.i("sjl", "what=123");
                    }
                    swipeRefreshLayout.setEnabled(true);
                    Log.i("sjl", "what=1234");
                    break;
                default:
                    Log.i("sjl", "what=default");
                    break;
            }
        }
    };
}