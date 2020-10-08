package com.gto.bang.personal.activity;

import android.content.Context;
import android.os.Bundle;

import com.gto.bang.R;
import com.gto.bang.base.BaseActivity;

/**
 * Created by shenjialong on 16/6/10 19:08.
 * 个人中心——消息列表页面
 *
 * @惠民 16年0731日，尚未调试
 * @六里桥东 16年0810日22:49 增加标记为已读的功能
 * @绿岛 20年07月05日 消息功能迁移到我的板块
 */
public class PMessageActivity extends BaseActivity {
    @Override
    public Context getContext() {
        return PMessageActivity.this;
    }

    @Override
    public String getRequestTag() {
        return PMessageActivity.class.getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_main);
    }
//
//    /**
//     * 网络获取列表数据
//     */
//    public void initData() {
//        ResponseListener listener = new ResponseListener();
//        String url= Constant.URL_BASE+ Constant.COMMENT_MINE_AJAX+"startid=0"
//                +"&authorid="+getSharedPreferences(Constant.DB, Activity.MODE_PRIVATE).getString(Constant.ID,"");
//        CustomRequest req = new CustomRequest(this,listener,listener,null,url, Request.Method.GET);
//        req.setTag(getRequestTag());
//        VolleyUtils.getRequestQueue(this).add(req);
//    }
//
//

//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        VolleyUtils.getRequestQueue(this).cancelAll(getRequestTag());
//        VolleyUtils.getRequestQueue(this).cancelAll("comment_update_status");
//
//    }
//
//
//    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
//        Toast t ;
//        @Override
//        public void onErrorResponse(VolleyError arg0) {
//            t = Toast.makeText(PMessageActivity.this, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
//            t.show();
//        }
//
//        @Override
//        public void onResponse(Map<String, Object> res) {
//
//            if(null==res.get(Constant.STATUS)||!Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())){
//                String data=(null==res.get(Constant.DATA))?Constant.REQUEST_ERROR:res.get(Constant.DATA).toString();
//                t = Toast.makeText(PMessageActivity.this, data, Toast.LENGTH_SHORT);
//                t.show();
//            }else{
//                final List<Map<String, Object>> datas = (List<Map<String, Object>>)res.get("data");
//                if(datas!=null&&datas.size()==0){
//                    findViewById(R.id.bang_common_tips).setVisibility(View.VISIBLE);
//                }
//                if(null!=datas){
//                    for(int i=0;i<datas.size();i++){
//
//                        String type=datas.get(i).get("type").toString();
//                        String status=datas.get(i).get("status").toString();
//                        if(StringUtils.isNotBlank(status)){
//                            if(Constant.UNREAD.equals(status)){
//                                datas.get(i).put("tips","消息未读");
//                            }else{
//                                datas.get(i).put("tips","消息已读");
//                            }
//                        }
//
//                        if(type.equals(Constant.TYPE_EXPERIENCE)){
//                            datas.get(i).put("tag","经验");
//                        }else if(type.equals(Constant.TYPE_QUESTION)) {
//                            datas.get(i).put("tag","问答");
//                        }else if(type.equals(Constant.TYPE_COMPLAINTS)) {
//                            datas.get(i).put("tag","吐槽");
//                            datas.get(i).put("artTitle",datas.get(i).get("content"));
//                        }else if(type.equals(Constant.TYPE_SERVICE)) {
//                            datas.get(i).put("tag","互助");
//                        }
//
//                    }
//
//                    ListView listView=(ListView)findViewById(R.id.bang_common_lv);
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Intent intent = new Intent(PMessageActivity.this, EMainActivity.class);
//                            Bundle bundle=new Bundle();
//                            //actId文章的ID
//                            bundle.putString("id",datas.get(position).get("actId").toString());
//                            if(datas.get(position).get("type").toString().equals(Constant.TYPE_COMPLAINTS)){
//                                bundle.putString("artTitle",datas.get(position).get("content").toString());
//                            }else{
//                                bundle.putString("artTitle",datas.get(position).get("artTitle").toString());
//                            }
//
//                            bundle.putString("artType",datas.get(position).get("type").toString());
//                            bundle.putString("from",MESSAGE);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                        }
//                    });
//
//                    SimpleAdapter adapter = new SimpleAdapter(PMessageActivity.this, datas, R.layout.bang_pmymessage_item, new String[]{
//                            "artTitle","tag","tips"},
//                            new int[]{R.id.bang_mtitle_tv,R.id.bang_mtag_tv,R.id.tips});
//                    listView.setAdapter(adapter);
//
//                    updateStatus(datas);
//                }
//            }
//        }
//    }
//
//    /**
//     * 针对已展示的消息回写成已读状态
//     */
//    public void updateStatus(List<Map<String, Object>> datas){
//        EmptyListener listener = new EmptyListener();
//        StringBuilder ids=new StringBuilder();
//        for(int i=0;i<datas.size();i++){
//            if(i==0){
//                ids.append(datas.get(i).get(Constant.ID));
//            }else{
//                ids.append(Constant.COMMA).append(datas.get(i).get(Constant.ID));
//            }
//        }
//        String url= Constant.URL_BASE+ Constant.COMMENT_UPDATE_STATUS_URL+"commentIds="+ids.toString();
//        CustomRequest req = new CustomRequest(this,listener,listener,null,url, Request.Method.GET);
//        req.setTag("comment_update_status");
//        VolleyUtils.getRequestQueue(this).add(req);
//    }
//
//    public  class EmptyListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
//        Toast t ;
//        @Override
//        public void onErrorResponse(VolleyError arg0) {
//        }
//        @Override
//        public void onResponse(Map<String, Object> res) {
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        initData();
//        MobclickAgent.onPageStart("我的－消息");
//        MobclickAgent.onResume(this);
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        MobclickAgent.onPageEnd("我的－消息");
//        MobclickAgent.onPause(this);
//    }

}
