package com.gto.bang.util;


import android.content.Context;

import com.android.volley.Request;
import com.gto.bang.response.CommonResponseListener;
import com.gto.bang.response.ResponseListener;
import com.gto.bang.util.request.CustomRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 工具类
 * 2020 06 22迁移出来独立的工具类
 */
public class RequestUtil {

    /**
     * 启动上报，携带deviceToken以及设备号androidId
     *
     * @param operateType
     * @param context
     * @param requestTag
     * @param deviceToken
     */
    public static void logForStartingApp(String operateType, Context context, String requestTag, String deviceToken) {
        Map<String, String> param = new HashMap<String, String>();
        param.put(Constant.OPERATETYPE, operateType);
        param.put(Constant.ANDROID_ID, CommonUtil.getAndroidId(context));
        param.put(Constant.DEVICE_TOKEN, deviceToken);
        CommonResponseListener responseListener = new CommonResponseListener(Constant.EMPTY, context);
        RequestUtil.request(Constant.LOG_URL, param, responseListener, requestTag, context);
    }


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
        CommonUtil.localLog(url);
        CustomRequest req = new CustomRequest(context, responseListener, responseListener, null, url, Request.Method.GET);
        req.setTag(requestTag);
        VolleyUtils.getRequestQueue(context).add(req);
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

        if (data == null) {
            return new ArrayList<Map<String, Object>>();
        }
        List<Map<String, Object>> list = (List<Map<String, Object>>) data.get(Constant.LIST);
        return list;
    }

    public static boolean hasNextPageByDatas(Map<String, Object> res) {
        Map<String, Object> data = (Map<String, Object>) res.get(Constant.DATA);
        boolean hasNextPage = (boolean) data.get(Constant.HASNEXTPAGE);
        return hasNextPage;
    }


}
