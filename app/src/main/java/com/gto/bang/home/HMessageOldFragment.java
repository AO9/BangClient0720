package com.gto.bang.home;

import com.gto.bang.base.BaseInputFragment;


public class HMessageOldFragment extends BaseInputFragment {

//    public HMessageOldFragment() {
//    }
//    private RelativeLayout noticeRL;
//    private RelativeLayout messageRL;
//    RelativeLayout[] rls;
//
//    //显示未读消息数
//    private TextView messageNumTV;
//    private TextView noticeNumTV;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.bang_person_main, container, false);
//        return rootView;
//    }
//
//    public void initViews(){
//        messageRL=(RelativeLayout)getView().findViewById(R.id.pmessage_rl);
//        noticeRL=(RelativeLayout)getView().findViewById(R.id.pnotice_rl);
//        messageNumTV=(TextView)getView().findViewById(R.id.pmessage_num_tv);
//        noticeNumTV=(TextView)getView().findViewById(R.id.pnotice_num_tv);
//        rls=new RelativeLayout[]{noticeRL,messageRL};
//        for(int i=0;i<rls.length;i++){
//            rls[i].setOnClickListener(listener);
//        }
//    }
//
//    View.OnClickListener listener=new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent intent;
//            switch (v.getId()){
//                case R.id.pmessage_rl:
//                    intent =new Intent(getActivity(),PMessageActivity.class);
//                    startActivity(intent);
//                    break;
//                case R.id.pnotice_rl:
//                    intent =new Intent(getActivity(),PNoticeActivity.class);
//                    startActivity(intent);
//                    break;
//                default:
//                    break;
//            }
//
//        }
//    };
//
//
//    String requestTag="numOfUnReadSystemMessage_request";
//    /**
//     * 未读通知数
//     */
//    public void initUnReadNumOfSystemMessage() {
//        ResponseListener listener = new ResponseListener("notice");
//        String url= Constant.URL_BASE+ Constant.SYSTEM_MESSAGE_UNREAD_NUM_AJAX+"userId="+getSharedPreferences().getString(Constant.ID,"");
//        CustomRequest req = new CustomRequest(getActivity(),listener,listener,null,url, Request.Method.GET);
//        req.setTag(requestTag);
//        VolleyUtils.getRequestQueue(getActivity()).add(req);
//    }
//
//
//    /**
//     * 未读消息数
//     */
//    public void initData() {
//        ResponseListener listener = new ResponseListener("message");
//        String url= Constant.URL_BASE+ Constant.COMMENT_UNREAD_NUM_AJAX+"startid=0"
//                +"&authorid="+getSharedPreferences().getString(Constant.ID,"");
//        CustomRequest req = new CustomRequest(getActivity(),listener,listener,null,url, Request.Method.GET);
//        req.setTag(getRequestTag());
//        VolleyUtils.getRequestQueue(getActivity()).add(req);
//    }
//
//    @Override
//    protected String getRequestTag(){
//        return "HBangbangFragment_request";
//    }
//
//
//
//    public  class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener {
//        Toast t ;
//        String type;
//        ResponseListener(String type){
//            this.type=type;
//        }
//
//        @Override
//        public void onErrorResponse(VolleyError arg0) {
//            t = Toast.makeText(getActivity(), Constant.MESSAGE_REQUEST_ERROR, Toast.LENGTH_SHORT);
//            t.show();
//        }
//
//        @Override
//        public void onResponse(Map<String, Object> res) {
//            if(null==res.get(Constant.STATUS)||!Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())){
//                t = Toast.makeText(getActivity(), Constant.SERVER_ERROR, Toast.LENGTH_SHORT);
//                t.show();
//            }else{
//                String num = res.get("data").toString();
//                if(null!=num&&!"0".equals(num)){
//                    if("notice".equals(type)){
//                        noticeNumTV.setText("+"+num);
//                    }else{
//                        messageNumTV.setText("+"+num);
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        initData();
//        MobclickAgent.onPageStart("主页－帮帮"); //统计页面
//        initUnReadNumOfSystemMessage();
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        MobclickAgent.onPageEnd("主页－帮帮");
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        VolleyUtils.getRequestQueue(getActivity()).cancelAll(requestTag);
//        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
//    }
}