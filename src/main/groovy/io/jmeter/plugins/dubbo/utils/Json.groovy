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
