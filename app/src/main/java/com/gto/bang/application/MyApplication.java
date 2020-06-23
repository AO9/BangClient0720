package com.gto.bang.application;

import android.app.Application;
import android.util.Log;

import com.gto.bang.util.CommonUtil;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * Created by shenjialong on 20/6/23 20:06.
 *
 * 主要目标：U-PUSH插件使用的前置初始化逻辑代码
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // add by shenjialong 以下是U-PUSH插件使用的前置逻辑
        UMConfigure.init(this, "57a98ed467e58e179b00397c", "Umeng", UMConfigure.DEVICE_TYPE_PHONE,
                "fafc29059d840c02214e6e4a83ac198f");

        // 获取消息推送代理示例
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setResourcePackageName("com.gto.bang");
        // 注册推送服务，每次调用register方法都会回调该接口
        CommonUtil.localLog("MyApplication step 1");
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                CommonUtil.localLog("MyApplication step 3 注册成功：deviceToken：-------->  " + deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {
                CommonUtil.localLog("MyApplication step 4 注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });

        CommonUtil.localLog("MyApplication step 2");

    }
}
