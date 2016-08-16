package com.gto.bang.util.request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shenjialong on 16/7/11 21:20.
 */
public class CustomRequest extends Gson4MapRequest {

    HashMap<String, String> params;

    public CustomRequest(Context context,Response.Listener<Map<String, Object>> listener,Response.ErrorListener errorListener,
                         HashMap<String, String> params,String url,int method) {

        super(context, url, null, listener, errorListener,method);
        this.params=params;
    }


    @Override
    protected HashMap<String, String> getParams()
            throws AuthFailureError {
        return params;
    }
}
