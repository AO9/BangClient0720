package com.gto.bang.experience.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.home.BaseDetailActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 20191116 好文详情页下面的评论列表
 * 20200615 评论列表增加标签功能
 */

public class ArticleCommentFragment extends Fragment {

    ListView listview;
    List<Map<String, Object>> datas;

    String artType;

    public ArticleCommentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.comment_list, container, false);
        listview = (ListView) rootView.findViewById(R.id.bang_c_listview);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initDatas();
    }

    public void initDatas() {

        String articleId = null;
        if (getActivity() instanceof BaseDetailActivity) {
            articleId = ((BaseDetailActivity) getActivity()).getArticleId();
        }
        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.COMMENT_LIST + "&artId=" + articleId;
        Log.i("sjl", "评论列表:" + url);
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
            if (null == res.get("status") || !Constant.RES_SUCCESS.equals(res.get("status").toString())) {
                t = Toast.makeText(getActivity(), Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
                t.show();
            } else {
                datas = (List<Map<String, Object>>) res.get("data");
                if (CollectionUtils.isNotEmpty(datas)) {
                    getView().findViewById(R.id.comment_tips).setVisibility(View.GONE);
                    MyAdapter adapter = new MyAdapter(getActivity(), datas);
                    listview.setAdapter(adapter);
                }
            }
        }
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
                convertView = mInflater.inflate(R.layout.bang_ecomment_item, null);
                holder = new ViewHolder();
                holder.author = (TextView) convertView.findViewById(R.id.comment_author_tv);
                holder.date = (TextView) convertView.findViewById(R.id.comment_date_tv);
                holder.content = (TextView) convertView.findViewById(R.id.comment_content_tv);
                holder.headIcon = (TextView) convertView.findViewById(R.id.comment_head_tv);
                holder.topping = (TextView) convertView.findViewById(R.id.topping);
                holder.highQuality = (TextView) convertView.findViewById(R.id.highQuality);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            holder.author.setText(datas.get(position).get("username").toString());
            holder.date.setText(datas.get(position).get("createtime").toString());
            holder.content.setText(datas.get(position).get("content").toString());

            String idStr = datas.get(position).get("userId").toString();
            Integer authorId = Integer.valueOf(idStr);
            CommonUtil.handlerHeadIcon(authorId, holder.headIcon, datas.get(position).get("username").toString());
            CommonUtil.setOnClickListenerForPHomePage(idStr, getActivity(), holder.headIcon);
            CommonUtil.setOnClickListenerForPHomePage(idStr, getActivity(), holder.author);

            holder.content.setText(datas.get(position).get("content").toString());
            // 回答内容增加标签功能：暂时可支持置顶+优质
            setTag(datas.get(position),holder);

            return convertView;
        }

    }

    /**
     * 评论内容增加标签功能：暂时可支持置顶+优质
     * 现在主要用在回答功能
     * @param map
     * @param holder
     * @date 20200615
     */
    public void setTag(Map<String, Object> map,ViewHolder holder ){

        String[] keys = new String[]{"topping", "highQuality"};
        TextView[] views = new TextView[]{holder.topping, holder.highQuality};
        for (int i = 0; i < keys.length; i++) {
            Object tem = map.get(keys[i]);
            if (tem != null && String.valueOf(tem).equals("1")) {
                views[i].setVisibility(View.VISIBLE);
            }
        }

    }

    public final class ViewHolder {
        public TextView author;
        public TextView date;
        public TextView content;
        public TextView headIcon;
        public TextView topping;
        public TextView highQuality;
    }

    protected String getRequestTag() {
        return "COMMENT_LIST_REQUEST";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Constant.TYPE_EXPERIENCE.equals(artType)) {
            MobclickAgent.onPageStart("评论－经验"); //统计页面
        } else if (Constant.TYPE_QUESTION.equals(artType)) {
            MobclickAgent.onPageStart("评论－问答");
        } else {
            MobclickAgent.onPageStart("评论－吐槽");
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if (Constant.TYPE_EXPERIENCE.equals(artType)) {
            MobclickAgent.onPageEnd("评论－经验"); //统计页面
        } else if (Constant.TYPE_QUESTION.equals(artType)) {
            MobclickAgent.onPageEnd("评论－问答");
        } else {
            MobclickAgent.onPageEnd("评论－吐槽");
        }
    }


}
