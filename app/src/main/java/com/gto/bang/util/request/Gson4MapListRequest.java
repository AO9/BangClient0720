package com.gto.bang.util.request;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.gto.bang.util.GsonUtils;
import com.gto.bang.util.SimpleDiskCacheUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by wjying on 13-12-2.
 */
public class Gson4MapListRequest extends GsonRequest<List<Map<String, Object>>>{

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url           URL of the request to make
     * @param headers       Map of request headers
     * @param listener
     * @param errorListener
     */
    public Gson4MapListRequest(Context context, String url, Map<String, String> headers, Response.Listener<List<Map<String, Object>>> listener, Response.ErrorListener errorListener,int method) {
        super(context, url, null, headers, listener, errorListener,method);
    }

    @Override
    protected Response<List<Map<String, Object>>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = getResponseStr(response);
            if (shouldCache()) {
                SimpleDiskCacheUtils.getSimpleDiskCache(getContext()).put(getUrl(), json);
            }
            List<Map<String, Object>> list = GsonUtils.getMapList(getGson(), json);
            return Response.success(list, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
