/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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