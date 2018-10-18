package com.gto.bang.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.base.BaseFragment;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;

import java.util.List;
import java.util.Map;

/**
 *20171119
 */
public class HNoticeFragment extends BaseFragment {

    public HNoticeFragment() {}

    ListView listView;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bang_common_listview, container, false);
        listView=(ListView) rootView.findViewById(R.id.bang_common_lv);
        this.rootView=rootView;
        setRequestTag("HNoticeFragment");
        setMyTag("通知");
        return rootView;
    }

    public Context getContext(){
        return getActivity();
    }

    /**
     * 网络获取列表数据
     */
    public void initData() {
        ResponseListener listener = new ResponseListener();
        String url= Constant.URL_BASE+ Constant.SYSTEM_MESSAGE_AJAX+"startid=0"
                +"&userId="+getActivity().getSharedPreferences(Constant.DB, Activity.MODE_MULTI_PROCESS).getString(Constant.ID,"");
        CustomRequest req = new CustomRequest(getContext(),listener,listener,null,url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getContext()).add(req);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getContext()).cancelAll("message_update_status");
    }

    /**
     * 针对已展示的消息回写成已读状态
     */
    public void updateStatus(List<Map<String, Object>> datas){
        EmptyListener listener = new EmptyListener();
        StringBuilder ids=new StringBuilder();
        for(int i=0;i<datas.size();i++){
            if(i==0){
                ids.append(datas.get(i).get(Constant.ID));
            }else{
                ids.append(Constant.COMMA).append(datas.get(i).get(Constant.ID));
            }
        }
        String url= Constant.URL_BASE+ Constant.MESSAGE_UPDATE_STATUS_URL+"messageIds="+ids.toString();
        CustomRequest req = new CustomRequest(getContext(),listener,listener,null,url, Request.Method.GET);
        req.setTag("message_update_status");
        VolleyUtils.getRequestQueue(getContext()).add(req);
    }


    public  class EmptyListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
        }
        @Override
        public void onResponse(Map<String, Object> res) {
        }
    }

    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(getContext(), Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            if(null==res.get(Constant.STATUS)||!Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())){
                String data=(null==res.get(Constant.DATA))?Constant.REQUEST_ERROR:res.get(Constant.DATA).toString();
                t = Toast.makeText(getContext(), data, Toast.LENGTH_SHORT);
                t.show();
            }else{
                final List<Map<String, Object>> datas = (List<Map<String, Object>>)res.get("data");
                if(datas!=null&&datas.size()==0){
                    rootView.findViewById(R.id.bang_common_tips).setVisibility(View.VISIBLE);
                }
                ListView listView=(ListView)rootView.findViewById(R.id.bang_common_lv);
                SimpleAdapter adapter = new SimpleAdapter(getContext(), datas, R.layout.bang_pmynotice_item, new String[]{
                        "msgInfo"},
                        new int[]{R.id.bang_ncontent_tv});
                listView.setAdapter(adapter);
                updateStatus(datas);
            }

        }
    }


}