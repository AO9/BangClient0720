package com.gto.bang.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gto.bang.response.ResponseListener;
import com.gto.bang.util.Constant;
import com.gto.bang.util.JsonUtil;
import com.gto.bang.util.RequestUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by shenjialong on 20/2/21 21:15.
 */
public class BaseResponseListenerForRefresh extends ResponseListener {

    public static final int SUCCESS = 1;

    Toast t;
    Context context;
    Handler callbackHandler;

    public BaseResponseListenerForRefresh(Context context, Handler callbackHandler) {
        this.context = context;
        this.callbackHandler = callbackHandler;
    }

    @Override
    public void onErrorResponse(VolleyError arg0) {
        t = Toast.makeText(context, Constant.REQUEST_ERROR, Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    public void onResponse(Map<String, Object> res) {

        if (null == res.get("status") || !Constant.RES_SUCCESS.equals(res.get("status").toString())) {
            String data = (null == res.get("data")) ? "null" : res.get("data").toString();
            t = Toast.makeText(context, data, Toast.LENGTH_SHORT);
            t.show();
        } else {
            List<Map<String, Object>> datas = RequestUtil.parseResponseForDatas(res);
            try {
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = datas;
                Log.i("sjl", "onResponse datas={}" + JsonUtil.obj2Str(datas));
                this.callbackHandler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}

