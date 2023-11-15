package io.jmeter.plugins.dubbo.utils

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.internal.LinkedTreeMap

import java.lang.reflect.Type

class MapDeserializerDoubleAsIntFix implements JsonDeserializer<Map<String, Object>> {
    @Override
    Map<String, Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (Map<String, Object>) read(json)
    }

    Object read(JsonElement inVal) {
        if (inVal.isJsonArray()) {
            List<Object> list = new ArrayList<Object>()
            JsonArray arr = inVal.getAsJsonArray()
            for (JsonElement anArr : arr) {
                list.add(read(anArr))
            }
            return list
        } else if (inVal.isJsonObject()) {
            Map<String, Object> map = new LinkedTreeMap<String, Object>()
            JsonObject obj = inVal.getAsJsonObject()
            Set<Map.Entry<String, JsonElement>> entitySet = obj.entrySet()
            for (Map.Entry<String, JsonElement> entry : entitySet) {
                map.put(entry.getKey(), read(entry.getValue()))
            }
            return map
        } else if (inVal.isJsonPrimitive()) {
            JsonPrimitive prim = inVal.getAsJsonPrimitive()
            if (prim.isBoolean()) {
                return prim.getAsBoolean()
            } else if (prim.isString()) {
                return prim.getAsString()
            } else if (prim.isNumber()) {
                if (prim.toString().count('.') > 0) {
                    Number num = prim.getAsNumber()
                    return num.doubleValue()
                } else {
                    Number num = prim.getAsNumber()
                    return num.longValue()
                }
            }
        }
        return null
    }
}