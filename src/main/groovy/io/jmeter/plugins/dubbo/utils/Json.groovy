package io.jmeter.plugins.dubbo.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import groovy.json.JsonBuilder
import org.apache.commons.lang.StringEscapeUtils

class Json {
    static Object convertToObject(String json, Class cls) {
        GsonBuilder gsonBuilder = new GsonBuilder()
        gsonBuilder.registerTypeAdapter(new TypeToken<Map<String, Object>>() {
        }.getType(), new MapDeserializerDoubleAsIntFix())
        Gson gson = gsonBuilder.create()
        return gson.fromJson(json, cls)
    }

    static String convertToString(Object object) {
        JsonBuilder jsonBuilder = new JsonBuilder(object)
        return StringEscapeUtils.unescapeJava(jsonBuilder.toPrettyString())
    }
}
