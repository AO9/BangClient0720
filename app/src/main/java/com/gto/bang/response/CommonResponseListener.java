package com.gto.bang.response;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.gto.bang.util.Constant;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * Created by shenjialong on 20/6/22 19:47.
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
