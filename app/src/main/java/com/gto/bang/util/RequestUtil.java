package com.gto.bang.util;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.gto.bang.base.ResponseListener;
import com.gto.bang.util.request.CustomRequest;

import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 工具类
 * 2020 06 22迁移出来独立的工具类
 */
public class RequestUtil {

    /**
     * @param param
     * @param responseListener
     * @date 20200621
     */
    public static void request(String requestPath, Map<String, String> param, ResponseListener responseListener, String requestTag, Context context) {

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
        CustomRequest req = new CustomRequest(context, responseListener, responseListener, null, url, Request.Method.GET);
        req.setTag(requestTag);
        VolleyUtils.getRequestQueue(context).add(req);
    }

    /**
     * 通用响应器,适用于写入类的网络请求
     *
     * @date 20200621
     */
    public class CommonResponseListener extends ResponseListener {

        String toastForSuccess;
        Toast t;
        Context context;

        public CommonResponseListener(String toastForSuccess, Context context) {
            this.toastForSuccess = toastForSuccess;
            this.context = context;
        }

        @Override
        public void onErrorResponse(VolleyError arg0) {
            t = Toast.makeText(this.context, Constant.NEWWORD_ERROR_TIPS, Toast.LENGTH_SHORT);
            t.show();
        }

        @Override
        public void onResponse(Map<String, Object> res) {
            Object status = res.get(Constant.STATUS);
            if (null == status || !Constant.RES_SUCCESS.equals(status.toString())) {
                t = Toast.makeText(this.context, Constant.SEVER_ERROR_TIPS, Toast.LENGTH_SHORT);
                Log.i(Constant.LOG_TAG, res.get(Constant.DATA).toString());
                t.show();
            } else {
                if (StringUtils.isNotBlank(toastForSuccess)) {
                    t = Toast.makeText(this.context, toastForSuccess, Toast.LENGTH_SHORT);
                    t.show();
                }
            }


        }
    }

    /**
     * 解析分页响应内容,返回list数组
     * 20200619
     *
     * @param res
     * @return
     */
    public static List<Map<String, Object>> parseResponseForDatas(Map<String, Object> res) {
        Map<String, Object> data = (Map<String, Object>) res.get(Constant.DATA);
        List<Map<String, Object>> list = (List<Map<String, Object>>) data.get(Constant.LIST);
        return list;
    }


}
