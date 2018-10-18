package com.gto.bang.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.gto.bang.util.VolleyUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 20171119
 */
public class BaseFragment extends Fragment {

    public BaseFragment() {}

    private String myTag;
    private String requestTag;

    public String getMyTag(){
        return myTag;
    }

    public void setMyTag(String myTag){
        this.myTag=myTag;
    }
    public Context getContext(){
        return getActivity();
    }

    public String getRequestTag(){
        return requestTag;
    }

    public void setRequestTag(String requestTag){
        this.requestTag=requestTag;
    }

    public void initData() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
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


}