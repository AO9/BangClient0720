package com.gto.bang.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseRefreshFragment;
import com.gto.bang.base.BaseResponseListenerForRefresh;
import com.gto.bang.base.ResponseListener;
import com.gto.bang.question.fragment.QuestionDetailActivity;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 2019年2月11日更新
 * title信息改成describe字段
 */
public abstract class HQuestionFragment extends BaseRefreshFragment {

    ListView listView;
    List<Map<String, Object>> datas;
    View rootView;
    int pageNum = 1;

    public static final String TAG = "HQuestionFragment";

    public HQuestionFragment() {
    }

    // 热门还是最新
    public abstract String getArticleType();

    /**
     * 父类抽象方法
     * @param datas
     */
    @Override
    public void refreshView(List<Map<String, Object>> datas) {
        // 将本地的datas数据更新，否则点击查看详情时跳转的不正确
        this.datas=datas;
        this.listView.setAdapter(new MyAdapter(getActivity(), datas));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRequestTag(TAG);
        View rootView = inflater.inflate(R.layout.fragment_main_v1, container, false);
        listView = (ListView) rootView.findViewById(R.id.msgListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//              Intent intent = new Intent(getActivity(), EMainActivity.class);
                //问答改版 20200619
                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
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
                        initDatas(++pageNum, new BaseResponseListenerForRefresh(getActivity(), mHandler));
                        Log.i("sjl", "sendEmptyMessage after ");
                    }
                }).start();
            }
        });
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initDatas(pageNum, new MyResponseListener());
    }


    public void initDatas(int pageNum, ResponseListener responseListener) {
        String url = Constant.URL_BASE + Constant.ARTICLE_LIST_AJAX + "pageNum=" + pageNum;
        url = url + "&type=" + getArticleType() + "&userId=" + getUserId();
        CustomRequest req = new CustomRequest(getActivity(), responseListener, responseListener, null, url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }

    public class MyResponseListener extends ResponseListener {
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
                datas = parseResponseForDatas(res);
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
                convertView = mInflater.inflate(R.layout.hquestion_item_v1, null);
                holder = new ViewHolder();
                holder.author = (TextView) convertView.findViewById(R.id.author_e_tv);
                holder.date = (TextView) convertView.findViewById(R.id.date_e_tv);
                holder.content = (TextView) convertView.findViewById(R.id.content_tv);
                holder.viewTimes = (TextView) convertView.findViewById(R.id.viewTimes);
                holder.commentNum = (TextView) convertView.findViewById(R.id.commentNum);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.author.setText(datas.get(position).get(Constant.USERNAME).toString());
            holder.date.setText(datas.get(position).get(Constant.CREATETIME).toString());
            holder.content.setText(datas.get(position).get(Constant.CONTENT).toString());
            holder.viewTimes.setText(datas.get(position).get(Constant.VIEWTIMES).toString());
            holder.commentNum.setText(datas.get(position).get(Constant.COMMENTNUM).toString());

            return convertView;
        }

    }

    public final class ViewHolder {
        public TextView content;
        public TextView author;
        public TextView date;
        public TextView viewTimes;
        public TextView commentNum;

    }

}