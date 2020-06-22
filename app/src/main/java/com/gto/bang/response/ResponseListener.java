package com.gto.bang.response;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Map;

/**
 * Created by shenjialong on 20/2/21 21:16.
 */
public class ResponseListener implements Response.Listener<Map<String, Object>>, Response.ErrorListener{
    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }

    @Override
    public void onResponse(Map<String, Object> stringObjectMap) {

    }
}
