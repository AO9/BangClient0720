package com.gto.bang.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.util.Log;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.gto.bang.util.cache.BitmapLruCache;

import java.io.File;

/**
 * Volley
 */
public class VolleyUtils {
    private static VolleyUtils sVolleyHelper;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleyUtils(Context context) {
        context = context.getApplicationContext();
        init(context);
    }

    private void init(Context context) {
        VolleyLog.setTag("System.out");
        VolleyLog.DEBUG = true;
        mRequestQueue = CustomVolley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(context));
        mRequestQueue.start();
    }

    private static class CustomVolley {
        /** Default on-disk cache directory. */
        // 修改缓存目录
        private static final String DEFAULT_CACHE_DIR = "bangbang_volley";

        public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
            File cacheDir = new File(context.getExternalCacheDir(), DEFAULT_CACHE_DIR);
            String userAgent = "volley/0";
            try {
                String packageName = context.getPackageName();
                PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
                userAgent = packageName + "/" + info.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("sjl","newReqeustQueue is error");
            }

            if (stack == null) {
                if (Build.VERSION.SDK_INT >= 9) {
                    stack = new HurlStack();
                } else {
                    // Prior to Gingerbread, HttpUrlConnection was unreliable.
                    // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                    stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
                }
            }

            Network network = new BasicNetwork(stack);

            // 修改缓存大小
            RequestQueue queue = new RequestQueue(new DiskBasedCache(cacheDir, 50 * 1024 * 1024), network);
            queue.start();

            return queue;
        }

        /**
         * Creates a default instance of the worker pool and calls {@link com.android.volley.RequestQueue#start()} on it.
         *
         * @param context A {@link Context} to use for creating the cache dir.
         * @return A started {@link com.android.volley.RequestQueue} instance.
         */
        public static RequestQueue newRequestQueue(Context context) {
            return newRequestQueue(context, null);
        }
    }

    private static VolleyUtils getVolleyHelper(Context context) {
        VolleyUtils helper = sVolleyHelper;//sVolleyHelper != null ? sVolleyHelper.get() : null;
        if (helper == null) {
            helper = new VolleyUtils(context);
            sVolleyHelper = helper;//new WeakReference<VolleyUtils>(helper);
        }
        return helper;
    }

    /**
     * 获取Volley请求队列
     * @param context
     * @return
     */
    public static RequestQueue getRequestQueue(Context context) {
        return getVolleyHelper(context).mRequestQueue;
    }

    /**
     * 获取Volley图片请求加载器
     * @param context
     * @return
     */
    public static ImageLoader getImageLoader(Context context) {
        return getVolleyHelper(context).mImageLoader;
    }
}
