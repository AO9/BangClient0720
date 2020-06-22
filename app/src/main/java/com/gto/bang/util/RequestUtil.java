package com.gto.bang.util;


import android.content.Context;

import com.android.volley.Request;
import com.gto.bang.response.ResponseListener;
import com.gto.bang.util.request.CustomRequest;

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
