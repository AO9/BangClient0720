package com.gto.bang.base;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import com.gto.bang.util.Constant;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 *
 */
public abstract class BaseRefreshFragment extends Fragment {

    public SwipeRefreshLayout swipeRefreshLayout;


    public BaseRefreshFragment() {
    }

    public abstract void refreshView(List<Map<String, Object>> datas);

    //handler
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.i("sjl", "handleMessage...");
                    Toast t;
                    swipeRefreshLayout.setRefreshing(false);
                    List<Map<String, Object>> datas = (List<Map<String, Object>>) msg.obj;
                    if (CollectionUtils.isNotEmpty(datas)) {
                        for (int i = 0; i < datas.size(); i++) {
                            Map<String, Object> map = datas.get(i);
                            map.put("createTime", null == map.get("createTime") ? null : map.get("createTime").toString().substring(0, 10));
                        }
                        refreshView(datas);
                        t = Toast.makeText(getActivity(), Constant.REFRESH_PROMPT+datas.size()+"条", Toast.LENGTH_SHORT);
                        t.show();
                    }else{
                        t = Toast.makeText(getActivity(), Constant.REFRESH_NOTHING, Toast.LENGTH_SHORT);
                        t.show();
                    }
                    swipeRefreshLayout.setEnabled(true);
                    break;
                default:
                    Log.i("sjl", "handleMessage=default");
                    break;
            }
        }
    };


}