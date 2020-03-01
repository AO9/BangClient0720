package com.gto.bang.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gto.bang.R;
import com.gto.bang.personal.activity.PInputActivity;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenjialong on 16/9/24 21:59.
 * 我的
 */
public class HPersonalFragment extends Fragment {

    RelativeLayout iconRL;
    LinearLayout nameRL;
    LinearLayout genderRL;
    LinearLayout regionRL;
    LinearLayout signatureRL;
    LinearLayout academyRL;
    TextView headIcon;
    ViewGroup []rls;
    Map<String, Object> userinfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.personal_info, container, false);
        initViews(rootView);
        return rootView;

    }


    /**
     * 网络获取列表数据
     */
    public void initData() {
        ResponseListener listener = new ResponseListener();
        String url= Constant.URL_BASE+ Constant.USER_INFO_AJAX+"authorid="+getSharedPreferences().getString(Constant.ID,"");
        Log.i("sjl","test"+url);
        CustomRequest req = new CustomRequest(getActivity(),listener,listener,null,url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getActivity()).add(req);
    }


    public void initViews(View view){
        iconRL=(RelativeLayout)view.findViewById(R.id.info_icon_rl);
        nameRL=(LinearLayout)view.findViewById(R.id.info_name_rl);
        genderRL=(LinearLayout)view.findViewById(R.id.info_gender_rl);
        signatureRL=(LinearLayout)view.findViewById(R.id.info_signature_rl);
        regionRL=(LinearLayout)view.findViewById(R.id.info_region_rl);
        academyRL=(LinearLayout)view.findViewById(R.id.info_academy_rl);
        rls=new ViewGroup[]{iconRL,regionRL,nameRL,genderRL,signatureRL,academyRL};
        headIcon=(TextView) view.findViewById(R.id.headIcon);

        String userName=getSharedPreferences().getString(Constant.USERNAME_V1,Constant.EMPTY);
        String id=getSharedPreferences().getString(Constant.ID,"0");
        CommonUtil.handlerHeadIcon(Integer.valueOf(id),headIcon,userName);


        for(int i=0;i<rls.length;i++){
            rls[i].setOnClickListener(listener);
        }

    }



    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            Bundle bundle=new Bundle();
            switch (v.getId()){
                case R.id.info_icon_rl:
                    String level_instruction=getSharedPreferences().getString(Constant.LEVEL_INSTRUCTION,"");
                    if(StringUtils.isNotBlank(level_instruction)){
                        CommonUtil.showDialog(getActivity(),level_instruction,"等级说明","知道了");
                    }
                    break;
                case R.id.info_name_rl:
//                    bundle.putInt("udpateType",Constant.PINFO_UPDATE_NAME);
//                    intent = new Intent(getActivity(), PInputActivity.class);
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent, Constant.PINFO_UPDATE_NAME);
                    Toast t = Toast.makeText(getActivity(), "昵称不支持修改", Toast.LENGTH_SHORT);
                    t.show();
                    break;
                case R.id.info_region_rl:
                    bundle.putInt("udpateType",Constant.PINFO_UPDATE_REGINON);
                    intent = new Intent(getActivity(), PInputActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, Constant.PINFO_UPDATE_REGINON);
                    break;
                case R.id.info_gender_rl:
                    showDialog();
                    break;
                case R.id.info_signature_rl:
                    bundle.putInt("udpateType",Constant.PINFO_UPDATE_SIGNATURE);
                    intent = new Intent(getActivity(), PInputActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, Constant.PINFO_UPDATE_SIGNATURE);
                    break;

                case R.id.info_academy_rl:
                    bundle.putInt("udpateType",Constant.PINFO_UPDATE_ACADAMY);
                    intent = new Intent(getActivity(), PInputActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, Constant.PINFO_UPDATE_ACADAMY);
                    break;
                default:
                    break;
            }

        }
    };


    public void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请选择");
        final String[] sex = {"男", "女"};

        builder.setSingleChoiceItems(sex, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                MyResponse listener = new MyResponse();
                HashMap<String, String> params=new HashMap<String, String>();

                params.put("userId",getSharedPreferences().getString(Constant.ID,Constant.AUTHORID_DEFAULT));
                params.put("updateType",String.valueOf(Constant.PINFO_UPDATE_GENDER));
                params.put("content",which==0?Constant.BOY:Constant.GIRL);

                String url=Constant.URL_BASE+ Constant.UPDATE_PERSION_INFO_AJAX;
                CustomRequest req = new CustomRequest(getActivity(),listener,listener,params,url, Request.Method.POST);
                req.setTag(REQUEST_TAG);
                VolleyUtils.getRequestQueue(getActivity()).add(req);

                TextView textView=(TextView)getView().findViewById(R.id.info_gender_tv);
                textView.setText(sex[which]);

                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static  final String REQUEST_TAG="PInfoActivity_update_gender";
    protected String getRequestTag(){
        return "PInfoActivity_Request";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null==data){
            //back键返回的情况
            return;
        }
        Bundle bundle= data.getExtras();
        String content=bundle.getString("content",null);
        if(null==content){
            return;
        }

        if (resultCode==PInputActivity.SUCCESS){
            View view=getView();
            TextView textView=null;
            switch (requestCode){
                case Constant.PINFO_UPDATE_NAME:
                    textView=(TextView) view.findViewById(R.id.info_name_tv);
                    break;
                case Constant.PINFO_UPDATE_REGINON:
                    textView=(TextView) view.findViewById(R.id.info_region_tv);
                    break;
                case Constant.PINFO_UPDATE_GENDER:
                    textView=(TextView) view.findViewById(R.id.info_gender_tv);
                    break;
                case Constant.PINFO_UPDATE_SIGNATURE:
                    textView=(TextView) view.findViewById(R.id.info_signature_tv);
                    break;
                case Constant.PINFO_UPDATE_ACADAMY:
                    textView=(TextView) view.findViewById(R.id.info_academy_tv);
                    break;
                default:
                    break;
            }
            if(textView!=null){
                textView.setText(content);
            }
        }
    }

    public  class MyResponse implements Response.Listener<Map<String, Object>>, Response.ErrorListener {

        Toast t ;
        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(getActivity(), "修改请求失败，请稍后重试", Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            Object status=res.get("status");
            if(null==status||!Constant.RES_SUCCESS.equals(status.toString())){
                t = Toast.makeText(getActivity(), "修改失败，请稍后重试", Toast.LENGTH_SHORT);
                t.show();
            }
        }
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
            if(null==res.get(Constant.STATUS)||!Constant.RES_SUCCESS.equals(res.get(Constant.STATUS).toString())){
                String data=(null==res.get(Constant.DATA))?Constant.REQUEST_ERROR:res.get(Constant.DATA).toString();
                t = Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT);
                t.show();
            }else{
                userinfo = (Map<String, Object>)res.get("data");
                initFeilds();
            }

        }
    }


    private void initFeilds(){

        if(null==userinfo){
            return;
        }
        View view =getView();
        TextView name=(TextView) view.findViewById(R.id.info_name_tv);
        TextView region=(TextView) view.findViewById(R.id.info_region_tv);
        TextView gender=(TextView) view.findViewById(R.id.info_gender_tv);
        TextView signature=(TextView) view.findViewById(R.id.info_signature_tv);
        TextView academy=(TextView) view.findViewById(R.id.info_academy_tv);
        TextView school=(TextView) view.findViewById(R.id.info_shcool_tv);
        TextView level=(TextView) view.findViewById(R.id.info_level_tv);

        String [] feilds=new String[]{Constant.USERNAME,Constant.CITY,Constant.GENDER,Constant.SIGNATURE,Constant.ACADEMY
                ,Constant.SCHOOL,Constant.LEVEL};
        TextView [] textViews=new TextView[]{name,region,gender,signature,academy,school,level};
        for(int i=0;i<feilds.length;i++){
            if(null!=userinfo.get(feilds[i])){
                textViews[i].setText(userinfo.get(feilds[i]).toString());
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(REQUEST_TAG);
        VolleyUtils.getRequestQueue(getActivity()).cancelAll(getRequestTag());
    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
        MobclickAgent.onPageStart("我的"); //统计页面
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的");
    }

    public SharedPreferences getSharedPreferences(){
        return getActivity().getSharedPreferences(Constant.DB, Activity.MODE_MULTI_PROCESS);
    }

}
