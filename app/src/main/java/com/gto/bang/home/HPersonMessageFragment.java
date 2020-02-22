package com.gto.bang.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseFragment;
import com.gto.bang.experience.EMainActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 个人消息
 * 20171119 来源PMessageActivity
 */
public class HPersonMessageFragment extends BaseFragment {

    ListView listView;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bang_common_listview, container, false);
        listView = (ListView) rootView.findViewById(R.id.msgListView);
        this.rootView = rootView;
        setRequestTag("HQuestionFragment");
        setMyTag("个人消息");
        return rootView;
    }

    /**
     * 网络获取列表数据
     */
    public void initData() {
        ResponseListener listener = new ResponseListener();
        String url = Constant.URL_BASE + Constant.COMMENT_MINE_AJAX + "startid=0"
                + "&authorid=" + getActivity().getSharedPreferences(Constant.DB, Activity.MODE_PRIVATE).getString(Constant.ID, "");
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

            if (null == res.get(Constant.STATUS) || !Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())) {
                String data = (null == res.get(Constant.DATA)) ? Constant.REQUEST_ERROR : res.get(Constant.DATA).toString();
                t = Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT);
                t.show();
            } else {
                final List<Map<String, Object>> datas = (List<Map<String, Object>>) res.get("data");
                if (CollectionUtils.isEmpty(datas)) {
                    rootView.findViewById(R.id.bang_common_tips).setVisibility(View.VISIBLE);
                    return;
                }
                for (int i = 0; i < datas.size(); i++) {

                    String type = datas.get(i).get("type").toString();
                    String status = datas.get(i).get("status").toString();
                    if (StringUtils.isNotBlank(status)) {
                        if (Constant.UNREAD.equals(status)) {
                            datas.get(i).put("tips", "消息未读");
                        } else {
                            datas.get(i).put("tips", "消息已读");
                        }
                    }

                    if (type.equals(Constant.TYPE_EXPERIENCE)) {
                        datas.get(i).put("tag", "经验");
                    } else if (type.equals(Constant.TYPE_QUESTION)) {
                        datas.get(i).put("tag", "问答");
                    } else if (type.equals(Constant.TYPE_COMPLAINTS)) {
                        datas.get(i).put("tag", "吐槽");
                        datas.get(i).put("artTitle", datas.get(i).get("content"));
                    } else if (type.equals(Constant.TYPE_SUPPORT)) {
                        datas.get(i).put("tag", "论文帮");
                    }

                }

                ListView listView = (ListView) rootView.findViewById(R.id.bang_common_lv);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), EMainActivity.class);
                        Bundle bundle = new Bundle();
                        //actId文章的ID
                        bundle.putString("id", datas.get(position).get("actId").toString());
                        if (datas.get(position).get("type").toString().equals(Constant.TYPE_COMPLAINTS)) {
                            bundle.putString("artTitle", datas.get(position).get("content").toString());
                        } else {
                            bundle.putString("artTitle", datas.get(position).get("artTitle").toString());
                        }

                        bundle.putString("artType", datas.get(position).get("type").toString());
                        bundle.putString("from", Constant.MESSAGE);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                MyAdapter adapter=new MyAdapter(getActivity(),datas);
                listView.setAdapter(adapter);
                updateStatus(datas);
            }
        }
    }

    /**
     * 针对已展示的消息回写成已读状态
     */
    public void updateStatus(List<Map<String, Object>> datas) {
        EmptyListener listener = new EmptyListener();
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < datas.size(); i++) {
            if (i == 0) {
                ids.append(datas.get(i).get(Constant.ID));
            } else {
                ids.append(Constant.COMMA).append(datas.get(i).get(Constant.ID));
            }
        }
        String url = Constant.URL_BASE + Constant.COMMENT_UPDATE_STATUS_URL + "commentIds=" + ids.toString();
        CustomRequest req = new CustomRequest(getActivity(), listener, listener, null, url, Request.Method.GET);
        req.setTag("comment_update_status");
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }

    public class EmptyListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t;

        @Override
        public void onErrorResponse(VolleyError arg0) {
        }

        @Override
        public void onResponse(Map<String, Object> res) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll("comment_update_status");
    }




    private class MyAdapter extends BaseAdapter {

        List<Map<String, Object>> datas;

        private LayoutInflater mInflater;

        public MyAdapter(Context context, List<Map<String, Object>> datas) {
            this.datas=datas;
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
                convertView = mInflater.inflate(R.layout.personal_comment_item,null);
                holder = new ViewHolder();
                holder.head=(TextView) convertView.findViewById(R.id.head);
                holder.userName=(TextView) convertView.findViewById(R.id.userName);
                holder.theme=(TextView) convertView.findViewById(R.id.theme);
                holder.content=(TextView) convertView.findViewById(R.id.content);
                holder.createtime=(TextView) convertView.findViewById(R.id.createTime);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            String [] keys=new String[]{Constant.USERNAME,Constant.FROMUSERID,
                    Constant.ARTTITLE,Constant.CONTENT,Constant.CREATETIME};

            TextView [] tvs=new TextView[]{holder.userName,holder.head,
                    holder.theme,holder.content,holder.createtime
            };

            for(int i=0;i<keys.length;i++){
                Object obj=datas.get(position).get(keys[i]);

                if(null!=obj){
                    if(Constant.FROMUSERID.equals(keys[i])){
                        String idStr=obj.toString();
                        Integer authorId=Integer.valueOf(idStr);
                        CommonUtil.handlerHeadIcon(authorId,holder.head,datas.get(position).get(Constant.USERNAME).toString());
                        CommonUtil.setOnClickListenerForPHomePage(idStr,getActivity(),holder.userName);
                        CommonUtil.setOnClickListenerForPHomePage(idStr,getActivity(),holder.head);
                        continue;
                    }
                    tvs[i].setText(obj.toString());
                }
            }
            return convertView;

        }
    }

    public final class ViewHolder{

        public TextView head;
        public TextView userName;
        public TextView theme;
        public TextView content;
        public TextView createtime;

    }

}