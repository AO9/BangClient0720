package com.gto.bang.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.gto.bang.response.ResponseListener;
import com.gto.bang.util.CommonUtil;
import com.gto.bang.util.Constant;
import com.gto.bang.util.RequestUtil;
import com.gto.bang.util.VolleyUtils;
import com.gto.bang.util.request.CustomRequest;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 20171119
 */
public abstract class BaseFragment extends Fragment {

    public BaseFragment() {
    }

    private String myTag;

    public Context getContext() {

        return getActivity();
    }

    public String getMyTag() {

        return myTag;
    }

    public void setMyTag(String myTag) {

        this.myTag = myTag;
    }

    public abstract String getRequestTag();


    public void initData() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyUtils.getRequestQueue(getContext()).cancelAll(getRequestTag());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getMyTag());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getMyTag());
    }

    public SharedPreferences getSharedPreferences() {
        SharedPreferences sp = getActivity().getSharedPreferences(Constant.DB, Activity.MODE_MULTI_PROCESS);
        return sp;
    }

    public String getUserId() {
        return getSharedPreferences().getString(Constant.ID, Constant.AUTHORID_DEFAULT);
    }

    public String getAndroidId() {
        return getSharedPreferences().getString(Constant.ANDROID_ID, Constant.AUTHORID_DEFAULT);
    }

    public String getStrFromPreferences(String key){
        return getSharedPreferences().getString(key, Constant.EMPTY);
    }


    // --------------分割线 20200620 --------------

    public void log(String operateType) {
        Map<String, String> param = new HashMap<String, String>();
        param.put(Constant.USERID_V1, getUserId());
        param.put(Constant.OPERATETYPE, operateType);
        param.put(Constant.ANDROID_ID, CommonUtil.getAndroidId(getActivity()));
        com.gto.bang.response.CommonResponseListener responseListener = new com.gto.bang.response.CommonResponseListener(Constant.EMPTY, getContext());
        RequestUtil.request(Constant.LOG_URL, param, responseListener, getRequestTag(), getContext());
    }

    /**
     * 关注或点赞
     *
     * @param param
     * @param responseListener
     */
    public void request(String requestPath, Map<String, String> param, ResponseListener responseListener) {

        String userId = getUserId();
        String url = Constant.URL_BASE + requestPath + Constant.URL_SEPARATOR;
        String key = null;
        String value = null;
        Iterator it = param.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            key = (String) entry.getKey();
            value = (String) entry.getValue();
            url = url + key + Constant.URL_EQUAL + value + Constant.URL_PARAM_SEPARATOR;
        }
        CustomRequest req = new CustomRequest(getActivity(), responseListener, responseListener, null, url, Request.Method.GET);
        req.setTag(getRequestTag());
        VolleyUtils.getRequestQueue(getContext()).add(req);
    }

    /**
     * 通用响应器
     * 20200620
     */
    public class CommonResponseListener extends ResponseListener {

        String toastForSuccess;
        Toast t;

        public CommonResponseListener(String toastForSuccess) {
            this.toastForSuccess = toastForSuccess;
        }

        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(getActivity(), Constant.NEWWORD_ERROR_TIPS, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            Object status = res.get(Constant.STATUS);
            if (null == status || !Constant.RES_SUCCESS.equals(status.toString())) {
                t = Toast.makeText(getActivity(), Constant.SEVER_ERROR_TIPS, Toast.LENGTH_SHORT);
                Log.i(Constant.LOG_TAG,res.get(Constant.DATA).toString());
                t.show();
            } else {
                if (StringUtils.isNotBlank(toastForSuccess)) {
                    t = Toast.makeText(getActivity(), toastForSuccess, Toast.LENGTH_SHORT);
                    t.show();
                }
            }


        }
    }

}