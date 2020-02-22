package com.gto.bang.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * hprovider_item.xml
 */
public class HProviderFragment extends Fragment {

    ListView listView;
    List<Map<String, Object>> datas;
    View rootView;


    public HProviderFragment() {
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//        listView=(ListView) rootView.findViewById(R.id.msgListView);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), EMainActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("id",datas.get(position).get("id").toString());
//                bundle.putString("artTitle",datas.get(position).get("title").toString());
//                bundle.putString("artType",Constant.TYPE_SERVICE);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
//        this.rootView=rootView;
//        return rootView;
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initDatas();
    }

    public void initDatas() {
        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.PRODUCT_LIST_URL;
        CustomRequest req = new CustomRequest(getActivity(), listener, listener, null, url, Request.Method.GET);
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
        return "HProviderFragment";
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
                convertView = mInflater.inflate(R.layout.hprovider_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.hprovider_title);
                holder.author = (TextView) convertView.findViewById(R.id.hprovider_author);
                holder.date = (TextView) convertView.findViewById(R.id.hprovider_date);
                holder.content = (TextView) convertView.findViewById(R.id.hprovider_content);
                holder.headIcon = (TextView) convertView.findViewById(R.id.hprovider_head);
                holder.viewtimes = (TextView) convertView.findViewById(R.id.hprovider_viewtimes);
                holder.comments = (TextView) convertView.findViewById(R.id.hprovider_comment);
                holder.praise = (TextView) convertView.findViewById(R.id.hprovider_praise);
                holder.price = (TextView) convertView.findViewById(R.id.hprovider_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title.setText(datas.get(position).get("title").toString());
            holder.author.setText(datas.get(position).get("username").toString());
            holder.date.setText(datas.get(position).get("createTime").toString());
            holder.content.setText(datas.get(position).get("content").toString());
            holder.viewtimes.setText(datas.get(position).get("viewtimes").toString());
            holder.praise.setText(datas.get(position).get("praise").toString());
            holder.price.setText(datas.get(position).get("price").toString());
//            holder.comments.setText(datas.get(position).get("comments").toString());
            String idStr = datas.get(position).get("authorId").toString();
            Integer authorId = Integer.valueOf(idStr);
            CommonUtil.handlerHeadIcon(authorId, holder.headIcon, datas.get(position).get("username").toString());
            CommonUtil.setOnClickListenerForPHomePage(idStr, getActivity(), holder.author);
            CommonUtil.setOnClickListenerForPHomePage(idStr, getActivity(), holder.headIcon);

            return convertView;
        }

    }

    public final class ViewHolder {
        public TextView title;
        public TextView author;
        public TextView date;
        public TextView content;
        public TextView headIcon;
        public TextView viewtimes;
        public TextView praise;
        public TextView comments;
        public TextView price;
    }


}
