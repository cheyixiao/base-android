package com.autoforce.common.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liusilong on 2018/10/8.
 * version:1.0
 * Describe:
 */
public class JSONUtil {

    /**
     * 验证 json 格式的数据是否有效
     * https://stackoverflow.com/questions/10174898/how-to-check-whether-a-given-string-is-valid-json-in-java
     *
     * @param json json 字符串
     * @return true 有效，false 无效
     */
    public static boolean isJSONValid(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
