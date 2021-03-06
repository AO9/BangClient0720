package com.gto.bang.util.request;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.gto.bang.util.SimpleDiskCache;
import com.gto.bang.util.SimpleDiskCacheUtils;

public class CacheTask<T> extends AsyncTask<Void, Void,  T>{
    /**
     * 异步取缓存回调接口
     * @param <T>
     */
    public interface CacheTaskResponse<T> {
        void onResponse(T t);
    }
    private final Gson gson = new Gson();
    private Context mContext;
    private String mUrl;
    private CacheTaskResponse mCacheTaskResponse;
    public CacheTask(Context context, String url, CacheTaskResponse cacheTaskResponse) {
        mContext = context.getApplicationContext();
        mUrl = url;
        mCacheTaskResponse = cacheTaskResponse;
    }

    @Override
    protected final T doInBackground(Void... params) {
        if (TextUtils.isEmpty(mUrl)) {
            return null;
        }        
        SimpleDiskCache.StringEntry entry = null;
        try {
            entry = SimpleDiskCacheUtils.getSimpleDiskCache(mContext).getString(mUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parse(entry != null ? entry.getString() : null);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mCacheTaskResponse = null;
    }

    @Override
    protected final void onPostExecute(T t) {
        if (mCacheTaskResponse != null) {
            mCacheTaskResponse.onResponse(t);
        }
    }

    protected T parse(String cache) {
        return null;
    }

    protected Context getContext() {
        return mContext;
    }

    protected Gson getGson() {
        return this.gson;
    }
}
