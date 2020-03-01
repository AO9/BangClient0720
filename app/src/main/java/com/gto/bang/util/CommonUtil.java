package com.gto.bang.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gto.bang.R;
import com.gto.bang.personal.activity.PHomePageActivity;

import org.apache.commons.lang.StringUtils;

/**
 * 工具类
 */
public class CommonUtil {


    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    public static final String getIMEI(Context context) {
        String id = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            id = telephonyManager.getImei();
            if (StringUtils.isBlank(id)) {
                id = telephonyManager.getMeid();
            }
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return id;
        }

    }


    public static final String getAndroidId(Context context) {

        String androidId = Settings.System.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return androidId;

    }

    /**
     * 渲染头像背景色
     *
     * @param id
     * @param textView
     */
    public static void handlerHeadIcon(int id, TextView textView, String userName) {
        int layout;
        switch (id % 4) {
            case 0:
                layout = R.drawable.corner;
                break;
            case 1:
                layout = R.drawable.corner_green;
                break;
            case 2:
                layout = R.drawable.corner_blue;
                break;
            case 3:
                layout = R.drawable.corner_pink;
                break;
            default:
                layout = R.drawable.corner;
                break;
        }
        textView.setBackgroundResource(layout);
        if (StringUtils.isNotBlank(userName)) {
            textView.setText(userName.substring(0, 1));
        }
    }

    public static void setOnClickListenerForPHomePage(final String id, final Context ct, TextView textView) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ct, PHomePageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("authorId", id);
                intent.putExtras(bundle);
                ct.startActivity(intent);
            }
        };
        textView.setOnClickListener(onClickListener);
    }


    public static void showDialog(Context context, String content, String title, String buttonText) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(content);
        dialog.setPositiveButton(buttonText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }


}
