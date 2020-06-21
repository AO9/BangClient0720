package com.gto.bang.util.request;

import android.content.Context;

import com.gto.bang.util.GsonUtils;

import java.util.List;
import java.util.Map;

public class Gson4MapListCacheTask extends CacheTask<List<Map<String, Object>>> {


    public Gson4MapListCacheTask(Context context, String url, CacheTaskResponse cacheTaskResponse) {
        super(context, url, cacheTaskResponse);
    }

    protected List<Map<String, Object>> parse(String cache) {
        try {
            List<Map<String, Object>> list = GsonUtils.getMapList(getGson(), cache);
            dealResultMapList(list);
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    protected void dealResultMapList(List<Map<String, Object>> list) {

    }
}
