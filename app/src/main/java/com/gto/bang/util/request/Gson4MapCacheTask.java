package com.gto.bang.util.request;

import android.content.Context;

import com.gto.bang.util.GsonUtils;

import java.util.Map;

public class Gson4MapCacheTask extends CacheTask<Map<String, Object>>{


    public Gson4MapCacheTask(Context context, String url, CacheTaskResponse cacheTaskResponse) {
        super(context, url, cacheTaskResponse);
    }

    protected Map<String, Object> parse(String cache) {
        try {
            Map<String, Object> map = GsonUtils.getMap(getGson(), cache);
            return map;
        } catch (Exception e) {
            return null;
        }
    }

}
