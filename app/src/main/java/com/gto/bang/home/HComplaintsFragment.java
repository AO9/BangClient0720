package com.gto.bang.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.gto.bang.experience.EMainActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 20161031
 * 吐槽TAB Fragment
 */
public class HComplaintsFragment extends Fragment {

    ListView listView;
    List<Map<String, Object>> datas;
    View rootView;

    public HComplaintsFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        listView=(ListView) rootView.findViewById(R.id.msgListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EMainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",datas.get(position).get("id").toString());
                bundle.putString("artTitle",datas.get(position).get("title").toString());
                bundle.putString("artType",Constant.TYPE_COMPLAINTS);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        this.rootView=rootView;
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initDatas();
    }

    public void initDatas() {
        ResponseListener listener = new ResponseListener();
        //TYPE=3 吐槽类的文章
        String url= Constant.URL_BASE+ Constant.ARTICLE_LIST_AJAX+"type="+Constant.TYPE_COMPLAINTS+"&startid=1";
        CustomRequest req = new CustomRequest(getActivity(),listener,listener,null,url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }

    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(getActivity(), Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {

            if(null==res.get("status")||!Constant.RES_SUCCESS.equals(res.get("status").toString())){
                String data=(null==res.get("data"))?"null":res.get("data").toString();
                t = Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT);
                t.show();
            }else{
                datas = (List<Map<String, Object>> )res.get("data");
                if(CollectionUtils.isNotEmpty(datas)) {
                    LinearLayout tips = (LinearLayout) rootView.findViewById(R.id.comment_tips);
                    tips.setVisibility(View.GONE);
                    ListView listView = (ListView) rootView.findViewById(R.id.msgListView);
                    listView.setVisibility(View.VISIBLE);
                    for(int i=0;i<datas.size();i++){
                        Map<String,Object> map=datas.get(i);
                        map.put("createTime",null==map.get("createTime")?null:map.get("createTime").toString().substring(0,10));
                    }
                    MyAdapter adapter=new MyAdapter(getActivity(),datas);
                    listView.setAdapter(adapter);
                }
            }
        }
    }
    protected String getRequestTag(){
        return "ARTICLE_COMPLAINTS_LIST_REQUEST";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("交流－吐槽"); //统计页面
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("交流－吐槽");
    }



    private class MyAdapter extends BaseAdapter{

        List<Map<String, Object>> datas;

        private LayoutInflater mInflater;

        public MyAdapter(Context context,List<Map<String, Object>> datas) {
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
                convertView = mInflater.inflate(R.layout.hcomplaints_item,null);
                holder = new ViewHolder();
                holder.author = (TextView) convertView.findViewById(R.id.author_c_tv);
                holder.content = (TextView) convertView.findViewById(R.id.content_c_tv);
                holder.headIcon = (TextView) convertView.findViewById(R.id.head_c_tv);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
            }
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.author.setText(datas.get(position).get("username").toString());
            holder.content.setText(datas.get(position).get("content").toString());


            String idStr=datas.get(position).get("authorId").toString();
            Integer authorId=Integer.valueOf(idStr);

            String userName=datas.get(position).get("username").toString();
            CommonUtil.handlerHeadIcon(Integer.valueOf(idStr),holder.headIcon,userName);
            CommonUtil.setOnClickListenerForPHomePage(idStr,getActivity(),holder.author);
            CommonUtil.setOnClickListenerForPHomePage(idStr,getActivity(),holder.headIcon);

            return convertView;
        }

    }

    public final class ViewHolder{
        public TextView author;
        public TextView content;
        public TextView headIcon;
    }


}
