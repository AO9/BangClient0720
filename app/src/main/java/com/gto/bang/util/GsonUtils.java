package com.gto.bang.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

/**
 * Created by wjying on 13-12-30.
 */
public class GsonUtils {
    public static Map<String, Object> getMap(Gson gson, String json) {
        try {
            TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>() {
            };
            return (Map<String, Object>) (gson.fromJson(json, typeToken.getType()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> getMap(Gson gson, File file) {
        try {
            TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>() {
            };
            return (Map<String, Object>) gson.fromJson(new FileReader(file), typeToken.getType());
        } catch (Exception e) {

        }
        return null;
    }

    public static List<Map<String, Object>> getMapList(Gson gson, String json) {
        try {
            TypeToken<List<Map<String, Object>>> typeToken = new TypeToken<List<Map<String, Object>>>() {
            };
            return (List<Map<String, Object>>) (gson.fromJson(json, typeToken.getType()));
        } catch (Exception e) {

        }
        return null;
    }


}
