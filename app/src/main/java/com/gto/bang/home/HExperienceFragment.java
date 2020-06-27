package com.gto.bang.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.gto.bang.experience.EMainActivity;
import com.gto.bang.response.ResponseListener;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.RequestUtil;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by shenjialong on 16/6/9 19:52.
 * 首页－经验tab
 * <p/>
 * 20191116 经验版块由推荐好文替换
 */
public class HExperienceFragment extends BaseRefreshFragment {

    ListView listView;
    List<Map<String, Object>> datas;
    View rootView;
    int pageNum = 1;
    boolean hasNextPage;

    public HExperienceFragment() {
    }

    @Override
    public void refreshView(List<Map<String, Object>> datas) {
        // 将本地的datas数据更新，否则点击查看详情时跳转的不正确
        this.datas = datas;
        this.listView.setAdapter(new MyAdapter(getActivity(), datas));
    }

    @Override
    public String getRequestTag() {
        return HExperienceFragment.class.getName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView) rootView.findViewById(R.id.msgListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EMainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", datas.get(position).get("id").toString());
                bundle.putString("artTitle", datas.get(position).get("title").toString());
                bundle.putString("artType", Constant.TYPE_EXPERIENCE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        this.rootView = rootView;

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CommonUtil.localLog("下拉刷新监听事件");
                if (hasNextPage) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setEnabled(false);
                            initDatas(++pageNum, new BaseResponseListenerForRefresh(getActivity(), mHandler));
                        }
                    }).start();
                } else {
                    CommonUtil.localLog("休息一下，暂无新内容推送");
                    CommonUtil.showTips("休息一下，暂无新内容推送.", getActivity());
                }

            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        CommonUtil.localLog("step 1");
        initDatas(pageNum, new MyResponseListener());
        CommonUtil.localLog("step 4");
    }

    public void initDatas(int pageNum, ResponseListener responseListener) {
        String url = Constant.URL_BASE + Constant.ARTICLE_LIST_AJAX + "type=1&startid=1" + "&pageNum=" + pageNum;
        CommonUtil.localLog("step 2-3 s" + url);
        CustomRequest req = new CustomRequest(getActivity(), responseListener, responseListener, null, url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }

    public class MyResponseListener extends ResponseListener {
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
                CommonUtil.showTips(data, getActivity());
                TextView tips = (TextView) rootView.findViewById(R.id.tips);
                tips.setText("无数据");
            } else {
                CommonUtil.localLog("step 2");
                datas = RequestUtil.parseResponseForDatas(res);
                hasNextPage = RequestUtil.hasNextPageByDatas(res);
                if (CollectionUtils.isNotEmpty(datas)) {
                    LinearLayout loading = (LinearLayout) rootView.findViewById(R.id.comment_tips);
                    loading.setVisibility(View.GONE);
                    ListView listView = (ListView) rootView.findViewById(R.id.msgListView);
                    listView.setVisibility(View.VISIBLE);
                    MyAdapter adapter = new MyAdapter(getActivity(), datas);
                    listView.setAdapter(adapter);
                }else {
                    TextView tips = (TextView) rootView.findViewById(R.id.tips);
                    tips.setText("无数据");
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
                convertView = mInflater.inflate(R.layout.hexperience_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title_e_tv);
                holder.author = (TextView) convertView.findViewById(R.id.author_e_tv);
                holder.content = (TextView) convertView.findViewById(R.id.content_e_tv);
                holder.headIcon = (TextView) convertView.findViewById(R.id.head_e_tv);
                holder.viewtimes = (TextView) convertView.findViewById(R.id.experience_viewtimes_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.title.setText(datas.get(position).get(Constant.TITLE).toString());
            holder.author.setText(datas.get(position).get(Constant.USERNAME).toString());
            holder.content.setText(datas.get(position).get(Constant.CONTENT).toString());
            holder.viewtimes.setText(datas.get(position).get(Constant.VIEWTIMES).toString());
            String idStr = datas.get(position).get("authorId").toString();

            CommonUtil.handlerHeadIcon(Integer.valueOf(idStr), holder.headIcon, datas.get(position).get(Constant.USERNAME).toString());
            CommonUtil.setOnClickListenerForPHomePage(idStr, getActivity(), holder.author);
            CommonUtil.setOnClickListenerForPHomePage(idStr, getActivity(), holder.headIcon);

            return convertView;
        }

    }

    public final class ViewHolder {
        public TextView title;
        public TextView author;
        public TextView content;
        public TextView headIcon;
        public TextView viewtimes;
    }


}
